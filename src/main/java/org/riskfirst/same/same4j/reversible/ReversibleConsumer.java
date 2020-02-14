package org.riskfirst.same.same4j.reversible;

import java.util.function.BiConsumer;

/**
 * You can combine several operations on an object to make a reversible function.
 * 
 * The operation accept(T, U) makes some change on T, given U.
 * The operation inverse(U, T) makes some change on U, given T.
 * 
 * This is really useful for when a ReversibleFunction builds its result in stages.
 */
public interface ReversibleConsumer<T, U> extends BiConsumer<T, U> {
	
	public void inverse(U u, T t);
}
