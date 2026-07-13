package com.hssa.publication.entity;

import com.hssa.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "publication_keywords", schema = "publication")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationKeyword extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String keyword;
}
