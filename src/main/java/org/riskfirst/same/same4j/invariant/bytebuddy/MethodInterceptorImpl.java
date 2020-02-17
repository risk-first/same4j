package org.riskfirst.same.same4j.invariant.bytebuddy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class MethodInterceptorImpl {
	
	PropConstructor pc;

	public MethodInterceptorImpl(PropConstructor pc) {
		this.pc = pc;
	}

	@RuntimeType 
	public Object intercept(@SuperCall Callable<?> self, @Origin Method m, @AllArguments Object[] args) throws Exception {
		pc.append(m, args);
		return self.call();
	}
}
