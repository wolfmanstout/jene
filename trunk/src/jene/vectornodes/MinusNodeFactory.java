package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class MinusNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final MinusNodeFactory INSTANCE = new MinusNodeFactory();
	private MinusNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public MinusNode newInstance(List<Node<Pixel, ?>> children) {
		return new MinusNode(children);
	}

}
