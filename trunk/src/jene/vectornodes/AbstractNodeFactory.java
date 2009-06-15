package jene.vectornodes;

import jene.NodeFactory;
import jene.Pixel;

/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class AbstractNodeFactory implements NodeFactory<Pixel, double[]>{
	
	public Class<Pixel> inputType() {
		return Pixel.class;
	}
	
	public Class<double[]> returnType() {
		return double[].class;
	}
	
}