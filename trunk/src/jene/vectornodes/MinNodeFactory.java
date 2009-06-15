package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class MinNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final MinNodeFactory INSTANCE = new MinNodeFactory();
	private MinNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public MinNode newInstance(List<Node<Pixel, ?>> children) {
		return new MinNode(children);
	}

}
