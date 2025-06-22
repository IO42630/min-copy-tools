package com.olexyn.copee;

import com.olexyn.copee.model.FcStatePair;
import com.olexyn.copee.model.PathPair;
import com.olexyn.min.copy.util.HashUtil;
import com.olexyn.min.log.LogU;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@UtilityClass
public class CopyU {

    public static void copy(FcStatePair pair) {
        try {
            if (!pair.getSrcFile().exists() || !pair.getDstFile().exists()) {
                return;
            }
            String srcHash = HashUtil.getHash(pair.getSrcPath());
            String dstHash = HashUtil.getHash(pair.getDstPath());
            if (srcHash == null || dstHash == null) {
                return;
            }
            if (srcHash.equals(dstHash)) {
                handleSameFile(pair);
            } else {
                handleModifiedFile(pair);
            }
        } catch (IOException e) {
            LogU.warnPlain(e.getMessage());
        }
    }

    /**
     * If the source file is older than the destination file, the destination file is updated.
     * In any other case, the source file is deleted.
     */
    private static void handleSameFile(FcStatePair pair) throws IOException {
        var src = pair.getSrcFile();
        var dst = pair.getDstFile();
        if (src.lastModified() < dst.lastModified()) {
            dst.setLastModified(src.lastModified());
            LogU.infoPlain("SAME File, DST stamp HIGH -> DROP AGE & DEL SRC (%s)", dst);
        } else {
            LogU.infoPlain("SAME File, DST stamp  LOW -> DEL SRC            (%s)", dst);
        }
        Files.deleteIfExists(src.toPath());
    }

    /**
     * If the source file is newer than the destination file, move the source file to the destination.
     */
    private static void handleModifiedFile(FcStatePair pair) throws IOException {
        var src = pair.getSrcFile();
        var dst = pair.getDstFile();
        if (src.lastModified() > dst.lastModified()) {
            Files.move(src.toPath(), dst.toPath(), REPLACE_EXISTING);
            LogU.infoPlain("DIFF File, DST stamp  LOW -> MOVE     (%s)", dst);
        } else {
            LogU.warnPlain("DIFF File, DST stamp HITH -> NOP      (%s)", dst);
        }
    }

    public static void moveIfDstMissing(PathPair pair)  {
        if (pair.getDst() != null && pair.getDst().toFile().exists()) {
            return;
        }
        try {
            FileUtils.createParentDirectories(pair.getDstFile());
            Files.move(pair.getSrc(), pair.getDst(), REPLACE_EXISTING);
            LogU.infoPlain("DST missing               -> MOVE     (%s)", pair.getDst());
        } catch (IOException e) {
            LogU.warnPlain(e.getMessage());
        }
    }

}
