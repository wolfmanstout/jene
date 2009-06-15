package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class BlendYNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final BlendYNodeFactory INSTANCE = new BlendYNodeFactory();
	private BlendYNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public BlendYNode newInstance(List<Node<Pixel, ?>> children) {
		return new BlendYNode(children);
	}

}
