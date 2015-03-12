package mirar;

import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author ebruch
 *
 * Note that this implements the PSID choice functions, which do not distinguish among renters and owners
 */
public class PSID_Test extends AgentDecision {
	/**
	 * 
	 */
	public PSID_Test() {
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
	        					+	this.getPctHispanicInNeighborhood(((HousingUnit)availableUnits.get(i)).block)) ;
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
            //System.out.println("pct blk = " + pctBlackInNeighborhood + " and pctwhite = " + pctWhiteInNeighborhood); 

   		if(a.getRace()==Agent.ASIAN){
        	utility = Math.exp( 
        			5*pctAsianInNeighborhood
        			);	
        	
        	}	else if(a.getRace()==Agent.WHITE) {
        	utility = Math.exp( 
        			5*pctWhiteInNeighborhood
        			);	
        	
        	} else if(a.getRace()==Agent.BLACK) {
        	utility = Math.exp( 
        			5*pctBlackInNeighborhood    			
        			);	
        	
        		} else if(a.getRace()==Agent.HISPANIC){
    	        	utility = Math.exp(  
   		        		 5*pctHispanicInNeighborhood 
    	        		);		
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
        double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
        double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
        double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
        double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
      	double utility= -12.0;

       		if(a.getRace()==Agent.ASIAN){
	        	utility = Math.exp( 
		        		 +-.4207741*pctBlackInNeighborhood -3.082691*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + -2.143601*pctHispanicInNeighborhood -.3641764*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (-1.61123 + 14.98459)*pctAsianInNeighborhood + (-1.049479 + -17.68683)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood
			   	         /*+  5.983235 */
	        		 + -3.002274*pctBlackInNeighborhood + 6.126364*pctBlackInNeighborhood*pctBlackInNeighborhood
	   	             + -1.487786*pctHispanicInNeighborhood + 2.698027*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	   	             + (5.805005 + -12.25118)*pctAsianInNeighborhood + (-10.83766+22.0611)*pctAsianInNeighborhood*pctAsianInNeighborhood
	   	             + .3502233*pctWhiteInNeighborhood*pctWhiteInNeighborhood
	   	             );	
	        	
	        	}	else if(a.getRace()==Agent.WHITE) {
	        	utility = Math.exp( 
		        		 +-.4207741*pctBlackInNeighborhood -3.082691*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + -2.143601*pctHispanicInNeighborhood -.3641764*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + (-2.242075+2.009974)*pctWhiteInNeighborhood*pctWhiteInNeighborhood
			   	         /*+  5.983235 */
		        		 + -3.002274*pctBlackInNeighborhood + 6.126364*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + -1.487786*pctHispanicInNeighborhood + 2.698027*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (5.805005)*pctAsianInNeighborhood + (-10.83766)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + (-.9886524+.3502233)*pctWhiteInNeighborhood*pctWhiteInNeighborhood
	        			);	
	        	
	        	} else if(a.getRace()==Agent.BLACK) {
	        	utility = Math.exp( 
		        		 +(-.4207741+5.146369)*pctBlackInNeighborhood + (-3.082691-.5826424)*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + (-2.143601+1.386053)*pctHispanicInNeighborhood + (-.3641764-.7654816)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood         			
			   	         /*+  5.983235 */
		        		 + (-3.002274+-1.935416)*pctBlackInNeighborhood + (6.126364-1.940417)*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + (3.03592-1.487786)*pctHispanicInNeighborhood + (-3.159731+2.698027)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             + (5.805005)*pctAsianInNeighborhood + (-10.83766)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             + .3502233*pctWhiteInNeighborhood*pctWhiteInNeighborhood
	        			);	
	        	
	        		} else if(a.getRace()==Agent.HISPANIC){
	    	        	utility = Math.exp( 
		   		        		 +(.3592121+-.4207741)*pctBlackInNeighborhood + (-3.082691+.2229024)*pctBlackInNeighborhood*pctBlackInNeighborhood
		   		   	             + (-2.143601+8.121124)*pctHispanicInNeighborhood + (-.3641764-3.704257)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   		   	             + (-1.61123)*pctAsianInNeighborhood + (-1.049479)*pctAsianInNeighborhood*pctAsianInNeighborhood
		   		   	             + -2.242075*pctWhiteInNeighborhood*pctWhiteInNeighborhood      
					   	         /*+  5.983235 */
				        		 + (-3.002274+4.366643)*pctBlackInNeighborhood + (6.126364+-2.746321)*pctBlackInNeighborhood*pctBlackInNeighborhood
				   	             + (.1024769-1.487786)*pctHispanicInNeighborhood + (2.698027+-1.666003)*pctHispanicInNeighborhood*pctHispanicInNeighborhood
				   	             + (5.805005)*pctAsianInNeighborhood + (-10.83766)*pctAsianInNeighborhood*pctAsianInNeighborhood
				   	             + .3502233*pctWhiteInNeighborhood*pctWhiteInNeighborhood
	    	        			);		
	        		}
        	return utility; 
	    }

    		/*public double computeAvgPctInNeighbourhood(Block b, Agent a) {
    			double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
    	        double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
    	        double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
    	        double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
    	    	double TotPctAsianInNeighborhood = 0;
    	    	double TotPctWhiteInNeighborhood = 0;
    	    	double TotPctBlackInNeighborhood = 0;
    	    	double TotPctHispanicInNeighborhood = 0;
    	    	
    	    	if(a.getRace()==Agent.ASIAN){
    	    		return 
    	    	}
    		}*/
    				
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
		        return "PSID_Test";
		    }

}
