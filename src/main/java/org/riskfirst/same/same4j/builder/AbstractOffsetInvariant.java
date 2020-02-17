package org.riskfirst.same.same4j.builder;

import org.riskfirst.same.same4j.Reversible;
import org.riskfirst.same.same4j.hierarchy.Prop;
import org.riskfirst.same.same4j.hierarchy.ReversibleConsumer;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

public abstract class AbstractOffsetInvariant<A,B> extends AbstractBaseInvariant<A, B>{

	private Prop<?,A> pA;
	private Prop<?, B> pB;
	protected Site<?,?> parent;
	
	protected AbstractOffsetInvariant(Prop<?,A> pA, Prop<?, B> pB, AbstractBaseInvariant<?, ?> parent) {
		super();
		this.parent = parent;
		this.pA = pA;
		this.pB = pB;
		parent.add(this);
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public <C, D> ReversibleFunction<C,D> appendTo(ReversibleFunction<C,D> rf) {
		ReversibleFunction out = done();
		ReversibleConsumer c = Reversible.of(pA, pB, out);
		return Reversible.combine(rf, c);
	}

	@Override
	public Site<?, ?> root() {
		return parent.root();
	}
	
}
