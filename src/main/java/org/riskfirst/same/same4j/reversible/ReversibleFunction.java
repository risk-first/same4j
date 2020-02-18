package org.riskfirst.same.same4j.reversible;

import java.util.function.Function;
import java.util.function.Predicate;

import org.riskfirst.same.same4j.Reversible;
import org.riskfirst.same.same4j.hierarchy.ReversibleConsumer;

/**
 * Describes a category of functions for which there is also an inverse() `i(x)` operation.
 * If `f(x: D) -> R` (function maps from a domain to a range) then `i(x: R) -> D` (i.e. the inverse maps from the
 * range back to the domain).
 * 
 * However, it is not necessarily the case that `f(i(x)) = x` or `i(f(x)) = x`. e.g. there are multiple different formats of a JSON string
 * that will turn into a `JsonObject`, however, they won't necessarily map back to the same string after serialization.
 * 
 * What we can say is that `f(i(f(x))) = f(x)` and `i(f(i(x))) = i(x)`.  So there is some kind of idempotency property (`idemEquals`)
 * at work here.  That is, after the initial application of `f(x)` or `i(x)`, running the reversible calculation will yeild the same answer 
 * each time.
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
	public default boolean idemEquals(T a, R b) {
		T inverse1 = inverse(b);
		R apply1 = apply(a);
		
		T inverse2 = inverse(apply(inverse1));
		R apply2 = apply(inverse(apply1));
		
		return apply1.equals(apply2) && inverse1.equals(inverse2);
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
