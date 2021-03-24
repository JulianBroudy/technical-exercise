package com.broudy.autocompletion.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.broudy.autocompletion.exception.InvalidInputException;
import com.broudy.autocompletion.model.Entry;
import com.broudy.autocompletion.service.CachedEntries;
import com.broudy.autocompletion.service.CachedEntriesWithTrie;

@Controller
public class AppController {

	private final CachedEntries cachedEntries;

	AppController() {
//		cachedEntries = CachedEntriesWithMap.getInstance();
		cachedEntries = CachedEntriesWithTrie.getInstance();
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("entry", new Entry());
		return "index";
	}

	@PostMapping("/")
	public String homePage(@ModelAttribute Entry entry, Model model) {
		String prefix = entry.getName();
		if (prefix.isBlank()) {
			throw new InvalidInputException("Empty field");
		}
		prefix = prefix.trim().toLowerCase();
		Pattern p = Pattern.compile("^([a-z]+([ ][a-z]+)?)$");
		Matcher m = p.matcher(prefix);
		if (!m.matches()) {
			throw new InvalidInputException("Contains special characters");
		}
		model.addAttribute("entries", cachedEntries.getListForPrefix(prefix));
		return "results";
	}

	@ExceptionHandler(InvalidInputException.class)
	public String invalidInputExceptionHandler() {
		return "invalidInput";
	}

}
