package com.olexyn.min.copy.util;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.util.Set;

@UtilityClass
public class FileNameUtil {


    Set<String> endings = Set.of(
        ".mp3"
    );



    public static String safeName(Path path) {
        var fileName = path.getFileName().toString();
        var ending = "";
        for (String end : endings) {
            if (fileName.endsWith(end)) {
                ending = end;
                fileName = fileName
                    .substring(0, fileName.length() - end.length());
                break;
            }
        }
        fileName = fileName.replaceAll("[^\\p{Alnum}-]", "_");
        return fileName + ending;
    }
}
