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
 
 public class XNode extends Node0Arg {
	int _nDims;
	XNodeFactory _fac;
	protected XNode(int ndims, XNodeFactory fac) {
		super();
		_nDims = ndims;
		_fac = fac;
	}

	protected XNode(List<Node<Pixel, ?>> children, int ndims, XNodeFactory fac) {
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
			output[i] = input.x();
		}
		return output;
}

	public XNodeFactory getFactory() {
		return _fac;
	}

	@Override
	protected String nodeName() {
		return "X";
	}

}
