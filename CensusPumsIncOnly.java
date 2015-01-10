/*
 * Created on Nov 10, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mirar;

import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/*public class CensusPumsIncOnly extends AgentDecision{

	*//**
	 * 
	 *//*
	public CensusPumsIncOnly() { // Priyasree_DeadCode : Unreachable code_
		super();
		// TODO Auto-generated constructor stub
	}


	
	 * Created on Nov 10, 2005
	 *
	 * To change the template for this generated file go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 

	public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) { // Priyasree_DeadCode : Unreachable code_
		   
			        ArrayList availableUnits = new ArrayList();
			        availableUnits.addAll(agent.getPossibleHousingUnitList());

			        if (currUnit == null) {
			            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
			        }
		   
			        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
			 
			        double utilityCurrentUnit = computeUtilitityForOwnUnit(availableUnits, currUnit, agent); 

			        utilities.add(utilityCurrentUnit);
			        availableUnits.add(currUnit);
			      
			        for(int i=0; i<availableUnits.size(); i++){
			        	if(Double.isNaN(utilities.get(i)) | Double.isInfinite((utilities.get(i)))){
			        		System.out.println("utility " + i + " is " + utilities.get(i));
			        		System.out.println("is this agent's own unit? " + (utilities.get(i)==utilityCurrentUnit)); // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
			        		System.out.println("characteristics of unit with NaN are: ");
			        		System.out.println("race comp: " + this.getPctAsianInNeighborhood(((HousingUnit)availableUnits.get(i)).block) 
			        					+ ", " + this.getPctBlackInNeighborhood(((HousingUnit)availableUnits.get(i)).block) + ", " 
			        					+	this.getPctHispanicInNeighborhood(((HousingUnit)availableUnits.get(i)).block)) ;
			        		System.out.println("number of agents in neigh is " + ((HousingUnit)availableUnits.get(i)).block.getTotalAgentsInNeighborhood());
			        	}
			        }
			        int choice = MirarUtils.sampleFromRawDistribution(utilities);
			  if (choice == -1) {
			        	      	
			       return null;
			        }
			        else {
			           return (HousingUnit) availableUnits.get(choice);
			        }
			    }
			    
	public double computeUtility(Block b, Agent a, int tenure) { // Priyasree_DeadCode : Unreachable code_
	    
	    double utility= -12.0;
	    double ratio=(b.getMedianRent(tenure)*12.0)/a.getIncome();
	  //  ArrayList blocks = new ArrayList();
	    double neighborhoodMedianIncome = b.getNeighborhoodMedianIncome();
	    if (b == null) {
	        System.out.println("block is null");
	    }   
	    else {
	        if(tenure==Agent.RENTER){
	             ASIAN
	             ratio |   17.89578   .3479101    51.44   0.000     17.21389    18.57767
	             ratio2 |  -23.77963   .4606577   -51.62   0.000     -24.6825   -22.87676
	             medInc |   .0136211   .0020459     6.66   0.000     .0096112    .0176309
	             medInc2 |  -.0001202   .0000189    -6.37   0.000    -.0001573   -.0000832
	             offset |   (offset)
	             
	            if(a.getRace()==Agent.ASIAN){
	                utility = Math.exp( 
	                        17.89578*ratio -23.77963*ratio*ratio
	                        + .0136211*(neighborhoodMedianIncome/1000) -.0001202*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	            }	else if(a.getRace()==Agent.WHITE) {
	                
	                 WHITE
	                 ratio |   16.87682   .1798653    93.83   0.000     16.52429    17.22935
	                 ratio2 |  -21.74727   .2303531   -94.41   0.000    -22.19875   -21.29579
	                 medInc |   .0836554   .0010716    78.06   0.000      .081555    .0857557
	                 medInc2 |  -.0004368   8.66e-06   -50.46   0.000    -.0004537   -.0004198
	                 offset |   (offset)
	                  
	                utility = Math.exp( 
	                        16.87682*ratio -21.74727*ratio*ratio
	                        + .0836554*(neighborhoodMedianIncome/1000) -.0004368*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	            } else if(a.getRace()==Agent.BLACK) {
	                 BLACK
	                 ratio |   19.28084   .3455543    55.80   0.000     18.60357    19.95812
	                 ratio2 |  -27.28715    .438987   -62.16   0.000    -28.14755   -26.42675
	                 medInc |  -.0288048   .0036335    -7.93   0.000    -.0359262   -.0216833
	                 medInc2 |   -.000152   .0000458    -3.32   0.001    -.0002417   -.0000622
	                    
	                utility = Math.exp( 
	                        19.28084*ratio -27.28715*ratio*ratio
	                        + -.0288048*(neighborhoodMedianIncome/1000)-.000152*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	                
	            } else if(a.getRace()==Agent.HISPANIC){
	                 	 
	                 HISPANIC
	                 ratio |   9.736794   .1416736    68.73   0.000     9.459119    10.01447
	                 ratio2 |  -16.86509    .182294   -92.52   0.000    -17.22238    -16.5078
	                 medInc |   .0104645   .0016513     6.34   0.000      .007228    .0137009
	                 medInc2 |    -.00046   .0000209   -22.00   0.000     -.000501    -.000419
	                 offset |   (offset)
	                   
	                utility = Math.exp( 
	                        9.736794*ratio -16.86509*ratio*ratio
	                        + .0104645*(neighborhoodMedianIncome/1000)-.00046*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	            } 
	        } else if(tenure==Agent.OWNER){
	            if(a.getRace()==Agent.ASIAN){
	                 ASIAN
	                 ratio |   20.27438   .4361076    46.49   0.000     19.41962    21.12913
	                 ratio2 |  -28.64516   .6901788   -41.50   0.000    -29.99788   -27.29243
	                 medInc |   .0272178   .0025713    10.59   0.000     .0221781    .0322574
	                 medInc2 |  -.0002098   .0000179   -11.74   0.000    -.0002448   -.0001747
	                 offset |   (offset)
	                 
	                utility = Math.exp( 
	                        20.27438*ratio -28.64516*ratio*ratio
	                        + .0272178*(neighborhoodMedianIncome/1000)-.0002098*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	            }	else if(a.getRace()==Agent.WHITE) {
	                 WHITE      
	                 ratio |   20.32956   .1953568   104.06   0.000     19.94667    20.71245
	                 ratio2 |  -29.57951   .3150745   -93.88   0.000    -30.19705   -28.96198
	                 medInc |   .0391353   .0008888    44.03   0.000     .0373933    .0408773
	                 medInc2 |  -.0001786   5.14e-06   -34.77   0.000    -.0001887   -.0001685
	                 offset |   (offset)
	                 
	                utility = Math.exp( 
	                        20.32956*ratio -29.57951*ratio*ratio
	                        + .0391353*(neighborhoodMedianIncome/1000)-.0001786*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	            } else if(a.getRace()==Agent.BLACK) {
	                 
	                 /*
	                  BLACK     
	                  ratio |   16.79473   .4564699    36.79   0.000     15.90006    17.68939
	                  ratio2 |  -22.26258   .6567087   -33.90   0.000    -23.54971   -20.97546
	                  medInc |  -.0854712   .0031817   -26.86   0.000    -.0917072   -.0792352
	                  medInc2 |   .0002675   .0000266    10.05   0.000     .0002153    .0003196
	                  offset |   (offset) 
	                   
	                utility = Math.exp( 
	                        16.79473*ratio -22.26258*ratio*ratio
	                        +-.0854712*(neighborhoodMedianIncome/1000)+.0002675*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	            } else if(a.getRace()==Agent.HISPANIC){
	                 
	                 HISP
	                 ratio |   18.72162    .275803    67.88   0.000     18.18106    19.26218
	                 ratio2 |  -26.91835   .4258184   -63.22   0.000    -27.75294   -26.08376
	                 medInc |  -.0172796   .0029054    -5.95   0.000    -.0229742    -.011585
	                 medInc2 |  -.0002668   .0000286    -9.32   0.000    -.0003229   -.0002107
	                 offset |   (offset)  
	                  
	                utility = Math.exp( 
	                        18.72162*ratio -26.91835*ratio*ratio
	                        +-.0172796*(neighborhoodMedianIncome/1000)-.0002668*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
	                
	            } 
	        } else {
	            System.out.println("no tenure");
	        }
	    }
	    return utility;
	}

			      public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) { // Priyasree_DeadCode : Unreachable code_
			          *//**
			           * note that I cap D to keep it from going to infinity
			           *//*       
			          int tenure = a.getTenure();
			          Block b = currUnit.getBlock();  //  block;
			          double neighborhoodMedianIncome = b.getNeighborhoodMedianIncome();
			          double utility= -12.0;
			          double ratio=(b.getMedianRent(tenure)*12.0)/a.getIncome();
			          if(tenure==Agent.RENTER){
			               ASIAN
			               ratio |   17.89578   .3479101    51.44   0.000     17.21389    18.57767
			               ratio2 |  -23.77963   .4606577   -51.62   0.000     -24.6825   -22.87676
			               medInc |   .0136211   .0020459     6.66   0.000     .0096112    .0176309
			               medInc2 |  -.0001202   .0000189    -6.37   0.000    -.0001573   -.0000832
			               offset |   (offset)
			               
			              if(a.getRace()==Agent.ASIAN){
			                  utility = Math.exp( 
			                          17.89578*ratio -23.77963*ratio*ratio
			                          + .0136211*(neighborhoodMedianIncome/1000) -.0001202*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			              }	
			              else if(a.getRace()==Agent.WHITE) {
			                  
			                   WHITE
			                   ratio |   16.87682   .1798653    93.83   0.000     16.52429    17.22935
			                   ratio2 |  -21.74727   .2303531   -94.41   0.000    -22.19875   -21.29579
			                   medInc |   .0836554   .0010716    78.06   0.000      .081555    .0857557
			                   medInc2 |  -.0004368   8.66e-06   -50.46   0.000    -.0004537   -.0004198
			                   offset |   (offset)
			                    
			                  utility = Math.exp( 
			                          16.87682*ratio -21.74727*ratio*ratio
			                          + .0836554*(neighborhoodMedianIncome/1000) -.0004368*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			              } else if(a.getRace()==Agent.BLACK) {
			                   BLACK
			                   ratio |   19.28084   .3455543    55.80   0.000     18.60357    19.95812
			                   ratio2 |  -27.28715    .438987   -62.16   0.000    -28.14755   -26.42675
			                   medInc |  -.0288048   .0036335    -7.93   0.000    -.0359262   -.0216833
			                   medInc2 |   -.000152   .0000458    -3.32   0.001    -.0002417   -.0000622
			                      
			                  utility = Math.exp( 
			                          19.28084*ratio -27.28715*ratio*ratio
			                          + -.0288048*neighborhoodMedianIncome-.000152*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			                  
			              } else if(a.getRace()==Agent.HISPANIC){
			                   	 
			                   HISPANIC
			                   ratio |   9.736794   .1416736    68.73   0.000     9.459119    10.01447
			                   ratio2 |  -16.86509    .182294   -92.52   0.000    -17.22238    -16.5078
			                   medInc |   .0104645   .0016513     6.34   0.000      .007228    .0137009
			                   medInc2 |    -.00046   .0000209   -22.00   0.000     -.000501    -.000419
			                   offset |   (offset)
			                     
			                  utility = Math.exp( 
			                          9.736794*ratio -16.86509*ratio*ratio
			                          + .0104645*(neighborhoodMedianIncome/1000)-.00046*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			              } 
			          } else if(tenure==Agent.OWNER){
			              if(a.getRace()==Agent.ASIAN){
			                   ASIAN
			                   ratio |   20.27438   .4361076    46.49   0.000     19.41962    21.12913
			                   ratio2 |  -28.64516   .6901788   -41.50   0.000    -29.99788   -27.29243
			                   medInc |   .0272178   .0025713    10.59   0.000     .0221781    .0322574
			                   medInc2 |  -.0002098   .0000179   -11.74   0.000    -.0002448   -.0001747
			                   offset |   (offset)
			                   
			                  utility = Math.exp( 
			                          20.27438*ratio -28.64516*ratio*ratio
			                          + .0272178*(neighborhoodMedianIncome/1000)-.0002098*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			              }	else if(a.getRace()==Agent.WHITE) {
			                   WHITE      
			                   ratio |   20.32956   .1953568   104.06   0.000     19.94667    20.71245
			                   ratio2 |  -29.57951   .3150745   -93.88   0.000    -30.19705   -28.96198
			                   medInc |   .0391353   .0008888    44.03   0.000     .0373933    .0408773
			                   medInc2 |  -.0001786   5.14e-06   -34.77   0.000    -.0001887   -.0001685
			                   offset |   (offset)
			                   
			                  utility = Math.exp( 
			                          20.32956*ratio -29.57951*ratio*ratio
			                          + .0391353*(neighborhoodMedianIncome/1000)-.0001786*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			              } 
			              else if(a.getRace()==Agent.BLACK) {
			                   
			                   /*
			                    BLACK     
			                    ratio |   16.79473   .4564699    36.79   0.000     15.90006    17.68939
			                    ratio2 |  -22.26258   .6567087   -33.90   0.000    -23.54971   -20.97546
			                    medInc |  -.0854712   .0031817   -26.86   0.000    -.0917072   -.0792352
			                    medInc2 |   .0002675   .0000266    10.05   0.000     .0002153    .0003196
			                    offset |   (offset) 
			                     
			                  utility = Math.exp( 
			                          16.79473*ratio -22.26258*ratio*ratio
			                          +-.0854712*(neighborhoodMedianIncome/1000)+.0002675*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			              } else if(a.getRace()==Agent.HISPANIC){
			                   
			                   HISP
			                   ratio |   18.72162    .275803    67.88   0.000     18.18106    19.26218
			                   ratio2 |  -26.91835   .4258184   -63.22   0.000    -27.75294   -26.08376
			                   medInc |  -.0172796   .0029054    -5.95   0.000    -.0229742    -.011585
			                   medInc2 |  -.0002668   .0000286    -9.32   0.000    -.0003229   -.0002107
			                   offset |   (offset)  
			                    
			                  utility = Math.exp( 
			                          18.72162*ratio -26.91835*ratio*ratio
			                          +-.0172796*(neighborhoodMedianIncome/1000)-.0002668*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000));	        		
			                  
			              } 
			          } else {
			              System.out.println("no tenure");
			          }
                      if(utility==0.0){ // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
                            utility = 0.00000000000000000001;
                        }

			          return utility; 
			      }

				    
				    public double computeMarginalUtility(Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
				    	*//**
				    	 * For now, not using market clearing rents with the LA FANS choice
				    	 * functions.. so these classes are undefined. 
				    	 *//*
				    	double t=0; 
				    	return t; 
				    }
				    
				    public double solveForPrice(double marginalUtil, Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
				    	double t=0; 
				    	return t; 

				   }
				    public String toString() { // Priyasree_DeadCode : Unreachable code_
				        return "CensusPumsIncOnly";
				    }

		}*/

	


	

