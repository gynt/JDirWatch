package com.gynt.jdirwatch.filters;

import java.util.regex.Pattern;

import com.gynt.jdirwatch.util.RegexPath;

public abstract class AbstractFilter {
	
	private Pattern pattern;
	private boolean active;

	public AbstractFilter(RegexPath regexpath) {
		this.pattern = regexpath.compile();
	}
	
	public boolean matches(String path) {
		return pattern.matcher(path).matches();
	}
	
	public String getRegexpath() {
		return pattern.pattern();
	}
	
	public synchronized void deactivate() {
		this.active = false;
	}
	
	public synchronized boolean isActive() {
		return this.active;
	}
	
	public abstract boolean filter(String path);

}
