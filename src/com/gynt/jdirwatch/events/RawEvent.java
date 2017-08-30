package com.gynt.jdirwatch.events;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**Wraps a WatchEvent*/
@RequiredArgsConstructor
public class RawEvent {
	
	@Getter
	private final WatchEvent<Path> event;
	
	/**The directory the event occurred in.*/
	@Getter
	private final Path directory;
	
	public Path getRelativePath() {
		return event.context();
	}
	
	public Path getFullPath() {
		return directory.resolve(event.context());
	}
	
	public Kind<Path> getKind() {
		return event.kind();
	}

}
