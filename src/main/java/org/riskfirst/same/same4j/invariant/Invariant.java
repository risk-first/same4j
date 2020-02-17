package org.riskfirst.same.same4j.invariant;

import java.util.function.Function;

import org.riskfirst.same.same4j.invariant.bytebuddy.BBBaseInvariant;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

/**
 * Allows you to declare things are the same
 */
public interface Invariant<A, B> extends ReversibleExpectation<A, B> { 
	
	/**
	 * Returns a new label, with the scope of this invariant.
	 */
	public Label label();
		
	public <C, D> ReversibleExpectation<C, D> and(C a, D b);
	
	A a();
	
	B b();
	
	public <C, D> ReversibleExpectation<C, D> and(Function<A, C> aFun, Function<B, D> bFun);


	public static <A, B> Invariant<A, B> with(Class<A> a, Class<B> b) {
		return new BBBaseInvariant<>(a, b);
	}

	/**
	 * Returns the reversible function set at this level.
	 */
	public ReversibleFunction<A, B> done();
	
}
