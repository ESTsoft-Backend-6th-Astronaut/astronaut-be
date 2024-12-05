package com.estsoft.astronautbe.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.estsoft.astronautbe.service.KrxApiService;

@Component
public class Scheduler {

	private final KrxApiService krxApiService;

	public Scheduler(KrxApiService krxApiService) {
		this.krxApiService = krxApiService;
	}

	@Scheduled(cron = "0 50 10 * * ?")
	public void schedule() {
		String response = krxApiService.getKrxDataForYesterday();
		krxApiService.saveKrxData(response);
	}
}
