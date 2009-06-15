package jene.vectornodes;

import jene.NodeFactory;


/**
 *  
 * @author James Stout
 *
 */
 
 public class WrappedDoubleFactory<I> extends WrappedFactory<I, Double, double[]> {

	public WrappedDoubleFactory(NodeFactory<I, double[]> factory) {
		super(factory);
	}

	@Override
	protected double[] unwrap(Double output) {
		double[] result = new double[1];
		result[0] = output;
		return result;
	}

	@Override
	protected Double wrap(double[] output) {
		return output[0];
	}

	@Override
	protected Class<double[]> unwrappedType() {
		return double[].class;
	}

	@Override
	protected Class<Double> wrappedType() {
		return Double.class;
	}

}
