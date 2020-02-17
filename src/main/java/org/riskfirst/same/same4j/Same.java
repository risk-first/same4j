package org.riskfirst.same.same4j;

import org.riskfirst.same.same4j.builder.Site;
import org.riskfirst.same.same4j.builder.bytebuddy.BBBaseInvariant;

public class Same {

	/**
	 * This starts building a new invariant.
	 */
	public static <A, B> Site<A, B> with(Class<A> a, Class<B> b) {
		return new BBBaseInvariant<>(a, b);
	}

}
