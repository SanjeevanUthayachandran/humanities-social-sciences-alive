package com.hssa.readingclub.repository;

import com.hssa.readingclub.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
