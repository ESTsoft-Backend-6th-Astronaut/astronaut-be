package com.estsoft.astronautbe.config;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.service.KeywordService;
import com.estsoft.astronautbe.service.KrxApiService;
import com.estsoft.astronautbe.service.PopularKeywordService;
import com.estsoft.astronautbe.service.keywordNews.KeywordNewsService;

import jakarta.transaction.Transactional;

@Component
public class Scheduler {

	private final KrxApiService krxApiService;
	private final PopularKeywordService popularKeywordService;
	private final KeywordService keywordService;
	private final KeywordNewsService keywordNewsService;

	public Scheduler(KrxApiService krxApiService, PopularKeywordService popularKeywordService,
		KeywordService keywordService, KeywordNewsService keywordNewsService) {
		this.krxApiService = krxApiService;
		this.popularKeywordService = popularKeywordService;
		this.keywordService = keywordService;
		this.keywordNewsService = keywordNewsService;
	}

	@Transactional
	@Scheduled(cron = "0 30 5 * * ?")
	public void stockSchedule() {
		String responseKospi = krxApiService.getKrxKospiDataForYesterday();
		krxApiService.saveKrxData(responseKospi);

		String responseKosdaq = krxApiService.getKrxKosdaqDataForYesterday();
		krxApiService.saveKrxData(responseKosdaq);
	}

	@Transactional
	@Scheduled(cron = "0 0 6 * * ?")
	public void keywordSchedule() {
		List<Keyword> keywords = popularKeywordService.getYesterdayPopularKeywords();
		keywordService.processKeywordRecommendation();
		keywordNewsService.processKeywordNews();
	}
}
