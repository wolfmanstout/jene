package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class MinNode extends Node2Arg<double[], double[]> {

	protected MinNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public MinNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2) {
		super(arg1, arg2);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = Math.min(a[i], b[i]);
		}
		return a;
	}

	public MinNodeFactory getFactory() {
		return MinNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "Min";
	}

}
