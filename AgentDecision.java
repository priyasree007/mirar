package mirar;
import java.util.*;

//import codecLib.mpa.i;
import cern.colt.list.*;
import cern.jet.stat.*;

/**
 *  An abstract superclass for all the AgentDecision subclasses.
 *  
 *  This class also defines helper functions for use by the subclasses.
 *
 *
 * @author Robert Najlis
 * @version 1.0 
 */

public abstract class AgentDecision {


    /*public AgentDecision() { // Priyasree_DeadCode : Unreachable code_AgentDecision()

    }*/


    public abstract HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent); // Priyasree_DeadCode : Unreachable code_
    
    public abstract double computeUtility(Block b, Agent agent, int tenure); 
    
    public abstract double computeMarginalUtility(Block b, int tenure); 
    
    public abstract double solveForPrice(double marginalUtil, Block b, int tenure); 
    
	/**
	 * Returns utilities for a given list of housing units. 
	 * 
	 * @param housingUnits
	 *            list of housing units
	 * @return list of utilities associated with housing units
	 */
    public DoubleArrayList getUtilitiesByHousingUnit(ArrayList housingUnits, Agent agent) { 
        DoubleArrayList utilities = new DoubleArrayList();
        int numUnits = housingUnits.size();

        HousingUnit hu;
       
        for (int i=0; i<numUnits; i++) {
            hu = (HousingUnit)housingUnits.get(i);
                utilities.add(getUtility(hu, agent));
        }
        return utilities;
    }
	/**
	 * Returns utility for a given housing unit. 
	 * 
	 * @param housingUnit
	 *            housing unit
	 * @return utility associated with block that contains housing unit
	 */
    public double getUtility(HousingUnit hu, Agent agent) { 
       
            Block b = hu.getBlock();
            return b.getUtility(agent);    
    }
	
	
    /**
     * Returns all blocks where at least x percent of incomes for block are
     * less than or equal to the agent's income.
     * 
     * @param quantile 
     *            quantile of income distribution for block
     * @return list of blocks where the specified proportion of incomes are
     *         less than or equal to the agent's income.
     */
    /*public ArrayList findBlocksByQuantile(double quantile, Agent agent) { // Priyasree_DeadCode : Unreachable code_ArrayList findBlocksByQuantile(double quantile, Agent agent)

        ArrayList units = agent.getPossibleHousingUnitList();
        DoubleArrayList incList = new DoubleArrayList();
        ArrayList availableBlocks = new ArrayList();
        Block b = null;
        for (int i = 0; i < units.size(); i++) {
           
            b = (Block) ((HousingUnit) units.get(i)).getBlock(); //Priyasree_Audit: Unnecessary type cast to Block_Delete the unnecessary cast.
            incList.sort(); // sort ascending
            if (quantile <= Descriptive.quantileInverse(incList, agent.getIncome())) {
                availableBlocks.add(b);
            }
        }
        return availableBlocks;
    }*/
    
	/**
	 * Returns all units where at least x percent of incomes for block are
	 * less than or equal to the agent's income.
	 * 
	 * @param quantile 
	 *            quantile of income distribution for block
	 * @return list of units where the specified proportion of neighborhood incomes are
	 *         less than or equal to the agent's income.
	 */
    /*public ArrayList findUnitsByQuantile(double quantile, Agent agent, ArrayList possibleHousingUnitList) { // Priyasree_DeadCode : Unreachable code_
        ArrayList units = new ArrayList();
        units.addAll(possibleHousingUnitList);
        DoubleArrayList incList = new DoubleArrayList();
        ArrayList availableUnits = new ArrayList();
        Block b = null;
        HousingUnit hu = null;
        for (int i = 0; i < units.size(); i++) {
           
            hu = (HousingUnit) units.get(i);
            b = (Block) hu.getBlock(); //Priyasree_Audit: Unnecessary type cast to Block_Delete the unnecessary cast.
            incList = b.getIncomeDistribution();
            incList.sort(); // sort ascending
            if (quantile <= Descriptive.quantileInverse(incList, agent.getIncome())) {
                availableUnits.add(hu);
            }
        }
        return availableUnits;
    }*/
	/**
	 * Returns all units where the rent is less than or equal to the agent's 
	 * income.
	 * 
	 * @return list of housing units where the unit rent is less than or 
	 * equal to the agent's income
	 */
    /*public ArrayList selectAffordableUnits(Agent agent) { // Priyasree_DeadCode : Unreachable code_
        double income = agent.getIncome();
        double threshold = (income/12.0)*0.3;
        ArrayList housingUnits = new ArrayList();
        housingUnits.addAll(agent.getPossibleHousingUnitList());
       
        ArrayList units = new ArrayList();
        
        int numHousingUnits = housingUnits.size();
        for (int i=0; i<numHousingUnits; i++) {
            if ( ((HousingUnit)housingUnits.get(i)).getBlock().getMedianRent(agent.getTenure()) <= threshold) {
                units.add(housingUnits.get(i));
            }
        }
        return units;
    }*/
    
    
    /*public ArrayList findBlocksAvgIncomeAbove(double income, boolean inclusive) { // Priyasree_DeadCode : Unreachable code_
        ArrayList resultList = new ArrayList();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext()) {
            Block b = (Block) blockIter.next();
            if (inclusive == true) { //Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                if (income >= b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
            else { // inclusive == false
                if (income > b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
        }
        return resultList;
    }*/

    /*public ArrayList findBlocksAvgIncomeBetween(double incomeLow,
            boolean includeLow, double incomeHigh, boolean includeHigh, Agent agent) { // Priyasree_DeadCode : Unreachable code_
        ArrayList resultList = new ArrayList();
            Iterator blockIter = MirarUtils.BLOCKS.iterator();
            while (blockIter.hasNext() ) {
            Block b = (Block) blockIter.next();
            // if inclusive
           
            if (includeLow == true && includeHigh == true) { //Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                if (agent.getIncome() >= b.getMeanIncome()
                        && agent.getIncome() <= b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
            else if (includeLow == true && includeHigh == false) { // inclusive //Priyasree_Audit: Equality test with boolean literal_ Remove the comparison with true/false.
                // == false
                if (agent.getIncome() >= b.getMeanIncome()
                        && agent.getIncome() < b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
            else if (includeLow == false && includeHigh == true) { //Priyasree_Audit: Equality test with boolean literal_ Remove the comparison with false/true.
                if (agent.getIncome() > b.getMeanIncome()
                        && agent.getIncome() <= b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
            else if (includeLow == false && includeHigh == false) { //Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
                if (agent.getIncome() > b.getMeanIncome()
                        && agent.getIncome() < b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
        }
        return resultList;
    }
*/
   /* public ArrayList findBlocksAvgIncomeBelow(double income, boolean inclusive) { // Priyasree_DeadCode : Unreachable code_
        ArrayList blocks = getAllBlocks();
        ArrayList resultList = new ArrayList();
        for (int i = 0; i < blocks.size(); i++) {
            Block b = (Block) blocks.get(i);
            if (inclusive == true) { //Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                if (income >= b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
            else { // inclusive == false
                if (income > b.getMeanIncome()) {
                    resultList.add(b);
                }
            }
        }
        return resultList;
    }*/
    
    
	/**
	 * Returns all blocks where the given race group is the dominant group.
	 * 
	 * @param race
	 *            
	 * @return list of blocks where the specified race group is the dominant race.
	 */
    /*public ArrayList findBlocksByRace(int race) { // Priyasree_DeadCode : Unreachable code_
        ArrayList resultList = new ArrayList();
        Iterator iter = MirarUtils.BLOCKS.iterator();
           while (iter.hasNext()) { 
            Block b = (Block) iter.next();
            if (race == (int) b.getRacePct()) resultList.add(b);
        }
        return resultList;
    }*/

    public ArrayList getAllBlocks() { // Priyasree_DeadCode : Unreachable code_
        return MirarUtils.BLOCKS;
    }

    /*public ArrayList getBlockNeighbors(int blockNum) { // Priyasree_DeadCode : Unreachable code_
        return (ArrayList) CensusUnitHandler.getInstance().getBlock(blockNum).getNeighbors(); //Priyasree_Audit: Unnecessary type cast to ArrayList_Delete the unnecessary cast.
    }*/

    /*public int numNeighbors(int blockNum) { // Priyasree_DeadCode : Unreachable code_
        return ((ArrayList) CensusUnitHandler.getInstance().getBlock(blockNum).getNeighbors()) //Priyasree_Audit: Unnecessary type cast to ArrayList_Delete the unnecessary cast.
                .size();
    }*/
    
  
    
    /*public int getTotalAgentsInNeighborhoodOld(Block b) {// Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(b.getNeighbors());
        neighborhood.add(b);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumAgents();
            
        }
        
        return totalAgents;
    }*/

	// rriolo version of these *Old versions.
	// the *Old versions would run ok, but use more and more
	// memory , till at some step the y hit some limit
	// and speed drops fast, thenn later depending o n how
	// much mem was allocated via the java parameters,
	// at so me step it crashes/
	// the hope of these new versions is that they
	// wont u se so much ram allocated all these arraylists
	// thar arent needed so slow it down anyway.


    public int getTotalAgentsInNeighborhood(Block b) { 
        int totalAgents = b.getNumAgents();
		for ( Object tb : b.getNeighbors() ) {
			totalAgents += ((Block) tb).getNumAgents();
        }		
		//System.stderr.printf( "getTotalAgentsInNbhood...\n" );
        return totalAgents;
    }


    /*public double getPctRaceInNeighborhoodOld(int r, Block b) {// Priyasree_DeadCode : Unreachable code_double getPctRaceInNeighborhoodOld(int r, Block b)

        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(b.getNeighbors());
        neighborhood.add(b);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumRace(r);            
        }
        
        return ( (totalAgents*1.0)/ (this.getTotalAgentsInNeighborhood(b)*1.0)) ;
    }*/
    

    /*public double getPctRaceInNeighborhood(int r, Block b) { // Priyasree_DeadCode : Unreachable code_
        int totalRAgents = b.getNumAgents();
		for ( Object tb : b.getNeighbors() ) {
			totalRAgents += ((Block) tb).getNumRace(r);
        }		
        double totalAgentsInNeighborhood = getTotalAgentsInNeighborhood(b);
		
		if( totalAgentsInNeighborhood > 0 ) {
			return totalRAgents / totalAgentsInNeighborhood ;
		} 
	   	return 0.0;
    }*/
    
    /*public double getPctWhiteInNeighborhoodOld(Block b) { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(b.getNeighbors());
        neighborhood.add(b);
        int totalAgents = 0;
        int totalAgentsInNeighborhood = this.getTotalAgentsInNeighborhood(b);
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumWhite();
            
        }
       if(totalAgentsInNeighborhood>0){
        return ( (totalAgents*1.0)/ (totalAgentsInNeighborhood*1.0)) ;
    } else {
    	return 0;
    }
    }*/
//priyaComm: statistics collector objects, d entire thing can be minimized, Predicate class. Call the method once, 
   // collector obj: 2.9,2.10, 2.11
/*    public double getPctWhiteInNeighborhood(Block b) { //Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        int totalWAgents = b.getNumWhite();
		for ( Object tb : b.getNeighbors() ) {
			totalWAgents += ((Block) tb).getNumWhite();
        }		
        double totalAgentsInNeighborhood = getTotalAgentsInNeighborhood(b);
		
		if( totalAgentsInNeighborhood > 0 ) {
			return totalWAgents / totalAgentsInNeighborhood ;
		} 
	   	return 0.0;
    }*/

    /*public double getPctBlackInNeighborhoodOld(Block b) { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(b.getNeighbors());
        int totalAgentsInNeighborhood = this.getTotalAgentsInNeighborhood(b);
        neighborhood.add(b);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumBlack();
        }
        if(totalAgentsInNeighborhood>0){
            return ( (totalAgents*1.0)/ (totalAgentsInNeighborhood*1.0)) ;
        } else {
            return 0;
        }
    }  */ 
 
    /*public double getPctBlackInNeighborhood(Block b) {  //Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        int totalBAgents = b.getNumBlack();
		for ( Object tb : b.getNeighbors() ) {
			totalBAgents += ((Block) tb).getNumBlack();
        }		
        double totalAgentsInNeighborhood = getTotalAgentsInNeighborhood(b);
		
		if( totalAgentsInNeighborhood > 0 ) {
			return totalBAgents / totalAgentsInNeighborhood ;
		} 
	   	return 0.0;
    }*/
    
    /*public double getPctAsianInNeighborhoodOld(Block b) { // Priyasree_DeadCode : Unreachable code_double getPctAsianInNeighborhoodOld(Block b)
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(b.getNeighbors());
        int totalAgentsInNeighborhood = this.getTotalAgentsInNeighborhood(b);
        neighborhood.add(b);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumAsian();
        }
        if(totalAgentsInNeighborhood>0){
            return ( (totalAgents*1.0)/ (totalAgentsInNeighborhood*1.0)) ;
        } else {
            return 0;
        }
    }*/
    
    /*public double getPctAsianInNeighborhood(Block b) { //Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        int totalAAgents = b.getNumAsian();
		for ( Object tb : b.getNeighbors() ) {
			totalAAgents += ((Block) tb).getNumAsian();
        }		
        double totalAgentsInNeighborhood = getTotalAgentsInNeighborhood(b);
		
		if( totalAgentsInNeighborhood > 0 ) {
			return totalAAgents / totalAgentsInNeighborhood ;
		} 
	   	return 0.0;
    }*/
    
    /*public double getPctHispanicInNeighborhoodOld(Block b) { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(b.getNeighbors());
        int totalAgentsInNeighborhood = this.getTotalAgentsInNeighborhood(b);
        neighborhood.add(b);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumHispanic();
        }
        if(totalAgentsInNeighborhood>0){
            return ( (totalAgents*1.0)/ (totalAgentsInNeighborhood*1.0)) ;
        } else {
            return 0;
        }
    }*/

/*    public double getPctHispanicInNeighborhood(Block b) { //Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        int totalHAgents = b.getNumHispanic();
		for ( Object tb : b.getNeighbors() ) {
			totalHAgents += ((Block) tb).getNumHispanic();
        }		
        double totalAgentsInNeighborhood = getTotalAgentsInNeighborhood(b);
		
		if( totalAgentsInNeighborhood > 0 ) {
			return totalHAgents / totalAgentsInNeighborhood ;
		} 
	   	return 0.0;
    }*/
    
}
 // end AgentDecision