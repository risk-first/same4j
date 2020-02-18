package org.riskfirst.same.same4j.hierarchy;

import java.util.List;

public class TestObject2 {
	
	String d, e;
	int[] f;
	List<String> g;
	
	public List<String> getG() {
		return g;
	}

	public void setG(List<String> g) {
		this.g = g;
	}

	public TestObject2() {
		super();
	}

	public TestObject2(String d, String e, int[] f, List<String> g) {
		super();
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public int getF(int n) {
		return f[n];
	}

	public void setF(int i, int v) {
		this.f[i] = v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestObject2 other = (TestObject2) obj;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestObject2 [d=" + d + ", e=" + e + ", f=" + f + "]";
	}
	
	
}
