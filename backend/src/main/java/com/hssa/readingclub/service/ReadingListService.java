package com.hssa.readingclub.service;

import com.hssa.readingclub.dto.request.CreateReadingListRequest;
import com.hssa.readingclub.dto.response.ReadingListResponse;

import java.util.List;
import java.util.UUID;

public interface ReadingListService {
    ReadingListResponse createReadingList(CreateReadingListRequest request, UUID userId);
    ReadingListResponse getReadingList(UUID listId);
    List<ReadingListResponse> getUserReadingLists(UUID userId);
    void addBookToList(UUID listId, UUID bookId);
    void removeBookFromList(UUID listId, UUID bookId);
    void deleteReadingList(UUID listId);
}
