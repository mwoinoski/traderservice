package com.acme.acmetrade.endpoints;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.acme.acmetrade.services.MockQuotesGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*; // any(), any(MyClass.class), anyString(), anyInt(), etc.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test just the web layer without a full application context. 
 * 
 * To test with the full application context, replace the @WebMvcTest annotation with this:
 * @SpringBootTest
 * @AutoConfigureMockMvc
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MockQuotesGeneratorEndpoint.class)
public class MockQuotesGeneratorEndpointTest {

	@Autowired
	private MockMvc mockMvc;
	
    @MockBean
    private MockQuotesGenerator mockGenerator;
    
    @Test
    public void generateMockQuotes_Success() throws Exception {
    	when(mockGenerator.generateQuotes(any(), any(), any(), any()))
    		.thenReturn("AAPL_20190101_100_LinearIncrease.csv");

    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
	        								"AAPL", "2019-01-01", 100, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isCreated())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.filename").value("AAPL_20190101_100_LinearIncrease.csv"));
    }
    
    @Test
    public void generateMockQuotes_FunctionNameWithNumbersAndUnderscores_Success() throws Exception {
    	when(mockGenerator.generateQuotes(any(), any(), any(), any()))
    		.thenReturn("AAPL_20190101_100_LinearIncrease.csv");

    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
	        								"AAPL", "2019-01-01", 100, "Linear_Increase_Percentage_0_5")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isCreated())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.filename").value("AAPL_20190101_100_LinearIncrease.csv"));
    }

    @Test
    public void generateMockQuotes_GeneratorThrowsException() throws Exception {
    	when(mockGenerator.generateQuotes(any(), any(), any(), any()))
    		.thenThrow(new IllegalArgumentException());

    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
	        								"AAPL", "2019-01-01", 100, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isInternalServerError());
    }

    @Test
    public void generateMockQuotes_SymbolNull() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											null, "2019-01-01", 100, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_SymbolEmpty() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"", "2019-01-01", 100, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_StartDateMissing() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"GOOG", null, 100, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_DaysMissing() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"GOOG", "2019-01-01", null, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_FunctionMissing() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"GOOG", "2019-01-01", 100, null)))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_SymbolTooLong() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"GOOGGOO", "2019-01-01", 100, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_DateInvalidFormat() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"GOOG", "01/01/2019", 100, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_DaysGreaterThanMax() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"GOOG", "2019-01-01", 10000, "LinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }

    @Test
    public void generateMockQuotes_FunctionNameTooLong() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders
        	.post("/mock-quotes")
	        	.content(asJsonString(new MockQuotesGeneratorEndpoint.GenerateMockQuotesRequest(
											"GOOG", "2019-01-01", 10000, 
											"LinearIncreaseLinearIncreaseLinearIncreaseLinearIncreaseLinearIncrease")))
	        	.contentType(MediaType.APPLICATION_JSON)
	        	.accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().is4xxClientError());

    	verify(mockGenerator, never()).generateQuotes(any(), any(), any(), any());
    }
    
    private static String asJsonString(final Object obj) {
    	try {
    		return new ObjectMapper().writeValueAsString(obj);
    	}
    	catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }

}
