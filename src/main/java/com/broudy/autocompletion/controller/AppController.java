package com.broudy.autocompletion.controller;

import java.util.Vector;
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

@Controller
public class AppController {

	private final CachedEntries cachedEntries;

	AppController() {
		cachedEntries = CachedEntries.getInstance();
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("entry", new Entry());
		return "index";
	}

	@PostMapping("/")
	public String homePage(@ModelAttribute Entry entry, Model model) {
		String name = entry.getName();
		if (name.isBlank()) {
			throw new InvalidInputException("Empty field");
		}
		name = name.trim().toLowerCase();
		Pattern p = Pattern.compile("^([a-z]+([ ][a-z]+)?)$");
		Matcher m = p.matcher(name);
		if (!m.matches()) {
			throw new InvalidInputException("Contains special characters");
		}
		model.addAttribute("entries", cachedEntries.getMappings().getOrDefault(name, new Vector<>()));
		return "results";
	}

	@ExceptionHandler(InvalidInputException.class)
	public String invalidInputExceptionHandler() {
		return "invalidInput";
	}

}
