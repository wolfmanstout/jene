package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class AbsNodeFactory extends Node1ArgFactory<double[]> {
	
	public static final AbsNodeFactory INSTANCE = new AbsNodeFactory();
	private AbsNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	public AbsNode newInstance(List<Node<Pixel, ?>> children) {
		return new AbsNode(children);
	}

}
