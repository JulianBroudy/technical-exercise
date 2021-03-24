package com.broudy.autocompletion.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.broudy.autocompletion.model.Entry;

public interface CachedEntries {

	public default void cacheEntries(Collection<Entry> entries) {
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
				PrefixUpdater updater = getPrefixUpdater(entry);
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

	public PrefixUpdater getPrefixUpdater(Entry entry);

	public List<Entry> getListForPrefix(String prefix);

}
