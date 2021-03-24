package com.broudy.autocompletion.service;

import java.util.Collections;
import java.util.List;

import com.broudy.autocompletion.model.Entry;
import com.broudy.autocompletion.model.Trie;
import com.broudy.autocompletion.model.TrieNode;

public class CachedEntriesWithTrie implements CachedEntries {

	private static CachedEntriesWithTrie INSTANCE;
	private final Trie trie;

	private CachedEntriesWithTrie() {
		super();
		this.trie = new Trie(); // Thread-safe
	}

	public synchronized static CachedEntriesWithTrie getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CachedEntriesWithTrie();
		}
		return INSTANCE;
	}

	public PrefixUpdater getPrefixUpdater(Entry entry) {
		return new PrefixUpdaterForTrie(trie, entry);
	}

	@Override
	public List<Entry> getListForPrefix(String prefix) {
		TrieNode current = trie.getRoot();
		TrieNode node;
		final char[] charArray = prefix.toCharArray();
		for (char character : charArray) {
			node = current.getChildren()[character == ' ' ? 26 : character - 'a'];
			if (node == null) {
				return Collections.emptyList();
			}
			current = node;
		}
		return current.getEntries();
	}

	class PrefixUpdaterForTrie extends PrefixUpdater {

		private final Trie trie;

		public PrefixUpdaterForTrie(Trie trie, Entry entry) {
			super(entry);
			this.trie = trie;
		}

		@Override
		protected void processWord(String word) {
			trie.insert(entry, word);
		}
	}

}
