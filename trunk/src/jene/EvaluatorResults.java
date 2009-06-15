package jene;

import java.util.List;

/**
 *  
 * @author James Stout
 *
 */
 
 public class EvaluatorResults<E extends Organism<?,?>> {

	private List<E> _asexual;
	private List<Couple<E>> _sexual;
	
	public static class Couple<E extends Organism<?,?>> {
		private final E _org1;
		private final E _org2;
		
		public Couple(E org1, E org2) {
			this._org1 = org1;
			this._org2 = org2;
		}
		
		public E org1() {
			return _org1;
		}
		
		public E org2() {
			return _org2;
		}
		
	}

	public EvaluatorResults(List<E> asexual,
			List<Couple<E>> sexual) {
		this._asexual = asexual;
		this._sexual = sexual;
	}
	
	public List<E> asexual() {
		return _asexual;
	}
	
	public List<Couple<E>> sexual() {
		return _sexual;
	}
}
