/*
 * Created on Jul 6, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mirar;

import java.io.FileOutputStream; // Priyasree_Audit: Unnecessary import: import java.io.FileOutputStream;_Delete the import.
import java.io.IOException; // Priyasree_Audit: Unnecessary import: import java.io.IOException;_Delete the import.
import java.io.PrintWriter; // Priyasree_Audit: Unnecessary import: import java.io.PrintWriter;_Delete the import.
import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;


public class LafansRaceIncome extends AgentDecision {

	/**
	 * 
	 */
	public LafansRaceIncome() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see edu.ucla.stat.mirar.AgentDecision#select(java.util.ArrayList, edu.ucla.stat.mirar.HousingUnit, edu.ucla.stat.mirar.Agent)
	 */
	  public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
       // System.out.println("number of available units is " + possibleHousingUnitList.size());
  	
        /*PrintWriter testSelect = null;
        try {
            testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", true));
            
        } catch (IOException ioe) {
            
        }    
        
*/
        
      //  this.possibleHousingUnitList.addAll(possibleHousingUnitList);
        
        ArrayList availableUnits = new ArrayList();
        //availableUnits = selectAffordableUnits(agent); // use only units with rents <= threshold
        //System.out.println("number of units is " + availableUnits.size());
        availableUnits.addAll(agent.getPossibleHousingUnitList());

        if (currUnit == null) {
            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
        }
    // availableUnits.add(currUnit);
        
        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
 
        double utilityCurrentUnit = computeUtilitityForOwnUnit(availableUnits, currUnit, agent); 

        utilities.add(utilityCurrentUnit);
        availableUnits.add(currUnit);
      
        for(int i=0; i<availableUnits.size(); i++){
        	if(Double.isNaN(utilities.get(i)) | Double.isInfinite((utilities.get(i)))){
        		System.out.println("utility " + i + " is " + utilities.get(i));
        		System.out.println("is this agent's own unit? " + (utilities.get(i)==utilityCurrentUnit));//  // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
        		System.out.println("characteristics of unit with NaN are: ");
        		System.out.println("race comp: " + this.getPctAsianInNeighborhood(((HousingUnit)availableUnits.get(i)).block) 
        					+ ", " + this.getPctBlackInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + ", " 
        					+	this.getPctHispanicInNeighborhood(((HousingUnit)availableUnits.get(i)).block)) ;
        		System.out.println("number of agents in neigh is " + ((HousingUnit)availableUnits.get(i)).block.getTotalAgentsInNeighborhood());
        	}
        }
        int choice = MirarUtils.sampleFromRawDistribution(utilities);
    // System.out.println("this is agent's income " + agent.getIncome());
    //    for(int i=0; i<availableUnits.size(); i++){
    //    	System.out.println("this is the 20th percentile rent in alternative " + i + " " + ((HousingUnit)availableUnits.get(i)).block.getNeighborhoodPercentileRent(.2));
    //    }
    //    System.out.println("this is 20th centile in chosen neighborhood " + ((HousingUnit)availableUnits.get(choice)).block.getPercentileRent(.2));
     //  System.out.println("is agent staying in own unit? " + ((availableUnits.size()-1)==choice));
        if (choice == -1) {
        	      	
  //          testSelect.println("choice == " + choice);
  //          testSelect.println("LaFansRaceIncome#select:  Agent race is " + agent.getRace() + " and income is " + agent.getIncome());
           
    //        for(int i=0; i<utilities.size(); i++){
    //           testSelect.println("utility " + i + " from select function is: " + utilities.get(i));
    //           testSelect.println("prop. race comp is " + this.getPctWhiteInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + ", " + this.getPctAsianInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + ", " + this.getPctHispanicInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + "," + this.getPctBlackInNeighborhood(((HousingUnit)availableUnits.get(i)).block));
    //           testSelect.println("20th percentile rent is " + ((HousingUnit)availableUnits.get(i)).block.getNeighborhoodPercentileRent(.2));
    //        }
     //      testSelect.println("AgentDecision select availableUnits " + availableUnits.size() + "  utilities: " + utilities.size());
     //      testSelect.close();
            return null;
        }
        else {
           return (HousingUnit) availableUnits.get(choice);
        }
    }
    
      public double computeUtility(Block b, Agent a, int tenure) {
      	/*
------------------------------------------------------------------------------
             |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
choice       |
    _Id_ij_1 |   17.01163     2.4713     6.88   0.000     12.16797    21.85529
       ratio |   17.10057   4.684457     3.65   0.000     7.919202    26.28194
_Id_iXrat~_1 |  -9.311201   4.407698    -2.11   0.035    -17.95013   -.6722721
      ratio2 |  -32.99257   9.120302    -3.62   0.000    -50.86804   -15.11711
_Id_iXrat~a1 |   19.94552   9.702447     2.06   0.040      .929073    38.96196
          hp |    7.91995   5.262329     1.51   0.132    -2.394026    18.23393
  _Id_iXhp_1 |  -12.75552   5.760003    -2.21   0.027    -24.04492   -1.466122
         hp2 |   -8.48665   3.766624    -2.25   0.024     -15.8691   -1.104202
 _Id_iXhp2_1 |   7.281859   4.122845     1.77   0.077    -.7987681    15.36249
          bp |   11.18182   2.443958     4.58   0.000     6.391747    15.97189
  _Id_iXbp_1 |   -15.8647   2.671796    -5.94   0.000    -21.10133   -10.62808
         bp2 |  -17.22778   3.473916    -4.96   0.000    -24.03653   -10.41902
 _Id_iXbp2_1 |   14.89582   3.797772     3.92   0.000      7.45232    22.33931
        wap2 |  -.0605225   2.247274    -0.03   0.979    -4.465099    4.344054
_Id_iXwap2_1 |  -6.924077   2.505621    -2.76   0.006      -11.835   -2.013149
  _IblaXbp_1 |  -6.195673   1.897577    -3.27   0.001    -9.914856   -2.476491
 _IblaXbp2_1 |   16.03301   3.643391     4.40   0.000     8.892099    23.17393
  _IlatXhp_1 |  -5.313924   1.704189    -3.12   0.002    -8.654073   -1.973774
 _IlatXhp2_1 |   7.598506   1.843699     4.12   0.000     3.984921    11.21209
 _IwaXwap2_1 |   3.107685    .344174     9.03   0.000     2.433116    3.782253
  numRenters |   .1663566   .0733666     2.27   0.023     .0225606    .3101525
   numOwners |      .1053   .0605236     1.74   0.082     -.013324     .223924 
*/

  
      	double utility= -12.0;
   		double wap = this.getPctWhiteInNeighborhood(b) + this.getPctAsianInNeighborhood(b);
        ArrayList blocks = new ArrayList();
        if (b == null) {
        System.out.println("block is null");
        }   
        else {
        	double ratio = (b.getNeighborhoodPercentileRent(.2, tenure)*12.0)/a.getIncome();
        if(a.getRace()==a.WHITE | a.getRace()==a.ASIAN) {
               	utility = Math.exp( 
               			17.10057*ratio + -32.99257*ratio*ratio
               			+ 7.91995*this.getPctHispanicInNeighborhood(b)  -8.48665*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
               			+ 11.18182*this.getPctBlackInNeighborhood(b) + -17.22778*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
               			+ (-.0605225+ 3.107685)*wap*wap);
        		} else if(a.getRace()==a.BLACK) {
                utility = Math.exp( 
               			17.10057*ratio + -32.99257*ratio*ratio
               			+ 7.91995*this.getPctHispanicInNeighborhood(b)  -8.48665*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
               			+ (11.18182-6.195673)*this.getPctBlackInNeighborhood(b) + (-17.22778+16.03301)*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
               			+ (-.0605225)*wap*wap);
        		} else if(a.getRace()==a.HISPANIC){
        		utility =  Math.exp( 
               			17.10057*ratio + -32.99257*ratio*ratio
               			+ (7.91995-5.313924)*this.getPctHispanicInNeighborhood(b)+ (-8.48665+ 7.598506)*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
               			+ 11.18182*this.getPctBlackInNeighborhood(b) + -17.22778*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
               			+ (-.0605225)*wap*wap);     			
        		} else {
        			System.out.println("\t \t !!! no race found !!!");
        		}
           	} 
        return utility;
    }

/*
 * ------------------------------------------------------------------------------
             |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
choice       |
    _Id_ij_1 |   17.01163     2.4713     6.88   0.000     12.16797    21.85529
       ratio |   17.10057   4.684457     3.65   0.000     7.919202    26.28194
_Id_iXrat~_1 |  -9.311201   4.407698    -2.11   0.035    -17.95013   -.6722721
      ratio2 |  -32.99257   9.120302    -3.62   0.000    -50.86804   -15.11711
_Id_iXrat~a1 |   19.94552   9.702447     2.06   0.040      .929073    38.96196
          hp |    7.91995   5.262329     1.51   0.132    -2.394026    18.23393
  _Id_iXhp_1 |  -12.75552   5.760003    -2.21   0.027    -24.04492   -1.466122
         hp2 |   -8.48665   3.766624    -2.25   0.024     -15.8691   -1.104202
 _Id_iXhp2_1 |   7.281859   4.122845     1.77   0.077    -.7987681    15.36249
          bp |   11.18182   2.443958     4.58   0.000     6.391747    15.97189
  _Id_iXbp_1 |   -15.8647   2.671796    -5.94   0.000    -21.10133   -10.62808
         bp2 |  -17.22778   3.473916    -4.96   0.000    -24.03653   -10.41902
 _Id_iXbp2_1 |   14.89582   3.797772     3.92   0.000      7.45232    22.33931
        wap2 |  -.0605225   2.247274    -0.03   0.979    -4.465099    4.344054
_Id_iXwap2_1 |  -6.924077   2.505621    -2.76   0.006      -11.835   -2.013149
  _IblaXbp_1 |  -6.195673   1.897577    -3.27   0.001    -9.914856   -2.476491
 _IblaXbp2_1 |   16.03301   3.643391     4.40   0.000     8.892099    23.17393
  _IlatXhp_1 |  -5.313924   1.704189    -3.12   0.002    -8.654073   -1.973774
 _IlatXhp2_1 |   7.598506   1.843699     4.12   0.000     3.984921    11.21209
 _IwaXwap2_1 |   3.107685    .344174     9.03   0.000     2.433116    3.782253
  numRenters |   .1663566   .0733666     2.27   0.023     .0225606    .3101525
   numOwners |      .1053   .0605236     1.74   0.082     -.013324     .223924 
*/
 
      public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) {
	    /**
	     * note that I cap D to keep it from going to infinity
	     */       
      	
      	Block b = currUnit.getBlock();  //  block;
	            double utility = -12.0;
	       		double wap = this.getPctWhiteInNeighborhood(b) + this.getPctAsianInNeighborhood(b);
	            double d = 7.0; 
	       		/* if(units.size()<1651) {
	       		d = 17.01163 - Math.log(1651/units.size());
	            } else {
	             d = 17.01163;
	            }
	            */
	       	//	System.out.println("this is number of choices " + units.size() + " and this is exp(d) " + Math.exp(d));
	            double ratio = (b.getNeighborhoodPercentileRent(.2, a.getTenure())*12.0)/a.getIncome();
	         
	            
	            /* PrintWriter testUtil = null;
	            try {
	                testUtil = new PrintWriter(new FileOutputStream("testUtil.txt", true));
	                
	            } catch (IOException ioe) {
	                
	            } 
	            
	            */
	            
	            // testUtil.println("block ID is " + b.getSTFID());
	        	// testUtil.println("this is number of agents in neighborhood " + b.getTotalAgentsInNeighborhood());
	        	// testUtil.println("this is unit size " + units.size());
	        	// testUtil.println("this is d " + d);
	            
	            if(a.getRace()==a.WHITE | a.getRace()==a.ASIAN) {
	               	utility = Math.exp(d 
	               			+ 17.10057*ratio + -32.99257*ratio*ratio
	               			+ -9.311201*ratio + 19.94552*ratio*ratio
	               			+ 7.91995*this.getPctHispanicInNeighborhood(b)  -8.48665*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
	               			+  -12.75552*this.getPctHispanicInNeighborhood(b) + 7.281859*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
	               			+ 11.18182*this.getPctBlackInNeighborhood(b) + -17.22778*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
	               			+ -15.8647*this.getPctBlackInNeighborhood(b) + 14.89582*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
	               			+ (-.0605225+ 3.107685)*wap*wap
	               			+ -6.924077*wap);
	        		} else if(a.getRace()==a.BLACK) {
	                utility = Math.exp(d 
	               			+ 17.10057*ratio + -32.99257*ratio*ratio
	               			+ -9.311201*ratio + 19.94552*ratio*ratio
	               			+ 7.91995*this.getPctHispanicInNeighborhood(b)  -8.48665*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
	               			+  -12.75552*this.getPctHispanicInNeighborhood(b) + 7.281859*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
		               		+ (11.18182-6.195673)*this.getPctBlackInNeighborhood(b) + (-17.22778+16.03301)*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
		               		+ -15.8647*this.getPctBlackInNeighborhood(b) + 14.89582*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
		               		+ (-.0605225)*wap*wap
	               			+ -6.924077*wap); 
	        		} else if(a.getRace()==a.HISPANIC){
	        		utility =  Math.exp(d 
	               			+ 17.10057*ratio + -32.99257*ratio*ratio
	               			+ -9.311201*ratio + 19.94552*ratio*ratio
	               			+ (7.91995-5.313924)*this.getPctHispanicInNeighborhood(b)+ (-8.48665+ 7.598506)*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
	               			+  -12.75552*this.getPctHispanicInNeighborhood(b) + 7.281859*this.getPctHispanicInNeighborhood(b)*this.getPctHispanicInNeighborhood(b)
	               			+ 11.18182*this.getPctBlackInNeighborhood(b) + -17.22778*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
	               			+ -15.8647*this.getPctBlackInNeighborhood(b) + 14.89582*this.getPctBlackInNeighborhood(b)*this.getPctBlackInNeighborhood(b)
	               			+ (-.0605225)*wap*wap
	               			+ -6.924077*wap); 
	            //    testUtil.println("utility is " + utility);
	            //    testUtil.close();
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
        return "LafansRaceIncome";
    }

}

