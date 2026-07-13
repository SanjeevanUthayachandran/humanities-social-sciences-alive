package com.hssa.research.specification;

import com.hssa.research.entity.ResearchProject;
import com.hssa.research.enums.ResearchStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResearchSpecification {

    public static Specification<ResearchProject> filterBy(String title, String status, String keyword, LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (title != null && !title.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), ResearchStatus.valueOf(status.trim().toUpperCase())));
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                // Assuming we join on researchProjectKeywords and keywords
                // This is a simplified specification. More complex join required if actually used via keyword string
                // Example for actual query (requires JPA Metamodel or explicit strings):
                // Join<ResearchProject, ResearchProjectKeyword> rpKeywords = root.join("projectKeywords", JoinType.INNER);
                // Join<ResearchProjectKeyword, ResearchKeyword> keywords = rpKeywords.join("keyword", JoinType.INNER);
                // predicates.add(cb.like(cb.lower(keywords.get("keyword")), "%" + keyword.toLowerCase() + "%"));
                // But since we don't have bi-directional mapping set up, we'll skip this or handle it in service/repo layer
            }

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("publishedDate"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("publishedDate"), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
