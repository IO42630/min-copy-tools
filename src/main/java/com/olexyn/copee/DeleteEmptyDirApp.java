package com.olexyn.copee;

import com.olexyn.min.log.LogU;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;

import static com.olexyn.min.prop.PropStore.getPath;
import static com.olexyn.min.prop.PropStore.set;

/**
 * Delete empty directories.
 */
public final class DeleteEmptyDirApp {

	private static final String SRC = "copee.src";
	private static final String DST = "copee.dst";
	private static final int TRY_COUNT = 4;

	private DeleteEmptyDirApp() {
	}

	public static void init() {
		set(SRC, "/home/user/home/shade/career/");
	}

	public static void main(String... args) throws IOException {
		init();
		LogU.infoPlain("START");

		long deleteCount = 1;
		while (deleteCount >  0) {
			try (var walk = Files.walk(getPath(SRC))) {
				deleteCount = walk
						.filter(filePath -> filePath.toFile().isDirectory())
						.filter(filePath -> {
							try {
								return FileUtils.isEmptyDirectory(filePath.toFile());
							} catch (IOException e) {
								return false;
							}
						})
						.peek(filePath -> {
							try {
								FileUtils.deleteDirectory(filePath.toFile());
								LogU.infoPlain("DELETED: %s", filePath);
							} catch (IOException ignored) {
							}
						}).count();
				LogU.infoPlain("PASS DONE:DELETED: %s", deleteCount);
			}
		}
		LogU.infoPlain("DONE");
	}

}
