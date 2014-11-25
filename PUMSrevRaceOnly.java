/*
 * Created on Nov 9, 2005
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
public class PUMSrevRaceOnly extends AgentDecision {

	/**
	 * 
	 */
	public PUMSrevRaceOnly() {
		super();
		// TODO Auto-generated constructor stub
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
	    
	      public double computeUtility(Block b, Agent a, int tenure) {
	  
	      	double utility= -12.0;
	   		ArrayList blocks = new ArrayList();
	        if (b == null) {
	        System.out.println("block is null");
	        }   
	        else {
                double neighborhoodMedianIncome =  b.getNeighborhoodMedianIncome();
                double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
                double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
                double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
                double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
	        	if(tenure==Agent.RENTER){
         

       		if(a.getRace()==Agent.ASIAN){
	        	utility = Math.exp( 
	        		 4.562*pctBlackInNeighborhood-17.872*pctBlackInNeighborhood*pctBlackInNeighborhood
	   	             + 0.221*pctHispanicInNeighborhood -0.819*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	   	             +17.834*pctAsianInNeighborhood-9.241*pctAsianInNeighborhood*pctAsianInNeighborhood
	   	             +0.208*pctWhiteInNeighborhood);	
	        	
	        	}	else if(a.getRace()==Agent.WHITE) {
	        	utility = Math.exp( 
	             - 5.212*pctBlackInNeighborhood-6.448*pctBlackInNeighborhood*pctBlackInNeighborhood
	             -6.867*pctHispanicInNeighborhood-0.972*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	             -6.530*pctAsianInNeighborhood+ 0.303*pctAsianInNeighborhood*pctAsianInNeighborhood
	             -3.935*pctWhiteInNeighborhood);
	        	
	        	} else if(a.getRace()==Agent.BLACK) {
	        	utility = Math.exp( 
	        			12.405*pctBlackInNeighborhood-13.521*pctBlackInNeighborhood*pctBlackInNeighborhood
	        			-8.841*pctHispanicInNeighborhood+ 2.774*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	        			-0.682*pctAsianInNeighborhood-12.249*pctAsianInNeighborhood*pctAsianInNeighborhood
	        			-9.760*pctWhiteInNeighborhood);
	        		} else if(a.getRace()==Agent.HISPANIC){
	    	        	utility = Math.exp( 
	    	        			-1.670*pctBlackInNeighborhood- 0.025*pctBlackInNeighborhood*pctBlackInNeighborhood
	    	        			+2.840*pctHispanicInNeighborhood+ - 1.430*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	    	        			- 1.094*pctAsianInNeighborhood+ 0.490*pctAsianInNeighborhood*pctAsianInNeighborhood
	    	        			- 1.317*pctWhiteInNeighborhood);  			
	        		} 
	        } else if(tenure==Agent.OWNER){
	               	if(a.getRace()==Agent.ASIAN){
/* ASIAN
       costs |   .0041999   .0002378    17.66   0.000     .0037339    .0046659
      costs2 |  -4.47e-07   2.75e-08   -16.27   0.000    -5.01e-07   -3.94e-07
       ratio |   14.39926   1.657198     8.69   0.000     11.15121     17.6473
      ratio2 |  -30.10154   1.575092   -19.11   0.000    -33.18867   -27.01442
      medInc |  -.0006859   .0041041    -0.17   0.867    -.0087298     .007358
     medInc2 |    .000024   .0000227     1.06   0.290    -.0000205    .0000686
          bp |   1.959622   1.159996     1.69   0.091    -.3139278    4.233171
         bp2 |  -14.26577    3.45145    -4.13   0.000    -21.03048   -7.501047
          hp |   3.222334   .7992103     4.03   0.000      1.65591    4.788757
         hp2 |  -3.080838   .5835771    -5.28   0.000    -4.224628   -1.937048
          ap |   13.96843   .8435818    16.56   0.000     12.31504    15.62182
         ap2 |  -3.938367   .8135705    -4.84   0.000    -5.532935   -2.343798
         wp2 |  -2.579125   .5686267    -4.54   0.000    -3.693613   -1.464637
      offset |   (offset)
------------------------------------------------------------------------------
*/
	       	        	utility = Math.exp( 
	       	        		 1.959622*pctBlackInNeighborhood-14.26577*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        			+ 3.222334*pctHispanicInNeighborhood-3.080838*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	   	             +13.96843*pctAsianInNeighborhood-3.938367*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	   	       -2.579125*pctWhiteInNeighborhood);	        		
	}	else if(a.getRace()==Agent.WHITE) {
       	/* WHITE      
       costs |   .0018232   .0000784    23.25   0.000     .0016696    .0019769
      costs2 |  -1.33e-07   8.49e-09   -15.64   0.000    -1.49e-07   -1.16e-07
       ratio |    13.8991   .6612793    21.02   0.000     12.60302    15.19519
      ratio2 |  -29.58694   .7158948   -41.33   0.000    -30.99006   -28.18381
      medInc |   .0194022   .0015782    12.29   0.000      .016309    .0224955
     medInc2 |  -.0000995   8.08e-06   -12.32   0.000    -.0001153   -.0000837
          bp |  -.8410856   .4962405    -1.69   0.090    -1.813699    .1315278
         bp2 |  -13.28789   .8273874   -16.06   0.000    -14.90954   -11.66625
          hp |  -5.482388   .5379252   -10.19   0.000    -6.536702   -4.428074
         hp2 |  -3.524827   .3800748    -9.27   0.000     -4.26976   -2.779894
          ap |   -13.8648   .5110502   -27.13   0.000    -14.86644   -12.86316
         ap2 |   7.737586   .5131302    15.08   0.000     6.731869    8.743303
         wp2 |  -5.575379   .3060228   -18.22   0.000    -6.175173   -4.975586
      offset |   (offset)
------------------------------------------------------------------------------
      */                                                    
	       	        	utility = Math.exp( 
	       	        	-.8410856*pctBlackInNeighborhood-13.28789*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        	-5.482388*pctHispanicInNeighborhood-3.524827*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	        	-13.864*pctAsianInNeighborhood+7.737586*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	        	-5.575379*pctWhiteInNeighborhood);
	       	        	
	} else if(a.getRace()==Agent.BLACK) {
	       	        	        /* 
 /*
       BLACK     
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
       costs |   .0030656   .0009595     3.19   0.001     .0011849    .0049462
      costs2 |  -1.07e-06   1.66e-07    -6.48   0.000    -1.40e-06   -7.49e-07
       ratio |   96.62042   5.404104    17.88   0.000     86.02857    107.2123
      ratio2 |   -116.256   4.941288   -23.53   0.000    -125.9407   -106.5712
      medInc |   .3118984    .024244    12.86   0.000     .2643812    .3594157
     medInc2 |  -.0028285   .0002274   -12.44   0.000    -.0032741   -.0023828
          bp |   29.45947   1.578343    18.66   0.000     26.36597    32.55296
         bp2 |  -35.28017   1.248239   -28.26   0.000    -37.72668   -32.83367
          hp |  -34.94042   2.181433   -16.02   0.000    -39.21596   -30.66489
         hp2 |   11.04974   1.488368     7.42   0.000     8.132592    13.96689
          ap |  -28.80547   2.325306   -12.39   0.000    -33.36298   -24.24795
         ap2 |  -.8614868   3.081611    -0.28   0.780    -6.901334     5.17836
         wp2 |  -36.59448   1.905652   -19.20   0.000    -40.32949   -32.85947
      offset |   (offset)
------------------------------------------------------------------------------
*/ 
	       	        	utility = Math.exp( 
	       	        			29.45947*pctBlackInNeighborhood-35.28017*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        			-34.94042*pctHispanicInNeighborhood+ 11.04974*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	        			-28.80547 *pctAsianInNeighborhood-.8614868*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	        			-36.59448*pctWhiteInNeighborhood);
	} else if(a.getRace()==Agent.HISPANIC){
 /* 

       costs |   .0024295   .0002341    10.38   0.000     .0019706    .0028883
      costs2 |  -7.93e-07   4.84e-08   -16.39   0.000    -8.88e-07   -6.98e-07
       ratio |   21.19439   1.049127    20.20   0.000     19.13813    23.25064
      ratio2 |  -25.86543   .8715878   -29.68   0.000    -27.57371   -24.15715
      medInc |   .0552472   .0038397    14.39   0.000     .0477216    .0627729
     medInc2 |  -.0003671    .000034   -10.80   0.000    -.0004338   -.0003005
          bp |   3.691533   .3523722    10.48   0.000     3.000896     4.38217
         bp2 |  -5.657794   .4796919   -11.79   0.000    -6.597973   -4.717616
          hp |   7.293655   .4999197    14.59   0.000      6.31383     8.27348
         hp2 |  -3.340575   .2941028   -11.36   0.000    -3.917006   -2.764144
          ap |  -2.317364   .4219855    -5.49   0.000    -3.144441   -1.490288
         ap2 |   1.637686   .6092015     2.69   0.007     .4436729    2.831699
         wp2 |  -1.474916    .333954    -4.42   0.000    -2.129454   -.8203784
      offset |   (offset)
------------------------------------------------------------------------------
  */ 
	       	    utility = Math.exp( 
	       	    		3.691533*pctBlackInNeighborhood+ -5.657794*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	    +7.293655*pctHispanicInNeighborhood+ -3.340575*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	    +-2.317364*pctAsianInNeighborhood+  1.637686*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	 -1.474916*pctWhiteInNeighborhood);  				       	        			        		
	        
	} 
	        } else {
	        	System.out.println("no tenure");
	        }
	  }
	        return utility;
	      }

	    public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) {
		    /**
		     * note that I cap D to keep it from going to infinity
		     */       
	      	double tenure = a.getTenure();
	      	Block b = currUnit.getBlock();  //  block;
            double neighborhoodMedianIncome =  b.getNeighborhoodMedianIncome();
            double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
            double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
            double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
            double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
	      	double utility= -12.0;
	        if(tenure==Agent.RENTER){
         
	        /* ASIAN
	             bp |    5.50094    .463471    11.87   0.000     4.592554    6.409327
	            bp2 |  -14.91612   1.190251   -12.53   0.000    -17.24897   -12.58327
	             hp |   3.948578    .363512    10.86   0.000     3.236108    4.661048
	            hp2 |  -2.875971   .2789034   -10.31   0.000    -3.422612   -2.329331
	             ap |   15.76788   .3940466    40.02   0.000     14.99556     16.5402
	            ap2 |  -3.989716   .5349245    -7.46   0.000    -5.038149   -2.941284
	            wp2 |   2.074587   .2497249     8.31   0.000     1.585135    2.564039
	         offset |   (offset)  
	         */
       		if(a.getRace()==Agent.ASIAN){
	        	utility = Math.exp( 
		        		 4.562*pctBlackInNeighborhood-17.872*pctBlackInNeighborhood*pctBlackInNeighborhood
		   	             + 0.221*pctHispanicInNeighborhood -0.819*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		   	             +17.834*pctAsianInNeighborhood-9.241*pctAsianInNeighborhood*pctAsianInNeighborhood
		   	             +0.208*pctWhiteInNeighborhood);	
		        	
		        	}	else if(a.getRace()==Agent.WHITE) {
		        	utility = Math.exp( 
		             - 5.212*pctBlackInNeighborhood-6.448*pctBlackInNeighborhood*pctBlackInNeighborhood
		             -6.867*pctHispanicInNeighborhood-0.972*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		             -6.530*pctAsianInNeighborhood+ 0.303*pctAsianInNeighborhood*pctAsianInNeighborhood
		             -3.935*pctWhiteInNeighborhood);
		        	
		        	} else if(a.getRace()==Agent.BLACK) {
		        	utility = Math.exp( 
		        			12.405*pctBlackInNeighborhood-13.521*pctBlackInNeighborhood*pctBlackInNeighborhood
		        			-8.841*pctHispanicInNeighborhood+ 2.774*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		        			-0.682*pctAsianInNeighborhood-12.249*pctAsianInNeighborhood*pctAsianInNeighborhood
		        			-9.760*pctWhiteInNeighborhood);
		        		} else if(a.getRace()==Agent.HISPANIC){
		    	        	utility = Math.exp( 
		    	        			-1.670*pctBlackInNeighborhood- 0.025*pctBlackInNeighborhood*pctBlackInNeighborhood
		    	        			+2.840*pctHispanicInNeighborhood+ - 1.430*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		    	        			- 1.094*pctAsianInNeighborhood+ 0.490*pctAsianInNeighborhood*pctAsianInNeighborhood
		    	        			- 1.317*pctWhiteInNeighborhood);  			
		        		} 
		        } else if(tenure==Agent.OWNER){
		               	if(a.getRace()==Agent.ASIAN){
	/* ASIAN
	       costs |   .0041999   .0002378    17.66   0.000     .0037339    .0046659
	      costs2 |  -4.47e-07   2.75e-08   -16.27   0.000    -5.01e-07   -3.94e-07
	       ratio |   14.39926   1.657198     8.69   0.000     11.15121     17.6473
	      ratio2 |  -30.10154   1.575092   -19.11   0.000    -33.18867   -27.01442
	      medInc |  -.0006859   .0041041    -0.17   0.867    -.0087298     .007358
	     medInc2 |    .000024   .0000227     1.06   0.290    -.0000205    .0000686
	          bp |   1.959622   1.159996     1.69   0.091    -.3139278    4.233171
	         bp2 |  -14.26577    3.45145    -4.13   0.000    -21.03048   -7.501047
	          hp |   3.222334   .7992103     4.03   0.000      1.65591    4.788757
	         hp2 |  -3.080838   .5835771    -5.28   0.000    -4.224628   -1.937048
	          ap |   13.96843   .8435818    16.56   0.000     12.31504    15.62182
	         ap2 |  -3.938367   .8135705    -4.84   0.000    -5.532935   -2.343798
	         wp2 |  -2.579125   .5686267    -4.54   0.000    -3.693613   -1.464637
	      offset |   (offset)
	------------------------------------------------------------------------------
	*/
		       	        	utility = Math.exp( 
		       	        		 1.959622*pctBlackInNeighborhood-14.26577*pctBlackInNeighborhood*pctBlackInNeighborhood
		       	        			+ 3.222334*pctHispanicInNeighborhood-3.080838*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		       	   	             +13.96843*pctAsianInNeighborhood-3.938367*pctAsianInNeighborhood*pctAsianInNeighborhood
		       	   	       -2.579125*pctWhiteInNeighborhood);	        		
		}	else if(a.getRace()==Agent.WHITE) {
	       	/* WHITE      
	       costs |   .0018232   .0000784    23.25   0.000     .0016696    .0019769
	      costs2 |  -1.33e-07   8.49e-09   -15.64   0.000    -1.49e-07   -1.16e-07
	       ratio |    13.8991   .6612793    21.02   0.000     12.60302    15.19519
	      ratio2 |  -29.58694   .7158948   -41.33   0.000    -30.99006   -28.18381
	      medInc |   .0194022   .0015782    12.29   0.000      .016309    .0224955
	     medInc2 |  -.0000995   8.08e-06   -12.32   0.000    -.0001153   -.0000837
	          bp |  -.8410856   .4962405    -1.69   0.090    -1.813699    .1315278
	         bp2 |  -13.28789   .8273874   -16.06   0.000    -14.90954   -11.66625
	          hp |  -5.482388   .5379252   -10.19   0.000    -6.536702   -4.428074
	         hp2 |  -3.524827   .3800748    -9.27   0.000     -4.26976   -2.779894
	          ap |   -13.8648   .5110502   -27.13   0.000    -14.86644   -12.86316
	         ap2 |   7.737586   .5131302    15.08   0.000     6.731869    8.743303
	         wp2 |  -5.575379   .3060228   -18.22   0.000    -6.175173   -4.975586
	      offset |   (offset)
	------------------------------------------------------------------------------
	      */                                                    
		       	        	utility = Math.exp( 
		       	        	-.8410856*pctBlackInNeighborhood-13.28789*pctBlackInNeighborhood*pctBlackInNeighborhood
		       	        	-5.482388*pctHispanicInNeighborhood-3.524827*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		       	        	-13.864*pctAsianInNeighborhood+7.737586*pctAsianInNeighborhood*pctAsianInNeighborhood
		       	        	-5.575379*pctWhiteInNeighborhood);
		       	        	
		} else if(a.getRace()==Agent.BLACK) {
		       	        	        /* 
	 /*
	       BLACK     
	------------------------------------------------------------------------------
	      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
	-------------+----------------------------------------------------------------
	       costs |   .0030656   .0009595     3.19   0.001     .0011849    .0049462
	      costs2 |  -1.07e-06   1.66e-07    -6.48   0.000    -1.40e-06   -7.49e-07
	       ratio |   96.62042   5.404104    17.88   0.000     86.02857    107.2123
	      ratio2 |   -116.256   4.941288   -23.53   0.000    -125.9407   -106.5712
	      medInc |   .3118984    .024244    12.86   0.000     .2643812    .3594157
	     medInc2 |  -.0028285   .0002274   -12.44   0.000    -.0032741   -.0023828
	          bp |   29.45947   1.578343    18.66   0.000     26.36597    32.55296
	         bp2 |  -35.28017   1.248239   -28.26   0.000    -37.72668   -32.83367
	          hp |  -34.94042   2.181433   -16.02   0.000    -39.21596   -30.66489
	         hp2 |   11.04974   1.488368     7.42   0.000     8.132592    13.96689
	          ap |  -28.80547   2.325306   -12.39   0.000    -33.36298   -24.24795
	         ap2 |  -.8614868   3.081611    -0.28   0.780    -6.901334     5.17836
	         wp2 |  -36.59448   1.905652   -19.20   0.000    -40.32949   -32.85947
	      offset |   (offset)
	------------------------------------------------------------------------------
	*/ 
		       	        	utility = Math.exp( 
		       	        			29.45947*pctBlackInNeighborhood-35.28017*pctBlackInNeighborhood*pctBlackInNeighborhood
		       	        			-34.94042*pctHispanicInNeighborhood+ 11.04974*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		       	        			-28.80547 *pctAsianInNeighborhood-.8614868*pctAsianInNeighborhood*pctAsianInNeighborhood
		       	        			-36.59448*pctWhiteInNeighborhood);
		} else if(a.getRace()==Agent.HISPANIC){
	 /* 

	       costs |   .0024295   .0002341    10.38   0.000     .0019706    .0028883
	      costs2 |  -7.93e-07   4.84e-08   -16.39   0.000    -8.88e-07   -6.98e-07
	       ratio |   21.19439   1.049127    20.20   0.000     19.13813    23.25064
	      ratio2 |  -25.86543   .8715878   -29.68   0.000    -27.57371   -24.15715
	      medInc |   .0552472   .0038397    14.39   0.000     .0477216    .0627729
	     medInc2 |  -.0003671    .000034   -10.80   0.000    -.0004338   -.0003005
	          bp |   3.691533   .3523722    10.48   0.000     3.000896     4.38217
	         bp2 |  -5.657794   .4796919   -11.79   0.000    -6.597973   -4.717616
	          hp |   7.293655   .4999197    14.59   0.000      6.31383     8.27348
	         hp2 |  -3.340575   .2941028   -11.36   0.000    -3.917006   -2.764144
	          ap |  -2.317364   .4219855    -5.49   0.000    -3.144441   -1.490288
	         ap2 |   1.637686   .6092015     2.69   0.007     .4436729    2.831699
	         wp2 |  -1.474916    .333954    -4.42   0.000    -2.129454   -.8203784
	      offset |   (offset)
	------------------------------------------------------------------------------
	  */ 
		       	    utility = Math.exp( 
		       	    		3.691533*pctBlackInNeighborhood+ -5.657794*pctBlackInNeighborhood*pctBlackInNeighborhood
		       	    +7.293655*pctHispanicInNeighborhood+ -3.340575*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		       	    +-2.317364*pctAsianInNeighborhood+  1.637686*pctAsianInNeighborhood*pctAsianInNeighborhood
		       	 -1.474916*pctWhiteInNeighborhood);  			       	        			        		
	        
	} 
	        } else {
	        	System.out.println("no tenure");
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
		        return "PUMSrevRaceOnly";
		    }

}
