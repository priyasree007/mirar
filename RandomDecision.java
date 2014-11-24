/*
 *  @author Robert Najlis
 *  Created on Mar 5, 2004
 *
 *
 */
package mirar;

import java.util.*;
import uchicago.src.sim.util.Random;
/**
 * @author rnajlis
 *
 */

public class RandomDecision extends AgentDecision {
    public RandomDecision() {}
 //   public RandomDecision(Agent agent) {
   //     this.agent = agent;
    //}
    
    public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
        possibleHousingUnitList.addAll(possibleHousingUnitList);
        int choice = Random.uniform.nextIntFromTo(0,possibleHousingUnitList.size()-1);
        HousingUnit h = (HousingUnit)possibleHousingUnitList.get(choice);
        possibleHousingUnitList.clear();
        return h;
        
    }
    /**
     * @see edu.ucla.stat.mirar.AgentDecision#computeUtility(edu.ucla.stat.mirar.Block)
     */
    public double computeUtility(Block b, Agent agent, int tenure) {
        return 0;
    }
    
	public double computeMarginalUtility(Block b, int tenure) {
		double marginalUtility = 0.0;
		if (b == null) {
			System.out.println("raceincome neighbor computeUtility Block is null");
		}
		else {
			double medIncome = b.getNeighborhoodMedianIncome();
			double medRent = b.getNeighborhoodMedianRent(tenure);
			marginalUtility = Math.exp(1.0*(medRent) + 1.0*(medIncome));
		}		
		return marginalUtility;
	}
	
	public double solveForPrice(double marginalUtil, Block b, int tenure) {
		return (1.0*b.getNeighborhoodMedianIncome())/1.0 + 1.0 - marginalUtil/1.0;
	}

	public String toString() {
	    return "RandomDecision";
	}
	
}
