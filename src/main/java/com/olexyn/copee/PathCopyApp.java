package com.olexyn.copee;

import com.olexyn.copee.model.CFilePair;
import com.olexyn.copee.model.PathPair;
import com.olexyn.min.lock.LockU;
import com.olexyn.min.log.LogU;
import com.olexyn.propconf.PropConf;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public final class PathCopyApp {

    static { PropConf.loadProperties("conf.properties"); }
    private static final Path SRC = PropConf.getPath("PathCopyApp.SRC");
    private static final Path DST = PropConf.getPath("PathCopyApp.DST");
    private static final int TRY_COUNT = Integer.parseInt(PropConf.get("PathCopyApp.TRY_COUNT"));



    public static void main(String... args) throws IOException {
        LogU.infoPlain("START");
        moveWhereDstMissing();
        try (var walk = Files.walk(SRC)) {
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

    private static Path findDst(Path srcPath) {
        var path = srcPath.toString().replace(SRC.toString(), DST.toString());
        return Path.of(path);
    }

    private static void moveWhereDstMissing() throws IOException {
        try (var walk = Files.walk(SRC)) {
            walk
                .filter(filePath -> filePath.toFile().isFile())
                .map(filePath -> new PathPair(filePath, findDst(filePath)))
                .filter(pair -> !pair.getValue().toFile().exists())
                .forEach(CopyU::moveIfDstMissing);
        }
    }

}
