package jene;

import java.util.Random;

/**
 *  
 * @author James Stout
 *
 */
 
 public class SeededRandom extends Random {

	private static final long serialVersionUID = 1L;
	
	private final long _mySeed;
	
	public SeededRandom() {
		super();
		_mySeed = new Random().nextLong();
		System.out.println("Seed: "+_mySeed);
		this.setSeed(_mySeed);
	}
	
	public SeededRandom(long seed) {
		super(seed);
		_mySeed = seed;
	}
	
	public long getSeed() {
		return _mySeed;
	}

}
