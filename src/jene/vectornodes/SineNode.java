package jene.vectornodes;

import java.util.Arrays;
import java.util.List;

import jene.Node;
import jene.Pixel;
import jene.Utils;


/**
 *  
 * @author James Stout
 *
 */
 
 public class SineNode extends Node1Arg<double[]> {

	private final int _nDims = 1;
	private List<Double> _coefs;
	
	protected SineNode(List<Node<Pixel, ?>> children, List<Double> coefs) {
		super(children);
		_coefs = coefs;
	}

	public SineNode(Node<Pixel, double[]> arg1, List<Double> coefs) {
		super(arg1);
		_coefs = coefs;
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = Math.sin(_coefs.get(0) * a[i]);
		}
		return a;
	}

	public SineNodeFactory getFactory() {
		return SineNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "sin";
	}
	
	@Override
	public boolean canShuffle() {
		return true;
	}
	
	@Override
	public SineNode shuffle() {
		Double[] newCoefs = new Double[_nDims];
		for (int i=0; i<newCoefs.length; i++) {
			// TODO adjust probability
			newCoefs[i] = _coefs.get(0) + .5 * Math.PI * Utils.RANDOM.nextGaussian();
			newCoefs[i] = Math.min(2 * Math.PI, Math.max(.5 * Math.PI, newCoefs[i]));
		}
		
		return new SineNode(children(), Arrays.asList(newCoefs));
	}
	
	@Override
	public SineNode copyWithChildren(List<Node<Pixel, ?>> children) {
		return new SineNode(children, _coefs);
	}
	
	@Override
	public void record() {
		getFactory().recordCoefs(_coefs);
	}

}
