package com.backend.spring.repositories;

import com.backend.spring.entities.User;
import com.backend.spring.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByUser(User user);

}

