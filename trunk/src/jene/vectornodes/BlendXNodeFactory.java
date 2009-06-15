package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class BlendXNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final BlendXNodeFactory INSTANCE = new BlendXNodeFactory();
	private BlendXNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public BlendXNode newInstance(List<Node<Pixel, ?>> children) {
		return new BlendXNode(children);
	}

}
