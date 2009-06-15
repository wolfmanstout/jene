package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class MultiplyNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final MultiplyNodeFactory INSTANCE = new MultiplyNodeFactory();
	private MultiplyNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public MultiplyNode newInstance(List<Node<Pixel, ?>> children) {
		return new MultiplyNode(children);
	}

}
