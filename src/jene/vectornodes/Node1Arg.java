package jene.vectornodes;

import java.util.Arrays;
import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class Node1Arg<A1> extends AbstractNode {

	final protected Node<Pixel, A1> _arg1;

	// convenience constructor
	protected Node1Arg(Node<Pixel, A1> arg1) {
		_arg1 = arg1;
		
		_rank = 0;
		if (!children().isEmpty()) {
			for (Node<Pixel, ?> child : children()) {
				_rank = Math.max(_rank, child.rank());
			}
			_rank++;
		}
	}

	@SuppressWarnings("unchecked")
	protected Node1Arg(List<Node<Pixel, ?>> children) {
		// check types
		super(children);

		_arg1 = (Node<Pixel, A1>) children.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Node<Pixel, ?>> children() {
		return Arrays.<Node<Pixel, ?>>asList(_arg1);
	}
}
