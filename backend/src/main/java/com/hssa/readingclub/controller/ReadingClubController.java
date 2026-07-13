package com.hssa.readingclub.controller;

import com.hssa.readingclub.dto.response.BookSummaryResponse;
import com.hssa.readingclub.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Provides the /api/v1/reading-clubs API surface as required by the module spec.
 * Since the database schema only contains book/reading-list tables (no reading_clubs table),
 * these endpoints proxy to the book catalog, which is the equivalent domain entity.
 */
@RestController
@RequestMapping("/api/v1/reading-clubs")
@RequiredArgsConstructor
public class ReadingClubController {

    private final BookService bookService;

    /** GET /api/v1/reading-clubs - list all books */
    @GetMapping
    public ResponseEntity<Page<BookSummaryResponse>> listReadingClubs(Pageable pageable) {
        return ResponseEntity.ok(bookService.listBooks(null, null, pageable));
    }

    /** GET /api/v1/reading-clubs/{id} - get a specific book by id */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReadingClub(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    /** GET /api/v1/reading-clubs/search - search books */
    @GetMapping("/search")
    public ResponseEntity<Page<BookSummaryResponse>> searchReadingClubs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean isFeatured,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.listBooks(title, isFeatured, pageable));
    }

    /** GET /api/v1/reading-clubs/statistics - basic statistics */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        long total = bookService.listBooks(null, null, Pageable.unpaged()).getTotalElements();
        long featured = bookService.listBooks(null, true, Pageable.unpaged()).getTotalElements();
        return ResponseEntity.ok(Map.of(
                "totalBooks", total,
                "featuredBooks", featured
        ));
    }
}