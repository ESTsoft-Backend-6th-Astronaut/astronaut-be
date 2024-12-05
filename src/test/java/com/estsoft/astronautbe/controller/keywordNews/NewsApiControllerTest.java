package com.estsoft.astronautbe.controller.keywordNews;

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

import com.estsoft.astronautbe.service.keywordNews.NaverNewsApiService;

@ExtendWith(MockitoExtension.class)
class NewsApiControllerTest {

	private MockMvc mockMvc;

	@Mock
	private NaverNewsApiService service;

	@InjectMocks
	private NewsApiController controller;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void testNewsApi() throws Exception {
		// given
		String query = "AI";
		String mockResponse = "{\"items\":[{\"title\":\"News Title\",\"link\":\"http://example.com/news\"}]}";
		when(service.getNews(anyString())).thenReturn(mockResponse);

		// when
		ResultActions resultActions = mockMvc.perform(get("/news_api")
			.param("query", query)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(mockResponse));

		verify(service, times(1)).getNews(anyString());
	}
}