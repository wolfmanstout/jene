package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class XNodeFactory extends Node0ArgFactory {
	int _nDims;
	public XNodeFactory(int ndims){
		_nDims = ndims;		
	}
	
	public XNode newInstance(List<Node<Pixel, ?>> children) {
		return new XNode(children, _nDims, this);
	}

}
