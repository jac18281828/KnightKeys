package com.cairns.knightkeys;
/*
 * Copyright 2012 (c) John A Cairns <john@2ad.com>
 * 
 * Provided for interview purposes only.
 * @author John A Cairns <john@2ad.com>
 * @date   Feb 29, 2012
 */


/**
 * This class represents a 5x4 keypad used for generating
 * move sequences.
 * 
 * @author John Cairns
 */
public class KeyPad {
	
	
	// global static data
	private static final int WIDTH = 5;
	private static final int HEIGHT = 4;
	private static final int NVOWELS = 2;
	
	// the keypad is a 5x4 grid which
	// we treat as a linear array of 20 elements
	
	// we take y positive as down, and x to the right
	
	// arrayPos = 5*y + x
	// for example 'H' 2 down and 3 right, would be
	// x=1, y=2, 5*1+2 = 7
	// note that 0 is the 'first' position as in C
	// the positions next to '1', '2', '3', are not allowed keys
	
	private static final char[] keys = { 
			'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O',
			 0,  '1', '2', '3',  0 
	};
	
	private static final boolean[] keyVowel = {
		true, false, false, false, true,
		false, false, false, true, false,
		false, false, false, false, true,
		false, false, false, false, false
	};
	
	// precalulate allowed moves
	// since we will examine each position multiple times
	// it makes sense to store the allowed moves for each key
	private static final int[][] allowedMoves;
	
	static {
		
		// there are 8 allowed moves for a knight on the keypad
		// UL, UR, DL, DR, LU, LD, RU, RD 
		// (up left, ...) 		
		final int[][] deltas = {
				{-1, -2}, // UL
				{+1, -2}, // UR
				{-1, +2}, // DL
				{+1, +2}, // DR
				{-2, -1}, // LU
				{-2, +1}, // LD
				{+2, -1}, // RU
				{+2, +1}  // RD
		};

		allowedMoves = new int[keys.length][];
		
		for(int k=0; k<keys.length; k++) {
			final int[] posArray = new int[8]; // out of 8 moes
			for(int i=0; i<posArray.length; i++) posArray[i] = -1;
			int nMoves = 0;
			// there are 8 possible allowed moves for each key
			final int x = getX(k);
			final int y = getY(k);
			
			// consider each possible move
			for(int[] delta : deltas) {
				final int xoff = x + delta[0];
				final int yoff = y + delta[1];

				final int testKey = getPos(xoff, yoff);
				// check bounds
				if((xoff >= 0) &&
					(xoff < WIDTH) &&
					(yoff >= 0) &&
					(yoff < HEIGHT) &&
					// consider dead key spaces
					keys[testKey] != 0) {
					
					// testKey has survived bounds checking
					// this move is allowed from k
					posArray[nMoves++] = testKey;
				}
			}
			
			if(nMoves > 0) {
				// copy only the allowed moves
				allowedMoves[k] = new int[nMoves];
				for(int i=0; i<nMoves; i++) {
					allowedMoves[k][i] = posArray[i];
				}
				
			} else {
				allowedMoves[k] = null;
			}
			
		
		}
	}
	
	/**
	 * construct a new keypad
	 */
	
	/*
	 public KeyPad() {
		for(int k=0; k<keys.length; k++) {
			if(keys[k] != 0) {
				System.out.print(keys[k]+": ");
				for(final int m : allowedMoves[k]) {
					System.out.print(m+" ("+getX(m)+","+getY(m)+"), ");
				}
				System.out.println();
			}
		} 
	}
	*/
	
	/**
	 * Count the number of sequences of specific lengths
	 * 
	 * @param length
	 * @return
	 */
	public int countSequences(final int maxLength) {
		//final long startT = System.nanoTime();
		int nSequences = 0;
		for(int k=0; k<keys.length; k++) {
			if(keys[k] != 0) { // all valid keys
				if(maxLength<=1) nSequences++;
				else nSequences += countRecursive(k, isVowel(k)?1:0, 1, maxLength);
			}
		}
		//final long runT = Math.abs(startT-System.nanoTime());
		//System.out.println("runTime: "+runT+" ns");
		return nSequences;
	}
	
	/**
	 * count recursive sequences starting at key
	 * 
	 * 
	 * @param k
	 * @return the number of allowed sequences for key
	 */
	// for maxLength=10, this method runs in 29ms on modern hardware
	private final static int countRecursive(final int key, final int nVowels, final int length, final int maxLength) {
		// count the number of allowed subpaths for each allowed move under key

		// we are permuting sequences, examine all allowed moves and return counts belonging to those subpaths
		int nSequences = 0;
		for(final int m : allowedMoves[key]) {
			// is move key a vowel
			final boolean isVowel = isVowel(m);
			// how many vowels will we have after this move
			final int     nexVowels = nVowels+1; 

			// detect terminal sequences before branching
			if(isVowel && nexVowels>NVOWELS) continue;
			else if(length>=maxLength-1) nSequences++;
			// otherwise continue path to next segment
			else nSequences += countRecursive(m, isVowel?nexVowels:nVowels, length+1, maxLength);
		}

		// return result
		return nSequences;

	}
	
	// return a y coordinate for the given key
	private static final int getY(final int k) {
		return k / WIDTH;
	}
	
	// return x coordiante for the given key
	private static final int getX(final int k) {
		return k % WIDTH;
	}
	
	// return the position in array of x and y
	private static final int getPos(final int x, final int y) {
		return y*WIDTH + x;
	}
	
	// return true if key is a vowel
	private static final boolean isVowel(final int key) {
		// do a static lookup in a table rather than test
		return keyVowel[key];
	}

}
