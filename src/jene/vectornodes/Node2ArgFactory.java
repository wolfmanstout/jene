package jene.vectornodes;

import java.util.Arrays;
import java.util.List;


/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class Node2ArgFactory<A1, A2> extends AbstractNodeFactory {
	
	abstract protected Class<A1> arg1Type();
	abstract protected Class<A2> arg2Type();
	
	@SuppressWarnings("unchecked")
	public List<Class<?>> childrenTypes() {
		return Arrays.asList(arg1Type(), arg2Type());
	}

}
