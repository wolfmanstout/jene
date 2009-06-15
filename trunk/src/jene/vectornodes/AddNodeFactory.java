package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class AddNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final AddNodeFactory INSTANCE = new AddNodeFactory();
	private AddNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public AddNode newInstance(List<Node<Pixel, ?>> children) {
		return new AddNode(children);
	}

}
