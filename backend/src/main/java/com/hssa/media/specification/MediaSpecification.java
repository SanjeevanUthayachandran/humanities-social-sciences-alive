package com.hssa.media.specification;

import com.hssa.media.dto.request.SearchMediaRequest;
import com.hssa.media.entity.MediaFile;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MediaSpecification {

    public static Specification<MediaFile> filterBy(SearchMediaRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Exclude soft deleted
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("originalName")), keyword),
                        cb.like(cb.lower(root.get("caption")), keyword),
                        cb.like(cb.lower(root.get("altText")), keyword)
                ));
            }

            if (request.getMediaType() != null && !request.getMediaType().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("mediaType").as(String.class), request.getMediaType().trim().toUpperCase()));
            }

            if (request.getExtension() != null && !request.getExtension().trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("fileExtension")), request.getExtension().trim().toLowerCase()));
            }

            if (request.getIsPublic() != null) {
                predicates.add(cb.equal(root.get("isPublic"), request.getIsPublic()));
            }

            if (request.getOwnerId() != null) {
                predicates.add(cb.equal(root.get("uploadedBy").get("id"), request.getOwnerId()));
            }

            if (request.getFolderId() != null) {
                predicates.add(cb.equal(root.get("folderId"), request.getFolderId()));
            }

            if (request.getCreatedAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedAfter()));
            }

            if (request.getCreatedBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getCreatedBefore()));
            }

            if (request.getMinSize() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fileSize"), request.getMinSize()));
            }

            if (request.getMaxSize() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fileSize"), request.getMaxSize()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
