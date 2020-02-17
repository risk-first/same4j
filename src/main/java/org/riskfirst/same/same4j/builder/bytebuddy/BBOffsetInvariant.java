package org.riskfirst.same.same4j.builder.bytebuddy;

import java.util.function.Function;

import org.riskfirst.same.same4j.builder.AbstractBaseInvariant;
import org.riskfirst.same.same4j.builder.AbstractOffsetInvariant;
import org.riskfirst.same.same4j.builder.ReversibleExpectation;
import org.riskfirst.same.same4j.hierarchy.Prop;

public class BBOffsetInvariant<A, B> extends AbstractOffsetInvariant<A, B>{

	public BBOffsetInvariant(Prop<?, A> pA, Prop<?, B> pB, AbstractBaseInvariant<?, ?> parent) {
		super(pA, pB, parent);
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

	@Override
	public <C, D> ReversibleExpectation<C, D> and(Prop<A, C> aProp, Prop<B, D> bProp) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
