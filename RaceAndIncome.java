/*
 *  @author Elizabeth Bruch, Robert Najlis
 *  Created on Mar 4, 2004
 *
 *
 */

package mirar;
import java.util.*;
import cern.colt.list.*;
import cern.jet.stat.Descriptive; // Priyasree_Audit: Unnecessary import: Delete the import.

/** 
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

public class RaceAndIncome extends AgentDecision {

    public RaceAndIncome() {

    }

   // public RaceAndIncome(Agent agent) {
    //    this.agent = agent;
   // }

	
	/** @return block number of destination block
	 */
	public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
	//    this.possibleHousingUnitList.addAll(possibleHousingUnitList);
		//ArrayList availableBlocks = findBlocksByQuantile(0.15);  // find available blocks
		ArrayList availableUnits = findUnitsByQuantile(0.15, agent, possibleHousingUnitList);
	//	ArrayList availableBlocks = agent.getPossibleHousingUnitList();
	//	availableBlocks.trimToSize();
		DoubleArrayList utilities = computeUtilities(possibleHousingUnitList, agent);
		    //new DoubleArrayList();   
		
		
		//return getChosenHousingUnitFromUtilities(utilities, availableBlocks); //choose from available blocks
		int choice = MirarUtils.sampleFromRawDistribution(utilities);
		if (choice == -1) {
		    possibleHousingUnitList.clear();
		    return null;
		}
		else {
		    possibleHousingUnitList.clear();
		    return (HousingUnit) availableUnits.get(choice);
		}
	}
	
	public DoubleArrayList computeUtilities(ArrayList housingUnits, Agent agent) {
	    DoubleArrayList utilities = new DoubleArrayList();
	    ArrayList availableUnits = findUnitsByQuantile(0.15, agent, housingUnits);
	    Iterator e = availableUnits.iterator();
		//    while(e.hasNext())
		//      ((Cat)e.next()).id();
		 // }
			//	for(int i=0; i<availableBlocks.size(); i++){
			int i=0;
			while (e.hasNext()) {
			    //System.out.println("agent decsion: select : i: " + i + "num hu: " + availableBlocks.size());
			   // Block b = (Block)((HousingUnit)e.next()).getBlock();
			    HousingUnit hu = (HousingUnit) e.next();
//			    Block b = (Block) e.next();
			    Block b =hu.getBlock();
			    utilities.add(computeUtility(hu.getBlock(), agent, agent.getTenure()));
//					Block b = (Block)((HousingUnit) availableBlocks.get(i)).getBlock();
			   
			    i++;
			}
			   return utilities;
	}
	
	public double computeUtility(Block b, Agent agent, int tenure) {
	 
	    if(agent.getRace()==0) {
	      //  utilities.add(Math.exp(b.getPctWhite())) ;
	 
	        return Math.exp(b.getPctWhite());
	    } else if(agent.getRace()==1) {
	        //utilities.add(Math.exp(b.getPctBlack()));
	 	        return Math.exp(b.getPctBlack());
	    }
	    else if(agent.getRace()==2){
	        //utilities.add(Math.exp(b.getPctAsian()));
	 
	        return Math.exp(b.getPctAsian());
	    }
	    else if(agent.getRace()==3){
	        //utilities.add(Math.exp(b.getPctHispanic()));
	 
	        return Math.exp(b.getPctHispanic());
	    } 
	    return -1.0;
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
		return (1.0*b.getNeighborhoodMedianIncome())/1.0 + (1.0*b.getNeighborhoodMedianRent(tenure))/1.0 - marginalUtil/1.0;
	}
	
	public String toString() {
	    return "RaceAndIncome";
	}
}
