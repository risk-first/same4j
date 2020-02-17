package org.riskfirst.same.same4j.reversible;

import java.util.function.Function;
import java.util.function.Predicate;

import org.riskfirst.same.same4j.Reversible;
import org.riskfirst.same.same4j.hierarchy.ReversibleConsumer;

/**
 * Describes a category of functions for which there is an inverse() operation such that
 * a == inverse(apply(a)) over the entire domain of the function.
 * 
 * @author robmoffat
 */
public interface ReversibleFunction<T, R> extends Function<T, R> {

	public T inverse(R r);
	
	/**
	 * Says whether apply() will work for its argument.
	 */
	public default Predicate<T> domain() {
		return t -> true;
	}
	
	/**
	 * Says whether inverse() will work for its argument.
	 */
	public default Predicate<R> range() {
		return t -> true;
	}

	/**
	 * Short-hand for {@link Reversible.include}
	 */
	public default ReversibleFunction<T, R> allows(Predicate<T> domain, Predicate<R> range) {
		return Reversible.allows(this, domain, range);
	}
	
	/**
	 * Tests that a and b have the same identity, by converting in both ways and 
	 * performing equals().
	 */
	public default boolean equals(T a, R b) {
		return b.equals(apply(a)) && a.equals(inverse(b));
	}
	
	/**
	 * Short-hand for Reversible.combine. 
	 * Doesn't always seem to compile correctly when called though.
	 */
	public default <S> ReversibleFunction<T, S> append(ReversibleFunction<R, S> f) {
		return Reversible.combine(this, f);
	}
	
	/**
	 * Short-hand for Reversible.reverse. 
	 * Doesn't always seem to compile correctly when called though.
	 */
	public default ReversibleFunction<R, T> reverse() {
		return Reversible.reverse(this);
	}
	
	
	public default ReversibleFunction<T, R> guard() {
		return Reversible.guard(this);
	}
	
	public default ReversibleFunction<T, R> combine(ReversibleConsumer<T, R> c) {
		return Reversible.combine(this, c);
	}
	
	public default ReversibleFunction<T, R> combine(ReversibleConsumer<T, R>... c) {
		return Reversible.combine(this, c);
	}
	
	public default ReversibleFunction<T, R> named() {
		return Reversible.name(this, "toString");
	}
}
