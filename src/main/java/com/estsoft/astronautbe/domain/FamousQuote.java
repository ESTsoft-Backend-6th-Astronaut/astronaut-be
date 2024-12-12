package com.estsoft.astronautbe.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "famous_quote")
@Getter
@Setter
@NoArgsConstructor
public class FamousQuote { // quote entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id", nullable = false)
    private Integer quoteId;

    @Column(name = "quote_content", columnDefinition = "VARCHAR(255)", nullable = false)
    private String quoteContent;

    @Column(name = "quote_author", columnDefinition = "VARCHAR(255)", nullable = false)
    private String quoteAuthor;
}
