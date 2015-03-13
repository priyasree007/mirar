
package mirar;

import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author ebruch
 *
 * Note that this implements the PSID choice functions, which do not distinguish among renters and owners
 */
public class PSID_RaceOnly extends AgentDecision {

	/**
	 * 
	 */
	public PSID_RaceOnly() {
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
            double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
            double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
            double pctHispInNeighborhood = b.getPctHispInNeighborhood();
            double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
	    	double ratPrice=(b.getMedianRent(tenure)*12.0)/a.getIncome();
         //   System.out.println("pct blk = " + pctBlackInNeighborhood + " and pctwhite = " + pctWhiteInNeighborhood); 

   		if(a.getRace()==Agent.ASIAN){
        	utility = Math.exp( 
        			-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
	   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
	   	             + (-1.61123 + 14.98459)*pctAsnInNeighborhood + (-1.049479 + -17.68683)*pctAsnInNeighborhood*pctAsnInNeighborhood
	   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood
        			);	
        	
        	}	else if(a.getRace()==Agent.WHITE) {
        	utility = Math.exp( 
        			-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
	   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
	   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
	   	             + (-2.242075+2.009974)*pctWhtInNeighborhood*pctWhtInNeighborhood
        			);	
        	
        	} else if(a.getRace()==Agent.BLACK) {
        	utility = Math.exp( 
        			(-.4207741+5.146369)*pctBlkInNeighborhood + (-3.082691-.5826424)*pctBlkInNeighborhood*pctBlkInNeighborhood
	   	             + (-2.143601+1.386053)*pctHispInNeighborhood + (-.3641764-.7654816)*pctHispInNeighborhood*pctHispInNeighborhood
	   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
	   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood     			
        			);	
        	
        		} else if(a.getRace()==Agent.HISPANIC){
    	        	utility = Math.exp(  
   		        		 (.3592121+-.4207741)*pctBlkInNeighborhood + (-3.082691+.2229024)*pctBlkInNeighborhood*pctBlkInNeighborhood
   		   	             + (-2.143601+8.121124)*pctHispInNeighborhood + (-.3641764-3.704257)*pctHispInNeighborhood*pctHispInNeighborhood
   		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
   		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood 
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
        double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
        double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
        double pctHispInNeighborhood = b.getPctHispInNeighborhood();
        double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
      	double utility= -12.0;

       		if(a.getRace()==Agent.ASIAN){
	        	utility = Math.exp( 
		        		 +-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (-1.61123 + 14.98459)*pctAsnInNeighborhood + (-1.049479 + -17.68683)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood
			   	         /*+  5.983235 */
	        		 + -3.002274*pctBlkInNeighborhood + 6.126364*pctBlkInNeighborhood*pctBlkInNeighborhood
	   	             + -1.487786*pctHispInNeighborhood + 2.698027*pctHispInNeighborhood*pctHispInNeighborhood
	   	             + (5.805005 + -12.25118)*pctAsnInNeighborhood + (-10.83766+22.0611)*pctAsnInNeighborhood*pctAsnInNeighborhood
	   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
	   	             );	
	        	
	        	}	else if(a.getRace()==Agent.WHITE) {
	        	utility = Math.exp( 
		        		 +-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + (-2.242075+2.009974)*pctWhtInNeighborhood*pctWhtInNeighborhood
			   	         /*+  5.983235 */
		        		 + -3.002274*pctBlkInNeighborhood + 6.126364*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + -1.487786*pctHispInNeighborhood + 2.698027*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + (-.9886524+.3502233)*pctWhtInNeighborhood*pctWhtInNeighborhood
	        			);	
	        	
	        	} else if(a.getRace()==Agent.BLACK) {
	        	utility = Math.exp( 
		        		 +(-.4207741+5.146369)*pctBlkInNeighborhood + (-3.082691-.5826424)*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + (-2.143601+1.386053)*pctHispInNeighborhood + (-.3641764-.7654816)*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood         			
			   	         /*+  5.983235 */
		        		 + (-3.002274+-1.935416)*pctBlkInNeighborhood + (6.126364-1.940417)*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + (3.03592-1.487786)*pctHispInNeighborhood + (-3.159731+2.698027)*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
	        			);	
	        	
	        		} else if(a.getRace()==Agent.HISPANIC){
	    	        	utility = Math.exp( 
		   		        		 +(.3592121+-.4207741)*pctBlkInNeighborhood + (-3.082691+.2229024)*pctBlkInNeighborhood*pctBlkInNeighborhood
		   		   	             + (-2.143601+8.121124)*pctHispInNeighborhood + (-.3641764-3.704257)*pctHispInNeighborhood*pctHispInNeighborhood
		   		   	             + (-1.61123)*pctAsnInNeighborhood + (-1.049479)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood      
					   	         /*+  5.983235 */
				        		 + (-3.002274+4.366643)*pctBlkInNeighborhood + (6.126364+-2.746321)*pctBlkInNeighborhood*pctBlkInNeighborhood
				   	             + (.1024769-1.487786)*pctHispInNeighborhood + (2.698027+-1.666003)*pctHispInNeighborhood*pctHispInNeighborhood
				   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
				   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
	    	        			);		
	        		}

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
		        return "PSID_RaceOnly";
		    }

}
