package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class ToonNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final ToonNodeFactory INSTANCE = new ToonNodeFactory();
	private ToonNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public ToonNode newInstance(List<Node<Pixel, ?>> children) {
		return new ToonNode(children);
	}

}
