package com.olexyn.copee;

import com.olexyn.copee.model.CFilePair;
import com.olexyn.copee.model.PathPair;
import com.olexyn.generic.Pair;
import com.olexyn.min.lock.LockU;
import com.olexyn.min.log.LogU;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.olexyn.min.prop.PropStore.getPath;
import static com.olexyn.min.prop.PropStore.set;

@UtilityClass
public final class PathCopyApp {

    private static final String SRC_KEY = "copee.src";
    private static final String DST_KEY = "copee.dst";


    private static final List<Pair<String>> COPY = List.of(
        new Pair<>(
            "/home/user/home/drive/key/admin/",
            "/home/user/home/shade/study/unifr/admin/"
        )
    );


    private static final int TRY_COUNT = 2;


    public static void main(String... args) throws IOException {

        LogU.infoPlain("START");

        for (var pair : COPY) {


            set(SRC_KEY, pair.getA());
            set(DST_KEY, pair.getB());


            moveWhereDstMissing();

            try (var walk = Files.walk(getPath(SRC_KEY))) {
                walk
                    .filter(filePath -> filePath.toFile().isFile())
                    .map(filePath -> new PathPair(filePath, findDst(filePath)))
                    .map(pathPair -> new CFilePair(
                        LockU.lockFile(pathPair.getSrc(), TRY_COUNT).orElse(null),
                        LockU.lockFile(pathPair.getDst(), TRY_COUNT).orElse(null)
                    ))
                    .filter(cFilePair -> cFilePair.getSrc() != null)
                    .filter(cFilePair -> cFilePair.getDst() != null)
                    .peek(CopyU::copy)
                    .peek(cFilePair -> LockU.unlockFile(cFilePair.getSrc(), TRY_COUNT))
                    .forEach(cFilePair -> LockU.unlockFile(cFilePair.getDst(), TRY_COUNT));
            }
            LogU.infoPlain("DONE");
        }
    }

    private static Path findDst(Path srcPath) {
        var path = srcPath.toString().replace(getPath(SRC_KEY).toString(), getPath(DST_KEY).toString());
        return Path.of(path);
    }

    private static void moveWhereDstMissing() throws IOException {
        try (var walk = Files.walk(getPath(SRC_KEY))) {
            walk
                .filter(filePath -> filePath.toFile().isFile())
                .map(filePath -> new PathPair(filePath, findDst(filePath)))
                .filter(pair -> !pair.getValue().toFile().exists())
                .forEach(CopyU::moveIfDstMissing);
        }
    }






}
