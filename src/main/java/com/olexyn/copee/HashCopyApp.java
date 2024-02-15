package com.olexyn.copee;

import com.olexyn.copee.model.CFilePair;
import com.olexyn.copee.model.PathPair;
import com.olexyn.min.lock.LockU;
import com.olexyn.min.log.LogU;
import com.olexyn.propconf.PropConf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A slighly more aggressive version of CopyApp.
 * It collects all files from the source directory and collects them into a map by hash.
 * Then it does the same for the destination directory.
 * Finally it performs a copy.
 * But instead of path pairs it uses hash pairs.
 */
public class HashCopyApp {

    static { PropConf.loadProperties("conf.properties"); }
    private static final Path SRC = PropConf.getPath("HashCopyApp.SRC");
    private static final Path DST = PropConf.getPath("HashCopyApp.DST");
    private static final int TRY_COUNT = Integer.parseInt(PropConf.get("HashCopyApp.TRY_COUNT"));

    public static void main(String... args) throws IOException {
        LogU.infoPlain("START");
            var srcMap = getMap(SRC);
            var dstMap = getMap(DST);
            srcMap.entrySet().stream()
                .map(entry -> new PathPair(entry.getValue(), dstMap.get(entry.getKey())))
                .peek(CopyU::moveIfDstMissing)
                .filter(pathPair -> pathPair.getDst() != null)
                .map(pathPair -> new CFilePair(
                    LockU.lockFile(pathPair.getSrc(), TRY_COUNT).orElse(null),
                    LockU.lockFile(pathPair.getDst(), TRY_COUNT).orElse(null)
                ))
                .peek(CopyU::copy)
                .peek(cFilePair -> LockU.unlockFile(cFilePair.getSrc(), TRY_COUNT))
                .forEach(cFilePair -> LockU.unlockFile(cFilePair.getDst(), TRY_COUNT));
            ;
            LogU.infoPlain("DONE");

    }

    private static Map<String, Path> getMap(Path path) throws IOException {
        var map = new HashMap<String, Path>();
        try (var walk = Files.walk(path)) {
            walk
                .filter(filePath -> filePath.toFile().isFile())
                .map(filepath -> LockU.lockFile(filepath, TRY_COUNT).orElse(null))
                .filter(Objects::nonNull)
                .forEach(cFile -> {
                    map.put(HashUtil.getHash(cFile), cFile.toPath());
                });
        }
        return map;
    }


}