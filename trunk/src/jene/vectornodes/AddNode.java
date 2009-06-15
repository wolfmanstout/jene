package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class AddNode extends Node2Arg<double[], double[]> {

	protected AddNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public AddNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2) {
		super(arg1, arg2);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = (a[i] + b[i]) / 2;
		}
		return a;
	}

	public AddNodeFactory getFactory() {
		return AddNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "+";
	}

}
