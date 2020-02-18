package org.riskfirst.same.same4j;

import java.util.function.Function;

import org.junit.Test;

public class Bug {

	interface Thing<A, B> extends Function<A, B> {
		
		
		public default Thing<A, B> add(Thing<A, B> next) {
			return Bug.append(this, next);
		}
		
		public default Thing<A, B> next() {
			return Bug.append(this, new Thing<A,B >() {});
		}
		
		public default B apply(A a) {
			return (B) "some b";
		}
		
		
	}
	
	static <A, B> Thing<A, B> append(Thing<A, B> one, Thing<A, B> two) {
		return new Thing<A,B>() {
			
		};
	}
	
	static <A, B> Thing<A, B> create() {
		return new Thing<A, B>() {};
	}
	
	@Test
	public void testStaticAndDefault() {
		
		// this one compiles ok
		var combined = append(
			new Thing<String, String>() {}, 
			new Thing<String, String>() {});
				
		// this one doesn't
		var alsoCombined = new Thing<String, String>() {}
			.add(new Thing<String, String>() {});
			
		// this one doesn't
		var next = alsoCombined.next();
					
	}
}
