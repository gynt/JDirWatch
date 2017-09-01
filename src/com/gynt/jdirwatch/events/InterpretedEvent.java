package com.gynt.jdirwatch.events;

import java.nio.file.Path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InterpretedEvent {
	
	@Getter
	private final EventSet set;
	
	@Getter
	private final EventType type;

}
