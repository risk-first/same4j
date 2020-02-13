package org.riskfirst.same.same4j.invariant;

import java.util.function.Function;

import org.riskfirst.same.same4j.reversible.ReversibleFunction;

public interface ReversibleExpectation<A, B> {
	
	public Invariant<A, B> exist();
	
	public Invariant<A, B> identical();
	
	public Invariant<A, B> use(Function<A, B> aToB, Function<B, A> bToA);
	
	public Invariant<A, B> use(ReversibleFunction<A, B> r);
	
}