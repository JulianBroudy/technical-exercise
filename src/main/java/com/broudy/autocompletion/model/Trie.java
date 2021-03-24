package com.broudy.autocompletion.model;

public class Trie {

	private TrieNode root;

	public Trie() {
		this.root = new TrieNode();
	}

	public TrieNode getRoot() {
		return root;
	}

	// TODO ask if this is enough?
	public synchronized void insert(Entry entry, String word) {
		TrieNode current = root;
		TrieNode[] children;
		int index;
		final char[] wordArray = word.toCharArray();
		for (char letter : wordArray) {
			index = letter == ' ' ? 26 : letter - 'a';
			children = current.getChildren();
			if (children[index] == null) {
				children[index] = new TrieNode();
			}
			current = children[index];
			current.addEntries(entry);
		}
	}

}
