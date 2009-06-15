package jene;

import java.util.Arrays;
import java.util.List;

/**
 *  
 * @author James Stout
 *
 */
 
 public class Histogram {

	private final int[] _hist;
	private int _totalCount;
	private final int _dim;
	private final Double[] _mins;
	private final Double[] _maxs;
	private final Integer[] _numBins;
	private final double[] _binSizes;
	
	public Histogram(List<Double> mins, List<Double> maxs, List<Integer> numBins) {
		if (!(mins.size() == maxs.size() && mins.size() == numBins.size())) {
			throw new IllegalArgumentException("All arguments must have equal dimensions");
		}
		_dim = numBins.size();
		
		// initialize table
		int totalBins = 1;
		for (int nBin : numBins) {
			totalBins *= nBin;
		}
		_hist = new int[totalBins];
		_totalCount = 0;
		
		// fill up min/max arrays
		_mins = mins.toArray(new Double[0]);
		_maxs = maxs.toArray(new Double[0]);
		_numBins = numBins.toArray(new Integer[0]);
		
		// determine bin sizes
		_binSizes = new double[_dim];
		for (int i=0; i<_dim; i++) {
			_binSizes[i] = (_maxs[i] - _mins[i]) / _numBins[i];
		}
	}
	
	public void record(List<Double> vector) {
		_hist[getIdx(vector)]++;
		_totalCount++;
	}
	
	public int getCount(List<Double> vector) {
		return _hist[getIdx(vector)];
	}
	
	/**
	 * Provides a rejection sampling interface for sampling from this histogram
	 * 
	 * @param vector the vector you wish to use
	 * @return true if the vector should be rejected
	 */
	public boolean rejectVector(List<Double> vector) {
		return Utils.RANDOM.nextDouble() * _totalCount > getCount(vector);
	}
	
	private int getIdx(List<Double> vector) {
		// construct index vector
		int[] indices = new int[_dim];
		for (int i=0; i<_dim; i++) {
			int idx = (int) ((vector.get(i) - _mins[i]) / _binSizes[i]);
			// bound the index at the edges
			indices[i] = idx < 0 ? 0 : (idx >= _numBins[i] ? (_numBins[i] - 1) : idx);
		}
		
		// construct overall index
		int overallIdx = 0;
		for (int i=0; i<_dim; i++) {
			overallIdx = (overallIdx * _numBins[i]) + indices[i];
		}
		
		return overallIdx;
	}
	
	public static void main(String[] args) {
		Histogram hist = new Histogram(Arrays.asList(0.0, 0.0), Arrays.asList(100.0, 100.0), Arrays.asList(10, 5));
		hist.record(Arrays.asList(-1.0, 1.0));
		hist.record(Arrays.asList(101.0, 1.0));
		hist.record(Arrays.asList(11.0, 1.0));
		hist.record(Arrays.asList(11.0, 11.0));
		hist.record(Arrays.asList(21.0, 11.0));
		hist.record(Arrays.asList(21.0, 21.0));
		
		// should be 1;
		System.out.println(hist.getCount(Arrays.asList(.5, .5)));
		// should be 1;
		System.out.println(hist.getCount(Arrays.asList(99.0, .5)));
		// should be 2;
		System.out.println(hist.getCount(Arrays.asList(15.0, 5.0)));
		// should be 1;
		System.out.println(hist.getCount(Arrays.asList(25.0, 5.0)));
		// should be 1;
		System.out.println(hist.getCount(Arrays.asList(25.0, 25.0)));
		// should be 0;
		System.out.println(hist.getCount(Arrays.asList(35.0, 35.0)));
	}
}
