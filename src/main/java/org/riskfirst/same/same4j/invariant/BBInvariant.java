package org.riskfirst.same.same4j.invariant;

import java.util.function.Function;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatchers;

public class BBInvariant<A, B> extends AbstractInvariant<A, B> {
	
	private A a;
	private B b;
	static ByteBuddy bb = new ByteBuddy();
	
	public BBInvariant(Invariant<?,?> parent, Class<A> ca, Class<B> cb) {
		super(ca, cb, parent);
		this.a = buildInvariant(ca);
		this.b = buildInvariant(cb);
	}

	public <X> X buildInvariant(Class<X> ca) throws Same4JException {
		try {
			return bb.subclass(ca)
					.name(ca.getName()+"Invariant")
					.method(ElementMatchers.any()).intercept(createInvariantImplementation(ca))
					.make()
					.load(getClass().getClassLoader())
					.getLoaded()
					.getConstructor().newInstance();
		} catch (Exception e) {
			throw new Same4JException("Couldn't instantiate invariant for: "+ca, e);
		}
	}

	private Implementation createInvariantImplementation(Class<?> ca) {
		return new Implementation() {

			@Override
			public InstrumentedType prepare(InstrumentedType instrumentedType) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ByteCodeAppender appender(Target implementationTarget) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}

	@Override
	public Label label() {
		// TODO Auto-generated method stub
		return null;
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
