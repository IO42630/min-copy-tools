package com.olexyn.min.copy.model;

import com.olexyn.min.obj.Pair;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CopyEntry<T> extends Pair<T> implements CopyPair<T> {


    public CopyEntry(@NonNull T a, @NonNull T b) {
        super(a, b);
    }

    @Override
	public T getSrc() {
		return getA();
	}

	@Override
	public T getDst() {
		return getB();
	}

    @Override
    public void setSrc(T src) {
        setA(src);
    }

    @Override
    public void setDst(T dst) {
        setB(dst);
    }


}
