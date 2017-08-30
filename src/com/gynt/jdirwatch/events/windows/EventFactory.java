package com.gynt.jdirwatch.events.windows;

import com.gynt.jdirwatch.events.RawEvent;

public class EventFactory {
	
	public boolean isMoveEvent(RawEvent create, RawEvent delete) {
		return create.getRelativePath().getFileName().equals(delete.getRelativePath().getFileName());
	}

}
