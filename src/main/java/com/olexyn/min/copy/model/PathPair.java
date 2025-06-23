package com.olexyn.min.copy.model;

import java.io.File;
import java.nio.file.Path;

public class PathPair extends CopyEntry<Path> {


	public PathPair(Path a, Path b) {
		super(a, b);
	}

	public File getSrcFile() {
		return getSrc().toFile();
	}

	public File getDstFile() {
		return getDst().toFile();
	}

}
