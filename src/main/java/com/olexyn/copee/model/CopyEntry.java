package com.olexyn.copee.model;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.AbstractMap.SimpleEntry;

public class CopyEntry<T> extends SimpleEntry<T, T> implements CopyPair<T> {

	@Serial
	private static final long serialVersionUID = 8620048335830481969L;

	public CopyEntry(@Nullable T key, @Nullable T value) {
		super(key, value);
	}

	@Override
	public T getSrc() {
		return getKey();
	}

	@Override
	public T getDst() {
		return getValue();
	}



}
