package jene.vectornodes;

import java.util.Collections;
import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class YNode extends Node0Arg {
	int _nDims;
	YNodeFactory _fac;
	protected YNode(int ndims, YNodeFactory fac) {
		super();
		_nDims = ndims;
		_fac = fac;
	}

	protected YNode(List<Node<Pixel, ?>> children, int ndims, YNodeFactory fac) {
		super(children);
		_nDims = ndims;
		_fac = fac;
	}

	public List<Node<Pixel, ?>> children() {
		return Collections.emptyList();
	}

	public double[] evaluate(Pixel input) {
		double[] output = new double[_nDims];
		for(int i = 0; i < _nDims; i++){
			output[i] = input.y();
		}
		return output;
}

	public YNodeFactory getFactory() {
		return _fac;
	}

	@Override
	protected String nodeName() {
		return "Y";
	}

}
