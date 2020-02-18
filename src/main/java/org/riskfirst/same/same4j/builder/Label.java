package org.riskfirst.same.same4j.builder;

import java.util.stream.Stream;

public interface Label<X> {

	public int _int();
	
	public float _float();
	
	public boolean _boolean();
	
	public double _double();
	
	public short _short();
	
	public long _long();
	
	public <K> K any();
	
	public Stream<X> stream();
}
