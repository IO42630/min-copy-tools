package com.olexyn.min.copy.util;

import com.olexyn.min.log.LogU;
import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@UtilityClass
public class FileUtil {


    public static long size(Path path) {
        try {
            return Files.size(path);
        } catch (Exception e) {
            LogU.warnPlain(e.getMessage());
            return 0;
        }
    }



    public static @Nullable Instant lastModified(Path path) {
        try {
            return Files.getLastModifiedTime(path).toInstant();
        } catch (Exception e) {
            LogU.warnPlain(e.getMessage());
            return null;
        }
    }
}
