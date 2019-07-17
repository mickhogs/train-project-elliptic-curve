package com.elliptic.point;

import java.math.BigInteger;

public class Point {

	private final BigInteger xCord;
	private final BigInteger yCord;
	
	public Point(BigInteger xCord, BigInteger yCord) {
		this.xCord = xCord;
		this.yCord = yCord;
	}

	public BigInteger getxCord() {
		return xCord;
	}

	public BigInteger getyCord() {
		return yCord;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((xCord == null) ? 0 : xCord.hashCode());
		result = prime * result + ((yCord == null) ? 0 : yCord.hashCode());
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
		Point other = (Point) obj;
		if (xCord == null) {
			if (other.xCord != null)
				return false;
		} else if (!xCord.equals(other.xCord))
			return false;
		if (yCord == null) {
			if (other.yCord != null)
				return false;
		} else if (!yCord.equals(other.yCord))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point (" + xCord + "," + yCord + ")";
	}
	
	
}
