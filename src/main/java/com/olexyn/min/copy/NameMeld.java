package com.olexyn.min.copy;

import java.io.IOException;


/**
 * Input : an directory to operate on.
 * Steps:
 * 1. Load all files with path and hash into a map.
 * 2. Find all files with same hash.
 * 3. For each set of files with same hash, keep the one which matches a certain "viable" pattern, and delete the rest.
 * 4. For the repaining files modify the name according to anoter "optimal" pattern.
 */

public final class NameMeld {


	private NameMeld() {
	}



	public static void main(String... args) throws IOException {
		// TODO
	}

}
