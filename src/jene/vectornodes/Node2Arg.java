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
 
 public abstract class Node2Arg<A1, A2> extends AbstractNode {

	final protected Node<Pixel, A1> _arg1;
	final protected Node<Pixel, A2> _arg2;

	// convenience constructor
	protected Node2Arg(Node<Pixel, A1> arg1, Node<Pixel, A2> arg2) {
		_arg1 = arg1;
		_arg2 = arg2;
		
		_rank = 0;
		if (!children().isEmpty()) {
			for (Node<Pixel, ?> child : children()) {
				_rank = Math.max(_rank, child.rank());
			}
			_rank++;
		}
	}

	@SuppressWarnings("unchecked")
	protected Node2Arg(List<Node<Pixel, ?>> children) {
		// check types
		super(children);

		_arg1 = (Node<Pixel, A1>) children.get(0);
		_arg2 = (Node<Pixel, A2>) children.get(1);
	}

	@SuppressWarnings("unchecked")
	public List<Node<Pixel, ?>> children() {
		return Arrays.asList(_arg1, _arg2);
	}
}
