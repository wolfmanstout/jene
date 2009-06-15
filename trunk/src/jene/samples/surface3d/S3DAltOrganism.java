package jene.samples.surface3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Point3d;

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
 
 public class S3DAltOrganism extends AbstractOrganism<UV, Point3d> {

	private Node<Pixel, Vector3> _root;
	
	S3DAltOrganism(Node<Pixel, Vector3> root) {
		_root = root;
	}
	
	public Point3d evaluate(UV input) {
		return new Point3d(_root.evaluate(new Pixel(input.u, input.v)).data());
	}

	public List<Node<?, ?>> nodes() {
		return Arrays.<Node<?, ?>>asList(_root);
	}

	public S3DAltOrganism mutateAsexually(Mutator mutator) {
		List<Mutation> mutations = new ArrayList<Mutation>();
		Node<Pixel, Vector3> newRoot = mutator.mutateNodeTree(_root, mutations);
		return new S3DAltOrganism(newRoot);
	}

	public S3DAltOrganism mutateSexually(
			Organism<UV, Point3d> mate, Mutator mutator) {
		@SuppressWarnings("unchecked")
		List<Node<Pixel, Vector3>> mateNodes = (List<Node<Pixel, Vector3>>) mate.nodes();
		Node<Pixel, Vector3> newRoot = mutator.mateNodeTrees(_root, mateNodes.get(0));
		return new S3DAltOrganism(newRoot);
	}

}