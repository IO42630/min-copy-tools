package com.olexyn.min.copy.model;

import com.olexyn.min.lock.FcState;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serial;
import java.nio.file.Path;

public class FcStatePair extends CopyEntry<FcState> {

	@Serial
	private static final long serialVersionUID = -2469259320900859047L;

	public FcStatePair(@Nullable FcState key, @Nullable FcState value) {
		super(key, value);
	}

	public Path getSrcPath() {
		return getKey().getPath();
	}

	public Path getDstPath() {
		return getValue().getPath();
	}

	public File getSrcFile() {
		return getSrcPath().toFile();
	}

	public File getDstFile() {
		return getDstPath().toFile();
	}

}
