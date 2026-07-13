package com.hssa.literary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "literary_article_tags", schema = "cms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiteraryArticleTag {

    @EmbeddedId
    private LiteraryArticleTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private LiteraryArticle article;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private LiteraryTag tag;
}
