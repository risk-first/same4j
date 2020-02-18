package org.riskfirst.same.same4j.hierarchy;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.Reversible;
import org.riskfirst.same.same4j.Same;
import org.riskfirst.same.same4j.atom.IntAtom;
import org.riskfirst.same.same4j.builder.Label;
import org.riskfirst.same.same4j.builder.Site;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;
import org.riskfirst.same.same4j.reversible.types.Collections;
import org.riskfirst.same.same4j.reversible.types.Numbers;
import org.riskfirst.same.same4j.reversible.types.Objects;


public class Test3ListInvariant {

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testConvertListToList() {
		List<String> in = new LinkedList(Arrays.asList("a", "b", "c"));
		
		ReversibleFunction<List<String>, List<String>> rf = Reversible.combine(
				Collections.listToValueStream(),
				Reversible.reverse(Collections.listToValueStream()));
		
		Assert.assertEquals(in, rf.apply(in));
	}
	
	@Test
	public void testListToArray() {
		ReversibleFunction<List<String>, String[]> rf = Reversible.function(
				(l) -> new String[l.size()], 
				(a) -> new ArrayList<String>(a.length)); 

		List<String> list = Arrays.asList("a", "b", "c");

		Assert.assertTrue(rf.apply(list).length == 3);
		
		ReversibleFunction<List<String>, Stream<IntAtom<String>>> listToAtomStream = Collections.listToAtomStream(() -> new ArrayList<>());
		ReversibleFunction<Stream<IntAtom<String>>, String[]> atomStreamToArray = Reversible.reverse(org.riskfirst.same.same4j.reversible.types.Arrays.arrayToAtomStream(i -> new String[i]));
		ReversibleFunction<List<String>, String[]> rf2 = Reversible.combine(
			listToAtomStream,
			atomStreamToArray);
			
		
		Assert.assertTrue(Arrays.deepEquals(new String[] {"a", "b", "c" }, rf2.apply(list)));
		Assert.assertEquals(list, rf2.inverse(new String[] {"a", "b", "c" }));
	}
		
		
		
		/*Label l = null;
		
		Prop<List<String>, String> lProp = Prop.of(list -> list.get(l._int()), (list, v) -> list.set(l._int(), v));
		Prop<String[], String> aProp = Prop.of(arr -> arr[l._int()], (arr, v) -> arr[l._int()] = v);
		
		rf = Reversible.combine(rf, Reversible.of(lProp, aProp, Objects.identity()));
		
		System.out.println(Arrays.toString(rf.apply(list)));

	
		ReversibleFunction<List<String>, String[]> rf2 = Reversible
			.function(
				l2 -> {
					String[] out = new String[40];
					for (int i = 0; i < l2.getSize(); i++) {
						out[i] = l2.get(i);
					}
					
					return out;
					
				}, 
				a -> {
					
					
					
				});
			
		// a property should be iterated over...
		
		// how would that work for set?*/
//	}
	
	
//	@Test
//	public void testSettingListContents() {
//		Label l = null;
//		
//		Site<TestObject1, TestObject2> i = Same.with(TestObject1.class, TestObject2.class);
//			
//		Site<List<String>, List<String>> lists = i.and(
//			Prop.of(a -> a.getC(), (a,p) -> a.setC(p)),
//			Prop.of(b -> b.getG(), (b,p) -> b.setG(p)));
//		
//		
//		lists.use(
//			
//				
//		
//		
//	}
}
