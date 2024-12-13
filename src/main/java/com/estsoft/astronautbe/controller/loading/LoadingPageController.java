package com.estsoft.astronautbe.controller.loading;

import com.estsoft.astronautbe.domain.FamousQuote;
import com.estsoft.astronautbe.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadingPageController {

    @Autowired
    private QuoteRepository quoteRepository;

    @GetMapping("/api/random-quote")
    public FamousQuote getRandomQuote() {
        return quoteRepository.findRandomQuote();
    }
}