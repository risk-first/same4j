package org.riskfirst.same.same4j.reversible.types;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.riskfirst.same.same4j.atom.Atom;
import org.riskfirst.same.same4j.atom.IntAtom;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

public class Collections {

	
	public static <V> ReversibleFunction<List<V>, Stream<V>> listToValueStream() {
		return Reversible.stream(m -> m.stream(), Collectors.toList());
	}

	/**
	 * Helper class that collects together all atoms for assembling into an object.
	 */
	public static <K, D extends Atom> Collector<D, List<D>, K> atomCollector(Supplier<K> con, Function<List<D>, K> finisher) {
		return Collector.of(
			() -> new ArrayList<D>(100),
			(l, d) -> l.add(d),
			( a, b ) -> { a.addAll(b); return b; },
			finisher);
	}
	

	public static <V> ReversibleFunction<List<V>, Stream<IntAtom<V>>> listToAtomStream(Supplier<List<V>> constructor) {
	
		Collector<IntAtom<V>, List<IntAtom<V>>, List<V>> collector = atomCollector(constructor, 
				a -> {
					int max = a.stream().mapToInt(d -> (Integer) d.getProperty()).max().orElse(-1);
					List<V> out = constructor.get();
					
					for (int i = 0; i <= max; i++) {
						out.add(null);
					}
					
					a.stream().forEach(d -> out.set(d.getProperty(), d.getValue()));
					return out;
				});
		
		Function<List<V>, Stream<IntAtom<V>>> splitter = l -> IntStream.range(0, l.size())
					.mapToObj(i -> IntAtom.of(l, i, l.get(i)));
	
		
		return Reversible.stream(splitter, collector);
	}
}
