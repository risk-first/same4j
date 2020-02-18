package org.riskfirst.same.same4j.reversible.types;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.riskfirst.same.same4j.Reversible;
import org.riskfirst.same.same4j.atom.IntAtom;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

public class Arrays {
	
	public static <V> ReversibleFunction<V[], Stream<IntAtom<V>>> arrayToAtomStream(Function<Integer, V[]> constructor) {
		
		Collector<IntAtom<V>, List<IntAtom<V>>, V[]> collector = Collections.atomCollector( 
				a -> {
					int max = a.stream().mapToInt(d -> (Integer) d.getProperty()).max().orElse(-1);
					V[] out = constructor.apply(max+1);
					a.stream().forEach(d -> out[d.getProperty()] = d.getValue());
					return out;
				});
		
		Function<V[], Stream<IntAtom<V>>> splitter = l -> IntStream.range(0, l.length)
					.mapToObj(i -> IntAtom.of(l, i, l[i]));
	
		
		return Reversible.stream(splitter, collector);
	}
}
