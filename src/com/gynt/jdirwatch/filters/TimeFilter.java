package com.gynt.jdirwatch.filters;

import com.gynt.jdirwatch.util.RegexPath;

public class TimeFilter extends AbstractFilter {

	private long until;

	public TimeFilter(RegexPath regexpath, long until) {
		super(regexpath);
		this.until = until;
	}

	@Override
	public boolean filter(String path) {
		long now = System.currentTimeMillis();
		if(until-now > 0) {
			return false;
		}
		deactivate();
		return true;
	}

}
