package com.elliptic.curve;

import java.math.BigInteger;

public class EllipticCurve {

	private final BigInteger mod;
	private final BigInteger order;
	private final BigInteger a;
	private final BigInteger b;
	
	public EllipticCurve(BigInteger mod, BigInteger order, BigInteger a, BigInteger b) {
		this.mod = mod;
		this.order = order;
		this.a = a;
		this.b = b;
	}
}
