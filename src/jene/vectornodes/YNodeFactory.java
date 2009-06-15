package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class YNodeFactory extends Node0ArgFactory {
	int _nDims;
	public YNodeFactory(int ndims){
		_nDims = ndims;		
	}
	
	public YNode newInstance(List<Node<Pixel, ?>> children) {
		return new YNode(children, _nDims, this);
	}

}
