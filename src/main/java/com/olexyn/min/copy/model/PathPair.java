package com.olexyn.min.copy.model;

import java.io.File;
import java.io.Serial;
import java.nio.file.Path;

public class PathPair extends CopyEntry<Path> {

	@Serial
	private static final long serialVersionUID = 1L;

	public PathPair(Path key, Path value) {
		super(key, value);
	}

	public File getSrcFile() {
		return getSrc().toFile();
	}

	public File getDstFile() {
		return getDst().toFile();
	}

}
