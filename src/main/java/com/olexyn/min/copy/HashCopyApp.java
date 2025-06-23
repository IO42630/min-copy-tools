package com.olexyn.min.copy;

import com.olexyn.min.copy.model.FcStatePair;
import com.olexyn.min.copy.model.PathPair;
import com.olexyn.min.copy.util.CopyU;
import com.olexyn.min.copy.util.HashUtil;
import com.olexyn.min.lock.LockUtil;
import com.olexyn.min.log.LogU;
import com.olexyn.propconf.PropConf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.olexyn.min.lock.LockUtil.lock;
import static com.olexyn.min.lock.LockUtil.unlock;


/**
 * A slighly more aggressive version of CopyApp.
 * It collects all files from the source directory and collects them into a map by hash.
 * Then it does the same for the destination directory.
 * Finally it performs a copy.
 * But instead of path pairs it uses hash pairs.
 */
public class HashCopyApp {

    static { PropConf.load("conf.properties"); }
    private static final Path SRC = PropConf.getPath("HashCopyApp.SRC");
    private static final Path DST = PropConf.getPath("HashCopyApp.DST");

    public static void main(String... args) throws IOException {
        LogU.infoPlain("START");
            var srcMap = getMap(SRC);
            var dstMap = getMap(DST);
            srcMap.entrySet().stream()
                .map(entry -> new PathPair(entry.getValue(), dstMap.get(entry.getKey())))
                .peek(CopyU::moveIfDstMissing)
                .filter(pathPair -> pathPair.getDst() != null)
                .map(pathPair -> new FcStatePair(
                    lock(pathPair.getSrc()),
                    lock(pathPair.getDst())
                ))
                .peek(CopyU::copy)
                .peek(cFilePair -> unlock(cFilePair.getSrc()))
                .forEach(cFilePair -> unlock(cFilePair.getDst()));
            ;
            LogU.infoPlain("DONE");

    }

    private static Map<String, Path> getMap(Path path) throws IOException {
        var map = new HashMap<String, Path>();
        try (var walk = Files.walk(path)) {
            walk
                .filter(filePath -> filePath.toFile().isFile())
                .map(LockUtil::lock)
                .filter(Objects::nonNull)
                .forEach(cFile -> {
                    map.put(HashUtil.getHash(cFile), cFile.getPath());
                });
        }
        return map;
    }


}
