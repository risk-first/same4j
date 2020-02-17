package org.riskfirst.same.same4j.reversible.types;

import java.util.function.Supplier;

import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

public class Objects {

	/**
	 * Identity relationship, where the in matches the out.
	 */
	public static <A> ReversibleFunction<A, A> identity() {
		return Reversible.name(Reversible.function(
			a -> a,
			b -> b), "identity");
	}

	/**
	 * Classes are the same, instances are different.
	 */
	public static <A> ReversibleFunction<A, A> sameClass() {
		return Reversible.name(Reversible.function(
				a -> a == null ? null : Reflection.newInstanceNoArgs(a.getClass()),
				b -> b == null ? null : Reflection.newInstanceNoArgs(b.getClass())), "sameClass");
	}
	
	/**
	 * Returns an object if there is one present, otherwise returns null.
	 */
	public static <A, B> ReversibleFunction<A, B> existence(
			Supplier<A> ca, 
			Supplier<B> cb) {
		
		return Reversible.name(Reversible.function(
			a -> a != null ? cb.get() : null,
			b -> b != null ? ca.get() : null), "existence");
	}

	/**
	 * Tries to create a new object using no-args constructors
	 */
	public static <A, B> ReversibleFunction<A, B> shallow(Class<?> a, Class<?> b) {
		return existence(
				() -> Reflection.newInstanceNoArgs(a), 
				() -> Reflection.newInstanceNoArgs(b));
	}

}
