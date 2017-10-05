package com.gynt.jdirwatch.filters;

import com.gynt.jdirwatch.util.RegexPath;

public class FrequencyFilter extends AbstractFilter {

	private int frequency;

	public FrequencyFilter(RegexPath regexpath, int frequency) {
		super(regexpath);
		this.frequency=frequency;
	}

	@Override
	public boolean filter(String path) {
		if(frequency > 0) {
			frequency--;
			return false;
		}
		deactivate();
		return true;
	}

}
