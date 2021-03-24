package com.broudy.autocompletion.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

public final class CachedEntries {

	private static CachedEntries INSTANCE;

	// Although ArrayList is preferable to Vector because it increases
	// its size by 50% rather than doubling it; Vector is thread-safe and RAM is
	// infinite.
	private final Map<String, Vector<Entry>> mappings;

	private CachedEntries() {
		super();
		this.mappings = new ConcurrentHashMap<>(); // Thread-safe
	}

	public synchronized static CachedEntries getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CachedEntries();
		}
		return INSTANCE;
	}

	public Map<String, Vector<Entry>> getMappings() {
		return mappings;
	}

	public void cacheEntries(Collection<Entry> entries) {

		// HashSet to keep tabs on duplicate names instead of cleaning first then doing
		// this process.
		HashSet<String> names = new HashSet<>();

		final int NUMBER_OF_CORES = 4; // Simulate 4-cores CPU.
		final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_CORES);

		String name;
		for (Entry entry : entries) {
			name = entry.getName();
			// If the name wasn't sent to processing yet, send it.
			if (!names.contains(name)) {
				names.add(name);
				PrefixUpdater updater = new PrefixUpdater(mappings, entry);
				executor.execute(updater);
			}
		}
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
			// TODO ask whether the Controller would still be up..?
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	class PrefixUpdater implements Runnable {

		final Entry entry;
		final Map<String, Vector<Entry>> mappings;

		PrefixUpdater(Map<String, Vector<Entry>> mappings, Entry entry) {
			this.mappings = mappings;
			this.entry = entry;
		}

		// Assumption: names are valid, i.e., "Julian Broudy" or "Julian".
		@Override
		public void run() {
			final StringBuilder prefixBuilder = new StringBuilder();
			final String fullName = entry.getName().toLowerCase();
			final String[] words = fullName.split(" ");

			// If it is a full name then process the first name as a part of the full name
			// to avoid duplication
			final int numberOfWords = words.length;
			int indexOfWordToProcess = 0;
			if (numberOfWords > 1) {
				processWord(prefixBuilder, fullName);
				indexOfWordToProcess = 1;
			}
			for (; indexOfWordToProcess < numberOfWords; indexOfWordToProcess++) {
				processWord(prefixBuilder, words[indexOfWordToProcess]);
			}
		}

		private void processWord(StringBuilder prefixBuilder, String word) {
			final int length = word.length();
			prefixBuilder.setLength(0);
			for (int index = 0; index < length; index++) {
				prefixBuilder.append(word.charAt(index));
				mappings.putIfAbsent(prefixBuilder.toString(), new Vector<>());
				mappings.get(prefixBuilder.toString()).add(entry);
			}

		}

	}

}
