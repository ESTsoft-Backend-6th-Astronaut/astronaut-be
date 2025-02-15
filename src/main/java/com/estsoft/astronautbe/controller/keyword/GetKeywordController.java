package com.estsoft.astronautbe.controller.keyword;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.service.GetKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
public class GetKeywordController {
    @Autowired
    private GetKeywordService getKeywordService;

    @GetMapping("/get_today")
    public List<Keyword> getTodayKeywords() { return getKeywordService.getTodayKeywords(); }

    @GetMapping("/interesting")
    public List<Keyword> getInterestingKeywords() {
        return getKeywordService.getInterestingKeywords();
    }
}