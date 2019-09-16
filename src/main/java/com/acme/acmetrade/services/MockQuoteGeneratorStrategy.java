package com.acme.acmetrade.services;

import java.time.LocalDate;
import java.util.List;

public interface MockQuoteGeneratorStrategy {
	/**
	 * Generate mock quotes.
	 * @param symbol Ticker symbol that we're generating quotes for.
	 * @param startDate Date of first mock quote.
	 * @param days Number of days.
	 * @param func Function used to generate quotes.
	 * @return The generate quotes as a list of JSON strings.
	 */
	List<String> generateMockQuotes(String symbol, LocalDate startDate, 
									Integer days, QuoteGeneratorFunction func);
	/**
	 * Get the metadata for this strategy.
	 * @return this strategy's metadata.
	 */
	String getMetaData();
}
