package com.olexyn.copee;

import com.olexyn.min.log.LogU;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static com.olexyn.min.prop.PropStore.getPath;
import static com.olexyn.min.prop.PropStore.set;

@UtilityClass
public final class DeleteTrailingSpaces {

    private static final String SRC = "copee.src";

    public static void main(String... args) throws IOException {
        set(SRC, "/home/user/home/shade/");
        LogU.infoPlain("START");

        long trimCount = 1;
        while (trimCount > 0) {
            try (var walk = Files.walk(getPath(SRC))) {
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
