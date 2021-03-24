package com.broudy.autocompletion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.broudy.autocompletion.controller.AppController;
import com.broudy.autocompletion.controller.EntryController;

@SpringBootTest
class AutoCompletionApplicationTests {

	@Autowired
	private AppController appController;
	@Autowired
	private EntryController entryController;

	@Test
	void contextLoads() {
		assertThat(appController).isNotNull();
		assertThat(entryController).isNotNull();
	}

}
