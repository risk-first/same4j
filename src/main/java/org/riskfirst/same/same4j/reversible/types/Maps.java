package org.riskfirst.same.same4j.reversible.types;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.riskfirst.same.same4j.atom.TypedAtom;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

/**
 * Utility method for creating reversible functions which are injective, that is
 * they map from one value in the input domain to one value in the range.
 */
public class Maps {

	/**
	 * Breaks maps into atoms and reassembles them.  This doesn't care about the type of map.  
	 */
	public static <K, V> ReversibleFunction<Map<K, V>, Stream<TypedAtom<K, V>>> mapToStream() {
		
		Function<Map<K, V>, Stream<TypedAtom<K, V>>> splitter = m -> m.entrySet()
			.stream()
			.map(e -> TypedAtom.of(null, e.getKey(), e.getValue()));
		
		Collector<TypedAtom<K, V>, ?, Map<K, V>> joiner = 
			Collectors.toMap(e -> e.getProperty(), e -> e.getValue());
		
		return Reversible.stream(splitter, joiner);
	}

	//public <A, B> ReversibleFunction<A, B> create
}
