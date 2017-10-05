package com.gynt.jdirwatch.includers;

import com.gynt.jdirwatch.util.RegexPath;

public class BasicIncluder extends AbstractIncluder {

	public BasicIncluder(RegexPath regexpath) {
		super(regexpath);
	}

	@Override
	public boolean include(String path) {
		return true;
	}

}
