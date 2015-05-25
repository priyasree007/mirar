
package mirar;

import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * PSID_RaceOnly : Note that this implements the PSID choice functions, which do not distinguish among renters and owners
 */
public class DecisionRule extends AgentDecision {

	public DecisionRule() {
		super();
	}
	double utility= -12.0; 
	double utilityCurrentUnit;
	//PSID_RaceOnly, PSID_IncomeOnly, PSID_RaceIncome and PSID_Test

	  public HousingUnit selectDR(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
	        ArrayList availableUnits = new ArrayList();
	        availableUnits.addAll(agent.getPossibleHousingUnitList());

	        if (currUnit == null) {
	            System.out.println("Agetndecision - selectDR: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
	        }
   
	        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
	        
	     // print utilities
		 //Priyasree : TEST_UTILITIES is set to false in MirarUtils and not reset by anyone, so these will not be used.
		        if (MirarUtils.TEST_UTILITIES == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
		            System.out.println("Agent " + agent.getAgentNum() + " has " + utilities.size() + " utilities");
		            for (int i=0; i<utilities.size(); i++) {
		                System.out.print(utilities.get(i) + " ");
		            }
		            System.out.println();
		        }
	 
	       //double utilityCurrentUnit = computeUtilitityForOwnUnitDR(availableUnits, currUnit, agent); 
	       if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_RaceOnly")){ 
				  utilityCurrentUnit = computeUtilitityForOwnUnitRO(availableUnits, currUnit, agent); 
	          }
	          else if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_IncomeOnly")){ //Priyasree_Test 
	        	  utilityCurrentUnit = computeUtilitityForOwnUnitIO(availableUnits, currUnit, agent); 
	          }
	          else if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_RaceIncome")){ //Priyasree_Test 
	        	  utilityCurrentUnit = computeUtilitityForOwnUnitRI(availableUnits, currUnit, agent); 
	          }
	          else if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_Test")){ //Priyasree_Test
	        	  utilityCurrentUnit = computeUtilitityForOwnUnitT(availableUnits, currUnit, agent); 
	          }

	       if (MirarUtils.TEST_UTILITIES==true){ // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
	        	System.out.println("Agent's utility for own unit is: " + utilityCurrentUnit);
	        }
	       
	      utilities.add(utilityCurrentUnit);
	      availableUnits.add(currUnit);	  
	      
	      for(int i=0; i<availableUnits.size(); i++){
		       	if(Double.isNaN(utilities.get(i)) | Double.isInfinite((utilities.get(i)))){
		        		System.out.println("utility " + i + " is " + utilities.get(i));
		        		System.out.println("is this agent's own unit? " + (utilities.get(i)==utilityCurrentUnit));
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
	  
	  public double computeUtilityDR(Block b, Agent agent, int tenure) {
		  if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_RaceOnly")){
			  computeUtilityRO(b, agent, tenure);              
          }
          else if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_IncomeOnly")){ //Priyasree_Test
        	  computeUtilityIO(b, agent, tenure);              
          }
          else if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_RaceIncome")){ //Priyasree_Test
        	  computeUtilityRI(b, agent, tenure);              
          }
          else if(MirarUtils.AGENT_DECISION_STRING.equalsIgnoreCase("PSID_Test")){ //Priyasree_Test
        	  computeUtilityT(b, agent, tenure);              
          }
		  return utility;
		}
	  
	  //PSID_RaceOnly
      public double computeUtilityRO(Block b, Agent a, int tenure) {
  
      	//double utility= -12.0;
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
        return utility;
      }

      //PSID_IncomeOnly
      public double computeUtilityIO(Block b, Agent a, int tenure) {
    	  
	      	//double utility= -12.0;
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

	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
	        			);	
	        	
	        if(Double.isNaN(utility)){ // NaN occurs when the neighborhood is empty (we've divided by zero)
	        	utility = 0.5;
	        }
	    }
	        return utility;
	      }
      
      //PSID_RaceIncome
      public double computeUtilityRI(Block b, Agent a, int tenure) { // Priyasree_DeadCode : Unreachable code_
    	  
	      	//double utility= -12.0;
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
	        	utility = 0.5;
	        } 
	        return utility;
	      }
      
      //PSID_Test
      public double computeUtilityT(Block b, Agent a, int tenure) {
      	//System.out.println("COMpute UTility"); //PriyaUnderStand
        	//double utility= -12.0;
     		//ArrayList blocks = new ArrayList();
          if (b == null) {
          System.out.println("block is null");
          }   
          else {
          	double medInc = b.getNeighborhoodMedianIncome()/1000; 
              double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
          	
              //double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
              double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
              //double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
              double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
              //double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
              double pctHispInNeighborhood = b.getPctHispInNeighborhood();
              //double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
              double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
  	    	double ratPrice=(b.getMedianRent(tenure)*12.0)/a.getIncome();

     		if(a.getRace()==Agent.ASIAN){
          	utility = Math.exp( 
//          			5*pctAsianInNeighborhood                Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
          			5*pctAsnInNeighborhood
          			);	
          	
          	}	else if(a.getRace()==Agent.WHITE) {
          	utility = Math.exp( 
//          			5*pctWhiteInNeighborhood                Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
          			5*pctWhtInNeighborhood
          			);	
          	
          	} else if(a.getRace()==Agent.BLACK) {
          	utility = Math.exp( 
//          			5*pctBlackInNeighborhood    			Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
          			5*pctBlkInNeighborhood    			
          			);	
          	
          		} else if(a.getRace()==Agent.HISPANIC){
      	        	utility = Math.exp(  
//     		             5*pctHispanicInNeighborhood       Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
     		        		 5*pctHispInNeighborhood 
      	        		);		
          		}
    }
          return utility;
        }
      
    //PSID_RaceOnly
    public double computeUtilitityForOwnUnitRO(ArrayList units, HousingUnit currUnit, Agent a) {
      	Block b = currUnit.getBlock();  //  block;
    	double medInc = b.getNeighborhoodMedianIncome()/1000; 
        double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
    	double ratPrice=(b.getMedianRent(a.getTenure())*12.0)/a.getIncome();
        double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
        double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
        double pctHispInNeighborhood = b.getPctHispInNeighborhood();
        double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
      	//double utility= -12.0; //Priyasree_declared at beginning

       		if(a.getRace()==Agent.ASIAN){
	        	utility = Math.exp( 
		        		 +-.4207741*pctBlkInNeighborhood -3.082691*pctBlkInNeighborhood*pctBlkInNeighborhood
		   	             + -2.143601*pctHispInNeighborhood -.3641764*pctHispInNeighborhood*pctHispInNeighborhood
		   	             + (-1.61123 + 14.98459)*pctAsnInNeighborhood + (-1.049479 + -17.68683)*pctAsnInNeighborhood*pctAsnInNeighborhood
		   	             + -2.242075*pctWhtInNeighborhood*pctWhtInNeighborhood
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
				        		 + (-3.002274+4.366643)*pctBlkInNeighborhood + (6.126364+-2.746321)*pctBlkInNeighborhood*pctBlkInNeighborhood
				   	             + (.1024769-1.487786)*pctHispInNeighborhood + (2.698027+-1.666003)*pctHispInNeighborhood*pctHispInNeighborhood
				   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
				   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
	    	        			);		
	        		}

        	return utility; 
	    }

    //PSID_IncomeOnly
    public double computeUtilitityForOwnUnitIO(ArrayList units, HousingUnit currUnit, Agent a) {
      	Block b = currUnit.getBlock();  //  block;
  	double medInc = b.getNeighborhoodMedianIncome()/1000; 
      double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
    	double ratPrice=(b.getMedianRent(a.getTenure())*12.0)/a.getIncome();
      	//double utility= -12.0; //Priyasree_declared at beginning

	        	utility = Math.exp( 
	        			-1.215498*ratIncome + .0020448*ratIncome*ratIncome +
	        			 2.07456*ratPrice + -4.65363*ratPrice*ratPrice 
	        			 + -.0309662*medInc + .0000572*medInc*medInc 
	        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
        			 + -.0309662*medInc + .0000572*medInc*medInc 
	   	             );			        	

        	return utility; 
	    }
    
	//PSID_RaceIncome
    public double computeUtilitityForOwnUnitRI(ArrayList units, HousingUnit currUnit, Agent a) { // Priyasree_DeadCode : Unreachable code_
      	Block b = currUnit.getBlock();  //  block;
    	double medInc = b.getNeighborhoodMedianIncome()/1000; 
        double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
    	double ratPrice=(b.getMedianRent(a.getTenure())*12.0)/a.getIncome();
        double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
        double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
        double pctHispInNeighborhood = b.getPctHispInNeighborhood();
        double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
      	//double utility= -12.0; //Priyasree_declared at beginning
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
				        		 + .4788978*ratIncome + -.0161221*ratIncome*ratIncome 
			        			 + -3.857658*ratPrice + 5.175906*ratPrice*ratPrice 
			        			 + -.0309662*medInc + .0000572*medInc*medInc 
				        		 + (-3.002274+4.366643)*pctBlkInNeighborhood + (6.126364+-2.746321)*pctBlkInNeighborhood*pctBlkInNeighborhood
				   	             + (.1024769-1.487786)*pctHispInNeighborhood + (2.698027+-1.666003)*pctHispInNeighborhood*pctHispInNeighborhood
				   	             + (5.805005)*pctAsnInNeighborhood + (-10.83766)*pctAsnInNeighborhood*pctAsnInNeighborhood
				   	             + .3502233*pctWhtInNeighborhood*pctWhtInNeighborhood
	    	        			);		
	        		}
        	return utility; 
	    }
    
    //PSID_Test
    public double computeUtilitityForOwnUnitT(ArrayList units, HousingUnit currUnit, Agent a) {
    	//System.out.println("COMpute UTility For Own Unit"); //PriyaUnderStand
      	Block b = currUnit.getBlock();  //  block;
    	double medInc = b.getNeighborhoodMedianIncome()/1000; 
        double ratIncome =  a.getIncome()/b.getNeighborhoodMedianIncome();
    	double ratPrice=(b.getMedianRent(a.getTenure())*12.0)/a.getIncome();
    	
        //double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        double pctBlkInNeighborhood = b.getPctBlkInNeighborhood();
        //double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        double pctWhtInNeighborhood = b.getPctWhtInNeighborhood();
        //double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        double pctHispInNeighborhood = b.getPctHispInNeighborhood();
        //double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b); Priyasree_remove pctBlackInNeighborhood calculation in AgentDecision
        double pctAsnInNeighborhood = b.getPctAsnInNeighborhood();
        
      	//double utility= -12.0; //Priyasree_declared at beginning

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
		        return "Decision_Ruley";
		    }
}

