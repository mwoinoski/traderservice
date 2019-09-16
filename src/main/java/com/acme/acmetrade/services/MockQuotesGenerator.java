package com.acme.acmetrade.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class MockQuotesGenerator {
	private static Logger logger = LoggerFactory.getLogger(MockQuotesGenerator.class);
	
	@Value("${mock.quotes.generator.dir}") // read from application.properties
	private String mockQuotesGeneratorDir;
	
	// Spring will inject all beans that implement MockQuoteGeneratorStrategy into the Map.
	// The key is the bean's name, the value is the bean itself.
	@Autowired
	private Map<String, MockQuoteGeneratorStrategy> quoteGeneratorStrategies;
	
	public List<String> getSupportedFunctions() {
		return quoteGeneratorStrategies.keySet().stream().sorted().collect(Collectors.toList());
	}

	/**
	 * Generate mock quotes. The REST endpoint will validate all args before it calls this method,
	 * so don't duplicate the validations here.
	 * 
	 * @param symbol Ticker symbol that we're generating quotes for.
	 * @param startDate Date of first mock quote.
	 * @param days Number of days.
	 * @param func Function used to generate quotes.
	 * @return name of file that contains the generated quotes.
	 */
	public String generateQuotes(String symbol, 
								 LocalDate startDate, 
								 Integer days, 
								 QuoteGeneratorFunction func) {
		logger.debug("enter generateQuotes({}, {}, {}, {})", symbol, startDate, days, func);

		String date = startDate.format(DateTimeFormatter.BASIC_ISO_DATE);
		String filename = String.format("%s_%s_%s_%s.json", symbol, date, days, func);

		MockQuoteGeneratorStrategy strategy = quoteGeneratorStrategies.get(WordUtils.uncapitalize(func.name()));
		List<String> quotesJson = strategy.generateMockQuotes(symbol, startDate, days, func);
		//TODO: write quotes to file
//		String filepath = String.format("%s/%s", mockQuotesGeneratorDir, filename);
//		BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
//		quotesJson.forEach(q -> writer.write(q));
//	    writer.close();
		return filename;
	}

}
