
package mirar;

import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author ebruch
 *
 * Note that this implements the PSID choice functions, which do not distinguish among renters and owners
 */
public class PSID_IncomeOnly extends AgentDecision {

	/**
	 * 
	 */
	public PSID_IncomeOnly() {
		super();
	}

	  public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
	        ArrayList availableUnits = new ArrayList();
	        availableUnits.addAll(agent.getPossibleHousingUnitList());

	        if (currUnit == null) {
	            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
	        }
   
	        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
	 
	        double utilityCurrentUnit = computeUtilitityForOwnUnit(availableUnits, currUnit, agent); 

	        utilities.add(utilityCurrentUnit);
	        availableUnits.add(currUnit);
 /*    
	        for(int i=0; i<availableUnits.size(); i++){
	        	if(Double.isNaN(utilities.get(i)) | Double.isInfinite((utilities.get(i)))){
	        		System.out.println("utility " + i + " is " + utilities.get(i));
	        		System.out.println("is this agent's own unit? " + (utilities.get(i)==utilityCurrentUnit));
	        		System.out.println("characteristics of unit with NaN are: ");
	        		System.out.println("race comp: " + this.getPctAsianInNeighborhood(((HousingUnit)availableUnits.get(i)).block) 
	        					+ ", " + this.getPctBlackInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + ", " 
	        					+	this.getPctHispanicInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + ", "
	        					+	this.getPctWhiteInNeighborhood(((HousingUnit)availableUnits.get(i)).block)) ;
	        		System.out.println("housing price: " + ((HousingUnit)availableUnits.get(i)).block.getMedianRent() + " and median inc: " + ((HousingUnit)availableUnits.get(i)).block.getMedianIncome());
	        		System.out.println("number of agents in neigh is " + ((HousingUnit)availableUnits.get(i)).block.getTotalAgentsInNeighborhood());
	        	}
	        }
*/	      
	        int choice = MirarUtils.sampleFromRawDistribution(utilities);
	  if (choice == -1) {
	        	      	
	       return null;
	        }
	        else {
	           return (HousingUnit) availableUnits.get(choice);
	        }
	    }
      public double computeUtility(Block b, Agent a, int tenure) {
    	  
	      	double utility= -12.0;
	   		//ArrayList blocks = new ArrayList();
	        if (b == null) {
	        System.out.println("block is null");
	        }   
	        else {
	        	double medInc = b.getNeighborhoodMedianIncome()/1000; 
              double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
              double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
              double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
              double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
              double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
  	    	double ratPrice=(b.getMedianRent(tenure)*12.0)/a.getIncome();
           //   System.out.println("pct blk = " + pctBlackInNeighborhood + " and pctwhite = " + pctWhiteInNeighborhood); 

	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
	        			);	
	        	
	        if(Double.isNaN(utility)){ // NaN occurs when the neighborhood is empty (we've divided by zero)
	        	utility = 0.5;
	        }
	    }
	       // System.out.println("utility is " + utility); 
	        return utility;
	      }
	        
	    public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) {
	      	Block b = currUnit.getBlock();  //  block;
      	double medInc = b.getNeighborhoodMedianIncome()/1000; 
          double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
	    	double ratPrice=(b.getMedianRent(a.getTenure())*12.0)/a.getIncome();
	      	double utility= -12.0;

		        	utility = Math.exp( 
		        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
		        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
				   	         /*+  5.983235 */
		        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
	        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		   	             );			        	

	        	return utility; 
		    }
		    public double computeMarginalUtility(Block b, int tenure) {
		    	/**
		    	 * For now, not using market clearing rents with the LA FANS choice
		    	 * functions.. so these classes are undefined. 
		    	 */
		    	double t=0; 
		    	return t; 
		    }
		    
		    public double solveForPrice(double marginalUtil, Block b, int tenure) {
		    	double t=0; 
		    	return t; 

		   }
		    public String toString() {
		        return "PSID_IncomeOnly";
		    }

}
