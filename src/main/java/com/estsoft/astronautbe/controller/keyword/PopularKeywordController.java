package com.estsoft.astronautbe.controller.keyword;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.service.PopularKeywordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/popular")
public class PopularKeywordController {

    private final PopularKeywordService popularKeywordService;

    public PopularKeywordController(PopularKeywordService popularKeywordService) {
        this.popularKeywordService = popularKeywordService;
    }

    @GetMapping("/yesterday")
    public ResponseEntity<List<Keyword>> getYesterdayPopularKeywords() {
        List<Keyword> keywords = popularKeywordService.getYesterdayPopularKeywords();
        return ResponseEntity.ok(keywords);
    }
}