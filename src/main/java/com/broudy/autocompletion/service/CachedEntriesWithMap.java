package com.broudy.autocompletion.service;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.broudy.autocompletion.model.Entry;

/**
 * Not a good solution if multiple JVMs run this application because the
 * Singleton is unique per JVM.
 * 
 * Check there is always a reference to the instance so it won't be
 * garbage-collected.
 * 
 * @author Julian
 *
 */

public final class CachedEntriesWithMap implements CachedEntries {

	private static CachedEntriesWithMap INSTANCE;

	// Although ArrayList is preferable to Vector because it increases
	// its size by 50% rather than doubling it; Vector is thread-safe and RAM is
	// infinite.
	private final Map<String, Vector<Entry>> mappings;

	private CachedEntriesWithMap() {
		super();
		this.mappings = new ConcurrentHashMap<>(); // Thread-safe
	}

	public synchronized static CachedEntriesWithMap getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CachedEntriesWithMap();
		}
		return INSTANCE;
	}

	public PrefixUpdater getPrefixUpdater(Entry entry) {
		return new PrefixUpdaterForMap(mappings, entry);
	}

	@Override
	public List<Entry> getListForPrefix(String prefix) {
		return mappings.getOrDefault(prefix, new Vector<>());
	}

	class PrefixUpdaterForMap extends PrefixUpdater {

		// TODO: ask if it is a better practice to access mappings from encapsulating
		// class?
		private final Map<String, Vector<Entry>> mappings;

		PrefixUpdaterForMap(Map<String, Vector<Entry>> mappings, Entry entry) {
			super(entry);
			this.mappings = mappings;
		}

		protected void processWord(String word) {
			final int length = word.length();
			final StringBuilder prefixBuilder = new StringBuilder();
			String prefix;
			for (int index = 0; index < length; index++) {
				prefixBuilder.append(word.charAt(index));
				prefix = prefixBuilder.toString();
				mappings.putIfAbsent(prefix, new Vector<>());
				mappings.get(prefix).add(entry);
			}
		}

	}

}
