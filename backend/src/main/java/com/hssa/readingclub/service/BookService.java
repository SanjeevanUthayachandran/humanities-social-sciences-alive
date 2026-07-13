package com.hssa.readingclub.service;

import com.hssa.readingclub.dto.request.CreateBookRequest;
import com.hssa.readingclub.dto.request.UpdateBookRequest;
import com.hssa.readingclub.dto.response.BookCategoryResponse;
import com.hssa.readingclub.dto.response.BookDetailResponse;
import com.hssa.readingclub.dto.response.BookSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BookService {
    BookDetailResponse createBook(CreateBookRequest request);
    BookDetailResponse updateBook(UUID bookId, UpdateBookRequest request);
    void deleteBook(UUID bookId);
    BookDetailResponse getBook(UUID bookId);
    Page<BookSummaryResponse> listBooks(String title, Boolean isFeatured, Pageable pageable);
    List<BookCategoryResponse> listCategories();
}
