package org.riskfirst.same.same4j;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class TestCamelCaseConverter {
	
	static ReversibleFunction<String, Stream<CharSequence>> CAMEL_SPLITTER = Same.stream(
		s -> Arrays.stream(s.split("(?=\\p{Upper})")),
		Collectors.joining(""));
		
	static ReversibleFunction<String, Stream<CharSequence>> UNDERSCORE_SPLITTER = Same.stream(
		s -> Arrays.stream(s.split("(?=_)")) ,	// split but keep underscore
		Collectors.joining());
	
	static ReversibleFunction<CharSequence, String> CASTER = Same.cast(CharSequence.class, String.class);
	
	// can reverse any individual word in the camel case
	// nb, won't handle nulls or empty strings
	static ReversibleFunction<String, String> CAMEL_TO_UNDERSCORE_WORD = Same.reversible(
		s -> Character.isUpperCase(s.charAt(0)) ? "_" +s.toUpperCase() : s.toUpperCase(),
		s -> s.startsWith("_") ? 
				s.substring(1, 2).toUpperCase() + s.substring(2).toLowerCase() : 
				s.toLowerCase());
	
	
	public static ReversibleFunction<String, String> CAMEL_TO_UNDERSCORE = 
		CAMEL_SPLITTER
			.append(Same.stream(CASTER.append(CAMEL_TO_UNDERSCORE_WORD).append(Same.reverse(CASTER))))
			.append(Same.reverse(UNDERSCORE_SPLITTER))
			.guard(
				(x) -> x.matches("[a-z][A-Za-z0-9]*"),
				(x) -> x.matches("[A-Z][A-Z_]*"));
	
	@Test
	public void testCamelCaseToUnderscoreCase() {
		Assert.assertEquals("SOME_EXAMPLE_CAMEL", CAMEL_TO_UNDERSCORE.apply("someExampleCamel"));
	}
	
	@Test
	public void testUnderscoreCaseToCamelCase() {
		Assert.assertEquals("someExampleCamel", CAMEL_TO_UNDERSCORE.inverse("SOME_EXAMPLE_CAMEL"));
	}
	
	@Test(expected = Same4JDataException.class)
	public void testItBreaks() {
		CAMEL_TO_UNDERSCORE.apply("THIS_WONT_WORK");
	}
	
	@Test(expected = Same4JDataException.class)
	public void testItBreaks2() {
		CAMEL_TO_UNDERSCORE.inverse("THIS_will_FAIL");
	}
	
	@Test(expected = NullPointerException.class)
	public void testNull() {
		CAMEL_TO_UNDERSCORE.inverse(null);
	}
	
	@Test(expected = Same4JDataException.class)
	public void testEmptyString() {
		CAMEL_TO_UNDERSCORE.inverse("");
	}
	
	@Test()
	public void testSingleCharString() {
		Assert.assertEquals("s", CAMEL_TO_UNDERSCORE.inverse("S"));
	}
}
