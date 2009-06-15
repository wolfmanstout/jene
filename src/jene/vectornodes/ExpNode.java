package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class ExpNode extends Node1Arg<double[]> {

	protected ExpNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public ExpNode(Node<Pixel, double[]> arg1) {
		super(arg1);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = Math.exp(a[i]);
		}
		return a;
	}

	public ExpNodeFactory getFactory() {
		return ExpNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "exp";
	}

}
