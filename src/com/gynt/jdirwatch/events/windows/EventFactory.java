package com.gynt.jdirwatch.events.windows;

import java.nio.file.Files;

import com.gynt.jdirwatch.events.RawEvent;

public class EventFactory {
	
	public boolean isMoveEvent(RawEvent create, RawEvent delete) {
		return create.getRelativePath().getFileName().equals(delete.getRelativePath().getFileName()) && (!(create.getFullPath().equals(delete.getFullPath())));
	}
	
	public boolean isDirDeleteEvent(RawEvent delete) {
		return false;
		//TODO : How do we know/knew it was a directory?
	}
	
	

}
