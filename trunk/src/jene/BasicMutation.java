package jene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *  
 * @author James Stout
 *
 */
 
 public enum BasicMutation implements Mutation {

	REPLACE_SUBTREE {
		public <I, O> Node<I, O> mutate(Node<I, O> node, NodeContext context, FactoryManager manager) {
			return Utils.createSubtree(manager, node.inputType(), node.returnType(), context.depth());
		}
	},
	
	PUSHUP {
		@SuppressWarnings("unchecked")
		public <I, O> Node<I, O> mutate(Node<I, O> node, NodeContext context,
				FactoryManager manager) {
			List<Node<I,O>> eligibleChildren = new ArrayList<Node<I,O>>();
			for (Node<I,?> child : node.children()) {
				if (child.returnType() == node.returnType()) {
					eligibleChildren.add((Node<I,O>) child);
				}
			}
			if (eligibleChildren.size() == 0) {
				return node;
			} else {
				int childIdx = Utils.RANDOM.nextInt(eligibleChildren.size());
				return eligibleChildren.get(childIdx);
			}
		}
	},
	
	PULLDOWN {
		public <I, O> Node<I, O> mutate(Node<I, O> node, NodeContext context,
				FactoryManager manager) {

			// get a random factory using rejection sampling
			NodeFactory<I, O> factory;
			do {
				factory = manager.factoryWithType(node.inputType(), node.returnType());
			} while (!factory.childrenTypes().contains(node.returnType()) 
					|| Utils.rejectSubtree(factory.childrenTypes().size(), 1, manager.getMaxChildren(), context.depth()));
			

			// find an eligible child slot
			int childSlot;
			do {
				childSlot = Utils.RANDOM.nextInt(factory.childrenTypes().size());
			} while (factory.childrenTypes().get(childSlot) != node.returnType());
			
			// construct children
			List<Node<I,?>> children = new ArrayList<Node<I,?>>();
			for (int i=0; i<factory.childrenTypes().size(); i++) {
				if (i == childSlot) {
					children.add(node);
				} else {
					children.add(Utils.createSubtree(manager, factory.inputType(), factory.childrenTypes().get(i), context.depth() + 1));
				}
			}
			
			// construct node and return
			return factory.newInstance(children);
		}
	},
	
	REPLACE_NODE {
		public <I, O> Node<I, O> mutate(Node<I, O> node, NodeContext context,
				FactoryManager manager) {

			// get a random factory using rejection sampling
			NodeFactory<I, O> factory;
			do {
				factory = (NodeFactory<I, O>) manager.factoryWithType(node.inputType(), node.returnType());
			} while (Utils.rejectSubtree(factory.childrenTypes().size(), 0, manager.getMaxChildren(), context.depth()));
			
			// group children by type
			Map<Class<?>, Queue<Node<I,?>>> childrenGroups = new HashMap<Class<?>, Queue<Node<I,?>>>();
			for (Node<I,?> child : node.children()) {
				if (!childrenGroups.containsKey(child.returnType())) {
					childrenGroups.put(child.returnType(), new LinkedList<Node<I,?>>());
				}
				childrenGroups.get(child.returnType()).add(child);
			}
			
			// create children using existing children when possible
			List<Node<I,?>> children = new ArrayList<Node<I,?>>();
			for (Class<?> childType : factory.childrenTypes()) {
				Queue<Node<I,?>> eligibleChildren = childrenGroups.get(childType);
				if (eligibleChildren != null && !eligibleChildren.isEmpty()) {
					children.add(eligibleChildren.remove());
				} else {
					children.add(Utils.createSubtree(manager, factory.inputType(), childType, context.depth() + 1));
				}
			}
			
			// construct node and return
			return factory.newInstance(children);
		}
	}, 

	SHUFFLE {
		//@Override
		public <I, O> Node<I, O> mutate(Node<I, O> node, NodeContext context,
				FactoryManager manager) {
			return node.shuffle();
		}
	}
	
}
