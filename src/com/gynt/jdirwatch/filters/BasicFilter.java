package com.gynt.jdirwatch.filters;

import com.gynt.jdirwatch.util.RegexPath;

public class BasicFilter extends AbstractFilter {
	
	public BasicFilter(RegexPath regexpath) {
		super(regexpath);
	}

	@Override
	public boolean filter(String path) {
		return true;
	}

}
