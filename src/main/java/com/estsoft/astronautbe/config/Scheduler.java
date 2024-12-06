package com.estsoft.astronautbe.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.estsoft.astronautbe.service.KrxApiService;

import jakarta.transaction.Transactional;

@Component
public class Scheduler {

	private final KrxApiService krxApiService;

	public Scheduler(KrxApiService krxApiService) {
		this.krxApiService = krxApiService;
	}

	@Transactional
	@Scheduled(cron = "0 10 11 * * ?")
	public void schedule() {
		String responseKospi = krxApiService.getKrxKospiDataForYesterday();
		krxApiService.saveKrxData(responseKospi);

		String responseKosdaq = krxApiService.getKrxKosdaqDataForYesterday();
		krxApiService.saveKrxData(responseKosdaq);
	}
}
