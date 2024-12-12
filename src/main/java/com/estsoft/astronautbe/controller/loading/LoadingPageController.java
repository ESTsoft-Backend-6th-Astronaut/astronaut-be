package com.estsoft.astronautbe.controller.loading;

import com.estsoft.astronautbe.domain.FamousQuote;
import com.estsoft.astronautbe.service.QuoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoadingPageController {

    private final QuoteService quoteService;

    public LoadingPageController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/loading/quotes")
    public List<FamousQuote> getQuotes() {
        return quoteService.getQuotes();
    }
}