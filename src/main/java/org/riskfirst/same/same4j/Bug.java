package org.riskfirst.same.same4j;

import org.junit.Test;

public class Bug {

	interface Thing<A, B> {
		
		
		public default Thing<A, B> add(Thing<A, B> next) {
			return Bug.append(this, next);
		}
		
		
	}
	
	static <A, B> Thing<A, B> append(Thing<A, B> one, Thing<A, B> two) {
		return new Thing<A,B>() {
			
		};
	}
	
	@Test
	public void testStaticAndDefault() {
		
		// this one compiles ok
		Thing<String, String> combined = append(
			new Thing<String, String>() {}, 
			new Thing<String, String>() {});
		
		// this one doesn't
		Thing<String, String> alsoCombined = new Thing<String, String>() {}
			.add(new Thing<String, String>() {});
	}
}
