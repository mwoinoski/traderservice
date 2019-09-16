package com.acme.acmetrade.endpoints;

import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.acme.acmetrade.services.MockQuotesGenerator;
import com.acme.acmetrade.services.QuoteGeneratorFunction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.Data;
import lombok.AllArgsConstructor;

@Api(value="ACME Trader Mock Quote Generator")
@RestController
@Validated
public class MockQuotesGeneratorEndpoint {
	private static Logger logger = LoggerFactory.getLogger(MockQuotesGeneratorEndpoint.class);

	@Autowired
	private MockQuotesGenerator mockQuotesGenerator;

	@ApiOperation(value="Gets the current version of the endpoint", 
			  response=GetVersionResponse.class)
	@GetMapping("/version")
	public GetVersionResponse getVersion() {
		return new GetVersionResponse("1");
	}

	@Data
	@AllArgsConstructor
	public static class GetVersionResponse {
		private String version;
	}

	@ApiOperation(value="Generates mock quotes for a given ticker symbol", 
			      response=GenerateMockQuotesResponse.class)
	@ApiResponses(value = {
		@ApiResponse(code=201, message="Successfully generated mock quotes file"),
		// other response status codes are defined globally in SwaggerConfig.java
	})
	@PostMapping(value = "/mock-quotes", 
				 consumes = "application/json", 
				 produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public GenerateMockQuotesResponse generateMockQuotes(@RequestBody @Valid GenerateMockQuotesRequest req) {
		logger.debug("req=" + req);
		QuoteGeneratorFunction func = QuoteGeneratorFunction.valueOf(req.getFunction());
		try {
			String filename = mockQuotesGenerator.generateQuotes(req.getSymbol(), 
								LocalDate.parse(req.getStartDate()), req.getDays(), func);
			return new GenerateMockQuotesResponse(filename);
		}
		catch (Exception e) {
			String msg = "Problem generating mock quotes";
			logger.error(msg, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg, e);
		}
	}

	@ApiModel("DTO for request to generate mock quotes")
	@Data
	@AllArgsConstructor
	public static class GenerateMockQuotesRequest {
		@ApiModelProperty("The ticker symbol")
		@NotNull @Pattern(regexp = "^\\w{1,6}$") 
		private String symbol;
		
		@ApiModelProperty("The start date in yyyy-MM-dd format")
		@NotNull @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") 
		private String startDate;
		
		@ApiModelProperty("The number of days to generate: min 1, max 10958 (30 years)")
		@NotNull @Min(1) @Max(10958) 
		private Integer days;
		
		@ApiModelProperty("The generator function to use. For a list of valid function names, " +
						  "send a request to /functions")
		@NotNull @Pattern(regexp = "^[\\w]{1,64}$") 
		private String function;
	}

	@Data
	@AllArgsConstructor
	public static class GenerateMockQuotesResponse {
		private String filename;
	}

	@ApiOperation(value="Gets a list of support generator functions", 
				  response=GetSupportedFunctionsResponse.class)
	@GetMapping("/functions")
	public GetSupportedFunctionsResponse getSupportedFunctions() {
		return new GetSupportedFunctionsResponse(mockQuotesGenerator.getSupportedFunctions());
	}
	
	@Data
	@AllArgsConstructor
	public static class GetSupportedFunctionsResponse {
		private List<String> functions;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
		return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
