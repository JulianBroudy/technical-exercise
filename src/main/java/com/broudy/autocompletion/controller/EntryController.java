package com.broudy.autocompletion.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.broudy.autocompletion.model.Entry;
import com.broudy.autocompletion.service.CachedEntries;
import com.broudy.autocompletion.service.CachedEntriesWithTrie;

@RestController
public class EntryController {

	private final CachedEntries cachedEntries;

	EntryController() {
//		cachedEntries = CachedEntriesWithMap.getInstance();
		cachedEntries = CachedEntriesWithTrie.getInstance();
	}

	@GetMapping("/nameAutocomplete")
	public List<String> nameAutocomplete(@RequestParam(value = "term", required = false) String term) {
		final List<String> suggestions = new ArrayList<String>();
		final List<Entry> entries = cachedEntries.getListForPrefix(term.trim().toLowerCase());
		if (entries != null) {
			for (Entry entry : entries) {
				suggestions.add(entry.getName());
			}
		}
		return suggestions;
	}

}
