package jene.vectornodes;

import java.util.ArrayList;
import java.util.List;

import jene.Node;
import jene.NodeFactory;


/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class WrappedFactory<I, O1, O2> implements NodeFactory<I, O1> {

	private NodeFactory<I,O2> _factory;
	private List<Class<?>> _childrenTypes;
	
	public WrappedFactory(NodeFactory<I, O2> factory) {
		_factory = factory;
		_childrenTypes = new ArrayList<Class<?>>();
		for (Class<?> type : _factory.childrenTypes()) {
			if (type == unwrappedType()) {
				_childrenTypes.add(wrappedType());
			}
		}
	}

	public Node<I, O1> newInstance(List<Node<I, ?>> children) {
		return new WrappedNode(_factory.newInstance(unwrapChildren(children)), children);
	}
	
	public Class<I> inputType() {
		return _factory.inputType();
	}
	
	public Class<O1> returnType() {
		return wrappedType();
	}
	
	public List<Class<?>> childrenTypes() {
		return _childrenTypes;
	}
	
	abstract protected O1 wrap(O2 output);
	abstract protected O2 unwrap(O1 output);
	abstract protected Class<O1> wrappedType();
	abstract protected Class<O2> unwrappedType();
	
	@SuppressWarnings("unchecked")
	private List<Node<I,?>> unwrapChildren(List<Node<I, ?>> children) {
		List<Node<I,?>> unwrapped = new ArrayList<Node<I,?>>();
		for (Node<I,?> child : children) {
			if (child.returnType() == wrappedType()) {
				unwrapped.add(new UnwrappedNode((Node<I, O1>) child));
			} else {
				unwrapped.add(child);
			}
		}
		return unwrapped;
	}
	
	private class WrappedNode implements Node<I, O1> {

		private final Node<I, O2> _node;
		private final List<Node<I, ?>> _children;

		public WrappedNode(Node<I, O2> node, List<Node<I, ?>> children) {
			_node = node;
			_children = new ArrayList<Node<I,?>>(children);
		}
		
		public boolean canShuffle() {
			return _node.canShuffle();
		}

		public List<Node<I, ?>> children() {
			return _children;
		}

		public List<Class<?>> childrenTypes() {
			return getFactory().childrenTypes();
		}

		public Node<I, O1> copyWithChildren(List<Node<I, ?>> children) {
			return new WrappedNode(_node.copyWithChildren(unwrapChildren(children)), children);
		}

		public O1 evaluate(I input) {
			return wrap(_node.evaluate(input));
		}

		public NodeFactory<I, O1> getFactory() {
			return WrappedFactory.this;
		}

		public Class<I> inputType() {
			return getFactory().inputType();
		}

		public void record() {
			_node.record();
		}

		public Class<O1> returnType() {
			return getFactory().returnType();
		}

		public Node<I, O1> shuffle() {
			return new WrappedNode(_node.shuffle(), _children);
		}
		
		@Override
		public String toString() {
			return _node.toString();
		}

		public int rank() {
			return _node.rank();
		}
		
	}
	
	// We must "unwrap" any recursive operation called by the wrapped node
	// The only such instances are evaluate() and toString()
	private class UnwrappedNode implements Node<I, O2> {

		private Node<I,O1> _node;
		
		public UnwrappedNode(Node<I,O1> node) {
			_node = node;
		}

		public boolean canShuffle() {
			return false;
		}

		public List<Node<I, ?>> children() {
			return null;
		}

		public List<Class<?>> childrenTypes() {
			return null;
		}

		public Node<I, O2> copyWithChildren(List<Node<I, ?>> children) {
			return null;
		}

		public O2 evaluate(I input) {
			return unwrap(_node.evaluate(input));
		}

		public NodeFactory<I, O2> getFactory() {
			return null;
		}

		public Class<I> inputType() {
			return null;
		}

		public void record() {
			
		}

		public Class<O2> returnType() {
			return null;
		}

		public Node<I, O2> shuffle() {
			return null;
		}
		
		@Override
		public String toString() {
			return _node.toString();
		}

		public int rank() {
			return _node.rank();
		}
		
	}
	
}
