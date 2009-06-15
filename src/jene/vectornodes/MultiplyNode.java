package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class MultiplyNode extends Node2Arg<double[], double[]> {

	protected MultiplyNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public MultiplyNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2) {
		super(arg1, arg2);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] *=b[i];
		}
		return a;
	}

	public MultiplyNodeFactory getFactory() {
		return MultiplyNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "*";
	}

}
