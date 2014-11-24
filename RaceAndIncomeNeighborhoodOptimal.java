/*
 *  @author Robert Najlis
 *  Created on Apr 5, 2004
 *
 *
 */
package mirar;


import java.util.*;
import cern.colt.list.*;
import cern.jet.stat.Descriptive; // Priyasree: Unnecessary import: Delete the import.

/** 
 * 
 * Uses MirarUtils.getOptimal() to get the highest utility.  If more than one are equal and highest
 * will take first one.
 * <p>
 * 
 * Agents care about both race and income composition of destination neighborhoods. 
 * <p>
 * Agents can only move into blocks where at least x percent of residents have incomes 
 * less than or equal to the agent's income. 
 * <p>
 * 
 * Among affordable units, agents wish to live with as many own-group
 * residents as possible. Agents are sensitive to even the smallest change
 * in proportion own group (continuous function)
 */

public class RaceAndIncomeNeighborhoodOptimal extends AgentDecision {
    

    public RaceAndIncomeNeighborhoodOptimal() {

    }

 //   public RaceAndIncomeNeighborhoodOptimal(Agent agent) {
  //      this.agent = agent;
   // }


/** @return block number of destination block
 */
public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
   // this.possibleHousingUnitList.addAll(possibleHousingUnitList);
    ArrayList availableUnits = new ArrayList();
    availableUnits.addAll(possibleHousingUnitList);
    System.out.println("this available unit size is" + availableUnits.size());
//	ArrayList availableUnits = findUnitsByQuantile(0.15);
	if (currUnit == null) {
            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
        }
	availableUnits.add(currUnit);
	DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
	System.out.println("the number of utilities is: " + utilities.size());

// print utilities
if (MirarUtils.TEST_UTILITIES == true) { // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
    System.out.println("Agent " + agent.getAgentNum() + " utilities");
    for (int i=0; i<utilities.size(); i++) {
        System.out.print(utilities.get(i) + " ");
    }
    System.out.println();
}
// ~: print utilities

	int choice = MirarUtils.selectOptimal(utilities);

	if (choice == -1) {
   		 possibleHousingUnitList.clear();
 //   System.out.println("agent decision randi neighbors: choice is null");
   	 return null;
	}	
	else {
    possibleHousingUnitList.clear();
    System.out.println("size of available units is: " + availableUnits.size());
    //System.out.println("+++++++   AgentDecision select curBlock: " + ((HousingUnit) availableUnits.get(choice)).getBlock().getSTFID());
    return (HousingUnit) availableUnits.get(choice);
	}
}

public DoubleArrayList computeUtilities(ArrayList housingUnits, Agent agent) {
    DoubleArrayList utilities = new DoubleArrayList();
 //   ArrayList availableUnits = findUnitsByQuantile(0.15);
 ArrayList availableUnits = housingUnits; 
    Iterator e = availableUnits.iterator();
int i=0;
while (e.hasNext()) {
    //System.out.println("agent decsion: select : i: " + i + "num hu: " + availableBlocks.size());
   // Block b = (Block)((HousingUnit)e.next()).getBlock();
    HousingUnit hu = (HousingUnit) e.next();
//    Block b = (Block) e.next();
    Block b =hu.getBlock();
    utilities.add(computeUtility(hu.getBlock(), agent, agent.getTenure()));
//Block b = (Block)((HousingUnit) availableBlocks.get(i)).getBlock();
   
    i++;
}
   return utilities;
}

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
    return "RaceAndIncomeNeighborhoodOptimal";
}
}
