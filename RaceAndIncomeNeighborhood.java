/*
 *  @author Elizabeth Bruch, Robert Najlis
 *  Created on Mar 19, 2004
 *
 *
 */

package mirar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

public class RaceAndIncomeNeighborhood extends AgentDecision {

    public RaceAndIncomeNeighborhood() { // Priyasree_DeadCode : Unreachable code_

    }

 //   public RaceAndIncomeNeighborhood(Agent agent) {
  //      this.agent = agent;
   // }

	
	/** @return block number of destination block
	 */
    public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) { // Priyasree_DeadCode : Unreachable code_
		
        PrintWriter testSelect = null;
        try {
            testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", true));
            
        } catch (IOException ioe) { // Priyasree_Audit: Empty catch clause for exception ioe_Delete the empty catch clause. // Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
            
        }
        
        
	  //  this.possibleHousingUnitList.addAll(possibleHousingUnitList);
		
		ArrayList availableUnits = new ArrayList();
		availableUnits.addAll(agent.getPossibleHousingUnitList());

		if (currUnit == null) {
            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
        }
		//System.out.println("-------   AgentDecision select currBlock: " + currUnit.getBlock().getSTFID());
		
	// availableUnits.add(currUnit);

		DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
		
		double utilityCurrentUnit = computeUtilitityForOwnUnit(currUnit); 

		utilities.add(utilityCurrentUnit);
		availableUnits.add(currUnit);

	//	System.out.println("Agent income is " + agent.getIncome() + " and race is " + agent.getRace());
	//	System.out.println("current STFID is " + agent.getSTFID());
//	for(int i=0; i<utilities.size(); i++){
//	System.out.println("utility i from select function is: " + utilities.get(i));
//	}
		    //new DoubleArrayList();   
		
		//System.out.println("AgentDecision select availableUnits " + availableUnits.size() + "  utilities: " + utilities.size());
		//return getChosenHousingUnitFromUtilities(utilities, availableBlocks); //choose from available blocks
		int choice = MirarUtils.sampleFromRawDistribution(utilities);
    /*    
          testSelect.println("agent decision randi neighbors: choice is null , choice == " + choice);
          testSelect.println("RaceAndIncomeNeighborhood#select:  Agent income is " + agent.getIncome() + " and race is " + agent.getRace());
          testSelect.println("RaceAndIncomeNeighborhood#select:  current STFID is " + agent.getSTFID());
          for(int i=0; i<utilities.size(); i++){
              testSelect.println("utility i from select function is: " + utilities.get(i));
          }
          testSelect.println("AgentDecision select availableUnits " + availableUnits.size() + "  utilities: " + utilities.size());
          testSelect.close();
      */  
        
		if (choice == -1) {
		    possibleHousingUnitList.clear();
		    System.out.println("agent decision randi neighbors: choice is null , choice == " + choice);
		    
            return null;
		}
		else {
		    possibleHousingUnitList.clear();
		//    System.out.println("+++++++   AgentDecision select curBlock: " + ((HousingUnit) availableUnits.get(choice)).getBlock().getSTFID());
		    return (HousingUnit) availableUnits.get(choice);
		}

	}
	
	/*public DoubleArrayList computeUtilities(ArrayList housingUnits, Agent agent) {
	    DoubleArrayList utilities = new DoubleArrayList();
	//	ArrayList availableUnits = possibleHousingUnitList;
	    Iterator e = availableUnits.iterator();

			int i=0;
			while (e.hasNext()) {
			    //System.out.println("agent decsion: select : i: " + i + "num hu: " + availableBlocks.size());
			   // Block b = (Block)((HousingUnit)e.next()).getBlock();
			    HousingUnit hu = (HousingUnit) e.next();
//			    Block b = (Block) e.next();
			    Block b =hu.getBlock();
			    utilities.add(computeUtility(hu.getBlock(), agent));
//					Block b = (Block)((HousingUnit) availableBlocks.get(i)).getBlock();
			   
			    i++;
			}
			   return utilities;
	}*/
	



	public double computeUtility(Block b, Agent agent, int tenure) { // Priyasree_DeadCode : Unreachable code_
		double utility=0.0;
		ArrayList blocks = new ArrayList();
		if (b == null) {
		System.out.println("raceincome neighbor computeUtility Block is null");
		}
		else {
			
		//	if(agent.getRace()==agent.BLACK){
		//	 System.out.println("inside blk loop");
		//	utility=Math.exp(-.0030126*b.getMedianRent() + .0015209*b.getMedianRent() 
		//			+ .008323*(this.getNeighborhoodMedianIncome(b)/1000.0) -.0664354*(this.getNeighborhoodMedianIncome(b)/1000.0));
//	  	} else if(agent.getRace()==agent.HISPANIC){
	  	//	 System.out.println("inside nonblk loop");
//			utility=Math.exp(-.0030126*b.getMedianRent() -.0015819*b.getMedianRent()
//					+ .008323*(this.getNeighborhoodMedianIncome(b)/1000.0) -.0588089*(this.getNeighborhoodMedianIncome(b)/1000.0));
	  	//	} else if(agent.getRace()==agent.WHITE | agent.getRace()==agent.ASIAN){
	  			utility=Math.exp(-b.getMedianRent(tenure)/1000 -b.getPctBlkInNeighborhood());// b.getPctBlack());
	 // 		}
		}

//		System.out.println("this is med income "  + b.getMedianIncome());
//		System.out.println("this is med rent " + b.getMedianRent());
//System.out.println("this is bp: " + b.getPctBlkInNeighborhood() + " and this is total agents in neigh " + b.getTotalAgentsInNeighborhood());
//		System.out.println("agent race is: " + agent.getRace());
//		System.out.println("this is util: " + utility);
	  return utility;
	}
	
	public double computeUtilitityForOwnUnit(HousingUnit currUnit) { // Priyasree_DeadCode : Unreachable code_
		Block b = currUnit.getBlock();
		return Math.exp(0.0 + -b.getMedianRent(currUnit.getTenure())/1000 -b.getPctBlkInNeighborhood()); 
	}

	    
	public double computeMarginalUtility(Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		double marginalUtility = 0.0;
		if (b == null) {
		System.out.println("raceincome neighbor computeUtility Block is null");
		}
		else {
	  //  double medIncome = this.getNeighborhoodMedianIncome(b);
	  //System.out.println("number of agents: " + b.getNumOccupiedHousingUnits());
	  //System.out.println("number of vacant units: " + b.getNumVacantHousingUnits());
	 // System.out.println("RAIN#computeMarginalUtility -  last price was: " + b.getMedianRent() + "  block " + b.getBlockNum());
	 // System.out.println("RAIN#computeMarginalUtility - last pctblack was: " + b.getPctBlack());
	//	System.out.println("RAIN#computeMarginalUtility - last medianInc was: " + b.getMedianIncome());
	//	System.out.println("RAIN#computeMarginalUtility - total number of housing units is: " + b.getHousingUnitList().size());
		marginalUtility = -b.getMedianRent(tenure)/1000.0 - b.getPctBlkInNeighborhood();
		}
		return marginalUtility;
	}

	public double solveForPrice(double marginalUtil, Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		double p = 1.0;
	//	if(b.getHousingUnitList().size()!=0) {
		p = -1000*(marginalUtil + b.getPctBlkInNeighborhood());
//	} else {
	//	p = 0.0 ;
//	}
		return p; 
	}

	public String toString() { // Priyasree_DeadCode : Unreachable code_
	    return "RaceAndIncomeNeighborhood";
	}
}
