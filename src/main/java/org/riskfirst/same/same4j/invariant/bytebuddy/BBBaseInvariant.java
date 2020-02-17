package org.riskfirst.same.same4j.invariant.bytebuddy;

import java.util.function.Function;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.invariant.AbstractBaseInvariant;
import org.riskfirst.same.same4j.invariant.Invariant;
import org.riskfirst.same.same4j.invariant.Label;
import org.riskfirst.same.same4j.invariant.ReversibleExpectation;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleConsumer;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class BBBaseInvariant<A, B> extends AbstractBaseInvariant<A, B> {
		
	private PropConstructor aCall, bCall;
	
	public BBBaseInvariant(Class<A> ca, Class<B> cb) {
		super(ca, cb);
		aCall = new PropConstructor();
		bCall = new PropConstructor();
		this.a = BBFunctions.buildInvariant(ca, aCall);
		this.b = BBFunctions.buildInvariant(cb, bCall);
	}	

	@Override
	public Label label() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C, D> ReversibleExpectation<C, D> and(C a, D b) {
		return new BBOffsetInvariant(aCall.getType(), aCall.getProp(), bCall.getType(), bCall.getProp(), this);
	}

	@Override
	public <C, D> ReversibleExpectation<C, D> and(Function<A, C> aFun, Function<B, D> bFun) {
		// TODO Auto-generated method stub
		return null;
	}

}
