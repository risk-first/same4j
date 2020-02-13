package org.riskfirst.same.same4j.reversible;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestNestingConversion {

	enum Location {
		LONDON, PARIS, NEWYORK
	}
	
	static class Address {
		
		Location l;
		String address;
		
		public Address(Location l, String address) {
			super();
			this.l = l;
			this.address = address;
		}
		
	}
	
	static class Seller {
		
		public Seller(String name, Location l) {
			super();
			this.name = name;
			this.l = l;
		}

		public Seller() {
			super();
		}

		String name;
		Location l;
	}
	
	static class Database {
		
		List<Seller> sellers;
		List<Address> addresses;
		
		public Database(List<Seller> sellers, List<Address> addresses) {
			super();
			this.sellers = sellers;
			this.addresses = addresses;
		}
	}
	
	static Database DATABASE = new Database(
		Arrays.asList(
			new Seller("John C", Location.LONDON),
			new Seller("Ole Birchman", Location.LONDON),
			new Seller("Finster Oberwald", Location.NEWYORK),
			new Seller("Nathan Barley", Location.PARIS),
			new Seller("Zachary L", Location.PARIS)),
		Arrays.asList(
			new Address(Location.PARIS, "13 Arondissment"),
			new Address(Location.PARIS, "77 Rue Charles de Gaulle"),
			new Address(Location.LONDON, "13 Baker St."),
			new Address(Location.NEWYORK, "55 Bleker St")));
	
	
	@Test
	public void testDenormalization() {
		
		
	}
}
