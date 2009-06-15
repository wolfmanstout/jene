package jene.vectornodes;

import java.util.Arrays;
import java.util.List;


/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class Node1ArgFactory<A1> extends AbstractNodeFactory {
	
	abstract protected Class<A1> arg1Type();
	
	public List<Class<?>> childrenTypes() {
		return Arrays.<Class<?>>asList(arg1Type());
	}

}
