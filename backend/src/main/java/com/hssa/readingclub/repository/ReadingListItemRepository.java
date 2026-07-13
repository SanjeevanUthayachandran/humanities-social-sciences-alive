package com.hssa.readingclub.repository;

import com.hssa.readingclub.entity.ReadingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ReadingListItemRepository extends JpaRepository<ReadingListItem, UUID> {
    List<ReadingListItem> findByReadingListId(UUID readingListId);
    void deleteByReadingListIdAndBookId(UUID readingListId, UUID bookId);
}
