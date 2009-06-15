package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class AbsNode extends Node1Arg<double[]> {

	protected AbsNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public AbsNode(Node<Pixel, double[]> arg1) {
		super(arg1);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = Math.abs(a[i]);
		}
		return a;
	}

	public AbsNodeFactory getFactory() {
		return AbsNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "abs";
	}

}
