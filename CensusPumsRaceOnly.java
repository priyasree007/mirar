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
public class CensusPumsRaceOnly extends AgentDecision {

	/**
	 * 
	 */
	public CensusPumsRaceOnly() {
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
	        		System.out.println("is this agent's own unit? " + (utilities.get(i)==utilityCurrentUnit)); // Priyasree: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
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
	        			5.50094*pctBlackInNeighborhood-14.91612*pctBlackInNeighborhood*pctBlackInNeighborhood
	   	             +3.948578*pctHispanicInNeighborhood-2.875971*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	   	             +15.76788*pctAsianInNeighborhood-3.989716*pctAsianInNeighborhood*pctAsianInNeighborhood
	   	             +2.074587*pctWhiteInNeighborhood);	        		
	        	}	else if(a.getRace()==Agent.WHITE) {
	        	/* WHITE      
	            bp |    -5.7467   .2258918   -25.44   0.000     -6.18944    -5.30396
	            bp2 |  -5.484273    .398768   -13.75   0.000    -6.265844   -4.702703
	             hp |  -6.716011   .2331027   -28.81   0.000    -7.172884   -6.259139
	            hp2 |  -1.098751   .1538727    -7.14   0.000    -1.400336   -.7971658
	             ap |  -6.190551   .2404499   -25.75   0.000    -6.661824   -5.719278
	            ap2 |  -.1027043   .3110723    -0.33   0.741    -.7123948    .5069862
	            wp2 |  -3.952846   .1383078   -28.58   0.000    -4.223924   -3.681768
	         offset |   (offset)   
	         
	         
	         */
	        	utility = Math.exp( 
	             -5.7467*pctBlackInNeighborhood-5.484273*pctBlackInNeighborhood*pctBlackInNeighborhood
	             -6.716011*pctHispanicInNeighborhood-1.098751*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	             -6.190551*pctAsianInNeighborhood-.1027043*pctAsianInNeighborhood*pctAsianInNeighborhood
	             -3.952846*pctWhiteInNeighborhood);
	        	} else if(a.getRace()==Agent.BLACK) {
	        	        /* 
	       	     BLACK
	       	   	  bp |   12.04391   .2860542    42.10   0.000     11.48326    12.60457
	       	     bp2 |  -12.04391   .2495878   -50.47   0.000    -13.08644   -12.10807
	       	      hp |  -6.443778   .3851006   -16.73   0.000    -7.198561   -5.688995
	       	     hp2 |   1.485464   .2763682     5.37   0.000     .9437926    2.027136
	       	      ap |  -.3481225   .5379036    -0.65   0.518    -1.402394    .7061492
	       	     ap2 |  -9.468959   .9558595    -9.91   0.000    -11.34241   -7.595509
	       	     wp2 |  -8.014921   .3456874   -23.19   0.000    -8.692456   -7.337386
	       	         */  
	        	utility = Math.exp( 
	        			12.04391*pctBlackInNeighborhood-12.04391*pctBlackInNeighborhood*pctBlackInNeighborhood
	        			-6.443778*pctHispanicInNeighborhood+ 1.485464*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	        			-.3481225*pctAsianInNeighborhood-9.468959*pctAsianInNeighborhood*pctAsianInNeighborhood
	        			-8.014921*pctWhiteInNeighborhood);
	        		} else if(a.getRace()==Agent.HISPANIC){
	       	   	     /* 	 
	   	   	 HISPANIC
	   	             bp |  -.8768531   .1100605    -7.97   0.000    -1.092568   -.6611385
	   	            bp2 |   .1480487    .163637     0.90   0.366    -.1726739    .4687713
	   	             hp |   4.505583   .1709631    26.35   0.000     4.170501    4.840665
	   	            hp2 |  -2.044714   .1043989   -19.59   0.000    -2.249332   -1.840096
	   	             ap |   1.083995   .1387773     7.81   0.000     .8119964    1.355993
	   	            ap2 |   .4345045   .1977337     2.20   0.028     .0469536    .8220553
	   	            wp2 |  -.6648807   .1162777    -5.72   0.000    -.8927807   -.4369807
	   	         offset |   (offset) 
	   	         */
	    	        	utility = Math.exp( 
	    	        			-.8768531*pctBlackInNeighborhood+.1480487*pctBlackInNeighborhood*pctBlackInNeighborhood
	    	        			+4.505583*pctHispanicInNeighborhood+ -2.044714*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	    	        			+1.083995*pctAsianInNeighborhood+.4345045*pctAsianInNeighborhood*pctAsianInNeighborhood
	    	        			-.6648807*pctWhiteInNeighborhood);  			
	        		} 
	        } else if(tenure==Agent.OWNER){
	               	if(a.getRace()==Agent.ASIAN){
/* ASIAN
   bp |   3.129966    .982341     3.19   0.001     1.204613    5.055319
  bp2 |   -16.7185   3.198247    -5.23   0.000    -22.98695   -10.45005
   hp |  -.2135005   .5712745    -0.37   0.709    -1.333178    .9061769
  hp2 |  -.5184816   .4501465    -1.15   0.249    -1.400752    .3637893
   ap |     11.536   .6650652    17.35   0.000      10.2325     12.8395
  ap2 |  -6.919635    .617949   -11.20   0.000    -8.130793   -5.708478
  wp2 |    -1.6042   .4434632    -3.62   0.000    -2.473372   -.7350282
*/
	       	        	utility = Math.exp( 
	       	        			3.129966*pctBlackInNeighborhood-16.7185*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        			-.2135005*pctHispanicInNeighborhood-.5184816*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	   	             +11.536*pctAsianInNeighborhood-6.919635*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	   	             -1.6042*pctWhiteInNeighborhood);	        		
	}	else if(a.getRace()==Agent.WHITE) {
       	/* WHITE      
          bp |  -1.489744   .4587417    -3.25   0.001    -2.388862   -.5906272
         bp2 |  -10.74584   .7464478   -14.40   0.000    -12.20885   -9.282832
          hp |  -6.682847   .4945837   -13.51   0.000    -7.652213    -5.71348
         hp2 |  -1.787978   .3428448    -5.22   0.000    -2.459942   -1.116015
          ap |  -11.05841   .4791876   -23.08   0.000     -11.9976   -10.11922
         ap2 |   4.968652   .5020795     9.90   0.000     3.984595     5.95271
         wp2 |  -4.600744   .2821544   -16.31   0.000    -5.153756   -4.047732
      */                                                    
	       	        	utility = Math.exp( 
	       	        	-1.489744*pctBlackInNeighborhood-10.74584*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        	-6.716011*pctHispanicInNeighborhood-1.098751*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	        	-11.05841*pctAsianInNeighborhood+4.968652*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	        	-4.600744*pctWhiteInNeighborhood);
	       	        	
	} else if(a.getRace()==Agent.BLACK) {
	       	        	        /* 
 /*
       BLACK     
         bp |    5.70483   1.132088     5.04   0.000     3.485978    7.923683
         bp2 |  -12.30612   1.032413   -11.92   0.000    -14.32962   -10.28263
          hp |  -36.55477   1.749729   -20.89   0.000    -39.98418   -33.12536
         hp2 |    11.3222    1.18021     9.59   0.000     9.009028    13.63537
          ap |  -26.65699   1.887824   -14.12   0.000    -30.35706   -22.95693
         ap2 |  -4.682142   2.563086    -1.83   0.068    -9.705697    .3414136
         wp2 |   -35.4673   1.533853   -23.12   0.000     -38.4736     -32.461
      offset |   (offset)  
*/ 
	       	        	utility = Math.exp( 
	       	        			5.70483*pctBlackInNeighborhood-12.30612*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        			-36.55477*pctHispanicInNeighborhood+ 11.3222*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	        			-26.65699*pctAsianInNeighborhood-4.682142*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	        			-35.4673*pctWhiteInNeighborhood);
	} else if(a.getRace()==Agent.HISPANIC){
 /* 
        HISP
         bp |   1.302403   .2911516     4.47   0.000     .7317561    1.873049
         bp2 |  -4.459787   .4423109   -10.08   0.000    -5.326701   -3.592874
          hp |   5.016759   .4551931    11.02   0.000     4.124597    5.908921
         hp2 |  -3.291582   .2697543   -12.20   0.000    -3.820291   -2.762874
          ap |  -3.141961   .3835435    -8.19   0.000    -3.893692   -2.390229
         ap2 |   .7064545   .5799682     1.22   0.223    -.4302624    1.843171
         wp2 |  -3.071358   .3002413   -10.23   0.000     -3.65982   -2.482896
      offset |   (offset)
  */ 
	       	    utility = Math.exp( 
	       	   1.302403*pctBlackInNeighborhood+-4.459787*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	    +5.016759*pctHispanicInNeighborhood+ -3.291582*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	    +-3.141961*pctAsianInNeighborhood+.7064545*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	    -3.071358*pctWhiteInNeighborhood);  				       	        			        		
	        
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
	        			5.50094*pctBlackInNeighborhood-14.91612*pctBlackInNeighborhood*pctBlackInNeighborhood
	   	             +3.948578*pctHispanicInNeighborhood-2.875971*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	   	             +15.76788*pctAsianInNeighborhood-3.989716*pctAsianInNeighborhood*pctAsianInNeighborhood
	   	             +2.074587*pctWhiteInNeighborhood);	        		
	        	}	else if(a.getRace()==Agent.WHITE) {
	        	/* WHITE      
	            bp |    -5.7467   .2258918   -25.44   0.000     -6.18944    -5.30396
	            bp2 |  -5.484273    .398768   -13.75   0.000    -6.265844   -4.702703
	             hp |  -6.716011   .2331027   -28.81   0.000    -7.172884   -6.259139
	            hp2 |  -1.098751   .1538727    -7.14   0.000    -1.400336   -.7971658
	             ap |  -6.190551   .2404499   -25.75   0.000    -6.661824   -5.719278
	            ap2 |  -.1027043   .3110723    -0.33   0.741    -.7123948    .5069862
	            wp2 |  -3.952846   .1383078   -28.58   0.000    -4.223924   -3.681768
	         offset |   (offset)   
	         
	         
	         */
	        	utility = Math.exp( 
	             -5.7467*pctBlackInNeighborhood-5.484273*pctBlackInNeighborhood*pctBlackInNeighborhood
	             -6.716011*pctHispanicInNeighborhood-1.098751*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	             -6.190551*pctAsianInNeighborhood-.1027043*pctAsianInNeighborhood*pctAsianInNeighborhood
	             -3.952846*pctWhiteInNeighborhood);
	        	} else if(a.getRace()==Agent.BLACK) {
	        	        /* 
	       	     BLACK
	       	   	  bp |   12.04391   .2860542    42.10   0.000     11.48326    12.60457
	       	     bp2 |  12.04391   .2495878   -50.47   0.000    -13.08644   -12.10807
	       	      hp |  -6.443778   .3851006   -16.73   0.000    -7.198561   -5.688995
	       	     hp2 |   1.485464   .2763682     5.37   0.000     .9437926    2.027136
	       	      ap |  -.3481225   .5379036    -0.65   0.518    -1.402394    .7061492
	       	     ap2 |  -9.468959   .9558595    -9.91   0.000    -11.34241   -7.595509
	       	     wp2 |  -8.014921   .3456874   -23.19   0.000    -8.692456   -7.337386
	       	         */  
	        	utility = Math.exp( 
	        			12.04391*pctBlackInNeighborhood-12.04391*pctBlackInNeighborhood*pctBlackInNeighborhood
	        			-6.443778*pctHispanicInNeighborhood+ 1.485464*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	        			-.3481225*pctAsianInNeighborhood-9.468959*pctAsianInNeighborhood*pctAsianInNeighborhood
	        			-8.014921*pctWhiteInNeighborhood);
	        		} else if(a.getRace()==Agent.HISPANIC){
	       	   	     /* 	 
	   	   	 HISPANIC
	   	             bp |  -.8768531   .1100605    -7.97   0.000    -1.092568   -.6611385
	   	            bp2 |   .1480487    .163637     0.90   0.366    -.1726739    .4687713
	   	             hp |   4.505583   .1709631    26.35   0.000     4.170501    4.840665
	   	            hp2 |  -2.044714   .1043989   -19.59   0.000    -2.249332   -1.840096
	   	             ap |   1.083995   .1387773     7.81   0.000     .8119964    1.355993
	   	            ap2 |   .4345045   .1977337     2.20   0.028     .0469536    .8220553
	   	            wp2 |  -.6648807   .1162777    -5.72   0.000    -.8927807   -.4369807
	   	         offset |   (offset) 
	   	         */
	    	        	utility = Math.exp( 
	    	        			-.8768531*pctBlackInNeighborhood+.1480487*pctBlackInNeighborhood*pctBlackInNeighborhood
	    	        			+4.505583*pctHispanicInNeighborhood+ -2.044714*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	    	        			+1.083995*pctAsianInNeighborhood+.4345045*pctAsianInNeighborhood*pctAsianInNeighborhood
	    	        			-.6648807*pctWhiteInNeighborhood);  			
	        		} 
	        } else if(tenure==Agent.OWNER){
	               	if(a.getRace()==Agent.ASIAN){
/* ASIAN
   bp |   3.129966    .982341     3.19   0.001     1.204613    5.055319
  bp2 |   -16.7185   3.198247    -5.23   0.000    -22.98695   -10.45005
   hp |  -.2135005   .5712745    -0.37   0.709    -1.333178    .9061769
  hp2 |  -.5184816   .4501465    -1.15   0.249    -1.400752    .3637893
   ap |     11.536   .6650652    17.35   0.000      10.2325     12.8395
  ap2 |  -6.919635    .617949   -11.20   0.000    -8.130793   -5.708478
  wp2 |    -1.6042   .4434632    -3.62   0.000    -2.473372   -.7350282
*/
	       	        	utility = Math.exp( 
	       	        			3.129966*pctBlackInNeighborhood-16.7185*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        			-.2135005*pctHispanicInNeighborhood-.5184816*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	   	             +11.536*pctAsianInNeighborhood-6.919635*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	   	             -1.6042*pctWhiteInNeighborhood);	        		
	}	else if(a.getRace()==Agent.WHITE) {
       	/* WHITE      
          bp |  -1.489744   .4587417    -3.25   0.001    -2.388862   -.5906272
         bp2 |  -10.74584   .7464478   -14.40   0.000    -12.20885   -9.282832
          hp |  -6.682847   .4945837   -13.51   0.000    -7.652213    -5.71348
         hp2 |  -1.787978   .3428448    -5.22   0.000    -2.459942   -1.116015
          ap |  -11.05841   .4791876   -23.08   0.000     -11.9976   -10.11922
         ap2 |   4.968652   .5020795     9.90   0.000     3.984595     5.95271
         wp2 |  -4.600744   .2821544   -16.31   0.000    -5.153756   -4.047732
      */                                                    
	       	        	utility = Math.exp( 
	       	        	-1.489744*pctBlackInNeighborhood-10.74584*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        	-6.716011*pctHispanicInNeighborhood-1.098751*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	        	-11.05841*pctAsianInNeighborhood+4.968652*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	        	-4.600744*pctWhiteInNeighborhood);
	       	        	
	} else if(a.getRace()==Agent.BLACK) {
	       	        	        /* 
 /*
       BLACK     
         bp |    5.70483   1.132088     5.04   0.000     3.485978    7.923683
         bp2 |  -12.30612   1.032413   -11.92   0.000    -14.32962   -10.28263
          hp |  -36.55477   1.749729   -20.89   0.000    -39.98418   -33.12536
         hp2 |    11.3222    1.18021     9.59   0.000     9.009028    13.63537
          ap |  -26.65699   1.887824   -14.12   0.000    -30.35706   -22.95693
         ap2 |  -4.682142   2.563086    -1.83   0.068    -9.705697    .3414136
         wp2 |   -35.4673   1.533853   -23.12   0.000     -38.4736     -32.461
      offset |   (offset)  
*/ 
	       	        	utility = Math.exp( 
	       	        			5.70483*pctBlackInNeighborhood-12.30612*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	        			-36.55477*pctHispanicInNeighborhood+ 11.3222*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	        			-26.65699*pctAsianInNeighborhood-4.682142*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	        			-35.4673*pctWhiteInNeighborhood);
	} else if(a.getRace()==Agent.HISPANIC){
 /* 
        HISP
         bp |   1.302403   .2911516     4.47   0.000     .7317561    1.873049
         bp2 |  -4.459787   .4423109   -10.08   0.000    -5.326701   -3.592874
          hp |   5.016759   .4551931    11.02   0.000     4.124597    5.908921
         hp2 |  -3.291582   .2697543   -12.20   0.000    -3.820291   -2.762874
          ap |  -3.141961   .3835435    -8.19   0.000    -3.893692   -2.390229
         ap2 |   .7064545   .5799682     1.22   0.223    -.4302624    1.843171
         wp2 |  -3.071358   .3002413   -10.23   0.000     -3.65982   -2.482896
      offset |   (offset)
  */ 
	       	    utility = Math.exp( 
	       	   1.302403*pctBlackInNeighborhood+-4.459787*pctBlackInNeighborhood*pctBlackInNeighborhood
	       	    +5.016759*pctHispanicInNeighborhood+ -3.291582*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       	    +-3.141961*pctAsianInNeighborhood+.7064545*pctAsianInNeighborhood*pctAsianInNeighborhood
	       	    -3.071358*pctWhiteInNeighborhood);  				       	        			        		
	        
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
		        return "CensusPumsRaceOnly";
		    }

}
