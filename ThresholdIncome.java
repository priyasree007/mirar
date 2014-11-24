
package mirar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

public class ThresholdIncome extends AgentDecision {

	/**
	 * 
	 */
	public ThresholdIncome() {
		super();
		// TODO Auto-generated constructor stub
	}
	   public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
        
        PrintWriter testSelect = null;
        try {
            testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", true));
            
        } catch (IOException ioe) { // Priyasree: Empty catch clause for exception ioe_Delete the empty catch clause. // Priyasree: Caught exception not logged_Use one of the logging methods to log the exception.
            
        }      
        
      //  this.possibleHousingUnitList.addAll(possibleHousingUnitList);
        
        ArrayList availableUnits = new ArrayList();
        //availableUnits = selectAffordableUnits(agent); // use only units with rents <= threshold
      //  System.out.println("number of affordable units is " + availableUnits.size());
        availableUnits.addAll(agent.getPossibleHousingUnitList());

        if (currUnit == null) {
            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
        }
    // availableUnits.add(currUnit);

        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
        
        double utilityCurrentUnit = computeUtilitityForOwnUnit(currUnit, agent); 

        utilities.add(utilityCurrentUnit);
        availableUnits.add(currUnit);
      
        int choice = MirarUtils.sampleFromRawDistribution(utilities);
    
        if (choice == -1) {
            
            return null;
        }
        else {
           return (HousingUnit) availableUnits.get(choice);
        }
    }
    
      public double computeUtility(Block b, Agent a, int tenure) {
        double utility=0.0;
        double agentInc = a.getIncome();
        double rent = b.getMedianRent(tenure);
        double ratio = (rent*12.0)/agentInc;  
        ArrayList blocks = new ArrayList();
        if (b == null) {
        System.out.println("thresholdrace neighbor computeUtility Block is null");
        }
        else {
	    //   if(a.getRace()==Agent.WHITE) {
            //   	if(b.getPctWhite()>=.20){
                	if(ratio<=0.3){
                		utility = Math.exp((1.0*ratio));            	
                	} else if (ratio>0.3) {
                		utility = Math.exp(-100.0);
                	} 
			//		       	} else if( b.getPctWhite()<.20){
               		//utility = Math.exp(-100.0);
			// 	}
			/*  } else if(a.getRace()==Agent.BLACK){
            	if(b.getPctBlack()>=.20){
                 	if(ratio<=0.3){
                        utility = Math.exp((-0.425*ratio) + (0.00000204)*b.getNeighborhoodMedianIncome());
                   	} else if(ratio>0.3){
                   		utility = Math.exp(-100.0);
                       	}
            	} else if(b.getPctBlack()<.20){
            		utility = Math.exp(-100.0);
            	}
             } else if(a.getRace()==Agent.HISPANIC) {
               	if(b.getPctHispanic()>=.20){
                 	if(ratio<=0.3){
                        utility = Math.exp((-0.425*ratio) + (0.00000204)*b.getNeighborhoodMedianIncome());
                   	} else if(ratio>0.3){
                   		utility = Math.exp(-100.0);
                       	}
            	} else if(b.getPctHispanic()<.20){
            		utility = Math.exp(-100.0);
            	}
             } else if(a.getRace()==Agent.ASIAN) {
               	if(b.getPctAsian()>=.20){
                 	if(ratio<=0.3){
                        utility = Math.exp((-0.425*ratio) + (0.00000204)*b.getNeighborhoodMedianIncome());
                   	} else if(ratio>0.3){
                   		utility = Math.exp(-100.0);
                       	}
            	} else if(b.getPctAsian()<.20){
            		utility = Math.exp(-100.0);
            	}
		} */       
             else {
                   System.out.println("!!!!!! Could not find race !!!!!! ");
                } 
        }
          

      return utility;
    }
    
    public double computeUtilitityForOwnUnit(HousingUnit currUnit, Agent a) {
            Block b = currUnit.getBlock();  //  block;
            double utility=0.0;
            double agentInc = a.getIncome();
            double rent = b.getMedianRent(a.getTenure());
            double ratio = (rent*12.0)/agentInc;      
	    //  if(a.getRace()==Agent.WHITE) {
	    //	if(b.getPctWhite()>=.20){
        	if(ratio<=0.3){
        		utility = Math.exp((1.0*ratio));            	
        	} else if (ratio>0.3) {
        		utility = Math.exp(-100.0);
        	} 
		/* 	} else if( b.getPctWhite()<.20){
           		utility = Math.exp(-100.0);
           	}
           } else if(a.getRace()==Agent.BLACK){
        	if(b.getPctBlack()>=.20){
             	if(ratio<=0.3){
                    utility = Math.exp((-0.425*ratio) + (0.00000204)*b.getNeighborhoodMedianIncome());
               	} else if(ratio>0.3){
               		utility = Math.exp(-100.0);
                   	}
        	} else if(b.getPctBlack()<.20){
        		utility = Math.exp(-100.0);
        	}
           } else if(a.getRace()==Agent.HISPANIC) {
           	if(b.getPctHispanic()>=.20){
             	if(ratio<=0.3){
                    utility = Math.exp((-0.425*ratio) + (0.00000204)*b.getNeighborhoodMedianIncome());
               	} else if(ratio>0.3){
               		utility = Math.exp(-100.0);
                   	}
        	} else if(b.getPctHispanic()<.20){
        		utility = Math.exp(-100.0);
        	}
         } else if(a.getRace()==Agent.ASIAN) {
           	if(b.getPctAsian()>=.20){
             	if(ratio<=0.3){
                    utility = Math.exp((-0.425*ratio) + (0.00000204)*b.getNeighborhoodMedianIncome());
               	} else if(ratio>0.3){
               		utility = Math.exp(-100.0);
                   	}
        	} else if(b.getPctAsian()<.20){
        		utility = Math.exp(-100.0);
        	}
         }  
		*/else {
                System.out.println("!!!!!! Could not find race !!!!!! ");
            }
       return utility; 
    }

        
    public double computeMarginalUtility(Block b, int tenure) {
        double marginalUtility = 0.0;
      //  System.out.println("total num agents is " + AgentHandler.getInstance().getNumAgents());
        if (b == null) {
        System.out.println("raceincome neighbor computeUtility Block is null");
        }
        else {
        	for(int i=0; i<MirarUtils.RENTER_INCOMES.length; i++){
        		double utility = 0.0; 
        		double income = MirarUtils.RENTER_INCOMES[i];
        		int numAgentsInCategory = MirarUtils.getNumAgentsOfIncomeType(income);
        	//	System.out.println("income is " + income);
        	//	System.out.println("number of agents in category" + i + " is " + numAgentsInCategory);
        		double ratio = (b.getMedianRent(tenure)*12.0)/income ; // yearly rent / income
        		if(ratio<=0.3) {
                    utility = ((-0.425)*((b.getMedianRent(tenure)*12.0)/income) + (0.00000204)*b.getNeighborhoodMedianIncome());
        		} else if(ratio>0.3){
        			utility = -100.0;
        		}
        		utility = utility*(numAgentsInCategory*1.0); // weight utility by number of agents in category
        		marginalUtility = marginalUtility + utility; // summing over income categories
        	}
        	marginalUtility =marginalUtility/(AgentHandler.getInstance().getNumAgents(tenure)*1.0); // divide by total number of agents
        //	System.out.println("marginalUtil is " + marginalUtility);
        }
        return marginalUtility;
    }

    public double solveForPrice(double marginalUtil, Block b, int tenure) {
    	
    	double N = AgentHandler.getInstance().getNumAgents(tenure)*1.0;	
    	double rent= -1.0;
    //	if(b.housingUnitList.size()!=0) { // if there are any housing units in the block 
        int numRents = MirarUtils.RENTER_INCOMES.length;
        double possibleRent = 0.0;
        double[] lowerRents = new double[(numRents+1)];
        double[] upperRents = new double[(numRents+1)];
        for(int i=0; i<=numRents;i++){
        	if(i==0){
        		lowerRents[i]= -10000000000000000000000000000.0;
        	} else {
        	lowerRents[i]=(MirarUtils.RENTER_INCOMES[i-1]/12.0)*0.3;
        	}
        }
        for(int i=0;i<numRents;i++){
        	if(i==(numRents)){
        		upperRents[i] = 10000000000000000000000000000.0;
        	} else {
        		upperRents[i] = (MirarUtils.RENTER_INCOMES[i]/12.0)*0.3;	
        	}
        }
        
        /**
         * we check each interval between the minimum and maximum rents
         */
  //  System.out.println("this is block med income " + b.getNeighborhoodMedianIncome());
  //  System.out.println("old rent is " + b.getMedianRent());
    	double d=0.0;
    	double n1=0.0;
    	double n2=0.0;
    	double income = 0.0;
    	    	
  for(int i=0; i<(numRents); i++){ // sum over possible rents
   	income = MirarUtils.RENTER_INCOMES[i];
    	double currentRent = (income/12.0)*0.3;
    	double sumN1=0.0;
    	double sumN2=0.0;
    	
    	for(int j=0;j<MirarUtils.RENTER_INCOMES.length;j++){ // sum over income cats in solve
    	    	d=0.0;    	    	
    	    	int numAgentsInCategory = MirarUtils.getNumAgentsOfIncomeType(MirarUtils.RENTER_INCOMES[j]);
  			if((currentRent*12.0)/MirarUtils.RENTER_INCOMES[j]<=0.3){
    				d = 0.0; // 1 * number of agents in income category
    			} else if((currentRent*12.0)/MirarUtils.RENTER_INCOMES[j]>0.3){
    				d = 1.0;; // Priyasree: Extra semicolon_Delete the extra semicolon.
    			}
    	
    		
    		n1=(-numAgentsInCategory*12.0*(1.0-d)*0.425)/(MirarUtils.RENTER_INCOMES[j]);
    		n2=(numAgentsInCategory*b.getNeighborhoodMedianIncome()*(1.0-d)*0.00000204);
    		
    		sumN1=sumN1+n1;
    		sumN2=sumN2+n2;
    		}
    		
    //System.out.println("sumN1 is " + sumN1 + " and sumN2 is " + sumN2);
  	//System.out.println("N is " + N + " and marginalUtil is " + marginalUtil);
    		possibleRent = (N*marginalUtil-sumN2)/sumN1; 
     	
    	//System.out.println("this is possible " + possibleRent);
    		
    		if(possibleRent>lowerRents[i] & possibleRent <=upperRents[i]) {
    			rent = possibleRent ; 
    		} 
    		if(i==(numRents-1) & possibleRent>=upperRents[i]){
    			rent=possibleRent; 
    		}
    	}
    	if(rent== -1.0){ // Priyasree: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
    		System.out.println("\t\t\t Could not find rent in interval!!!!");
    	} else {
System.out.println("this is rent: " + rent);
    	}
    		return rent;
    }

    public String toString() {
        return "ThresholdIncome";
    }
}
