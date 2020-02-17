package org.riskfirst.same.same4j.hierarchy;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Allows you to address some component within in an object.
 */
public interface Prop<O, P> {

	public void set(O o, P p); 

	public P get(O o);

	static <O, P> Prop<O, P> of(Function<O, P> getterOnO, BiConsumer<O, P> setterOnO) {
	
		return new Prop<O, P>() {
	
			@Override
			public void set(O o, P p) {
				setterOnO.accept(o, p);
			}
	
			@Override
			public P get(O o) {
				return getterOnO.apply(o);
			}
	
		};
	}

}