package jene.vectornodes;

import java.text.DecimalFormat;
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
 
 public class ConstantNode extends Node0Arg {

	private List<Double> _coefs;
	private ConstantNodeFactory _fac;
	private int _nDims;
	
	protected ConstantNode(List<Double> coefs, int nDims, ConstantNodeFactory fac) {
		super();
		_fac = fac;
		_coefs = coefs;
		_nDims = nDims;
	}

	protected ConstantNode(List<Node<Pixel, ?>> children, List<Double> coefs, int nDims, ConstantNodeFactory fac) {
		super(children);
		_coefs = coefs;
		_fac = fac;
		_nDims = nDims;
	}

	public double[] evaluate(Pixel input) {
		return Utils.listToArray(_coefs);
	}

	public ConstantNodeFactory getFactory() {
		return _fac;
	}
	
	@Override
	protected String nodeName() {
		StringBuilder sb = new StringBuilder();
		sb.append("#(");
		DecimalFormat df = new DecimalFormat("#.##");
		for (double coef : _coefs) {
			sb.append(df.format(coef));
		}
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public boolean canShuffle() {
		return true;
	}
	
	@Override
	public ConstantNode shuffle() {
		Double[] newCoefs = new Double[_nDims];
		for (int i=0; i<newCoefs.length; i++) {
			// TODO adjust probability
			newCoefs[i] = _coefs.get(0) + .2 * Utils.RANDOM.nextGaussian();
			newCoefs[i] = Math.min(1, Math.max(-1, newCoefs[i]));
		}
		
		return new ConstantNode(Arrays.asList(newCoefs), _nDims, _fac);
	}
	
	@Override
	public ConstantNode copyWithChildren(List<Node<Pixel, ?>> children) {
		// we could just return 'this' because this is terminal, but this is more generic and will check the types of children
		return new ConstantNode(children, _coefs, _nDims, _fac);
	}
	
	@Override
	public void record() {
		getFactory().recordCoefs(_coefs);
	}

}
