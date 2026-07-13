package com.hssa.literary.specification;

import com.hssa.literary.entity.LiteraryArticle;
import com.hssa.literary.enums.ArticleType;
import com.hssa.literary.enums.ContentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LiterarySpecification {

    public static Specification<LiteraryArticle> filterBy(
            String title, String status, String articleType, UUID categoryId, UUID authorId, Boolean isFeatured) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), ContentStatus.valueOf(status.toUpperCase())));
            }
            if (articleType != null && !articleType.isBlank()) {
                predicates.add(cb.equal(root.get("articleType"), ArticleType.valueOf(articleType.toUpperCase())));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (authorId != null) {
                predicates.add(cb.equal(root.get("author").get("id"), authorId));
            }
            if (isFeatured != null) {
                predicates.add(cb.equal(root.get("isFeatured"), isFeatured));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
