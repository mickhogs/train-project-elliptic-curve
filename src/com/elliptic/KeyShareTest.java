package com.elliptic;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.elliptic.curve.EllipticCurve;
import com.elliptic.point.Point;

public class KeyShareTest {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		EllipticCurve btcCurve = EllipticCurve.btcEllipticCurve();
		
		// Key shares - key is 1000
		final BigInteger aliceShare = new BigInteger("800");
		final BigInteger bobShare = new BigInteger("100");
		final BigInteger jackShare = new BigInteger("100");
		
		// private key assembled from Alice, Bob and Jack's shares
		BigInteger privateKey = aliceShare.add(bobShare).add(jackShare);
		System.out.println("Private key: " + privateKey);
		
		//  public key
		Point publicKey = btcCurve.multiplyWithBasePoint(privateKey);
		System.out.println("Public key: " + publicKey);
		
		// Create hash of 'transaction'
		final String text = "This is a bitcoin transaction: Alice pay Bob 10 BTC";
		final MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(text.getBytes());
		final byte[] hashByte = md.digest();
		final BigInteger hash = new BigInteger(hashByte).abs();
		System.out.println("Text: " + text + ", hash: " + hash);
		
		// a random key is generated for each signature usually - we will just use this for testing
		final BigInteger randomKey = new BigInteger(256, new SecureRandom());
		final Point randomPoint = btcCurve.multiplyWithBasePoint(randomKey);
		System.out.println("Random point: " + randomPoint);
		
		// r - > cord x of random point mod order. This is the first half of the signature. We generate a term 's' which is verified with public key against the term 'r'
		final BigInteger r = randomPoint.getxCord().remainder(btcCurve.getOrder());
		
		// S computed form Bob, Alice and Jack's share of the key
		final BigInteger aliceHash = hash.add(r.multiply(aliceShare));
		final BigInteger bobHash = aliceHash.add(r.multiply(bobShare));
		final BigInteger jackhash = bobHash.add(r.multiply(jackShare));
		
		// calculate s - need that random key again..
		final BigInteger s = jackhash.multiply(randomKey.modInverse(btcCurve.getOrder())).remainder(btcCurve.getOrder());
		
		System.out.println("(r,s) " + "(" + r + ", " + s + ")");
		// verify the signature:
		final BigInteger w = s.modInverse(btcCurve.getOrder());
						
		final Point u1 = btcCurve.multiplyWithBasePoint((hash.multiply(w).remainder(btcCurve.getOrder())));
		final Point u2 = btcCurve.pointMultiplication(publicKey, (r.multiply(w).remainder(btcCurve.getOrder())));
		final Point checkpoint = btcCurve.pointAddition(u1, u2);
				
		if(checkpoint.getxCord().compareTo(r) == 0){
					
			System.out.println("signature is valid...");
					
		} else {
					
			System.out.println("invalid signature detected!!!");
					
		}
		// for comparison - create signature with private key for comparison against Alice, Bob and Jack
		final BigInteger sFromPrivKey = (hash.add(r.multiply(privateKey)).multiply(randomKey.modInverse(btcCurve.getOrder()))).remainder(btcCurve.getOrder());
		System.out.println("(r,sPriv) " + "(" + r + ", " + sFromPrivKey + ")");
	}
	
}
