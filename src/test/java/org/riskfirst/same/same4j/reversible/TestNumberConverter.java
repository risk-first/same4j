package org.riskfirst.same.same4j.reversible;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.reversible.types.Numbers;


public class TestNumberConverter {

	@Test
	public void testSimpleConversion() {
		Assert.assertEquals((Integer) 54, (Integer) Numbers.STRING_TO_INTEGER.apply("54"));
		Assert.assertEquals("-54", Numbers.STRING_TO_INTEGER.inverse(-54));
	}
	
	@Test 
	public void testCustomFormat() {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.setDecimalFormatSymbols(symbols);
		ReversibleFunction<String, Number> c = Numbers.stringToNumber(formatter);
		Assert.assertEquals((Double) 54583.2, (Double) c.apply("54,583.2"));
		Assert.assertEquals("-548,473", c.inverse(-548473));
		
	}
}
