package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class XORNodeFactory extends Node2ArgFactory<double[], double[]> {
	
	public static final XORNodeFactory INSTANCE = new XORNodeFactory();
	private XORNodeFactory() {
	}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	@Override
	protected Class<double[]> arg2Type() {
		return double[].class;
	}

	public XORNode newInstance(List<Node<Pixel, ?>> children) {
		return new XORNode(children);
	}

}
