
package mirar;

import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author ebruch
 *
 * Note that this implements the PSID choice functions, which do not distinguish among renters and owners
 */
public class PSID_RaceIncome extends AgentDecision {
	
	 ArrayList availableUnits = new ArrayList();

	/**
	 * 
	 */
	public PSID_RaceIncome() {
		super();
	}

	  public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
	        availableUnits.clear();
	        availableUnits.addAll(agent.getPossibleHousingUnitList());
	        if (currUnit == null) {
	            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
	        }
   
	        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
	        
	     // print utilities
	        if (MirarUtils.TEST_UTILITIES == true) { // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
	            System.out.println("Agent " + agent.getAgentNum() + " has " + utilities.size() + " utilities");
	            for (int i=0; i<utilities.size(); i++) {
	                System.out.print(utilities.get(i) + " ");
	            }
	            System.out.println();
	        }

	        double utilityCurrentUnit = computeUtilitityForOwnUnit(availableUnits, currUnit, agent); 
	        
	        if (MirarUtils.TEST_UTILITIES==true){ // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
	        	System.out.println("Agent's utility for own unit is: " + utilityCurrentUnit);
	        }
	        utilities.add(utilityCurrentUnit);
	        availableUnits.add(currUnit);
	      /*
	           for(int i=0; i<availableUnits.size(); i++){
	       	if(Double.isNaN(utilities.get(i)) | Double.isInfinite((utilities.get(i)))){
	        		System.out.println("utility " + i + " is " + utilities.get(i));
	        		System.out.println("is this agent's own unit? " + (utilities.get(i)==utilityCurrentUnit));
	        		//System.out.println("characteristics of unit with NaN are: ");
	        		System.out.println("median income is: " + ((HousingUnit)availableUnits.get(i)).block.getMedianIncome()); 
	        		System.out.println("race comp: " + this.getPctAsianInNeighborhood(((HousingUnit)availableUnits.get(i)).block) 
	        					+ ", " + this.getPctBlackInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + ", " 
	        					+	this.getPctHispanicInNeighborhood(((HousingUnit)availableUnits.get(i)).block)
	        					+ ", " + this.getPctWhiteInNeighborhood(((HousingUnit)availableUnits.get(i)).block)) ;
	        		System.out.println("agent's race is " + agent.getRace() + " and its income is: " + agent.getIncome());
	        }
	        }
	        */ 
	   
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

       		if(a.getRace()==Agent.ASIAN){
	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 +-.4207741*pctBlackInNeighborhood -3.082691*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + -2.143601*pctHispanicInNeighborhood -.3641764*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (-1.61123 + 14.98459)*pctAsianInNeighborhood + (-1.049479 + -17.68683)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood
	        			);	
	        	
	        	}	else if(a.getRace()==Agent.WHITE) {
	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 +-.4207741*pctBlackInNeighborhood -3.082691*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + -2.143601*pctHispanicInNeighborhood -.3641764*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + (-2.242075+2.009974)*pctWhiteInNeighborhood*pctWhiteInNeighborhood
	        			);	
	        	
	        	} else if(a.getRace()==Agent.BLACK) {
	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 +(-.4207741+5.146369)*pctBlackInNeighborhood + (-3.082691-.5826424)*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + (-2.143601+1.386053)*pctHispanicInNeighborhood + (-.3641764-.7654816)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood     			
	        			);	
	        	
	        		} else if(a.getRace()==Agent.HISPANIC){
	    	        	utility = Math.exp( 
	    	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	   	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	   	        			 + -.0309662*medInc + .0000572*medInc*medInc 
	   		        		 +(.3592121+-.4207741)*pctBlackInNeighborhood + (-3.082691+.2229024)*pctBlackInNeighborhood*pctBlackInNeighborhood
	   		   	             + (-2.143601+8.121124)*pctHispanicInNeighborhood + (-.3641764-3.704257)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	   		   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
	   		   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood 
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

	    public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) {
	      	Block b = currUnit.getBlock();  //  block;
        	double medInc = b.getNeighborhoodMedianIncome()/1000; 
            double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
	    	double ratPrice=(b.getMedianRent(a.getTenure())*12.0)/a.getIncome();
            double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
            double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
            double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
            double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
	      	double utility= -12.0;
/*
 * Note that sometimes Asians in 100% Asian neighborhoods end up with "infinite" utilities. This is a scaling issue, related to the fact
 * that my estimates for Asians aren't so great. So I recode these values to just a high number. 
 */
	       		if(a.getRace()==Agent.ASIAN){
	       			if (pctAsianInNeighborhood == 1.0){ // Priyasree: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
	       				utility = 10000; 
	       			} else {
		        	utility = Math.exp( 
		        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
		        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 +-.4207741*pctBlackInNeighborhood -3.082691*pctBlackInNeighborhood*pctBlackInNeighborhood
			   	             + -2.143601*pctHispanicInNeighborhood -.3641764*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			   	             + (-1.61123 + 14.98459)*pctAsianInNeighborhood + (-1.049479 + -17.68683)*pctAsianInNeighborhood*pctAsianInNeighborhood
			   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood
				   	         /*+  5.983235 */
		        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
	        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
		        		 + -3.002274*pctBlackInNeighborhood + 6.126364*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + -1.487786*pctHispanicInNeighborhood + 2.698027*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (5.805005 + -12.25118)*pctAsianInNeighborhood + (-10.83766+22.0611)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + .3502233*pctWhiteInNeighborhood*pctWhiteInNeighborhood
		   	             );	
	       			}
		        	
		        	}	else if(a.getRace()==Agent.WHITE) {
		        	utility = Math.exp( 
		        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
		        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 +-.4207741*pctBlackInNeighborhood -3.082691*pctBlackInNeighborhood*pctBlackInNeighborhood
			   	             + -2.143601*pctHispanicInNeighborhood -.3641764*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
			   	             + (-2.242075+2.009974)*pctWhiteInNeighborhood*pctWhiteInNeighborhood
				   	         /*+  5.983235 */
			        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
		        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 + -3.002274*pctBlackInNeighborhood + 6.126364*pctBlackInNeighborhood*pctBlackInNeighborhood
			   	             + -1.487786*pctHispanicInNeighborhood + 2.698027*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			   	             + (5.805005)*pctAsianInNeighborhood + (-10.83766)*pctAsianInNeighborhood*pctAsianInNeighborhood
			   	             + (-.9886524+.3502233)*pctWhiteInNeighborhood*pctWhiteInNeighborhood
		        			);	
		        	
		        	} else if(a.getRace()==Agent.BLACK) {
		        	utility = Math.exp( 
		        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
		        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 +(-.4207741+5.146369)*pctBlackInNeighborhood + (-3.082691-.5826424)*pctBlackInNeighborhood*pctBlackInNeighborhood
			   	             + (-2.143601+1.386053)*pctHispanicInNeighborhood + (-.3641764-.7654816)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
			   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood         			
				   	         /*+  5.983235 */
			        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
		        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
		        			 + -.0309662*medInc + .0000572*medInc*medInc 
			        		 + (-3.002274+-1.935416)*pctBlackInNeighborhood + (6.126364-1.940417)*pctBlackInNeighborhood*pctBlackInNeighborhood
			   	             + (3.03592-1.487786)*pctHispanicInNeighborhood + (-3.159731+2.698027)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			   	             + (5.805005)*pctAsianInNeighborhood + (-10.83766)*pctAsianInNeighborhood*pctAsianInNeighborhood
			   	             + .3502233*pctWhiteInNeighborhood*pctWhiteInNeighborhood
		        			);	
		        	
		        		} else if(a.getRace()==Agent.HISPANIC){
		    	        	utility = Math.exp( 
		    	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
			   	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
			   	        			 + -.0309662*medInc + .0000572*medInc*medInc 
			   		        		 +(.3592121+-.4207741)*pctBlackInNeighborhood + (-3.082691+.2229024)*pctBlackInNeighborhood*pctBlackInNeighborhood
			   		   	             + (-2.143601+8.121124)*pctHispanicInNeighborhood + (-.3641764-3.704257)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			   		   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
			   		   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood      
						   	         /*+  5.983235 */
					        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
				        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
				        			 + -.0309662*medInc + .0000572*medInc*medInc 
					        		 + (-3.002274+4.366643)*pctBlackInNeighborhood + (6.126364+-2.746321)*pctBlackInNeighborhood*pctBlackInNeighborhood
					   	             + (.1024769-1.487786)*pctHispanicInNeighborhood + (2.698027+-1.666003)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
					   	             + (5.805005)*pctAsianInNeighborhood + (-10.83766)*pctAsianInNeighborhood*pctAsianInNeighborhood
					   	             + .3502233*pctWhiteInNeighborhood*pctWhiteInNeighborhood
		    	        			);		
		        		}
//System.out.println("utility for own unit is... " + utility);
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
		        return "PSID_RaceIncome";
		    }

}
