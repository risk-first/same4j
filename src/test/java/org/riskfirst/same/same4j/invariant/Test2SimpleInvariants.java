package org.riskfirst.same.same4j.invariant;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;


public class Test2SimpleInvariants {
	
	@Test
	public void testObjectInvariantsTopLevel() {
		Invariant<TestObject1, TestObject2> i = Invariant.with(TestObject1.class, TestObject2.class);
		ReversibleFunction<TestObject1, TestObject2> f = i.exist().done();
		Assert.assertTrue(f.apply(new TestObject1()) instanceof TestObject2);
	}
	
	@Test
	public void testOb1Ob2FieldInvariant() {
		Invariant<TestObject1, TestObject2> i = 
			Invariant.with(TestObject1.class, TestObject2.class);
	
		i.exist()
			.and(
				i.a().getA(), 
				i.b().getD())
			.identical();
		
		ReversibleFunction<TestObject1, TestObject2> f = i.done();
		
		TestObject1 from = new TestObject1("a", null, null);
		TestObject2 to = new TestObject2("a", null, null);
		
		TestObject2 toA = f.apply(from);
		Assert.assertEquals(to, toA);
		TestObject1 fromA = f.inverse(to);
		Assert.assertEquals(from, fromA);
	}
	
	
	
	/*@Test
	public void testOb1Ob2Invariants() {
	
		// alternate syntax (functions, better), declares that b is the upper case of a's lower case
		i.and(a -> a.getA(), b -> b.getD())
			.use(a -> a.toUpperCase(), b -> b.toLowerCase());
		
		// do lower-level invariant setting, third syntax ftw.
		
		Label x = Label.;
		
		Invariant<>i.and(TestObject1::getC, TestObject2::getF)
			.and(a -> a.get(x._int()), b -> b.get(x._int()));
		
		
		ReversibleFunction<TestObject1, TestObject2> rf = i.build();
		
		TestObject1 to = new TestObject1(a, b, c)
		
		.use(Numbers.STRING_TO_INTEGER);
		
	}*/
}
