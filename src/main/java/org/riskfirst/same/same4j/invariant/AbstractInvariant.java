package org.riskfirst.same.same4j.invariant;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;
import org.riskfirst.same.same4j.reversible.types.Objects;

public abstract class AbstractInvariant<A,B> implements Invariant<A, B> {

	Class<A> cA;
	Class<B> cB;
	A a;
	B b;
	ReversibleFunction<A, B> f;
	Invariant<?,?> parent;
	List<Invariant<?, ?>> children = new ArrayList<>();
	
	protected AbstractInvariant(Class<A> cA, Class<B> cB, Invariant<?, ?> parent) {
		this.parent = parent;
		this.cA = cA;
		this.cB = cB;
	}
	
	@Override
	public Invariant<A, B> exist() {
		f = Objects.shallow(cA, cB);
		return this;
	}

	@Override
	public Invariant<A, B> use(Function<A, B> aToB, Function<B, A> bToA) {
		f = Reversible.of(aToB, bToA);
		return this;
	}

	@Override
	public Invariant<A, B> use(ReversibleFunction<A, B> r) {
		f = r;
		return this;
	}

	@Override
	public A a() {
		return a;
	}

	@Override
	public B b() {
		return b;
	}

	@Override
	public ReversibleFunction<A, B> build() {
		return f;
	}

	@Override
	public Invariant<A, B> identical() {
		if (cA == cB) {
			f = (ReversibleFunction<A, B>) Objects.identity();
		} else {
			throw new Same4JException("Types not identical: "+cA+", "+cB);
		}
		return this;
	}
	
	

	
}
