package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class MaxNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final MaxNodeFactory INSTANCE = new MaxNodeFactory();
	private MaxNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public MaxNode newInstance(List<Node<Pixel, ?>> children) {
		return new MaxNode(children);
	}

}
