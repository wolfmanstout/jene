package jene.vectornodes;

import java.util.Collections;
import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class Node0Arg extends AbstractNode {

	public Node0Arg() {
		super();
		
		_rank = 0;
	}

	public Node0Arg(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public List<Node<Pixel, ?>> children() {
		return Collections.emptyList();
	}
	
	@Override
	public String toString() {
		return nodeName();
	}

}
