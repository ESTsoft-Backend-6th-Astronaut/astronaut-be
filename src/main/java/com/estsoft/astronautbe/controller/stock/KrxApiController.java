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

	@GetMapping("/krx-data")
	public ResponseEntity<String> krxApi() {
		String response = krxApiService.getKrxDataForYesterday();
		krxApiService.saveKrxData(response);
		return ResponseEntity.ok(response);
	}
}
