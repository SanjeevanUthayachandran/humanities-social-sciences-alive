package com.hssa.readingclub.controller;

import com.hssa.auth.entity.User;
import com.hssa.readingclub.dto.request.CreateReadingListRequest;
import com.hssa.readingclub.dto.response.ReadingListResponse;
import com.hssa.readingclub.service.ReadingListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reading-lists")
@RequiredArgsConstructor
public class ReadingListController {

    private final ReadingListService readingListService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReadingListResponse> createReadingList(
            @Valid @RequestBody CreateReadingListRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(readingListService.createReadingList(request, user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingListResponse> getReadingList(@PathVariable UUID id) {
        return ResponseEntity.ok(readingListService.getReadingList(id));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReadingListResponse>> getMyReadingLists(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(readingListService.getUserReadingLists(user.getId()));
    }

    @PostMapping("/{listId}/books/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addBookToList(
            @PathVariable UUID listId,
            @PathVariable UUID bookId) {
        readingListService.addBookToList(listId, bookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{listId}/books/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeBookFromList(
            @PathVariable UUID listId,
            @PathVariable UUID bookId) {
        readingListService.removeBookFromList(listId, bookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReadingList(@PathVariable UUID id) {
        readingListService.deleteReadingList(id);
        return ResponseEntity.noContent().build();
    }
}
