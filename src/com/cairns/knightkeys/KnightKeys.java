package com.cairns.knightkeys;
/*
 * Copyright 2012 (c) John A Cairns <john@2ad.com>
 * 
 * Provided for interview purposes only.
 * @author John A Cairns <john@2ad.com>
 * @date   Feb 29, 2012
 */


/**
 * Main class for KnightKeys
 * 
 * Report the number of sequences of a given length.
 * 
 * If no length is specified, then 10 is used as the default.
 */
public final class KnightKeys {
	
	private static final int DEFAULT_LENGTH = 10;

	public static void main(String... args) {
		final KeyPad kp = new KeyPad();
		
		if(args.length > 0) {
			for(final String arg : args) {
				try {
					final int length = Integer.parseInt(arg);
					System.out.println(kp.countSequences(length));
				} catch(final NumberFormatException ex) {
					System.err.println(arg+" is not a number: "+ex);
				}
			}
		} else {
			System.out.println(kp.countSequences(DEFAULT_LENGTH));
		}
	}
}
