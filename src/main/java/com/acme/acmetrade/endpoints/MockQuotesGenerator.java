package com.acme.acmetrade.endpoints;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.NonNull;

@Component
@Validated
public class MockQuotesGenerator {
	@Value("${mock.quotes.generator.dir}") // read from application.properties
	private String mockQuotesGeneratorDir;

	public String generateQuotes(@NonNull String symbol, @NonNull LocalDate startDate, 
								 @NonNull Integer days, @NonNull QuoteGeneratorFunction func) {
		String date = startDate.format(DateTimeFormatter.BASIC_ISO_DATE);
		String filename = String.format("%s_%s_%s_%s.csv", symbol, date, days, func);
		//TODO: generate quotes
//		List<String> quotesCsv = new ArrayList<>();
		//TODO: write quotes to file
//		String filepath = String.format("%s/%s", mockQuotesGeneratorDir, filename);
//		BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
//		quotesCsv.forEach(q -> writer.write(q));
//	    writer.close();
		return filename;
	}

}
