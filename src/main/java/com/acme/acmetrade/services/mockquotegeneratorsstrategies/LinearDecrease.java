package com.acme.acmetrade.services.mockquotegeneratorsstrategies;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acme.acmetrade.services.MockQuoteGeneratorStrategy;
import com.acme.acmetrade.services.QuoteGeneratorFunction;

@Component
public class LinearDecrease implements MockQuoteGeneratorStrategy {
	private static Logger logger = LoggerFactory.getLogger(MockQuoteGeneratorStrategy.class);

	@Override
	public List<String> generateMockQuotes(String symbol, LocalDate startDate, Integer days,
			QuoteGeneratorFunction func) {
		logger.debug("enter generateMockQuotes({}, {}, {}, {})", symbol, startDate, days, func);
		return new ArrayList<String>();
	}

	@Override
	public String getMetaData() {
		return "constant linear increase";
	}

}
