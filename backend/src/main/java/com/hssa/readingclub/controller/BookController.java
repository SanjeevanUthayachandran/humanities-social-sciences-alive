package com.hssa.readingclub.controller;

import com.hssa.readingclub.dto.request.CreateBookRequest;
import com.hssa.readingclub.dto.request.UpdateBookRequest;
import com.hssa.readingclub.dto.response.BookCategoryResponse;
import com.hssa.readingclub.dto.response.BookDetailResponse;
import com.hssa.readingclub.dto.response.BookSummaryResponse;
import com.hssa.readingclub.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<BookDetailResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<BookDetailResponse> updateBook(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> getBook(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping
    public ResponseEntity<Page<BookSummaryResponse>> listBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean isFeatured,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.listBooks(title, isFeatured, pageable));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BookCategoryResponse>> listCategories() {
        return ResponseEntity.ok(bookService.listCategories());
    }
}
