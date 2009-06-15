package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class ToonNode extends Node2Arg<double[], double[]> {

	protected ToonNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public ToonNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2) {
		super(arg1, arg2);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		
		for(int i = 0; i<a.length; i++){
			double frac = a[i]/b[i];
			double value = b[i] * Math.round(frac);
			a[i] = value;
		}
		return a;
	}

	public ToonNodeFactory getFactory() {
		return ToonNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "Toon";
	}

}
