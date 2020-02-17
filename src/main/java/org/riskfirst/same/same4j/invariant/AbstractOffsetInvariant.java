package org.riskfirst.same.same4j.invariant;

import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleConsumer;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;
import org.riskfirst.same.same4j.reversible.types.Beans;
import org.riskfirst.same.same4j.reversible.types.Beans.Prop;

public abstract class AbstractOffsetInvariant<A,B> extends AbstractBaseInvariant<A, B>{

	private Prop<?,A> pA;
	private Prop<?, B> pB;
	protected Invariant<?,?> parent;
	
	protected AbstractOffsetInvariant(Class<A> cA, Prop<?,A> pA, Class<B> cB, Prop<?, B> pB, AbstractBaseInvariant<?, ?> parent) {
		super(cA, cB);
		this.parent = parent;
		this.pA = pA;
		this.pB = pB;
		parent.add(this);
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public <C, D> ReversibleFunction<C,D> appendTo(ReversibleFunction<C,D> rf) {
		ReversibleFunction out = done();
		ReversibleConsumer c = Beans.of(pA, pB, out);
		return Reversible.combine(rf, c);
	}

}
