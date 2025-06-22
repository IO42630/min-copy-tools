package com.olexyn.min.copy;

import com.olexyn.min.log.LogU;
import com.olexyn.propconf.PropConf;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;


@UtilityClass
public final class DeleteTrailingSpacesApp {

    static { PropConf.load("conf.properties"); }
    private static final Path SRC = PropConf.getPath("DeleteTrailingSpacesApp.SRC");

    public static void main(String... args) throws IOException {
        LogU.infoPlain("START");

        long trimCount = 1;
        while (trimCount > 0) {
            try (var walk = Files.walk(SRC)) {
                try {
                    trimCount = walk
                        .map(Path::toFile)
                        .filter(File::exists)
                        .filter(file -> file.getName().endsWith(" "))
                        .map(file -> {
                            try {
                                var dst = new File(file.getAbsolutePath().trim());
                                if (dst.exists()) {
                                    dst = new File(dst.getAbsolutePath() + new Random().nextInt());
                                }
                                if (file.isFile()) {
                                    FileUtils.moveFile(file, dst);
                                    LogU.infoPlain("TRIMMED FILE: %s", file);
                                } else if (file.isDirectory()) {
                                    FileUtils.moveDirectory(file, dst);
                                    LogU.infoPlain("TRIMMED DIR: %s", file);
                                }
                            } catch (IOException e) {
                                LogU.infoPlain("ERROR: %s", e.getMessage());
                            }
                            return true;
                        }).count();
                } catch (Exception e) {
                    LogU.infoPlain("ERROR: %s", e.getMessage());
                }
                LogU.infoPlain("PASS DONE:TRIMMED: %s", trimCount);
            }
        }
        LogU.infoPlain("DONE");
    }

}
