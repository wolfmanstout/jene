package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class AndNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final AndNodeFactory INSTANCE = new AndNodeFactory();
	private AndNodeFactory() {
	}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public AndNode newInstance(List<Node<Pixel, ?>> children) {
		return new AndNode(children);
	}

}
