package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class AndNode extends Node2Arg<double[], double[]> {

	protected AndNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public AndNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2) {
		super(arg1, arg2);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = (((int) (Integer.MAX_VALUE*a[i])) & ((int) (Integer.MAX_VALUE*b[i]))) / (double) Integer.MAX_VALUE;
		}
		return a;
	}

	public AndNodeFactory getFactory() {
		return AndNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "and";
	}

}
