package com.estsoft.astronautbe.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "famous_quote")
@Getter
@NoArgsConstructor
public class FamousQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id", nullable = false)
    private Integer quoteId;

    @Column(name = "quote_content", columnDefinition = "VARCHAR(255)", nullable = false)
    private String quoteContent;

    @Column(name = "quote_author", columnDefinition = "VARCHAR(255)", nullable = false)
    private String quoteAuthor;
}
