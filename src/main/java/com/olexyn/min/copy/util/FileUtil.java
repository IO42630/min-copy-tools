package com.olexyn.min.copy.util;

import com.olexyn.min.log.LogU;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;

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
}
