package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class HsvToRgbNodeFactory extends Node1ArgFactory<double[]> {
	
	public static final HsvToRgbNodeFactory INSTANCE = new HsvToRgbNodeFactory();
	private HsvToRgbNodeFactory() {}
	
	@Override
	protected Class<double[]> arg1Type() {
		return double[].class;
	}

	public HsvToRgbNode newInstance(List<Node<Pixel, ?>> children) {
		return new HsvToRgbNode(children);
	}

}
