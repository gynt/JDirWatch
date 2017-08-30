package com.gynt.jdirwatch.events;

import java.nio.file.Path;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoveEvent {
	
	//TODO: There is no modify event if things are moved in the root WatchDir. There are two modify events if things are moved in subfolders.
	
	private final RawEvent create;
	private final RawEvent delete;
	//private final RawEvent modify;
	
	public Path getSource() {
		return delete.getFullPath();
	}
	
	public Path getDestination() {
		return create.getFullPath();
	}
	
	public Path getSourceDirectory() {
		return delete.getDirectory();
	}
	
	public Path getDestinationDirectory() {
		return create.getDirectory();
	}

}
