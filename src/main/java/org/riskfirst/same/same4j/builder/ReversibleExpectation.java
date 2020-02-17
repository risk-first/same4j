package org.riskfirst.same.same4j.builder;

import java.util.function.Function;

import org.riskfirst.same.same4j.reversible.ReversibleFunction;

public interface ReversibleExpectation<A, B> {
	
	public Site<A, B> identical();
	
	public Site<A, B> sameClass();
	
	public Site<A, B> use(Function<A, B> aToB, Function<B, A> bToA);
	
	public Site<A, B> use(ReversibleFunction<A, B> r);
	
}