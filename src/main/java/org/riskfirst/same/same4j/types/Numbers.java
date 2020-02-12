package org.riskfirst.same.same4j.types;

import java.text.NumberFormat;

import org.riskfirst.same.same4j.ReversibleFunction;

public class Numbers {

	public static ReversibleFunction<String, Number> stringToInteger(NumberFormat nf) {
		return new ReversibleFunction<String, Integer>() {

			@Override
			public Integer apply(String t) {
				return nf.parse(t);
			}

			@Override
			public String inverse(Integer in) {
				return nf.par
			}
			
		};
	}
}
