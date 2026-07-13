package com.hssa.readingclub.service.impl;

import com.hssa.exception.ResourceNotFoundException;
import com.hssa.readingclub.dto.request.CreateBookRequest;
import com.hssa.readingclub.dto.request.UpdateBookRequest;
import com.hssa.readingclub.dto.response.BookCategoryResponse;
import com.hssa.readingclub.dto.response.BookDetailResponse;
import com.hssa.readingclub.dto.response.BookSummaryResponse;
import com.hssa.readingclub.entity.Book;
import com.hssa.readingclub.entity.BookCategory;
import com.hssa.readingclub.repository.BookCategoryRepository;
import com.hssa.readingclub.repository.BookRepository;
import com.hssa.readingclub.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookCategoryRepository categoryRepository;

    @Override
    @Transactional
    public BookDetailResponse createBook(CreateBookRequest request) {
        BookCategory category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .publisher(request.getPublisher())
                .publicationYear(request.getPublicationYear())
                .language(request.getLanguage())
                .category(category)
                .coverMediaId(request.getCoverMediaId())
                .description(request.getDescription())
                .totalPages(request.getTotalPages())
                .pdfMediaId(request.getPdfMediaId())
                .externalLink(request.getExternalLink())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isActive(true)
                .build();
        book = bookRepository.save(book);
        return toDetailResponse(book);
    }

    @Override
    @Transactional
    public BookDetailResponse updateBook(UUID bookId, UpdateBookRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getPublisher() != null) book.setPublisher(request.getPublisher());
        if (request.getPublicationYear() != null) book.setPublicationYear(request.getPublicationYear());
        if (request.getLanguage() != null) book.setLanguage(request.getLanguage());
        if (request.getCategoryId() != null) {
            BookCategory cat = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            book.setCategory(cat);
        }
        if (request.getCoverMediaId() != null) book.setCoverMediaId(request.getCoverMediaId());
        if (request.getDescription() != null) book.setDescription(request.getDescription());
        if (request.getTotalPages() != null) book.setTotalPages(request.getTotalPages());
        if (request.getPdfMediaId() != null) book.setPdfMediaId(request.getPdfMediaId());
        if (request.getExternalLink() != null) book.setExternalLink(request.getExternalLink());
        if (request.getIsFeatured() != null) book.setIsFeatured(request.getIsFeatured());
        if (request.getIsActive() != null) book.setIsActive(request.getIsActive());
        book = bookRepository.save(book);
        return toDetailResponse(book);
    }

    @Override
    @Transactional
    public void deleteBook(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setIsActive(false);
        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDetailResponse getBook(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return toDetailResponse(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookSummaryResponse> listBooks(String title, Boolean isFeatured, Pageable pageable) {
        return bookRepository.findAll(pageable).map(b -> BookSummaryResponse.builder()
                .id(b.getId())
                .title(b.getTitle())
                .author(b.getAuthor())
                .isbn(b.getIsbn())
                .categoryName(b.getCategory() != null ? b.getCategory().getName() : null)
                .isFeatured(b.getIsFeatured())
                .coverMediaId(b.getCoverMediaId())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCategoryResponse> listCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> BookCategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .description(c.getDescription())
                        .parentId(c.getParent() != null ? c.getParent().getId() : null)
                        .build())
                .collect(Collectors.toList());
    }

    private BookDetailResponse toDetailResponse(Book b) {
        return BookDetailResponse.builder()
                .id(b.getId())
                .title(b.getTitle())
                .author(b.getAuthor())
                .isbn(b.getIsbn())
                .publisher(b.getPublisher())
                .publicationYear(b.getPublicationYear())
                .language(b.getLanguage())
                .coverMediaId(b.getCoverMediaId())
                .description(b.getDescription())
                .totalPages(b.getTotalPages())
                .pdfMediaId(b.getPdfMediaId())
                .externalLink(b.getExternalLink())
                .isFeatured(b.getIsFeatured())
                .isActive(b.getIsActive())
                .categoryName(b.getCategory() != null ? b.getCategory().getName() : null)
                .build();
    }
}
