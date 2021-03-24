package com.broudy.autocompletion.model;

import java.util.ArrayList;
import java.util.List;

public class TrieNode {

	private TrieNode[] children;
	private List<Entry> entries;

	public TrieNode() {
		this.children = new TrieNode[27];
		this.entries = new ArrayList<>();
	}

	public TrieNode[] getChildren() {
		return children;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void addEntries(Entry entry) {
		if (!entries.contains(entry)) {
			entries.add(entry);
		}
	}

}
