package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class MaxNode extends Node2Arg<double[], double[]> {

	protected MaxNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public MaxNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2) {
		super(arg1, arg2);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = Math.max(a[i], b[i]);
		}
		return a;
	}

	public MaxNodeFactory getFactory() {
		return MaxNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "Max";
	}

}
