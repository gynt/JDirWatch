package com.gynt.jdirwatch.util;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class RegexPath {
	
	private String regex;

	public RegexPath(Path path) {
		this.regex = prepare(path.toAbsolutePath().toString());
	}
	
	public static String prepare(String path) {
		return path.replaceAll("\\", "/")
				.replaceAll("\\.", "\\\\.");
	}
	
	public Pattern compile() {
		return Pattern.compile(regex);
	}
}
