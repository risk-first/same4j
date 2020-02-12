package org.riskfirst.same.same4j;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ReversibleFunction<T, R> extends Function<T, R>, Reversible {

	public T inverse(R in);
	
	public default ReversibleFunction<T, R> guard(Predicate<T> domain, Predicate<R> range) {
		
		ReversibleFunction<T, R> orig = this;
		
		return new ReversibleMatchingFunction<T, R>() {

			@Override
			public R apply(T t) {
				if (!domain.test(t)) {
					throw new NonReversibleDataException("Failed domain check:"+t);
				} else {
					return orig.apply(t);
				}
			}

			@Override
			public T inverse(R in) {
				if (!range.test(in)) {
					throw new NonReversibleDataException("Failed range check:"+in);
				} else {
					return orig.inverse(in);
				}
			}

			@Override
			public Predicate<T> domain() {
				return domain;
			}

			@Override
			public Predicate<R> range() {
				return range;
			}
			
			
		};
	}
	
	/**
	 * Tests that a and b have the same identity, by converting in both ways and 
	 * performing equals().
	 */
	public default boolean equals(T a, R b) {
		return b.equals(apply(a)) && a.equals(inverse(b));
	}
	
	/**
	 * Adds another step in the pipeline, like Function.compose.
	 * If this causes a compile error, try Same.combine.
	 */
	public default <S> ReversibleFunction<T, S> append(ReversibleFunction<R, S> f) {
		return Same.combine(this, f);
	}
	
	public default ReversibleFunction<R, T> reverse() {
		return Same.reverse(this);
	}
	
}
