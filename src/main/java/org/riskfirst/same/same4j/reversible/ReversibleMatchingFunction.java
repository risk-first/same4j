package org.riskfirst.same.same4j.reversible;

import java.util.function.Predicate;

/**
 * A reversible function that applies within a given range/domain.
 */
public interface ReversibleMatchingFunction<T, R> extends ReversibleFunction<T, R> {

	Predicate<T> domain();
	
	Predicate<R> range();
}
