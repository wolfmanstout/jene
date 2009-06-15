package jene.samples.bw2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jene.AbstractOrganism;
import jene.Mutation;
import jene.Mutator;
import jene.Node;
import jene.Organism;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class BW2DOrganism extends AbstractOrganism<Pixel, double[]> {

	private Node<Pixel, Double> _root;
	
	BW2DOrganism(Node<Pixel, Double> root) {
		_root = root;
	}

	public double[] evaluate(Pixel input) {
		return new double[] {_root.evaluate(input)};
	}

	public List<Node<?, ?>> nodes() {
		return Arrays.<Node<?, ?>>asList(_root);
	}

	public BW2DOrganism mutateAsexually(Mutator mutator) {
		List<Mutation> mutations = new ArrayList<Mutation>();
		Node<Pixel, Double> newRoot = mutator.mutateNodeTree(_root, mutations);
		return new BW2DOrganism(newRoot);
	}

	public BW2DOrganism mutateSexually(
			Organism<Pixel, double[]> mate, Mutator mutator) {
		@SuppressWarnings("unchecked")
		List<Node<Pixel, Double>> mateNodes = (List<Node<Pixel, Double>>) mate.nodes();
		Node<Pixel, Double> newRoot = mutator.mateNodeTrees(_root, mateNodes.get(0));
		return new BW2DOrganism(newRoot);
	}

}