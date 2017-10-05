package com.gynt.jdirwatch.events.old;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventSet {
	
	@Getter
	private final RawEvent[] events;

}
