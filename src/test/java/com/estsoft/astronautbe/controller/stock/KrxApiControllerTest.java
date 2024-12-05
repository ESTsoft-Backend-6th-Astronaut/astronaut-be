package com.estsoft.astronautbe.controller.stock;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.estsoft.astronautbe.service.KrxApiService;

@ExtendWith(MockitoExtension.class)
class KrxApiControllerTest {

	private MockMvc mockMvc;

	@Mock
	private KrxApiService krxApiService;

	@InjectMocks
	private KrxApiController krxApiController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(krxApiController).build();
	}

	@Test
	void testKrxApi() throws Exception {
		// given
		String mockResponse = "mockResponse";
		when(krxApiService.getKrxDataForYesterday()).thenReturn(mockResponse);

		// when
		ResultActions resultActions = mockMvc.perform(get("/krx-data")
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().string(mockResponse));

		verify(krxApiService, times(1)).getKrxDataForYesterday();
	}
}