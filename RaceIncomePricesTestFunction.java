/*
 * Created on Apr 22, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mirar;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RaceIncomePricesTestFunction extends AgentDecision {

	/**
	 * 
	 */
	public RaceIncomePricesTestFunction() { // Priyasree_DeadCode : Unreachable code_
		super();
	}

	/**
	 * @param agent
	 */
//	public RaceIncomePricesTestFunction(Agent agent) {
//		super(agent);
//	}

	/* (non-Javadoc)
	 * @see edu.ucla.stat.mirar.AgentDecision#select(java.util.ArrayList, edu.ucla.stat.mirar.HousingUnit)
	 */
	public HousingUnit select(
		ArrayList possibleHousingUnitList,
		HousingUnit currUnit, Agent agent) { // Priyasree_DeadCode : Unreachable code_
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ucla.stat.mirar.AgentDecision#computeUtility(edu.ucla.stat.mirar.Block)
	 */
	public double computeUtility(Block b, Agent agent, int tenure) { // Priyasree_DeadCode : Unreachable code_
		return 0;
	}
	
	public double computeMarginalUtility(Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		double marginalUtility = 0.0;
		if (b == null) {
			System.out.println("raceincome neighbor computeUtility Block is null");
		}
		else {
			marginalUtility = 0;
		}		
		return marginalUtility;
	}
	
	public double solveForPrice(double marginalUtil, Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		return (1.0*b.getNeighborhoodMedianIncome())/1.0 + (1.0*b.getNeighborhoodMedianRent(tenure))/1.0 - marginalUtil/1.0;
	}

	public String toString() { // Priyasree_DeadCode : Unreachable code_
	    return "RaceIncomePricesTestFunction";
	}
}
