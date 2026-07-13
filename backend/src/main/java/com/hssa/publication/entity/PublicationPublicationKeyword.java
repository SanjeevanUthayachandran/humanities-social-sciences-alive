package com.hssa.publication.entity;

import com.hssa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publication_keyword_map", schema = "publication", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"publication_id", "keyword_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationPublicationKeyword extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private PublicationKeyword keyword;
}
