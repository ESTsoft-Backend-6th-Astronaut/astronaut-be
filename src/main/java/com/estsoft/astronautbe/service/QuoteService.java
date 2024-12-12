package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.FamousQuote;
import com.estsoft.astronautbe.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {

        this.quoteRepository = quoteRepository;
    }

    public List<FamousQuote> getQuotes() {

        return quoteRepository.findAll().subList(0, 3);
    }
}