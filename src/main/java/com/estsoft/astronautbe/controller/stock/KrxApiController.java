package com.estsoft.astronautbe.controller.stock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.service.KrxApiService;

@RestController
public class KrxApiController {

	private final KrxApiService krxApiService;

	public KrxApiController(KrxApiService krxApiService) {
		this.krxApiService = krxApiService;
	}

	@GetMapping("/krx-data/kospi")
	public ResponseEntity<String> krxKospiApi() {
		String response = krxApiService.getKrxKospiDataForYesterday();
		krxApiService.saveKrxData(response);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/krx-data/kosdaq")
	public ResponseEntity<String> krxKosdaqApi() {
		String response = krxApiService.getKrxKosdaqDataForYesterday();
		krxApiService.saveKrxData(response);
		return ResponseEntity.ok(response);
	}
}
