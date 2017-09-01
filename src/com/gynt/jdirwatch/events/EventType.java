package com.gynt.jdirwatch.events;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EventType {
	
	@Getter
	private final int type;
	
	public static final EventType CREATE = new EventType(0);
	public static final EventType MODIFY = new EventType(1);
	public static final EventType MOVE = new EventType(2);
	public static final EventType REMOVE = new EventType(3);
	public static final EventType COPY = new EventType(4);

}
