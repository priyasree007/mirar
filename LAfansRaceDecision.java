/*
 * Created on Jun 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mirar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LAfansRaceDecision extends AgentDecision {

	/**
	 * 
	 */
	public LAfansRaceDecision() {
		super();
	}


	  public HousingUnit select(ArrayList availableUnits, HousingUnit currUnit, Agent agent) {
        
        PrintWriter testSelect = null;
        try {
            testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", true));
            
        } catch (IOException ioe) { // Priyasree_Audit: Empty catch clause for exception ioe_Delete the empty catch clause. // Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
            
        }      
        
      //  this.possibleHousingUnitList.addAll(possibleHousingUnitList);
        
      //  ArrayList availableUnits = new ArrayList();
        //availableUnits = selectAffordableUnits(agent); // use only units with rents <= threshold
      //  System.out.println("number of affordable units is " + availableUnits.size());
      //  availableUnits.addAll(agent.getPossibleHousingUnitList());

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
        	/*
------------------------------------------------------------------------------
             |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
choice       |
    _Id_ij_1 |   15.92267   2.383076     6.68   0.000     11.25193    20.59341
          hp |   7.699987    5.19644     1.48   0.138    -2.484847    17.88482
  _Id_iXhp_1 |  -12.43244   5.687882    -2.19   0.029    -23.58048   -1.284393
         hp2 |  -8.500039   3.726776    -2.28   0.023    -15.80438   -1.195693
 _Id_iXhp2_1 |   7.095949   4.079227     1.74   0.082    -.8991895    15.09109
          bp |   11.01869   2.475144     4.45   0.000     6.167498    15.86989
  _Id_iXbp_1 |  -15.77903    2.70589    -5.83   0.000    -21.08247   -10.47558
         bp2 |  -17.46394   3.483382    -5.01   0.000    -24.29124   -10.63664
 _Id_iXbp2_1 |   15.11169    3.80812     3.97   0.000     7.647914    22.57547
        wap2 |  -.6285508   2.218633    -0.28   0.777    -4.976992     3.71989
_Id_iXwap2_1 |  -6.547089   2.473687    -2.65   0.008    -11.39543   -1.698751
  _IblaXbp_1 |   -7.11285   1.911911    -3.72   0.000    -10.86013   -3.365574
 _IblaXbp2_1 |   16.84475   3.678234     4.58   0.000     9.635541    24.05396
  _IlatXhp_1 |   -5.84296   1.697024    -3.44   0.001    -9.169066   -2.516854
 _IlatXhp2_1 |   8.031236   1.836282     4.37   0.000     4.432189    11.63028
 _IwaXwap2_1 |   3.601059   .3142589    11.46   0.000     2.985123    4.216995
  numRenters |   .1532357   .0725234     2.11   0.035     .0110924     .295379
   numOwners |   .1420399   .0559996     2.54   0.011     .0322827    .2517971
------------------------------------------------------------------------------ 
        	 */
        		double wap = this.getPctWhiteInNeighborhood(b)+this.getPctAsianInNeighborhood(b);
        		if(a.getRace()==a.WHITE | a.getRace()==a.ASIAN) {
               	utility = Math.exp(7.699987*this.getPctHispanicInNeighborhood(b)  -8.500039*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
               			+ 11.01869*this.getPctBlackInNeighborhood(b) + -17.46394*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
               			+ (-.6285508+3.601059)*wap*wap);
        		} else if(a.getRace()==a.BLACK) {
                utility = Math.exp(7.699987*this.getPctHispanicInNeighborhood(b)  -8.500039*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
               			+ (11.01869+-7.11285)*this.getPctBlackInNeighborhood(b) + (-17.46394+16.84475)*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
               			+ (-.6285508)*wap*wap);
        		} else if(a.getRace()==a.HISPANIC){
        		utility = Math.exp((7.699987-5.84296)*this.getPctHispanicInNeighborhood(b) + (-8.500039+8.031236)*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
                		+ 11.01869*this.getPctBlackInNeighborhood(b) + -17.46394*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
                		+ (-.6285508)*wap*wap);     			
        		}
       	} 
        return utility;
    }
    
	    public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) {

            Block b = currUnit.getBlock();  //  block;
            double utility = -12.0;
            double d = -12.0;
            if (units.size()<1651){
            	d = 15.92267 - Math.log(1651/units.size()); //Priyasree_Audit: Integer division in a floating-point expression_Cast one of the integers to a float. This will prevent round off error."
            } else {
            	d = 15.92267;
            }
       		double wap = this.getPctWhiteInNeighborhood(b)+this.getPctAsianInNeighborhood(b);
    		if(a.getRace()==a.WHITE | a.getRace()==a.ASIAN) {
           	utility = Math.exp( d  
           			+ 7.699987*this.getPctHispanicInNeighborhood(b)  -8.500039*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
           			+ -12.43244* this.getPctHispanicInNeighborhood(b) + 7.095949*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
           			+ 11.01869*this.getPctBlackInNeighborhood(b) + -17.46394*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
           			+ -15.77903*this.getPctBlackInNeighborhood(b) + 15.11169*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
           			+ (-.6285508+3.601059)*wap*wap
           			+ -6.547089*wap*wap);
    		} else if(a.getRace()==a.BLACK) {
            utility = Math.exp( d
            		+ 7.699987*this.getPctHispanicInNeighborhood(b)  -8.500039*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
            		+ -12.43244* this.getPctHispanicInNeighborhood(b) + 7.095949*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
           			+ (11.01869+-7.11285)*this.getPctBlackInNeighborhood(b) + (-17.46394+16.84475)*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
           			+ -15.77903*this.getPctBlackInNeighborhood(b) + 15.11169*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
	       			+ (-.6285508)*wap*wap
	       			+ -6.547089*wap*wap);
    		} else if(a.getRace()==a.HISPANIC){
    		utility = Math.exp( d
    				+ (7.699987-5.84296)*this.getPctHispanicInNeighborhood(b) + (-8.500039+8.031236)*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
    				+ -12.43244* this.getPctHispanicInNeighborhood(b) + 7.095949*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
	   				+ 11.01869*this.getPctBlackInNeighborhood(b) + -17.46394*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
	   				+ -15.77903*this.getPctBlackInNeighborhood(b) + 15.11169*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
	   				+ (-.6285508)*wap*wap
	   				+ -6.547089*wap*wap);     			
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
        return "LAfansRaceDecision";
    }

}
