package com.olexyn.copee;

import com.olexyn.min.log.LogU;
import com.olexyn.propconf.PropConf;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Delete empty directories.
 */
@UtilityClass
public final class DeleteEmptyDirApp {

    static { PropConf.load("conf.properties"); }
    private static final Path SRC = PropConf.getPath("DeleteEmptyDirApp.SRC");

    public static void main(String... args) throws IOException {
        LogU.infoPlain("START");
        long deleteCount = 1;
        while (deleteCount > 0) {
            try (var walk = Files.walk(SRC)) {
                deleteCount = walk
                    .filter(filePath -> filePath.toFile().isDirectory())
                    .filter(filePath -> {
                        try {
                            return FileUtils.isEmptyDirectory(filePath.toFile());
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .map(filePath -> {
                        try {
                            FileUtils.deleteDirectory(filePath.toFile());
                            LogU.infoPlain("DELETED: %s", filePath);
                            return true;
                        } catch (IOException ignored) {
                            return false;
                        }
                    }).count();
                LogU.infoPlain("PASS DONE:DELETED: %s", deleteCount);
            }
        }
        LogU.infoPlain("DONE");
    }

}
