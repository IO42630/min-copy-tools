package com.olexyn.min.copy.model;

import com.olexyn.min.lock.FcState;
import com.olexyn.min.obj.Pair;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;

@Getter
@Setter
public class FcStatePair extends CopyEntry<FcState> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

    public FcStatePair(@NonNull FcState a, @NonNull FcState b) {
        super(a, b);
    }


    public Path getSrcPath() {
		return getSrc().getPath();
	}

	public Path getDstPath() {
		return getDst().getPath();
	}

	public File getSrcFile() {
		return getSrcPath().toFile();
	}

	public File getDstFile() {
		return getDstPath().toFile();
	}

}
