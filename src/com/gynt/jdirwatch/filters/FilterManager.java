package com.gynt.jdirwatch.filters;

import java.util.HashMap;

public class FilterManager {
	
	private HashMap<String, AbstractFilter> map = new HashMap<>();

	public void registerFilter(AbstractFilter filter) {
		this.map.put(filter.getRegexpath(), filter);
	}
	
	public void removeFilterForPath(String regexpath) {
		this.map.remove(regexpath);
	}
	
	public void removeFilter(AbstractFilter filter) {
		this.map.remove(filter.getRegexpath());	
	}

}
