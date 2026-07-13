package com.hssa.readingclub.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.readingclub.dto.request.CreateReadingListRequest;
import com.hssa.readingclub.dto.response.BookSummaryResponse;
import com.hssa.readingclub.dto.response.ReadingListResponse;
import com.hssa.readingclub.entity.Book;
import com.hssa.readingclub.entity.ReadingList;
import com.hssa.readingclub.entity.ReadingListItem;
import com.hssa.readingclub.repository.BookRepository;
import com.hssa.readingclub.repository.ReadingListItemRepository;
import com.hssa.readingclub.repository.ReadingListRepository;
import com.hssa.readingclub.service.ReadingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadingListServiceImpl implements ReadingListService {

    private final ReadingListRepository readingListRepository;
    private final ReadingListItemRepository readingListItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReadingListResponse createReadingList(CreateReadingListRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ReadingList list = ReadingList.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .build();
        list = readingListRepository.save(list);
        return buildResponse(list);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadingListResponse getReadingList(UUID listId) {
        ReadingList list = readingListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Reading list not found"));
        return buildResponse(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadingListResponse> getUserReadingLists(UUID userId) {
        return readingListRepository.findByUserId(userId).stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addBookToList(UUID listId, UUID bookId) {
        ReadingList list = readingListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Reading list not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        ReadingListItem item = ReadingListItem.builder()
                .readingList(list)
                .book(book)
                .build();
        readingListItemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeBookFromList(UUID listId, UUID bookId) {
        readingListItemRepository.deleteByReadingListIdAndBookId(listId, bookId);
    }

    @Override
    @Transactional
    public void deleteReadingList(UUID listId) {
        ReadingList list = readingListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Reading list not found"));
        readingListRepository.delete(list);
    }

    private ReadingListResponse buildResponse(ReadingList list) {
        List<BookSummaryResponse> books = readingListItemRepository.findByReadingListId(list.getId()).stream()
                .map(item -> BookSummaryResponse.builder()
                        .id(item.getBook().getId())
                        .title(item.getBook().getTitle())
                        .author(item.getBook().getAuthor())
                        .isbn(item.getBook().getIsbn())
                        .coverMediaId(item.getBook().getCoverMediaId())
                        .isFeatured(item.getBook().getIsFeatured())
                        .build())
                .collect(Collectors.toList());
        return ReadingListResponse.builder()
                .id(list.getId())
                .title(list.getTitle())
                .description(list.getDescription())
                .isPublic(list.getIsPublic())
                .createdAt(list.getCreatedAt())
                .books(books)
                .build();
    }
}
