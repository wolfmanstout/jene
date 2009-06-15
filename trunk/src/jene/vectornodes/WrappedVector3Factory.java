package jene.vectornodes;

import jene.NodeFactory;
import jene.Vector3;


/**
 *  
 * @author James Stout
 *
 */
 
 public class WrappedVector3Factory<I> extends WrappedFactory<I, Vector3, double[]> {

	public WrappedVector3Factory(NodeFactory<I, double[]> factory) {
		super(factory);
	}

	@Override
	protected double[] unwrap(Vector3 output) {
		return output.data();
	}

	@Override
	protected Vector3 wrap(double[] output) {
		return new Vector3(output[0], output[1], output[2]);
	}

	@Override
	protected Class<double[]> unwrappedType() {
		return double[].class;
	}

	@Override
	protected Class<Vector3> wrappedType() {
		return Vector3.class;
	}

}
