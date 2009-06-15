package jene.samples.color2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jene.AbstractOrganism;
import jene.Mutation;
import jene.Mutator;
import jene.Node;
import jene.Organism;
import jene.Pixel;
import jene.Vector3;


/**
 *  
 * @author James Stout
 *
 */
 
 public class C2DOrganism extends AbstractOrganism<Pixel, double[]> {

	private Node<Pixel, Vector3> _root;
	
	C2DOrganism(Node<Pixel, Vector3> root) {
		_root = root;
	}
	
	public double[] evaluate(Pixel input) {
		return _root.evaluate(input).data();
	}

	public List<Node<?, ?>> nodes() {
		return Arrays.<Node<?, ?>>asList(_root);
	}

	public C2DOrganism mutateAsexually(Mutator mutator) {
		List<Mutation> mutations = new ArrayList<Mutation>();
		Node<Pixel, Vector3> newRoot = mutator.mutateNodeTree(_root, mutations);
		return new C2DOrganism(newRoot);
	}

	public C2DOrganism mutateSexually(
			Organism<Pixel, double[]> mate, Mutator mutator) {
		@SuppressWarnings("unchecked")
		List<Node<Pixel, Vector3>> mateNodes = (List<Node<Pixel, Vector3>>) mate.nodes();
		Node<Pixel, Vector3> newRoot = mutator.mateNodeTrees(_root, mateNodes.get(0));
		return new C2DOrganism(newRoot);
	}

}