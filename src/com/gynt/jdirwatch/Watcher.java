package com.gynt.jdirwatch;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;

import com.gynt.jdirwatch.events.EventListener;

public class Watcher implements Runnable {
	
	private boolean running = true;
	private int code = 0;
	private ArrayList<EventListener> listeners = new ArrayList<>();
		
	
	private synchronized boolean isRunning() {
		return running;
	}
	
	private synchronized void setRunning(boolean running) {
		this.running = running;
	}
	
	
	private synchronized void setTerminationCode(int code) {
		this.code=code;
	}
	
	public synchronized int getTerminationCode(int code) {
		return this.code;
	}
	
	private void startUp() {
		
	}
	
	private void cleanUp() {
		
	}

	public void shutdown() {
		setRunning(false);
	}
	
	public void addEventListener(EventListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeEventListener(EventListener listener) {
		this.listeners.remove(listener);
	}
	
	public void removeAllEventListener() {
		this.listeners.clear();
	}
	
	public void addFileToWatchList(Path path) {
		Path dir = path.getParent();
		Path regex = dir.resolve(".*");
		
	}
	
	public void addDirectoryToWatchList(Path path, boolean recursive) {
		
	}
	
	private void register(Path path) {
		
	}
	
	private boolean isRelevant(WatchedPath w) {
		return running;
		
	}

	public void removeFromWatchList(Path path) {
		
	}
	
	@Override
	public void run() {
		startUp();
		while(isRunning()) {
			
		}
		cleanUp();
		setTerminationCode(0);
	}

}
