package com.olexyn.copee;

import com.olexyn.copee.model.CFilePair;
import com.olexyn.copee.model.PathPair;
import com.olexyn.generic.Pair;
import com.olexyn.min.lock.LockU;
import com.olexyn.min.log.LogU;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.olexyn.min.prop.PropStore.getPath;
import static com.olexyn.min.prop.PropStore.set;


/**
 * A slighly more aggressive version of CopyApp.
 * It collects all files from the source directory and collects them into a map by hash.
 * Then it does the same for the destination directory.
 * Finally it performs a copy.
 * But instead of path pairs it uses hash pairs.
 */
public class HashCopyApp {

    private static final String SRC_KEY = "copee.src";
    private static final String DST_KEY = "copee.dst";
    private static final int TRY_COUNT = 2;


    private static final List<Pair<String>> COPY = List.of(
        new Pair<>(
            "/home/user/home/drive/key/copyToSq2021/",
            "/home/user/home/shade/docs/swissquote/2021/"
        )
    );

    public static void main(String... args) throws IOException {
        LogU.infoPlain("START");
        for (var pair : COPY) {
            set(SRC_KEY, pair.getA());
            set(DST_KEY, pair.getB());
            var srcMap = getMap(getPath(SRC_KEY));
            var dstMap = getMap(getPath(DST_KEY));
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