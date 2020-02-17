package org.riskfirst.same.same4j.builder.bytebuddy;

import java.util.function.Function;

import org.riskfirst.same.same4j.builder.AbstractBaseInvariant;
import org.riskfirst.same.same4j.builder.Label;
import org.riskfirst.same.same4j.builder.ReversibleExpectation;
import org.riskfirst.same.same4j.reversible.types.Objects;
import org.riskfirst.same.same4j.reversible.types.Beans.Prop;

public class BBBaseInvariant<A, B> extends AbstractBaseInvariant<A, B> {
		
	private PropConstructor aCall, bCall;
	
	public BBBaseInvariant(Class<A> ca, Class<B> cb) {
		super();
		aCall = new PropConstructor();
		bCall = new PropConstructor();
		this.a = BBFunctions.buildInvariant(ca, aCall);
		this.b = BBFunctions.buildInvariant(cb, bCall);
		// default is to make sure classes are same.
		this.f = Objects.shallow(ca, cb);
	}	

	@Override
	public Label label() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <C, D> ReversibleExpectation<C, D> and(C a, D b) {
		return new BBOffsetInvariant(aCall.getProp(), bCall.getProp(), this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <C, D> ReversibleExpectation<C, D> and(Function<A, C> aFun, Function<B, D> bFun) {
		aFun.apply(a());
		bFun.apply(b());
		return new BBOffsetInvariant(aCall.getProp(), bCall.getProp(), this);
	}

	@Override
	public <C, D> ReversibleExpectation<C, D> and(Prop<A, C> aProp, Prop<B, D> bProp) {
		return new BBOffsetInvariant<C, D>(aProp, bProp, this);
	}
}
