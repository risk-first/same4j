package org.riskfirst.same.same4j.reversible;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Describes a category of functions for which there is an inverse() operation such that
 * a == inverse(apply(a)) over the entire domain of the function.
 * 
 * @author robmoffat
 */
public interface ReversibleFunction<T, R> extends Function<T, R>, Reversible {

	public T inverse(R in);
	
	/**
	 * Short-hand for Same.guard. 
	 * Doesn't always seem to compile correctly when called though.
	 */
	public default ReversibleFunction<T, R> guard(Predicate<T> domain, Predicate<R> range) {
		return Reversible.guard(this, domain, range);
	}
	
	/**
	 * Tests that a and b have the same identity, by converting in both ways and 
	 * performing equals().
	 */
	public default boolean equals(T a, R b) {
		return b.equals(apply(a)) && a.equals(inverse(b));
	}
	
	/**
	 * Short-hand for Same.combine. 
	 * Doesn't always seem to compile correctly when called though.
	 */
	public default <S> ReversibleFunction<T, S> append(ReversibleFunction<R, S> f) {
		return Reversible.combine(this, f);
	}
	
	/**
	 * Short-hand for Same.reverse. 
	 * Doesn't always seem to compile correctly when called though.
	 */
	public default ReversibleFunction<R, T> reverse() {
		return Reversible.reverse(this);
	}
	
	/**
	 * Says whether apply() will work for its argument.
	 */
	public default Predicate<T> domain() {
		return o -> true;
	}
	
	/**
	 * Says whether inverse() will work for its argument.
	 */
	public default Predicate<R> range() {
		return o -> true; 
	}
}
