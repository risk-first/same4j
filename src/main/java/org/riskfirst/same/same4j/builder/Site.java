package org.riskfirst.same.same4j.builder;

import java.util.function.Function;

import org.riskfirst.same.same4j.reversible.ReversibleFunction;
import org.riskfirst.same.same4j.reversible.types.Beans.Prop;

/**
 * Allows you to declare things are the same
 */
public interface Site<A, B> extends ReversibleExpectation<A, B> { 
	
	/**
	 * Returns a new label, with the scope of this invariant.
	 */
	public Label label();
		
	public <C, D> ReversibleExpectation<C, D> and(C a, D b);
	
	A a();
	
	B b();
	
	public <C, D> ReversibleExpectation<C, D> and(Function<A, C> aFun, Function<B, D> bFun);

	public <C, D> ReversibleExpectation<C, D> and(Prop<A, C> aProp, Prop<B, D> bProp);

	/**
	 * Returns the reversible function set at this level.
	 */
	public ReversibleFunction<A, B> done();

	/**
	 * Returns the top-level Site again
	 */
	public Site<?,?> root();
	
	public default <C, D> ReversibleFunction<C, D> rootDone() {
		return (ReversibleFunction<C, D>) root().done();
	}
}
