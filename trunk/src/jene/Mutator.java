package jene;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * @author James Stout
 *
 */
 
 public class Mutator {
	
	private final FactoryManager _manager;
	private final List<Mutation> _mutations;
	
	private final MapCounter<Mutation> _mutCounter = new MapCounter<Mutation>();
	
	
	public Mutator(FactoryManager manager) {
		super();
		_manager = manager;
		_mutations = new ArrayList<Mutation>();
	}
	
	public void addMutation(Mutation mut) {
		_mutations.add(mut);
	}
	
	public <I,O> Node<I,O> mutateNodeTree(Node<I,O> node, List<Mutation> mutationsUsed) {
		return mutateNode(node, 0, Utils.getMaxDepth(node), mutationsUsed);
	}
	
	public <I,O> Node<I,O> mateNodeTrees(Node<I,O> baseTree, Node<I,O> mateTree) {
		// construct tables of what trees are at what depth
		List<List<Node<I,?>>> baseTreeTable = new ArrayList<List<Node<I,?>>>();
		addToTreeTable(baseTreeTable, baseTree);
		List<List<Node<I,?>>> mateTreeTable = new ArrayList<List<Node<I,?>>>();
		addToTreeTable(mateTreeTable, mateTree);
		
		return mateNodeTrees(baseTree, 0, Utils.getMaxDepth(baseTree), baseTreeTable, mateTreeTable);
	}
	
	private <I, O> void addToTreeTable(List<List<Node<I,?>>> table, Node<I,O> node) {
		for (Node<I,?> child : node.children()) {
			addToTreeTable(table, child);
		}
		
		int rank = node.rank();
		if (rank >= table.size()) {
			table.add(new ArrayList<Node<I,?>>());
		}
		table.get(rank).add(node);
	}
	
	private <I,O> Node<I,O> mateNodeTrees(Node<I,O> baseTree, int depth, int maxDepth, List<List<Node<I,?>>> baseTreeTable, List<List<Node<I,?>>> mateTreeTable) {
		Node<I,O> mutatedNode = baseTree;
		
		// possibly swap current node
		double pMutation = getMutationProbability(depth, maxDepth);
		if (Utils.RANDOM.nextDouble() < pMutation) {
			System.out.println("Trying to mate");
			
			// select a random node from the mate
			List<Node<I,?>> nodeChoices = mateTreeTable.get(Math.min(baseTree.rank(), mateTreeTable.size()-1));
			int nodeIdx = Utils.RANDOM.nextInt(nodeChoices.size());
			Node<I,?> mateNode = nodeChoices.get(nodeIdx); 
			if (mateNode.returnType() == baseTree.returnType()) {
				// do mating
				System.out.println("Swapping nodes");
				@SuppressWarnings("unchecked")
				Node<I,O> checkedMateNode = (Node<I, O>) mateNode;
				mutatedNode = checkedMateNode;
			}
		}
		
		List<Node<I,?>> children = new ArrayList<Node<I,?>>();
		for (int i=0; i<mutatedNode.children().size(); i++) {
			if (i < baseTree.children().size()
					&& baseTree.children().get(i).returnType() == mutatedNode.children().get(i).returnType()) {
				children.add(mateNodeTrees(baseTree.children().get(i), depth+1, maxDepth, baseTreeTable, mateTreeTable));
			} else {
				children.add(mutatedNode.children().get(i));
			}
		}
		
		return mutatedNode.copyWithChildren(children);
	}
	
	private <I,O> Node<I,O> mutateNode(Node<I,O> node, int depth, int maxDepth, List<Mutation> mutationsUsed) {
		
		double pMutation = getMutationProbability(depth, maxDepth);
		
		// possibly mutate current node
		if (Utils.RANDOM.nextDouble() < pMutation) {
			// pick a random mutation
			Mutation mutation;
			if (Utils.RANDOM.nextDouble() < .5) {
				do {
					int mIdx = Utils.RANDOM.nextInt(_mutations.size());
					mutation = _mutations.get(mIdx);
				} while ((!node.canShuffle() && mutation == BasicMutation.SHUFFLE));
			} else {
				do {
					int mIdx = Utils.RANDOM.nextInt(_mutations.size());
					mutation = _mutations.get(mIdx);
					// TODO fix bias issue
					// TODO adjust probability
				} while ((!node.canShuffle() && mutation == BasicMutation.SHUFFLE)
						|| _mutCounter.rejectKey(mutation));
			}
			
			// perform mutation
			System.out.println("Performing mutation: " + mutation);
			node = mutation.mutate(node, new NodeContext(depth), _manager);
			
			// record mutation
			mutationsUsed.add(mutation);
		}
		
		// get mutated children
		List<Node<I,?>> mutatedChildren = new ArrayList<Node<I,?>>();
		for (Node<I,?> child : node.children()) {
			mutatedChildren.add(mutateNode(child, depth+1, maxDepth, mutationsUsed));
		}
		return node.getFactory().newInstance(mutatedChildren);
	}
	
	public void recordMutations(List<Mutation> mutations) {
		for (Mutation mut : mutations) {
			_mutCounter.incrementCount(mut);
		}
	}

	private double getMutationProbability(int depth, int maxDepth) {
		// TODO adjust probability
		// TODO depth may go above maxDepth
		double sharpness = .5;
		double maxProbability = .3;
		return maxProbability * Math.exp(-sharpness * (maxDepth - depth));
	}

}
