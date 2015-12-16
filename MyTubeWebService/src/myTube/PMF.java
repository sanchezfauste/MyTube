package myTube;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMF {
	
	private static final PersistenceManagerFactory pmf = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	private PMF() {
		
	}
	
	public static PersistenceManagerFactory get() {
		return pmf;
	}

}
