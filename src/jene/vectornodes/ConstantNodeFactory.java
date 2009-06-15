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
 
 public class ConstantNodeFactory extends Node0ArgFactory {

	
	


	
	private Histogram _hist;
	private int _nDims;
	
	public ConstantNodeFactory(int ndims){
		_nDims = ndims;
		List<Double> mins = Collections.nCopies(ndims, Double.valueOf(-1.0));
		List<Double> maxs = Collections.nCopies(ndims, Double.valueOf(1.0));
		List<Integer> numBins = Collections.nCopies(ndims, Integer.valueOf(10));
		
		_hist = new Histogram(mins, maxs, numBins);
	}
	
	public ConstantNode newInstance(List<Node<Pixel, ?>> children) {
		return new ConstantNode(children, generateCoefs(), _nDims, this);
	}
	
	void recordCoefs(List<Double> coefs) {
		_hist.record(coefs);
	}
	
	List<Double> generateCoefs() {
		List<Double> coefs;
		if (Utils.RANDOM.nextDouble() < .25) {
			coefs = new ArrayList<Double>();
			for(int i = 0; i<_nDims; i++){
				coefs.add(Utils.RANDOM.nextDouble() * 2.0 - 1.0);
			}
		} else {
			do {
				coefs = new ArrayList<Double>();
				for(int i = 0; i<_nDims; i++){
					coefs.add(Utils.RANDOM.nextDouble() * 2.0 - 1.0);
				}
				// TODO adjust probability
			} while (_hist.rejectVector(coefs));
		}
		return coefs;
	}

}
