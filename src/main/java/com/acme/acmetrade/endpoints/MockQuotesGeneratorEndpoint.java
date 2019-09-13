package com.acme.acmetrade.endpoints;

import java.time.LocalDate;

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

import lombok.*;

@RestController
@Validated
public class MockQuotesGeneratorEndpoint {
	private static Logger logger = LoggerFactory.getLogger(MockQuotesGeneratorEndpoint.class);

	@Autowired
	private MockQuotesGenerator mockQuotesGenerator;

	@GetMapping(value = "/version", produces = "application/json")
	public GetVersionResponse getVersion() {
		return new GetVersionResponse("1");
	}

	@Data
	public static class GetVersionResponse {
		private @NonNull String version;
	}

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

	@Data
	@AllArgsConstructor
	public static class GenerateMockQuotesRequest {
		private @NotNull @Pattern(regexp = "^\\w{1,6}$") String symbol;
		private @NotNull @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String startDate;
		private @NotNull @Min(1) @Max(3650) Integer days;
		private @NotNull @Pattern(regexp = "^[\\w]{1,50}$") String function;
	}

	@Data
	@AllArgsConstructor
	public static class GenerateMockQuotesResponse {
		private @NotNull String filename;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
		return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
