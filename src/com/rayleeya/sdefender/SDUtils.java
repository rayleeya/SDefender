package com.rayleeya.sdefender;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SDUtils {

	public static Comparator<File> COMPARATOR_ALPHABET = new Comparator<File> () {
		@Override
		public int compare(File lhs, File rhs) {
			String nl = lhs.getName();
			String nr = rhs.getName();
			return nl.compareToIgnoreCase(nr);
		}
	};
	
	public static Comparator<File> COMPARATOR_TIME_ASC = new Comparator<File> () {
		@Override
		public int compare(File lhs, File rhs) {
			long nl = lhs.lastModified();
			long nr = rhs.lastModified();
			return nl > nr ? 1 : nl < nr ? -1 : 0;
		}
	};
	
	public static Comparator<File> COMPARATOR_TIME_DSC = new Comparator<File> () {
		@Override
		public int compare(File lhs, File rhs) {
			long nl = lhs.lastModified();
			long nr = rhs.lastModified();
			return nl > nr ? -1 : nl < nr ? 1 : 0;
		}
	};
	
	public static List<File> getSubFiles(File src, FilenameFilter filter) {
		File[] files = filter == null ? src.listFiles() : src.listFiles(filter);
		List<File> list = Arrays.asList(files);
		return list;
	}
	
	public static List<File> getSubFiles(File src) {
		List<File> list = getSubFiles(src, (FilenameFilter)null);
		return list;
	}
	
	public static List<File> getSubFiles(File src, Comparator<File> comparator) {
		List<File> list = getSubFiles(src, (FilenameFilter)null);
		Collections.sort(list, comparator);
		return list;
	}
	
	public static List<File> getSubFiles(File src, Comparator<File> comparator, boolean showDotFile) {
		FilenameFilter filter = null;
		if (!showDotFile) {
			filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return !filename.startsWith(".");
				}
			};
		}
		List<File> list = getSubFiles(src, filter);
		Collections.sort(list, comparator);
		return list;
	}
}
