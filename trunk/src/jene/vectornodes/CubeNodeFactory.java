package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class CubeNodeFactory extends Node1ArgFactory<double[]> {
	
	public static final CubeNodeFactory INSTANCE = new CubeNodeFactory();
	private CubeNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	public CubeNode newInstance(List<Node<Pixel, ?>> children) {
		return new CubeNode(children);
	}

}
