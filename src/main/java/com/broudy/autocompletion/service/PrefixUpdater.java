package com.broudy.autocompletion.service;

import com.broudy.autocompletion.model.Entry;

public abstract class PrefixUpdater implements Runnable {

	protected final Entry entry;

	public PrefixUpdater(Entry entry) {
		this.entry = entry;
	}

	// Assumption: names are valid, i.e., "Julian Broudy" or "Julian".
	@Override
	public void run() {
		final String fullName = entry.getName().toLowerCase();
		final String[] words = fullName.split(" ");

		// If it is a full name then process the first name as a part of the full name
		// to avoid duplication
		final int numberOfWords = words.length;
		int indexOfWordToProcess = 0;
		if (numberOfWords > 1) {
			processWord(fullName);
			indexOfWordToProcess = 1;
		}
		for (; indexOfWordToProcess < numberOfWords; indexOfWordToProcess++) {
			processWord(words[indexOfWordToProcess]);
		}
	}

	protected abstract void processWord(String word);

}
