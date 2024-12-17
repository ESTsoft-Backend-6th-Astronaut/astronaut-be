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

import com.estsoft.astronautbe.service.keywordNews.AllenAIService;

@ExtendWith(MockitoExtension.class)
class AllenAIControllerTest {

	private MockMvc mockMvc;

	@Mock
	private AllenAIService allenAIService;

	@InjectMocks
	private AllenAIController allenAIController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(allenAIController).build();
	}

	@Test
	void testAllenApi() throws Exception {
		// given
		String mockResponse = "This is a mock response from Allen AI";
		when(allenAIService.getAnswerFromAllenAI(1L)).thenReturn(mockResponse);

		// when
		ResultActions resultActions = mockMvc.perform(get("/allen_api")
			.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().string(mockResponse));

		verify(allenAIService, times(1)).getAnswerFromAllenAI(1L);
	}
}