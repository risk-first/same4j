package org.riskfirst.same.same4j.invariant.bytebuddy;

import java.util.function.Function;

import org.riskfirst.same.same4j.invariant.AbstractBaseInvariant;
import org.riskfirst.same.same4j.invariant.AbstractOffsetInvariant;
import org.riskfirst.same.same4j.invariant.Invariant;
import org.riskfirst.same.same4j.invariant.ReversibleExpectation;
import org.riskfirst.same.same4j.reversible.types.Beans.Prop;

public class BBOffsetInvariant<A, B> extends AbstractOffsetInvariant<A, B>{

	public BBOffsetInvariant(Class<A> cA, Prop<?, A> pA, Class<B> cB, Prop<?, B> pB, AbstractBaseInvariant<?, ?> parent) {
		super(cA, pA, cB, pB, parent);
	}

	@Override
	public <C, D> ReversibleExpectation<C, D> and(C a, D b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C, D> ReversibleExpectation<C, D> and(Function<A, C> aFun, Function<B, D> bFun) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
