package com.olexyn.copee;

import java.io.IOException;

import static com.olexyn.min.prop.PropStore.set;


/**
 * Input : an directory to operate on.
 * Steps:
 * 1. Load all files with path and hash into a map.
 * 2. Find all files with same hash.
 * 3. For each set of files with same hash, keep the one which matches a certain "viable" pattern, and delete the rest.
 * 4. For the repaining files modify the name according to anoter "optimal" pattern.
 */

public final class NameMeld {

	private static final String SRC = "copee.src";
	private static final String DST = "copee.dst";
	private static final int TRY_COUNT = 4;

	private NameMeld() {
	}

	public static void init() {
		set(SRC, "/home/user/home/shade/collection-share/");
		set(DST, "/home/user/home/shade/collection/");
	}

	public static void main(String... args) throws IOException {
		init();
		// TODO
	}

}
