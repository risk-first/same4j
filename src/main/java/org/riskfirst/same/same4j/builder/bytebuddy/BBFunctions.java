package org.riskfirst.same.same4j.builder.bytebuddy;

import org.riskfirst.same.same4j.Same4JException;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class BBFunctions {

	static ByteBuddy bb = new ByteBuddy();
	
	public static <X> X buildInvariant(Class<X> ca, PropConstructor pc) throws Same4JException {
		try {
			Implementation i = MethodDelegation.to(new MethodInterceptorImpl(pc));
			return bb.subclass(ca)
					.name(ca.getName()+"Invariant")
					.method(ElementMatchers.isDeclaredBy(ca)).intercept(i)
					.make()
					.load(BBFunctions.class.getClassLoader())
					.getLoaded()
					.getConstructor().newInstance();
		} catch (Exception e) {
			throw new Same4JException("Couldn't instantiate invariant for: "+ca, e);
		}
	}
}
