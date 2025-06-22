package com.olexyn.min.copy;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.olexyn.min.copy.TestCase.DIFF_DST_NEWER;
import static com.olexyn.min.copy.TestCase.DIFF_DST_OLDER;
import static com.olexyn.min.copy.TestCase.DIFF_SAME_AGE;
import static com.olexyn.min.copy.TestCase.SAME_DST_NEWER;
import static com.olexyn.min.copy.TestCase.SAME_FILE;
import static com.olexyn.min.copy.TestCase.SRC_ONLY;
import static org.junit.jupiter.api.Assertions.*;

public class CopeeTest {

	private static final String MSG = "hello AeW7iu4u king1Oa2 ri5phuK4 Ef8goach waiz1Ohn uaGhai5r";
	private static final Path ROOT = Path.of("/tmp/copee");
//
//	@BeforeAll
//	public static void prepare() throws IOException, InterruptedException {
//		// create parent dirs
//
//		if (!ROOT.toFile().exists()) {
//			Files.createDirectory(ROOT);
//		}
//		Files.createDirectory(Path.of("/tmp/copee/src"));
//		Files.createDirectory(Path.of("/tmp/copee/dst"));
//
//		FileUtils.createParentDirectories(srcPath(SRC_ONLY, "src/only/").toFile());
//		Files.createFile(srcPath(SRC_ONLY, "src/only/"));
//
//		Files.createFile(srcPath(SAME_FILE));
//		Files.copy(srcPath(SAME_FILE), dstPath(SAME_FILE));
//
//		Files.createFile(srcPath(SAME_DST_NEWER));
//		Files.copy(srcPath(SAME_DST_NEWER), dstPath(SAME_DST_NEWER));
//		long lastModif = srcPath(SAME_DST_NEWER).toFile().lastModified();
//		dstPath(SAME_DST_NEWER).toFile().setLastModified(lastModif + 5);
//
//
//		var src0 = Files.createFile(srcPath(DIFF_SAME_AGE));
//		var dst0 = Files.createFile(dstPath(DIFF_SAME_AGE));
//		Files.writeString(src0.toAbsolutePath(), MSG);
//		srcPath(DIFF_SAME_AGE).toFile().setLastModified(dstPath(DIFF_SAME_AGE).toFile().lastModified());
//
//		var src1 = Files.createFile(srcPath(DIFF_DST_OLDER));
//		var dst1 = Files.createFile(dstPath(DIFF_DST_OLDER));
//		Thread.sleep(5L);
//		Files.writeString(src1.toAbsolutePath(), MSG);
//
//		var src2 = Files.createFile(srcPath(DIFF_DST_NEWER));
//		var dst2 = Files.createFile(dstPath(DIFF_DST_NEWER));
//		Thread.sleep(5L);
//		Files.writeString(dst2.toAbsolutePath(), MSG);
//
//		Thread.sleep(5L);
//		PathCopyApp.main();
//
//	}
//
//	@Test
//	public void testSrcOnly() {
//		assertTrue(
//				dstPath(SRC_ONLY, "src/only/").toFile().exists()
//		);
//	}
//
//	@Test
//	public void testSameDstNewer() {
//		assertEquals(
//				srcPath(SAME_DST_NEWER).toFile().lastModified(),
//				dstPath(SAME_DST_NEWER).toFile().lastModified()
//		);
//	}
//
//	@Test
//	public void testDiffSameAge() throws IOException {
//		test(DIFF_SAME_AGE, true, false);
//	}
//
//	@Test
//	public void testDiffDstOlder() throws IOException {
//		assertEquals(
//				MSG,
//				FileUtils.readFileToString(dstPath(DIFF_DST_OLDER).toFile(), Charset.defaultCharset())
//		);
//	}
//
//	@Test
//	public void testDiffDstNewer() throws IOException {
//		test(DIFF_DST_NEWER, false, false);
//	}
//
//	@AfterAll
//	public static void reset() throws IOException {
//		FileUtils.deleteDirectory(ROOT.toFile());
//	}
//
//	private static void test(TestCase testCase, boolean sameTime, boolean sameContent) throws IOException {
//		if (sameTime) {
//			assertEquals(
//					srcPath(testCase).toFile().lastModified(),
//					dstPath(testCase).toFile().lastModified()
//			);
//		} else {
//			assertNotEquals(
//					srcPath(testCase).toFile().lastModified(),
//					dstPath(testCase).toFile().lastModified()
//			);
//		}
//		if (sameContent) {
//			assertArrayEquals(
//					Files.readAllBytes(srcPath(testCase)),
//					Files.readAllBytes(dstPath(testCase))
//			);
//		} else {
//			assertNotEquals(
//					Files.readAllBytes(srcPath(testCase)),
//					Files.readAllBytes(dstPath(testCase))
//			);
//		}
//
//	}
//
//	private static Path srcPath(TestCase testCase, String... subDir) {
//		String foo = Arrays.stream(subDir).reduce(String::concat).orElse("");
//		return Path.of("/tmp/copee/src").resolve(foo + testCase.toString().toLowerCase() + ".txt");
//	}
//
//	private static Path dstPath(TestCase testCase, String... subDir) {
//		String foo = Arrays.stream(subDir).reduce(String::concat).orElse("");
//		return Path.of("/tmp/copee/dst").resolve(foo + testCase.toString().toLowerCase() + ".txt");
//	}

}
