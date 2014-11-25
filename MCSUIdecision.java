/*
 * Created on Aug 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mirar;

import java.io.PrintWriter; // Priyasree_Audit: Unnecessary import: import java.io.PrintWriter;_Delete the import.
import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

public class MCSUIdecision extends AgentDecision {

	/**
	 * 
	 */
	public MCSUIdecision() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	  public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
   
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
      
      double utilityCurrentUnit = computeUtilitityForOwnUnit(availableUnits, currUnit, agent); 

      utilities.add(utilityCurrentUnit);
      availableUnits.add(currUnit);
    
      int choice = MirarUtils.sampleFromRawDistribution(utilities);
  // System.out.println("this is agent's income " + agent.getIncome());
  //    for(int i=0; i<availableUnits.size(); i++){
  //    	System.out.println("this is the 20th percentile rent in alternative " + i + " " + ((HousingUnit)availableUnits.get(i)).block.getNeighborhoodPercentileRent(.2));
  //    }
  //    System.out.println("this is 20th centile in chosen neighborhood " + ((HousingUnit)availableUnits.get(choice)).block.getPercentileRent(.2));
  //    System.out.println("is agent staying in own unit? " + ((availableUnits.size()-1)==choice));
      if (choice == -1) {
          
          return null;
      }
      else {
         return (HousingUnit) availableUnits.get(choice);
      }
  }
  
	 public double computeUtility(Block b, Agent a, int tenure) {
    	double utility= -12.0;
      ArrayList blocks = new ArrayList();
      if (b == null) {
      System.out.println("block is null");
      }   
      else {
      	
        //{11.1112, .4341032}, // white prefs    mcsui 1990s
        //{22.3579, -20.26687}}; // black prefs
      	
      		if(a.getRace()==a.WHITE) {
             	utility = Math.exp(11.1112*this.getPctWhiteInNeighborhood(b) + 0.43*this.getPctWhiteInNeighborhood(b)*this.getPctWhiteInNeighborhood(b));
      		} else if(a.getRace()==a.BLACK) {
              utility = Math.exp(22.3579*this.getPctBlackInNeighborhood(b) - 20.267*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b));
      		} else if(a.getRace()==a.HISPANIC){
      		//System.out.println("Hispanic in the sample!!");     			
      		} else if(a.getRace()==a.ASIAN){
      		//	System.out.println("Asians in the sample!!!");
     	} 
      }
      return utility;
  }
  
	    public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) {

          Block b = currUnit.getBlock();  //  block;
          double utility = -12.0;
    		if(a.getRace()==a.WHITE) {
             	utility = Math.exp(11.1112*this.getPctWhiteInNeighborhood(b) + 0.43*this.getPctWhiteInNeighborhood(b)*this.getPctWhiteInNeighborhood(b));
      		} else if(a.getRace()==a.BLACK) {
              utility = Math.exp(22.3579*this.getPctBlackInNeighborhood(b) - 20.267*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b));
      		} else if(a.getRace()==a.HISPANIC){
      		//System.out.println("Hispanic in the sample!!");     			
      		} else if(a.getRace()==a.ASIAN){
      		//	System.out.println("Asians in the sample!!!");
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
       		if(ratio<.45) {
                 	utility = Math.exp(ratio*22.36772 - ratio*ratio*79.33567);
          		} else if( ratio>=.45) {
          			utility = Math.exp(-6); 
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
  				d = 1.0;; // Priyasree_Audit: Extra semicolon_Delete the extra semicolon.
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
  	if(rent== -1.0){ // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
  		System.out.println("\t\t\t Could not find rent in interval!!!!");
  	} else {
System.out.println("this is rent: " + rent);
  	}
  		return rent;
  }
  
  public String toString() {
      return "MCSUIdecision";
  }

}



