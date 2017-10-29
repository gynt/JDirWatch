package com.gynt.jdirwatch;

import java.nio.file.Path;
import java.nio.file.WatchKey;

public class WatchedPath {

	private WatchKey key;
	private Path path;

	public WatchedPath(Path path, WatchKey key) {
		this.path = path;
		this.key = key;
	}

	public WatchKey getKey() {
		return key;
	}

	public void setKey(WatchKey key) {
		this.key = key;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	public boolean checkIsChild(Path child) {
		return child.toAbsolutePath().startsWith(path.toAbsolutePath());
	}

	public void addChild()
	
}
