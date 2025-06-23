package com.olexyn.min.copy;

import com.olexyn.min.copy.model.FcStatePair;
import com.olexyn.min.copy.model.PathPair;
import com.olexyn.min.copy.util.CopyU;
import com.olexyn.min.log.LogU;
import com.olexyn.propconf.PropConf;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.olexyn.min.lock.LockUtil.lock;
import static com.olexyn.min.lock.LockUtil.unlock;

@UtilityClass
public final class PathCopyApp {

    static { PropConf.load("conf.properties"); }
    private static final Path SRC = PropConf.getPath("PathCopyApp.SRC");
    private static final Path DST = PropConf.getPath("PathCopyApp.DST");



    public static void main(String... args) throws IOException {
        LogU.infoPlain("START");
        moveWhereDstMissing();
        try (var walk = Files.walk(SRC)) {
            walk
                .filter(filePath -> filePath.toFile().isFile())
                .map(filePath -> new PathPair(filePath, findDst(filePath)))
                .map(pathPair -> new FcStatePair(
                    lock(pathPair.getSrc()),
                    lock(pathPair.getDst())
                ))
                .filter(cFilePair -> cFilePair.getSrc() != null)
                .filter(cFilePair -> cFilePair.getDst() != null)
                .peek(CopyU::copy)
                .peek(cFilePair -> unlock(cFilePair.getSrc()))
                .forEach(cFilePair -> unlock(cFilePair.getDst()));
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
