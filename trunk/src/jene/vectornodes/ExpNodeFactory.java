package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class ExpNodeFactory extends Node1ArgFactory<double[]> {
	
	public static final ExpNodeFactory INSTANCE = new ExpNodeFactory();
	private ExpNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	public ExpNode newInstance(List<Node<Pixel, ?>> children) {
		return new ExpNode(children);
	}

}
