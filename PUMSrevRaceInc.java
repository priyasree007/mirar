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
public class PUMSrevRaceInc extends AgentDecision {

	/**
	 * 
	 */
	public PUMSrevRaceInc() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * Created on Nov 10, 2005
	 *
	 * To change the template for this generated file go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */



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
  	double ratio=(b.getMedianRent(tenure)*12.0)/a.getIncome();
//		ArrayList blocks = new ArrayList();
    if (b == null) {
    System.out.println("block is null");
    }   
    else {
        double neighborhoodMedianIncome =  b.getNeighborhoodMedianIncome();
        double rent = b.getMedianRent(tenure);
        double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
        double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
        double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
        double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
        if(tenure==Agent.RENTER){
            /* ASIAN
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
     rentgrs |   .0110718   .0003908    28.33   0.000     .0103059    .0118377
    rentgrs2 |  -2.44e-06   1.08e-07   -22.56   0.000    -2.65e-06   -2.23e-06
       ratio |   -12.7366    1.20691   -10.55   0.000     -15.1021    -10.3711
      ratio2 |  -3.896984   .9683817    -4.02   0.000    -5.794977   -1.998991
      medInc |   -.032303   .0027532   -11.73   0.000    -.0376991   -.0269068
     medInc2 |   .0001615   .0000221     7.30   0.000     .0001182    .0002049
          bp |   4.561891    .593008     7.69   0.000     3.399617    5.724166
          hp |   .2206965   .4486256     0.49   0.623    -.6585934    1.099986
          ap |   17.83406   .4669824    38.19   0.000      16.9188    18.74933
         bp2 |  -17.87197    1.63318   -10.94   0.000    -21.07294   -14.67099
         hp2 |  -.8190418    .336224    -2.44   0.015    -1.478029   -.1600549
         ap2 |  -9.240982   .5633857   -16.40   0.000     -10.3452   -8.136766
         wp2 |   .2083493    .307955     0.68   0.499    -.3952314      .81193
      offset |   (offset)
------------------------------------------------------------------------------

             */
            if(a.getRace()==Agent.ASIAN){
                utility = Math.exp( 
                		.0110718*rent + -0.00000244*rent*rent
                        + -12.7366*ratio -3.896984*ratio*ratio
                        -.032303*(neighborhoodMedianIncome/1000) + .0001615*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                        + 4.561891*pctBlackInNeighborhood-17.87197*pctBlackInNeighborhood*pctBlackInNeighborhood
                        +.2206965*pctHispanicInNeighborhood-.8190418*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                        +17.83406*pctAsianInNeighborhood -9.24098*pctAsianInNeighborhood*pctAsianInNeighborhood
                        +.2083493*pctWhiteInNeighborhood);	        		
            }	else if(a.getRace()==Agent.WHITE) {
                
                /* WHITE
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
     rentgrs |   .0074767   .0001445    51.74   0.000     .0071935    .0077599
    rentgrs2 |  -1.41e-06   3.89e-08   -36.35   0.000    -1.49e-06   -1.34e-06
       ratio |   -13.7593   .4554832   -30.21   0.000    -14.65203   -12.86656
      ratio2 |     1.0231   .3539406     2.89   0.004     .3293898    1.716811
      medInc |   -.003181   .0011542    -2.76   0.006    -.0054432   -.0009189
     medInc2 |   6.25e-06   8.06e-06     0.78   0.438    -9.54e-06     .000022
          bp |  -5.212282   .2700135   -19.30   0.000    -5.741499   -4.683066
          hp |  -6.866833   .2752685   -24.95   0.000     -7.40635   -6.327317
          ap |  -6.530174   .2844234   -22.96   0.000    -7.087634   -5.972715
         bp2 |   -6.44866   .4740676   -13.60   0.000    -7.377816   -5.519505
         hp2 |    -.97222   .1807675    -5.38   0.000    -1.326518   -.6179222
         ap2 |   .3025913   .3626553     0.83   0.404    -.4081999    1.013383
         wp2 |  -3.935416   .1647653   -23.88   0.000     -4.25835   -3.612482
      offset |   (offset)
------------------------------------------------------------------------------
                 */ 
                utility = Math.exp( 
                		.0074767*rent + -0.00000141*rent*rent
                        + -13.7593*ratio + 1.0231*ratio*ratio
                        + -.003181*(neighborhoodMedianIncome/1000) + 0.00000625*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                        +  -5.212282*pctBlackInNeighborhood-6.44866*pctBlackInNeighborhood*pctBlackInNeighborhood
                        +-6.866833*pctHispanicInNeighborhood-.9722*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                        +-6.530174*pctAsianInNeighborhood+ 0.3025913*pctAsianInNeighborhood*pctAsianInNeighborhood
                        +-3.935416 *pctWhiteInNeighborhood);	        		
            } else if(a.getRace()==Agent.BLACK) {
                /* BLACK
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
     rentgrs |   .0107934   .0005933    18.19   0.000     .0096306    .0119563
    rentgrs2 |  -3.21e-06   2.08e-07   -15.47   0.000    -3.62e-06   -2.81e-06
       ratio |  -6.787745   1.501505    -4.52   0.000    -9.730641   -3.844849
      ratio2 |  -7.148086   1.213444    -5.89   0.000    -9.526392   -4.769779
      medInc |   .0307874   .0055848     5.51   0.000     .0198414    .0417333
     medInc2 |  -.0004037   .0000687    -5.88   0.000    -.0005384   -.0002691
          bp |   12.40578   .3884667    31.94   0.000      11.6444    13.16716
          hp |  -8.841161   .5332843   -16.58   0.000    -9.886379   -7.795943
          ap |  -.6815458   .7164054    -0.95   0.341    -2.085675     .722583
         bp2 |  -13.52152   .3299524   -40.98   0.000    -14.16821   -12.87482
         hp2 |   2.774236   .3639586     7.62   0.000     2.060891    3.487582
         ap2 |  -12.24857   1.314413    -9.32   0.000    -14.82478   -9.672371
         wp2 |  -9.760022   .4668376   -20.91   0.000    -10.67501   -8.845037
      offset |   (offset)
------------------------------------------------------------------------------
                 */   
                utility = Math.exp( 
                		.0107934* rent + -0.00000321*rent*rent
                       - 6.787745*ratio  -7.148086*ratio*ratio
                        +   .0307874*(neighborhoodMedianIncome/1000) + -.0004037*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                        + 12.40578*pctBlackInNeighborhood -13.52152*pctBlackInNeighborhood*pctBlackInNeighborhood
                        -8.841161*pctHispanicInNeighborhood + 2.774236*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                        +-.6815458*pctAsianInNeighborhood+-12.24857*pctAsianInNeighborhood*pctAsianInNeighborhood
                        -9.760022*pctWhiteInNeighborhood);
            } else if(a.getRace()==Agent.HISPANIC){
                /* 	 
                 HISPANIC
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
     rentgrs |   .0060138   .0002128    28.26   0.000     .0055967    .0064309
    rentgrs2 |  -1.83e-06   8.00e-08   -22.85   0.000    -1.99e-06   -1.67e-06
       ratio |   -6.66967   .5356767   -12.45   0.000    -7.719577   -5.619763
      ratio2 |  -4.824713   .4768441   -10.12   0.000     -5.75931   -3.890116
      medInc |   .0070514    .001799     3.92   0.000     .0035255    .0105773
     medInc2 |  -.0001089   .0000211    -5.17   0.000    -.0001502   -.0000676
          bp |  -1.669998   .1437394   -11.62   0.000    -1.951722   -1.388274
          hp |   2.840491   .2091093    13.58   0.000     2.430645    3.250338
          ap |   -1.09497    .169039    -6.48   0.000     -1.42628   -.7636599
         bp2 |  -.0256678   .1984839    -0.13   0.897     -.414689    .3633534
         hp2 |  -1.432767    .125123   -11.45   0.000    -1.678004   -1.187531
         ap2 |   .4907525   .2263183     2.17   0.030     .0471767    .9343282
         wp2 |  -1.317926   .1455236    -9.06   0.000    -1.603147   -1.032705
      offset |   (offset)
------------------------------------------------------------------------------
                 */  
                utility = Math.exp( 
                		 .0060138*rent + -0.00000183*rent*rent
                        + -6.66967*ratio-4.824713*ratio*ratio
                        + .0070514*(neighborhoodMedianIncome/1000)-.0001089*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                        + -1.669998*pctBlackInNeighborhood + -.0256678*pctBlackInNeighborhood*pctBlackInNeighborhood
                        + 2.840491*pctHispanicInNeighborhood-1.432767*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                        + -1.09497*pctAsianInNeighborhood+.490752*pctAsianInNeighborhood*pctAsianInNeighborhood
                        + -1.317926*pctWhiteInNeighborhood);		} 
        } else if(tenure==Agent.OWNER){
            if(a.getRace()==Agent.ASIAN){
                /* ASIAN
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
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
                		.0041999*rent + - 0.000000447*rent*rent
                        + 14.39926*ratio -30.10154*ratio*ratio
                        -.0006859*(neighborhoodMedianIncome/1000) + .000024*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                        +  1.959622*pctBlackInNeighborhood -14.26577*pctBlackInNeighborhood*pctBlackInNeighborhood
                        + 3.222334*pctHispanicInNeighborhood -3.080838*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                        +13.96843*pctAsianInNeighborhood+ -3.938367*pctAsianInNeighborhood*pctAsianInNeighborhood
                        +-2.579125*pctWhiteInNeighborhood);
            }	else if(a.getRace()==Agent.WHITE) {
                /* WHITE      
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
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
                		.0018232*rent + -0.000000133*rent*rent
                        + 13.8991*ratio -29.5869*ratio*ratio
                        +.0194022*(neighborhoodMedianIncome/1000) -.0000995*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                        + -.8410856*pctBlackInNeighborhood -13.28789*pctBlackInNeighborhood*pctBlackInNeighborhood
                        + -5.482388*pctHispanicInNeighborhood -3.524827*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                        + -13.8648*pctAsianInNeighborhood+7.737586*pctAsianInNeighborhood*pctAsianInNeighborhood
                        +-5.575379*pctWhiteInNeighborhood);		
            } else if(a.getRace()==a.BLACK) {
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
                		 .0030656*rent + -0.00000107*rent*rent 
                        +96.62042 *ratio -116.256*ratio*ratio
                        + .3118984*(neighborhoodMedianIncome/1000) +-.0028285*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                        + 29.45947*pctBlackInNeighborhood -35.2801*pctBlackInNeighborhood*pctBlackInNeighborhood
                        +-34.94042*pctHispanicInNeighborhood+ 11.04974*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                        + -28.80547*pctAsianInNeighborhood+-.8614868*pctAsianInNeighborhood*pctAsianInNeighborhood
                        +-36.59448*pctWhiteInNeighborhood);						
                } else if(a.getRace()==Agent.HISPANIC){
                            /* 
                             HISP
------------------------------------------------------------------------------
      choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
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
                            		.0024295*rent + -0.000000793
                                    + 21.19439*ratio -25.86543*ratio*ratio
                                    + .0552472*(neighborhoodMedianIncome/1000) +  -.0003671*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                                    + 3.691533*pctBlackInNeighborhood  -5.657794*pctBlackInNeighborhood*pctBlackInNeighborhood
                                    + 7.29365*pctHispanicInNeighborhood+ -3.340575*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                                    + -2.317364*pctAsianInNeighborhood+ 1.637686*pctAsianInNeighborhood*pctAsianInNeighborhood
                                    + -1.474916*pctWhiteInNeighborhood);			} 
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
  	int tenure = a.getTenure();
  	
  	Block b = currUnit.getBlock();  //  block;
  double neighborhoodMedianIncome =  b.getNeighborhoodMedianIncome();
  double pctBlackInNeighborhood = this.getPctBlackInNeighborhood(b);
  double pctWhiteInNeighborhood = this.getPctWhiteInNeighborhood(b);
  double pctHispanicInNeighborhood = this.getPctHispanicInNeighborhood(b);
  double pctAsianInNeighborhood = this.getPctAsianInNeighborhood(b);
  
  
  
	double ratio=(b.getMedianRent(tenure)*12.0)/a.getIncome();
	double rent = b.getMedianRent(tenure); 
		
  	double utility= -12.0;
    if(tenure==Agent.RENTER){
        /* ASIAN
------------------------------------------------------------------------------
  choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
 rentgrs |   .0110718   .0003908    28.33   0.000     .0103059    .0118377
rentgrs2 |  -2.44e-06   1.08e-07   -22.56   0.000    -2.65e-06   -2.23e-06
   ratio |   -12.7366    1.20691   -10.55   0.000     -15.1021    -10.3711
  ratio2 |  -3.896984   .9683817    -4.02   0.000    -5.794977   -1.998991
  medInc |   -.032303   .0027532   -11.73   0.000    -.0376991   -.0269068
 medInc2 |   .0001615   .0000221     7.30   0.000     .0001182    .0002049
      bp |   4.561891    .593008     7.69   0.000     3.399617    5.724166
      hp |   .2206965   .4486256     0.49   0.623    -.6585934    1.099986
      ap |   17.83406   .4669824    38.19   0.000      16.9188    18.74933
     bp2 |  -17.87197    1.63318   -10.94   0.000    -21.07294   -14.67099
     hp2 |  -.8190418    .336224    -2.44   0.015    -1.478029   -.1600549
     ap2 |  -9.240982   .5633857   -16.40   0.000     -10.3452   -8.136766
     wp2 |   .2083493    .307955     0.68   0.499    -.3952314      .81193
  offset |   (offset)
------------------------------------------------------------------------------

         */
        if(a.getRace()==Agent.ASIAN){
            utility = Math.exp( 
            		.0110718*rent + -0.00000244*rent*rent
                    + -12.7366*ratio -3.896984*ratio*ratio
                    -.032303*(neighborhoodMedianIncome/1000) + .0001615*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                    + 4.561891*pctBlackInNeighborhood-17.87197*pctBlackInNeighborhood*pctBlackInNeighborhood
                    +.2206965*pctHispanicInNeighborhood-.8190418*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                    +17.83406*pctAsianInNeighborhood -9.24098*pctAsianInNeighborhood*pctAsianInNeighborhood
                    +.2083493*pctWhiteInNeighborhood);	        		
        }	else if(a.getRace()==Agent.WHITE) {
            
            /* WHITE
------------------------------------------------------------------------------
  choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
 rentgrs |   .0074767   .0001445    51.74   0.000     .0071935    .0077599
rentgrs2 |  -1.41e-06   3.89e-08   -36.35   0.000    -1.49e-06   -1.34e-06
   ratio |   -13.7593   .4554832   -30.21   0.000    -14.65203   -12.86656
  ratio2 |     1.0231   .3539406     2.89   0.004     .3293898    1.716811
  medInc |   -.003181   .0011542    -2.76   0.006    -.0054432   -.0009189
 medInc2 |   6.25e-06   8.06e-06     0.78   0.438    -9.54e-06     .000022
      bp |  -5.212282   .2700135   -19.30   0.000    -5.741499   -4.683066
      hp |  -6.866833   .2752685   -24.95   0.000     -7.40635   -6.327317
      ap |  -6.530174   .2844234   -22.96   0.000    -7.087634   -5.972715
     bp2 |   -6.44866   .4740676   -13.60   0.000    -7.377816   -5.519505
     hp2 |    -.97222   .1807675    -5.38   0.000    -1.326518   -.6179222
     ap2 |   .3025913   .3626553     0.83   0.404    -.4081999    1.013383
     wp2 |  -3.935416   .1647653   -23.88   0.000     -4.25835   -3.612482
  offset |   (offset)
------------------------------------------------------------------------------
             */ 
            utility = Math.exp( 
            		.0074767*rent + -0.00000141*rent*rent
                    + -13.7593*ratio + 1.0231*ratio*ratio
                    + -.003181*(neighborhoodMedianIncome/1000) + 0.00000625*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                    +  -5.212282*pctBlackInNeighborhood-6.44866*pctBlackInNeighborhood*pctBlackInNeighborhood
                    +-6.866833*pctHispanicInNeighborhood-.9722*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                    +-6.530174*pctAsianInNeighborhood+ 0.3025913*pctAsianInNeighborhood*pctAsianInNeighborhood
                    +-3.935416 *pctWhiteInNeighborhood);	        		
        } else if(a.getRace()==Agent.BLACK) {
            /* BLACK
------------------------------------------------------------------------------
  choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
 rentgrs |   .0107934   .0005933    18.19   0.000     .0096306    .0119563
rentgrs2 |  -3.21e-06   2.08e-07   -15.47   0.000    -3.62e-06   -2.81e-06
   ratio |  -6.787745   1.501505    -4.52   0.000    -9.730641   -3.844849
  ratio2 |  -7.148086   1.213444    -5.89   0.000    -9.526392   -4.769779
  medInc |   .0307874   .0055848     5.51   0.000     .0198414    .0417333
 medInc2 |  -.0004037   .0000687    -5.88   0.000    -.0005384   -.0002691
      bp |   12.40578   .3884667    31.94   0.000      11.6444    13.16716
      hp |  -8.841161   .5332843   -16.58   0.000    -9.886379   -7.795943
      ap |  -.6815458   .7164054    -0.95   0.341    -2.085675     .722583
     bp2 |  -13.52152   .3299524   -40.98   0.000    -14.16821   -12.87482
     hp2 |   2.774236   .3639586     7.62   0.000     2.060891    3.487582
     ap2 |  -12.24857   1.314413    -9.32   0.000    -14.82478   -9.672371
     wp2 |  -9.760022   .4668376   -20.91   0.000    -10.67501   -8.845037
  offset |   (offset)
------------------------------------------------------------------------------
             */   
            utility = Math.exp( 
            		.0107934* rent + -0.00000321*rent*rent
                   - 6.787745*ratio  -7.148086*ratio*ratio
                    +   .0307874*(neighborhoodMedianIncome/1000) + -.0004037*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                    + 12.40578*pctBlackInNeighborhood -13.52152*pctBlackInNeighborhood*pctBlackInNeighborhood
                    -8.841161*pctHispanicInNeighborhood + 2.774236*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                    +-.6815458*pctAsianInNeighborhood+-12.24857*pctAsianInNeighborhood*pctAsianInNeighborhood
                    -9.760022*pctWhiteInNeighborhood);
        } else if(a.getRace()==Agent.HISPANIC){
            /* 	 
             HISPANIC
------------------------------------------------------------------------------
  choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
 rentgrs |   .0060138   .0002128    28.26   0.000     .0055967    .0064309
rentgrs2 |  -1.83e-06   8.00e-08   -22.85   0.000    -1.99e-06   -1.67e-06
   ratio |   -6.66967   .5356767   -12.45   0.000    -7.719577   -5.619763
  ratio2 |  -4.824713   .4768441   -10.12   0.000     -5.75931   -3.890116
  medInc |   .0070514    .001799     3.92   0.000     .0035255    .0105773
 medInc2 |  -.0001089   .0000211    -5.17   0.000    -.0001502   -.0000676
      bp |  -1.669998   .1437394   -11.62   0.000    -1.951722   -1.388274
      hp |   2.840491   .2091093    13.58   0.000     2.430645    3.250338
      ap |   -1.09497    .169039    -6.48   0.000     -1.42628   -.7636599
     bp2 |  -.0256678   .1984839    -0.13   0.897     -.414689    .3633534
     hp2 |  -1.432767    .125123   -11.45   0.000    -1.678004   -1.187531
     ap2 |   .4907525   .2263183     2.17   0.030     .0471767    .9343282
     wp2 |  -1.317926   .1455236    -9.06   0.000    -1.603147   -1.032705
  offset |   (offset)
------------------------------------------------------------------------------
             */  
            utility = Math.exp( 
            		 .0060138*rent + -0.00000183*rent*rent
                    + -6.66967*ratio-4.824713*ratio*ratio
                    + .0070514*(neighborhoodMedianIncome/1000)-.0001089*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                    + -1.669998*pctBlackInNeighborhood + -.0256678*pctBlackInNeighborhood*pctBlackInNeighborhood
                    + 2.840491*pctHispanicInNeighborhood-1.432767*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                    + -1.09497*pctAsianInNeighborhood+.490752*pctAsianInNeighborhood*pctAsianInNeighborhood
                    + -1.317926*pctWhiteInNeighborhood);		} 
    } else if(tenure==Agent.OWNER){
        if(a.getRace()==Agent.ASIAN){
            /* ASIAN
------------------------------------------------------------------------------
  choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
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
            		.0041999*rent + - 0.000000447*rent*rent
                    + 14.39926*ratio -30.10154*ratio*ratio
                    -.0006859*(neighborhoodMedianIncome/1000) + .000024*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                    +  1.959622*pctBlackInNeighborhood -14.26577*pctBlackInNeighborhood*pctBlackInNeighborhood
                    + 3.222334*pctHispanicInNeighborhood -3.080838*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                    +13.96843*pctAsianInNeighborhood+ -3.938367*pctAsianInNeighborhood*pctAsianInNeighborhood
                    +-2.579125*pctWhiteInNeighborhood);
        }	else if(a.getRace()==Agent.WHITE) {
            /* WHITE      
------------------------------------------------------------------------------
  choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
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
            		.0018232*rent + -0.000000133*rent*rent
                    + 13.8991*ratio -29.5869*ratio*ratio
                    +.0194022*(neighborhoodMedianIncome/1000) -.0000995*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                    + -.8410856*pctBlackInNeighborhood -13.28789*pctBlackInNeighborhood*pctBlackInNeighborhood
                    + -5.482388*pctHispanicInNeighborhood -3.524827*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                    + -13.8648*pctAsianInNeighborhood+7.737586*pctAsianInNeighborhood*pctAsianInNeighborhood
                    +-5.575379*pctWhiteInNeighborhood);		
        } else if(a.getRace()==a.BLACK) {
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
            		 .0030656*rent + -0.00000107*rent*rent 
                    +96.62042 *ratio -116.256*ratio*ratio
                    + .3118984*(neighborhoodMedianIncome/1000) +-.0028285*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                    + 29.45947*pctBlackInNeighborhood -35.2801*pctBlackInNeighborhood*pctBlackInNeighborhood
                    +-34.94042*pctHispanicInNeighborhood+ 11.04974*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                    + -28.80547*pctAsianInNeighborhood+-.8614868*pctAsianInNeighborhood*pctAsianInNeighborhood
                    +-36.59448*pctWhiteInNeighborhood);						
            } else if(a.getRace()==Agent.HISPANIC){
                        /* 
                         HISP
------------------------------------------------------------------------------
  choice |      Coef.   Std. Err.      z    P>|z|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
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
                        		.0024295*rent + -0.000000793
                                + 21.19439*ratio -25.86543*ratio*ratio
                                + .0552472*(neighborhoodMedianIncome/1000) +  -.0003671*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
                                + 3.691533*pctBlackInNeighborhood  -5.657794*pctBlackInNeighborhood*pctBlackInNeighborhood
                                + 7.29365*pctHispanicInNeighborhood+ -3.340575*pctHispanicInNeighborhood*pctHispanicInNeighborhood
                                + -2.317364*pctAsianInNeighborhood+ 1.637686*pctAsianInNeighborhood*pctAsianInNeighborhood
                                + -1.474916*pctWhiteInNeighborhood);			} 
	} else {
	    System.out.println("no tenure");
	}
    if(utility==0.0){ // Priyasree: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
        utility = 0.00000000000000000001;
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
        return "CensusPumsRaceInc";
    }

}



