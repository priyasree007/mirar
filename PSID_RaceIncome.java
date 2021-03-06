
package mirar;

import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author ebruch
 *
 * Note that this implements the PSID choice functions, which do not distinguish among renters and owners
 */
public class PSID_RaceIncome extends AgentDecision {
	
	 ArrayList availableUnits = new ArrayList(); // Priyasree_DeadCode : Unreachable code_

	/**
	 * 
	 */
	/*public PSID_RaceIncome() { // Priyasree_DeadCode : Unreachable code_
		super();
	}*/

	  public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) { // Priyasree_DeadCode : Unreachable code_
	        availableUnits.clear();
	        availableUnits.addAll(agent.getPossibleHousingUnitList());
	        if (currUnit == null) {
	            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
	        }
   
	        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
	        
	     // print utilities
	        if (MirarUtils.TEST_UTILITIES == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
	            System.out.println("Agent " + agent.getAgentNum() + " has " + utilities.size() + " utilities");
	            for (int i=0; i<utilities.size(); i++) {
	                System.out.print(utilities.get(i) + " ");
	            }
	            System.out.println();
	        }

	        double utilityCurrentUnit = computeUtilitityForOwnUnit(availableUnits, currUnit, agent); 
	        
	        if (MirarUtils.TEST_UTILITIES==true){ // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
	        	System.out.println("Agent's utility for own unit is: " + utilityCurrentUnit);
	        }
	        utilities.add(utilityCurrentUnit);
	        availableUnits.add(currUnit);
	      
	           for(int i=0; i<availableUnits.size(); i++){
	       	if(Double.isNaN(utilities.get(i)) | Double.isInfinite((utilities.get(i)))){
	        		System.out.println("utility " + i + " is " + utilities.get(i));
	        		System.out.println("is this agent's own unit? " + (utilities.get(i)==utilityCurrentUnit));
	        		//System.out.println("characteristics of unit with NaN are: ");
	        		System.out.println("median income is: " + ((HousingUnit)availableUnits.get(i)).block.getMedianIncome()); 
	        		System.out.println("race comp: " + ((HousingUnit)availableUnits.get(i)).block.getPctAsnInNeighborhood() 
	        					+ ", " + ((HousingUnit)availableUnits.get(i)).block.getPctBlkInNeighborhood() + ", " 
	        					+	((HousingUnit)availableUnits.get(i)).block.getPctHispInNeighborhood()
	        					+ ", " + ((HousingUnit)availableUnits.get(i)).block.getPctWhtInNeighborhood());
	        		System.out.println("agent's race is " + agent.getRace() + " and its income is: " + agent.getIncome());
	        }
	        }
	         
	   
	        int choice = MirarUtils.sampleFromRawDistribution(utilities);
	  if (choice == -1) {
	        System.out.println("#AGENT_DECISION: no choice made--returned NULL");	      	
	        /*
	         * Note: I'm not entirely sure why this happens. It was happening for a while with the Atlanta data, and once I adjusted
	         * the number of agents sampled and the number of potential vacant units, the problem seems to have disappeared. 
	         */
	       return null;
	        }
	        else {
	           return (HousingUnit) availableUnits.get(choice);
	        }
	    }
	    
	      public double computeUtility(Block b, Agent a, int tenure) { // Priyasree_DeadCode : Unreachable code_
	  
	      	double utility= -12.0;
	   		//ArrayList blocks = new ArrayList();
	        if (b == null) {
	        System.out.println("block is null");
	        }   
	        else {
	        	double medInc = b.getNeighborhoodMedianIncome()/1000; 
                double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
                double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
                double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
                double pctHispInNeighborhood = b.getPctHispInNeighborhood();
                double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
    	    	double ratPrice=(b.getMedianRent(tenure)*12.0)/a.getIncome();
             //   System.out.println("pct blk = " + pctBlackInNeighborhood + " and pctwhite = " + pctWhiteInNeighborhood); 

       		if(a.getRace()==Agent.ASIAN){
	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 +-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (-1.61123 + 14.98459)*pctAsnInNeighborhood + (-1.049479 + -17.68683)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood
	        			);	
	        	
	        	}	else if(a.getRace()==Agent.WHITE) {
	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 +-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + (-2.242075+2.009974)*pctWhtInNeighborhood*pctWhtInNeighborhood
	        			);	
	        	
	        	} else if(a.getRace()==Agent.BLACK) {
	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 +(-.4207741+5.146369)*pctBlkInNeighborhood + (-3.082691-.5826424)*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + (-2.143601+1.386053)*pctHispInNeighborhood + (-.3641764-.7654816)*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood     			
	        			);	
	        	
	        		} else if(a.getRace()==Agent.HISPANIC){
	    	        	utility = Math.exp( 
	    	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	   	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	   	        			 + -.0309662*medInc + .0000572*medInc*medInc 
	   		        		 +(.3592121+-.4207741)*pctBlkInNeighborhood + (-3.082691+.2229024)*pctBlkInNeighborhood*pctBlkInNeighborhood
	   		   	             + (-2.143601+8.121124)*pctHispInNeighborhood + (-.3641764-3.704257)*pctHispInNeighborhood*pctHispInNeighborhood
	   		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
	   		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood 
	    	        		);		
	        		}
	  }
	        if(Double.isNaN(utility)){ // NaN occurs when the neighborhood is empty (we've divided by zero)
	        	//System.out.println("Utility is NaN! --> total number of agents in neighborhood is:" + b.getTotalAgentsInNeighborhood() 
	        	//		+ " and total num housing units in neigh: " + b.getNeighborhoodTotalHousingUnits());
	        	utility = 0.5;
	        }
	     //   System.out.println("utility is " + utility); 
	        return utility;
	      }

	    public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) { // Priyasree_DeadCode : Unreachable code_
	      	Block b = currUnit.getBlock();  //  block;
        	double medInc = b.getNeighborhoodMedianIncome()/1000; 
            double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
	    	double ratPrice=(b.getMedianRent(a.getTenure())*12.0)/a.getIncome();
            double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
            double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
            double pctHispInNeighborhood = b.getPctHispInNeighborhood();
            double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
	      	double utility= -12.0;
/*
 * Note that sometimes Asians in 100% Asian neighborhoods end up with "infinite" utilities. This is a scaling issue, related to the fact
 * that my estimates for Asians aren't so great. So I recode these values to just a high number. 
 */
	       		if(a.getRace()==Agent.ASIAN){
	       			if (pctAsnInNeighborhood == 1.0){ // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
	       				utility = 10000; 
	       			} else {
		        	utility = Math.exp( 
		        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
		        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 +-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
			   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
			   	             + (-1.61123 + 14.98459)*pctAsnInNeighborhood + (-1.049479 + -17.68683)*pctAsnInNeighborhood*pctAsnInNeighborhood
			   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood
				   	         /*+  5.983235 */
		        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
	        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 + -3.002274*pctBlkInNeighborhood + 6.126364*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + -1.487786*pctHispInNeighborhood + 2.698027*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (5.805005 + -12.25118)*pctAsnInNeighborhood + (-10.83766+22.0611)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
		   	             );	
	       			}
		        	
		        	}	else if(a.getRace()==Agent.WHITE) {
		        	utility = Math.exp( 
		        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
		        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 +-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
			   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
			   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
			   	             + (-2.242075+2.009974)*pctWhtInNeighborhood*pctWhtInNeighborhood
				   	         /*+  5.983235 */
			        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
		        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 + -3.002274*pctBlkInNeighborhood + 6.126364*pctBlkInNeighborhood*pctBlkInNeighborhood
			   	             + -1.487786*pctHispInNeighborhood + 2.698027*pctHispInNeighborhood*pctHispInNeighborhood
			   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
			   	             + (-.9886524+.3502233)*pctWhtInNeighborhood*pctWhtInNeighborhood
		        			);	
		        	
		        	} else if(a.getRace()==Agent.BLACK) {
		        	utility = Math.exp( 
		        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
		        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 +(-.4207741+5.146369)*pctBlkInNeighborhood + (-3.082691-.5826424)*pctBlkInNeighborhood*pctBlkInNeighborhood
			   	             + (-2.143601+1.386053)*pctHispInNeighborhood + (-.3641764-.7654816)*pctHispInNeighborhood*pctHispInNeighborhood
			   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
			   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood         			
				   	         /*+  5.983235 */
			        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
		        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 + (-3.002274+-1.935416)*pctBlkInNeighborhood + (6.126364-1.940417)*pctBlkInNeighborhood*pctBlkInNeighborhood
			   	             + (3.03592-1.487786)*pctHispInNeighborhood + (-3.159731+2.698027)*pctHispInNeighborhood*pctHispInNeighborhood
			   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
			   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
		        			);	
		        	
		        		} else if(a.getRace()==Agent.HISPANIC){
		    	        	utility = Math.exp( 
		    	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
			   	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
			   	        			 + -.0309662*medInc + .0000572*medInc*medInc 
			   		        		 +(.3592121+-.4207741)*pctBlkInNeighborhood + (-3.082691+.2229024)*pctBlkInNeighborhood*pctBlkInNeighborhood
			   		   	             + (-2.143601+8.121124)*pctHispInNeighborhood + (-.3641764-3.704257)*pctHispInNeighborhood*pctHispInNeighborhood
			   		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
			   		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood      
						   	         /*+  5.983235 */
					        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
				        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
				        			 + -.0309662*medInc + .0000572*medInc*medInc 
					        		 + (-3.002274+4.366643)*pctBlkInNeighborhood + (6.126364+-2.746321)*pctBlkInNeighborhood*pctBlkInNeighborhood
					   	             + (.1024769-1.487786)*pctHispInNeighborhood + (2.698027+-1.666003)*pctHispInNeighborhood*pctHispInNeighborhood
					   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
					   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
		    	        			);		
		        		}
//System.out.println("utility for own unit is... " + utility);
	        	return utility; 
		    }

		    
		    public double computeMarginalUtility(Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		    	/**
		    	 * For now, not using market clearing rents with the LA FANS choice
		    	 * functions.. so these classes are undefined. 
		    	 */
		    	double t=0; 
		    	return t; 
		    }
		    
		    public double solveForPrice(double marginalUtil, Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		    	double t=0; 
		    	return t; 

		   }
		    public String toString() { // Priyasree_DeadCode : Unreachable code_
		        return "PSID_RaceIncome";
		    }

}
