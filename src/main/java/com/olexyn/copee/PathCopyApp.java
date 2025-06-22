package com.olexyn.copee;

import com.olexyn.copee.model.FcStatePair;
import com.olexyn.copee.model.PathPair;
import com.olexyn.min.log.LogU;
import com.olexyn.propconf.PropConf;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.olexyn.min.lock.LockUtil.lockFile;
import static com.olexyn.min.lock.LockUtil.unlockFile;

@UtilityClass
public final class PathCopyApp {

    static { PropConf.load("conf.properties"); }
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
                .map(pathPair -> new FcStatePair(
                    lockFile(pathPair.getSrc(), TRY_COUNT),
                    lockFile(pathPair.getDst(), TRY_COUNT)
                ))
                .filter(cFilePair -> cFilePair.getSrc() != null)
                .filter(cFilePair -> cFilePair.getDst() != null)
                .peek(CopyU::copy)
                .peek(cFilePair -> unlockFile(cFilePair.getSrc(), TRY_COUNT))
                .forEach(cFilePair -> unlockFile(cFilePair.getDst(), TRY_COUNT));
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
