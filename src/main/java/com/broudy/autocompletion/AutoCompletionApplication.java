package com.broudy.autocompletion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.broudy.autocompletion.repository.EntryRepository;
import com.broudy.autocompletion.service.CachedEntries;

@SpringBootApplication
public class AutoCompletionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoCompletionApplication.class, args);
	}

	@Component
	class AutoCompletionCommandLineRunner implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			CachedEntries cachedEntries = CachedEntries.getInstance();
			cachedEntries.cacheEntries(entryRepository.findAll());
		}

		@Autowired
		EntryRepository entryRepository;

	}

}
