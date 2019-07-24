package com.elliptic.curve;

import java.math.BigInteger;

import com.elliptic.point.Point;

public class EllipticCurve {

	private final BigInteger mod;
	private final BigInteger order;
	private final BigInteger a;
	private final BigInteger b;
	private final Point basePoint; // usualy termed 'G'
	
	public EllipticCurve(BigInteger mod, BigInteger order, BigInteger a, BigInteger b, Point basePoint) {
		this.mod = mod;
		this.order = order;
		this.a = a;
		this.b = b;
		this.basePoint = basePoint;
	}
	
	public BigInteger getOrder() {
		return order;
	}
	
	private static BigInteger generatePrimeMod(){
		///modulo for BTC: 2^256 - 2^32 - 2^9 - 2^8 - 2^7 - 2^6 - 2^4 - 2^0
				
		final BigInteger base = new BigInteger("2");
				
		return base.pow(256)
			.subtract(base.pow(32))
			.subtract(base.pow(9))
			.subtract(base.pow(8))
			.subtract(base.pow(7))
			.subtract(base.pow(6))
			.subtract(base.pow(4))
			.subtract(base.pow(0));
	}
	
	public Point multiplyWithBasePoint(BigInteger k) {
		return pointMultiplication(basePoint, k);
	}
	
	public Point pointMultiplication(Point p, BigInteger k) {
		Point tempPoint = new Point(p.getxCord(), p.getyCord());
		final String kAsBinary = k.toString(2);
		
		for(int i = 1; i < kAsBinary.length(); i++) {
			int currentBit = Integer.parseInt(kAsBinary.substring(i, i+1));
			tempPoint = pointAddition(tempPoint, tempPoint);
			if(currentBit == 1) {
				tempPoint = pointAddition(tempPoint, p);
			}
		}
		return tempPoint;
	}
	
	public Point pointAddition(Point P, Point Q) {
		
		
		BigInteger x1 = P.getxCord();
		BigInteger y1 = P.getyCord();
		
		BigInteger x2 = Q.getxCord();
		BigInteger y2 = Q.getyCord();
		
		BigInteger beta;
		
		if(x1.compareTo(x2) == 0 && y1.compareTo(y2) == 0) {
			
			//apply doubling
			
			beta = (BigInteger.valueOf(3).multiply(x1.multiply(x1))
					.add(a))
					.multiply((BigInteger.valueOf(2).multiply(y1)).modInverse(mod));
			
		}
		else {
			
			//apply point addition
			
			beta = (y2.subtract(y1))
					.multiply(x2.subtract(x1).modInverse(mod));
			
		}
		
		BigInteger x3 = (beta.multiply(beta)).subtract(x1).subtract(x2);
		BigInteger y3 = (beta.multiply(x1.subtract(x3))).subtract(y1);
		
		while(x3.compareTo(BigInteger.valueOf(0)) < 0) {
			
			BigInteger times = x3.abs().divide(mod).add(BigInteger.valueOf(1));
			
			x3 = x3.add(times.multiply(mod));
			
		}
		
		while(y3.compareTo(BigInteger.valueOf(0)) < 0) {
			
			BigInteger times = y3.abs().divide(mod).add(BigInteger.valueOf(1));
			
			y3 = y3.add(times.multiply(mod));
			
		}
		
		x3 =x3.remainder(mod);
		y3 = y3.remainder(mod);
				
		return new Point(x3, y3);
		
	}
	
	public static EllipticCurve btcEllipticCurve() {
		final BigInteger mod = generatePrimeMod();
		final BigInteger order = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
		final BigInteger a = new BigInteger("0"), b = new BigInteger("7");
		final Point basePointG = new Point(new BigInteger("55066263022277343669578718895168534326250603453777594175500187360389116729240"), 
				new BigInteger("32670510020758816978083085130507043184471273380659243275938904335757337482424"));
		return new EllipticCurve(mod, order,a, b, basePointG);
	}
}
