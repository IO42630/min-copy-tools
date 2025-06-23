package com.olexyn.min.copy.model;

public interface CopyPair<T> {

	T getSrc();

	T getDst();

    void setSrc(T src);

    void setDst(T dst);

}
