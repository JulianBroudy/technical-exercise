package com.broudy.autocompletion.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broudy.autocompletion.model.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {

	Collection<Entry> findByName(String name);

}
