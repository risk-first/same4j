package org.riskfirst.same.same4j.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;
import org.riskfirst.same.same4j.reversible.types.Objects;

public abstract class AbstractBaseInvariant<A,B> implements Site<A, B> {

	protected A a;
	protected B b;
	protected ReversibleFunction<A, B> f;
	protected List<AbstractOffsetInvariant<?, ?>> children = new ArrayList<>();
	
	protected AbstractBaseInvariant() {
	}

	@Override
	public Site<A, B> use(Function<A, B> aToB, Function<B, A> bToA) {
		f = Reversible.function(aToB, bToA);
		return this;
	}

	@Override
	public Site<A, B> use(ReversibleFunction<A, B> r) {
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
	public ReversibleFunction<A, B> done() {
		if (f == null) {
			throw new Same4JException("Function not set on invariant");
		}
		
		ReversibleFunction<A, B> out = f;
		for (AbstractOffsetInvariant<?, ?> abstractInvariant : children) {
			out = abstractInvariant.appendTo(out);
		}
		
		return out;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Site<A, B> identical() {
		f = (ReversibleFunction<A, B>) Objects.identity();
		return this;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Site<A, B> sameClass() {
		f = (ReversibleFunction<A, B>) Objects.sameClass();
		return this;
	}

	public void add(AbstractOffsetInvariant<?,?> i) {
		children.add(i);
	}

	@Override
	public Label label() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site<?, ?> root() {
		return this;
	}

	
}
