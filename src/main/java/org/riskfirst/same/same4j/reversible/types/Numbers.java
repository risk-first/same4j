package org.riskfirst.same.same4j.reversible.types;

import java.text.NumberFormat;
import java.text.ParseException;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

public class Numbers {

	public static ReversibleFunction<String, Integer> STRING_TO_INTEGER = Reversible.allows(
			Reversible.function(
					s -> Integer.parseInt(s), 
					i -> i.toString()), 
			s -> s.matches("^[-+]?\\d+$"),
			i -> true);
	
	public static ReversibleFunction<String, Number> stringToNumber(NumberFormat nf) {
		return new ReversibleFunction<String, Number>() {

			@Override
			public Number apply(String t) {
				try {
					return nf.parse(t);
				} catch (ParseException e) {
					throw new Same4JException("Couldn't parse number: "+t, e);
				}
			}

			@Override
			public String inverse(Number in) {
				return nf.format(in);
			}
			
		};
	}
}
