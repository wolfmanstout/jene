package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class NoiseNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final NoiseNodeFactory INSTANCE = new NoiseNodeFactory();
	private NoiseNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public NoiseNode newInstance(List<Node<Pixel, ?>> children) {
		double z = Math.random() * 256.0;
		return new NoiseNode(children, z);
	}

}
