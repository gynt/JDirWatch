package com.gynt.jdirwatch.windows;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gynt.jdirwatch.events.old.EventSet;
import com.gynt.jdirwatch.events.old.RawEvent;
import com.sun.nio.file.ExtendedWatchEventModifier;

import lombok.Getter;

public class WatchDir {
	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private final boolean recursive;
	private boolean trace = false;

	@Getter
	private final Queue<EventSet> queue = new LinkedBlockingQueue<>();

	private final ArrayList<RawEvent> events = new ArrayList<>();

	private long lastEvent = 0L;
	private long cutoff = 100L;

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * Register the given directory with the WatchService
	 */
	private void register(Path dir) throws IOException {
		@SuppressWarnings("restriction")
		ExtendedWatchEventModifier a = com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE; // Windows
																								// only

		WatchKey key = dir.register(watcher, new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY }, a);
		if (trace) {
			Path prev = keys.get(key);
			if (prev == null) {
				System.out.format("register: %s\n", dir);
			} else {
				if (!dir.equals(prev)) {
					System.out.format("update: %s -> %s\n", prev, dir);
				}
			}
		}
		keys.put(key, dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		// Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
		// @Override
		// public FileVisitResult preVisitDirectory(Path dir,
		// BasicFileAttributes attrs)
		// throws IOException
		// {
		// register(dir);
		// return FileVisitResult.CONTINUE;
		// }
		// });
		register(start);
	}

	/**
	 * Creates a WatchService and registers the given directory
	 */
	WatchDir(Path dir, boolean recursive) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		this.recursive = recursive;

		if (recursive) {
			System.out.format("Scanning %s ...\n", dir);
			registerAll(dir);
			System.out.println("Done.");
		} else {
			register(dir);
		}

		// enable trace after initial registration
		this.trace = true;
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	public void processEvents() {

		for (;;) {

			// wait for key to be signalled
			WatchKey key;

			key = watcher.poll();
			// key = watcher.take();

			if (key == null) {
				if(events.size() > 0 && System.currentTimeMillis()-lastEvent > cutoff) {
					EventSet set = new EventSet(events.toArray(new RawEvent[0]));
					queue.offer(set);
					events.clear();
					lastEvent = 0;
					System.out.format("EventSet: total: %s, path: %s\n", set.getEvents().length, set.getEvents()[0].getFullPath());
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException x) {
					return;
				}			
				continue;
			} 
			
			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}
			
			

			List<WatchEvent<?>> poll = key.pollEvents();
			System.out.println("Events in poll: " + poll.size());
			
			//TODO: Consider a single poll to be a chunk of events... Not sure how this works with slow computers.
			
			for (WatchEvent<?> event : poll) {
				WatchEvent.Kind<?> kind = event.kind();

				// TBD - provide example of how OVERFLOW event is handled
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = dir.resolve(name);
				
				RawEvent raw = new RawEvent(ev, dir);
				
				if(events.size()>0) {
					if(events.get(events.size()-1).getFullPath().equals(raw.getFullPath())) {
						events.add(raw);
						lastEvent = System.currentTimeMillis();
						System.out.format("Add event to Set: path: %s\n", child);
					} else {
						EventSet set = new EventSet(events.toArray(new RawEvent[0]));
						queue.offer(set);
						events.clear();
						lastEvent = 0;
						System.out.format("EventSet: total: %s, path: %s\n", set.getEvents().length, child);
					}
				} else {
					events.add(raw);
					lastEvent = System.currentTimeMillis();
					System.out.format("Add event to Set: path: %s\n", child);
				}

				// print out event
				System.out.format("%s: count: %s path: %s\n", event.kind().name(), event.count(), child);

				// if directory is created, and watching recursively, then
				// register it and its sub-directories
				// if (recursive && (kind ==
				// StandardWatchEventKinds.ENTRY_CREATE)) {
				// try {
				// if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
				// registerAll(child);
				// }
				// } catch (IOException x) {
				// // ignore to keep sample readbale
				// }
				// }
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}

	static void usage() {
		System.err.println("usage: java WatchDir [-r] dir");
		System.exit(-1);
	}

	public static void main(String[] args) throws IOException {
		// parse arguments
		args = new String[] { "-r", "X:\\test" };
		if (args.length == 0 || args.length > 2)
			usage();
		boolean recursive = false;
		int dirArg = 0;
		if (args[0].equals("-r")) {
			if (args.length < 2)
				usage();
			recursive = true;
			dirArg++;
		}

		// register directory and process its events
		Path dir = Paths.get(args[dirArg]);
		new WatchDir(dir, recursive).processEvents();
	}
}
