package com.olexyn.copee.model;

import com.olexyn.min.lock.CFile;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serial;
import java.nio.file.Path;

public class CFilePair extends CopyEntry<CFile> {

	@Serial
	private static final long serialVersionUID = -2469259320900859047L;

	public CFilePair(@Nullable CFile key, @Nullable CFile value) {
		super(key, value);
	}

	public Path getSrcPath() {
		return getKey().toPath();
	}

	public Path getDstPath() {
		return getValue().toPath();
	}

	public File getSrcFile() {
		return getSrcPath().toFile();
	}

	public File getDstFile() {
		return getDstPath().toFile();
	}

}
