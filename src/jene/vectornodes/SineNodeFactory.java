package jene.vectornodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jene.Histogram;
import jene.Node;
import jene.Pixel;
import jene.Utils;


/**
 *  
 * @author James Stout
 *
 */
 
 public class SineNodeFactory extends Node1ArgFactory<double[]> {
	
	public static final SineNodeFactory INSTANCE = new SineNodeFactory();
	private final int _nDims = 1;
	private Histogram _hist;
	
	private SineNodeFactory() {
		List<Double> mins = Collections.nCopies(_nDims, Double.valueOf(.5 * Math.PI));
		List<Double> maxs = Collections.nCopies(_nDims, Double.valueOf(2 * Math.PI));
		List<Integer> numBins = Collections.nCopies(_nDims, Integer.valueOf(10));
		
		_hist = new Histogram(mins, maxs, numBins);
	}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	public SineNode newInstance(List<Node<Pixel, ?>> children) {
		return new SineNode(children, generateCoefs());
	}
	
	void recordCoefs(List<Double> coefs) {
		_hist.record(coefs);
	}
	
	List<Double> generateCoefs() {
		List<Double> coefs;
		if (Utils.RANDOM.nextDouble() < .25) {
			coefs = new ArrayList<Double>();
			for(int i = 0; i<_nDims; i++){
				coefs.add(Utils.RANDOM.nextDouble() * 1.5 * Math.PI + .5 * Math.PI);
			}
		} else {
			do {
				coefs = new ArrayList<Double>();
				for(int i = 0; i<_nDims; i++){
					coefs.add(Utils.RANDOM.nextDouble() * 1.5 * Math.PI + .5 * Math.PI);
				}
				// TODO adjust probability
			} while (_hist.rejectVector(coefs));
		}
		return coefs;
	}

}
