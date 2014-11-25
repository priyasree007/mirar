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
public class CensusPumsRaceInc extends AgentDecision {

	/**
	 * 
	 */
	public CensusPumsRaceInc() {
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
			      	double ratio=(b.getMedianRent(tenure)*12.0)/a.getIncome();
			   //		ArrayList blocks = new ArrayList();
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
			                 ratio |   30.39265   .5013943    60.62   0.000     29.40994    31.37537
			                 ratio2 |   -41.2187   .6498624   -63.43   0.000     -42.4924   -39.94499
			                 medInc |  -.0398962    .002425   -16.45   0.000    -.0446491   -.0351433
			                 medInc2 |   .0002194   .0000187    11.75   0.000     .0001828     .000256
			                 bp |   5.195075   .5168825    10.05   0.000     4.182004    6.208146
			                 bp2 |  -17.66069   1.306932   -13.51   0.000    -20.22223   -15.09915
			                 hp |   3.108545   .4288997     7.25   0.000     2.267917    3.949173
			                 hp2 |  -2.893353   .3162775    -9.15   0.000    -3.513245    -2.27346
			                 ap |   16.84894   .4396158    38.33   0.000     15.98731    17.71057
			                 ap2 |  -1.391105   .5565787    -2.50   0.012    -2.481979   -.3002307
			                 wp2 |   1.703594   .2881452     5.91   0.000      1.13884    2.268348
			                 offset |   (offset) 
			                 */
			                if(a.getRace()==Agent.ASIAN){
			                    utility = Math.exp( 
			                            30.39265*ratio -41.2187*ratio*ratio
			                            -.0398962*(neighborhoodMedianIncome/1000) +.0002194*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                            + 5.195075*pctBlackInNeighborhood-17.66069*pctBlackInNeighborhood*pctBlackInNeighborhood
			                            +3.108545*pctHispanicInNeighborhood-2.893353*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                            +16.84894*pctAsianInNeighborhood-1.391105 *pctAsianInNeighborhood*pctAsianInNeighborhood
			                            +1.703594*pctWhiteInNeighborhood);	        		
			                }	else if(a.getRace()==Agent.WHITE) {
			                    
			                    /* WHITE
			                     ratio |   16.40714   .1839556    89.19   0.000      16.0466    16.76769
			                     ratio2 |  -22.20756    .238067   -93.28   0.000    -22.67416   -21.74095
			                     medInc |  -.0060357   .0009943    -6.07   0.000    -.0079845   -.0040868
			                     medInc2 |   .0000303   6.91e-06     4.39   0.000     .0000168    .0000439
			                     bp |  -6.122301   .2356718   -25.98   0.000    -6.584209   -5.660393
			                     bp2 |  -5.498796   .4103137   -13.40   0.000    -6.302996   -4.694596
			                     hp |  -7.013763   .2412022   -29.08   0.000    -7.486511   -6.541016
			                     hp2 |   -1.18878   .1585744    -7.50   0.000     -1.49958   -.8779798
			                     ap |  -6.504971   .2488499   -26.14   0.000    -6.992707   -6.017234
			                     ap2 |   .0058741   .3190918     0.02   0.985    -.6195342    .6312825
			                     wp2 |  -4.037964   .1442055   -28.00   0.000    -4.320601   -3.755326
			                     offset |   (offset)
			                     */ 
			                    utility = Math.exp( 
			                            16.40714*ratio -22.20756*ratio*ratio
			                            -.0060357*(neighborhoodMedianIncome/1000) +.0000303*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                            + -6.12230*pctBlackInNeighborhood-5.498796*pctBlackInNeighborhood*pctBlackInNeighborhood
			                            +-7.013763*pctHispanicInNeighborhood-1.18878*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                            +-6.504971*pctAsianInNeighborhood+.0058741*pctAsianInNeighborhood*pctAsianInNeighborhood
			                            +-4.037964 *pctWhiteInNeighborhood);	        		
			                } else if(a.getRace()==Agent.BLACK) {
			                    /* BLACK
			                     ratio |   28.80536   .4571892    63.01   0.000     27.90928    29.70143
			                     ratio2 |  -39.35272   .5636884   -69.81   0.000    -40.45753   -38.24791
			                     medInc |   .0124012   .0047311     2.62   0.009     .0031285     .021674
			                     medInc2 |  -.0002096   .0000581    -3.61   0.000    -.0003234   -.0000958
			                     bp |   14.90247   .3495975    42.63   0.000     14.21727    15.58767
			                     bp2 |  -15.78452   .3031258   -52.07   0.000    -16.37863    -15.1904
			                     hp |  -9.306205   .4794391   -19.41   0.000    -10.24589   -8.366522
			                     hp2 |   2.509415   .3323768     7.55   0.000     1.857968    3.160861
			                     ap |  -1.252509    .614171    -2.04   0.041    -2.456262   -.0487557
			                     ap2 |  -10.45178    1.06885    -9.78   0.000    -12.54669   -8.356873
			                     wp2 |  -10.03043   .4135346   -24.26   0.000    -10.84094   -9.219913
			                     offset |   (offset)
			                     */   
			                    utility = Math.exp( 
			                            28.80536*ratio -39.35272*ratio*ratio
			                            +.0124012*(neighborhoodMedianIncome/1000) -.0002096*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                            + 14.90247*pctBlackInNeighborhood-15.78452*pctBlackInNeighborhood*pctBlackInNeighborhood
			                            -9.306205*pctHispanicInNeighborhood+2.509415*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                            +-1.252509*pctAsianInNeighborhood+-10.45178*pctAsianInNeighborhood*pctAsianInNeighborhood
			                            -10.03043*pctWhiteInNeighborhood);
			                } else if(a.getRace()==Agent.HISPANIC){
			                    /* 	 
			                     HISPANIC
			                     ratio |   10.49675    .146674    71.57   0.000     10.20927    10.78423
			                     ratio2 |  -17.73934   .1879713   -94.37   0.000    -18.10776   -17.37093
			                     medInc |   .0016539   .0015037     1.10   0.271    -.0012933    .0046011
			                     medInc2 |  -.0000782   .0000177    -4.41   0.000     -.000113   -.0000435
			                     bp |  -1.759831   .1222127   -14.40   0.000    -1.999363   -1.520298
			                     bp2 |   .4704964   .1690864     2.78   0.005     .1390931    .8018997
			                     hp |   3.553886   .1805333    19.69   0.000     3.200047    3.907725
			                     hp2 |  -1.733281   .1081743   -16.02   0.000    -1.945299   -1.521263
			                     ap |   .5541233   .1467466     3.78   0.000     .2665051    .8417414
			                     ap2 |   .6330551   .2015418     3.14   0.002     .2380404     1.02807
			                     wp2 |  -.9076131    .125293    -7.24   0.000    -1.153183   -.6620433
			                     offset |   (offset) 
			                     */  
			                    utility = Math.exp( 
			                            10.49675*ratio-17.73934*ratio*ratio
			                            +.0016539*(neighborhoodMedianIncome/1000) -.0000782*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                            +-1.759831*pctBlackInNeighborhood+.4704964*pctBlackInNeighborhood*pctBlackInNeighborhood
			                            +3.553886*pctHispanicInNeighborhood-1.733281*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                            +.5541233*pctAsianInNeighborhood+.6330551*pctAsianInNeighborhood*pctAsianInNeighborhood
			                            +-.9076131  *pctWhiteInNeighborhood);		} 
			            } else if(tenure==Agent.OWNER){
			                if(a.getRace()==Agent.ASIAN){
			                    /* ASIAN
			                     -------------+----------------------------------------------------------------
			                     ratio |    41.4081   .9278486    44.63   0.000     39.58955    43.22665
			                     ratio2 |  -50.79082   1.275268   -39.83   0.000     -53.2903   -48.29134
			                     medInc |   .0003712   .0040314     0.09   0.927    -.0075301    .0082725
			                     medInc2 |    .000021   .0000222     0.94   0.346    -.0000226    .0000646
			                     bp |   1.879937   1.139814     1.65   0.099    -.3540565    4.113931
			                     bp2 |  -14.30295   3.393129    -4.22   0.000    -20.95336   -7.652543
			                     hp |   2.530468   .7788418     3.25   0.001     1.003966     4.05697
			                     hp2 |  -2.654589   .5729554    -4.63   0.000     -3.77756   -1.531617
			                     ap |   13.56877   .8320284    16.31   0.000     11.93802    15.19952
			                     ap2 |   -4.19505   .8097312    -5.18   0.000    -5.782094   -2.608006
			                     wp2 |  -2.539022   .5540852    -4.58   0.000    -3.625009   -1.453035
			                     */
			                    utility = Math.exp( 
			                            41.4081*ratio -50.79082*ratio*ratio
			                            +.0003712*(neighborhoodMedianIncome/1000) +.000021*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                            + 1.879937*pctBlackInNeighborhood-14.30295*pctBlackInNeighborhood*pctBlackInNeighborhood
			                            +2.530468*pctHispanicInNeighborhood -2.654589*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                            +13.56877*pctAsianInNeighborhood+-4.19505*pctAsianInNeighborhood*pctAsianInNeighborhood
			                            +-2.539022*pctWhiteInNeighborhood);
			                }	else if(a.getRace()==Agent.WHITE) {
			                    /* WHITE      
			                     ratio |   31.91786   .3603015    88.59   0.000     31.21168    32.62404
			                     ratio2 |  -45.41915   .5659845   -80.25   0.000    -46.52846   -44.30984
			                     medInc |   .0185751   .0015357    12.10   0.000     .0155651    .0215851
			                     medInc2 |  -.0000874   7.75e-06   -11.28   0.000    -.0001026   -.0000722
			                     bp |  -.6461822   .4921126    -1.31   0.189    -1.610705    .3183408
			                     bp2 |  -13.44504   .8217032   -16.36   0.000    -15.05555   -11.83453
			                     hp |  -5.963862   .5318626   -11.21   0.000    -7.006294    -4.92143
			                     hp2 |   -2.89897   .3730259    -7.77   0.000    -3.630087   -2.167853
			                     ap |  -13.60793   .5083565   -26.77   0.000    -14.60429   -12.61156
			                     ap2 |   7.575933   .5089695    14.88   0.000     6.578371    8.573495
			                     wp2 |  -5.403888   .3033748   -17.81   0.000    -5.998492   -4.809285
			                     offset |   (offset) 
			                     */
			                    utility = Math.exp( 
			                            31.91786*ratio -45.41915*ratio*ratio
			                            +.0185751*(neighborhoodMedianIncome/1000) -.0000874*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                            + -.6461822*pctBlackInNeighborhood-13.44504*pctBlackInNeighborhood*pctBlackInNeighborhood
			                            +-5.963862*pctHispanicInNeighborhood-2.89897*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                            +-13.60793*pctAsianInNeighborhood+7.575933*pctAsianInNeighborhood*pctAsianInNeighborhood
			                            +-5.403888*pctWhiteInNeighborhood);		
			                } else if(a.getRace()==a.BLACK) {
			                    /* 
			                     /*
			                      BLACK     
			                      ratio |   97.51121   2.198777    44.35   0.000     93.20168    101.8207
			                      ratio2 |  -119.2507   3.057823   -39.00   0.000    -125.2439   -113.2575
			                      medInc |   .3355175   .0238136    14.09   0.000     .2888438    .3821912
			                      medInc2 |  -.0030947   .0002205   -14.03   0.000     -.003527   -.0026625
			                      bp |   29.57548   1.566052    18.89   0.000     26.50608    32.64489
			                      bp2 |  -35.62544   1.250575   -28.49   0.000    -38.07652   -33.17435
			                      hp |  -33.75439   2.134514   -15.81   0.000    -37.93796   -29.57082
			                      hp2 |   9.625405   1.481656     6.50   0.000     6.721413     12.5294
			                      ap |  -29.23912   2.322873   -12.59   0.000    -33.79187   -24.68637
			                      ap2 |  -.9898796   3.091281    -0.32   0.749    -7.048679     5.06892
			                      wp2 |  -37.37623   1.869553   -19.99   0.000    -41.04049   -33.71198
			                      offset |   (offset)       
			                      */ 
			                    utility = Math.exp( 
			                            97.51121 *ratio -119.2507*ratio*ratio
			                            +.3355175*(neighborhoodMedianIncome/1000) +-.0030947*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                            +29.57548*pctBlackInNeighborhood-35.62544*pctBlackInNeighborhood*pctBlackInNeighborhood
			                            +-33.75439*pctHispanicInNeighborhood+9.625405*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                            +-29.23912*pctAsianInNeighborhood+-.9898796*pctAsianInNeighborhood*pctAsianInNeighborhood
			                            +-37.37623*pctWhiteInNeighborhood);						
                                } else if(a.getRace()==Agent.HISPANIC){
			                                /* 
			                                 HISP
			                                 ratio |   23.51575   .3568434    65.90   0.000     22.81635    24.21515
			                                 ratio2 |  -28.08815   .4738149   -59.28   0.000    -29.01681   -27.15949
			                                 medInc |   .0595098   .0038286    15.54   0.000      .052006    .0670137
			                                 medInc2 |  -.0004245   .0000339   -12.51   0.000     -.000491    -.000358
			                                 bp |   3.623307   .3500446    10.35   0.000     2.937232    4.309382
			                                 bp2 |  -5.671086   .4814002   -11.78   0.000    -6.614613   -4.727559
			                                 hp |   7.710153   .4970096    15.51   0.000     6.736032    8.684274
			                                 hp2 |  -3.771794   .2923683   -12.90   0.000    -4.344825   -3.198762
			                                 ap |   -2.53052   .4189787    -6.04   0.000    -3.351703   -1.709336
			                                 ap2 |    1.78119   .6060696     2.94   0.003     .5933156    2.969065
			                                 wp2 |  -1.748021   .3308424    -5.28   0.000     -2.39646   -1.099582
			                                 offset |   (offset)   
			                                 */ 
			                                utility = Math.exp( 
			                                        23.51575*ratio -28.08815*ratio*ratio
			                                        -.0060357*(neighborhoodMedianIncome/1000) +-.0004245*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
			                                        + 3.623307*pctBlackInNeighborhood-5.671086*pctBlackInNeighborhood*pctBlackInNeighborhood
			                                        +7.710153*pctHispanicInNeighborhood+-3.771794*pctHispanicInNeighborhood*pctHispanicInNeighborhood
			                                        + -2.53052*pctAsianInNeighborhood+1.78119*pctAsianInNeighborhood*pctAsianInNeighborhood
			                                        +-1.748021*pctWhiteInNeighborhood);			} 
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
			   		
			      	double utility= -12.0;
		        	if(tenure==Agent.RENTER){
		        		/* ASIAN
 ratio |   30.39265   .5013943    60.62   0.000     29.40994    31.37537
  ratio2 |   -41.2187   .6498624   -63.43   0.000     -42.4924   -39.94499
  medInc |  -.0398962    .002425   -16.45   0.000    -.0446491   -.0351433
 medInc2 |   .0002194   .0000187    11.75   0.000     .0001828     .000256
      bp |   5.195075   .5168825    10.05   0.000     4.182004    6.208146
     bp2 |  -17.66069   1.306932   -13.51   0.000    -20.22223   -15.09915
      hp |   3.108545   .4288997     7.25   0.000     2.267917    3.949173
     hp2 |  -2.893353   .3162775    -9.15   0.000    -3.513245    -2.27346
      ap |   16.84894   .4396158    38.33   0.000     15.98731    17.71057
     ap2 |  -1.391105   .5565787    -2.50   0.012    -2.481979   -.3002307
     wp2 |   1.703594   .2881452     5.91   0.000      1.13884    2.268348
  offset |   (offset) 
		        	*/
	       		if(a.getRace()==Agent.ASIAN){
	       		    utility = Math.exp( 
	       		            30.39265*ratio -41.2187*ratio*ratio
	       		            -.0398962*(neighborhoodMedianIncome/1000) +.0002194*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
	       		            + 5.195075*pctBlackInNeighborhood-17.66069*pctBlackInNeighborhood*pctBlackInNeighborhood
	       		            +3.108545*pctHispanicInNeighborhood-2.893353*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       		            +16.84894*pctAsianInNeighborhood-1.391105 *pctAsianInNeighborhood*pctAsianInNeighborhood
	       		            +1.703594*pctWhiteInNeighborhood);	        		
	       		}	else if(a.getRace()==Agent.WHITE) {

	       		    /* WHITE
	       		     ratio |   16.40714   .1839556    89.19   0.000      16.0466    16.76769
	       		     ratio2 |  -22.20756    .238067   -93.28   0.000    -22.67416   -21.74095
	       		     medInc |  -.0060357   .0009943    -6.07   0.000    -.0079845   -.0040868
	       		     medInc2 |   .0000303   6.91e-06     4.39   0.000     .0000168    .0000439
	       		     bp |  -6.122301   .2356718   -25.98   0.000    -6.584209   -5.660393
	       		     bp2 |  -5.498796   .4103137   -13.40   0.000    -6.302996   -4.694596
	       		     hp |  -7.013763   .2412022   -29.08   0.000    -7.486511   -6.541016
	       		     hp2 |   -1.18878   .1585744    -7.50   0.000     -1.49958   -.8779798
	       		     ap |  -6.504971   .2488499   -26.14   0.000    -6.992707   -6.017234
	       		     ap2 |   .0058741   .3190918     0.02   0.985    -.6195342    .6312825
	       		     wp2 |  -4.037964   .1442055   -28.00   0.000    -4.320601   -3.755326
	       		     offset |   (offset)
	       		     */ 
	       		    utility = Math.exp( 
	       		            16.40714*ratio -22.20756*ratio*ratio
	       		            -.0060357*(neighborhoodMedianIncome/1000) +.0000303*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
	       		            + -6.12230*pctBlackInNeighborhood-5.498796*pctBlackInNeighborhood*pctBlackInNeighborhood
	       		            +-7.013763*pctHispanicInNeighborhood-1.18878*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       		            +-6.504971*pctAsianInNeighborhood+.0058741*pctAsianInNeighborhood*pctAsianInNeighborhood
	       		            +-4.037964 *pctWhiteInNeighborhood);	        		
	       		} else if(a.getRace()==Agent.BLACK) {
	       		    /* BLACK
	       		     ratio |   28.80536   .4571892    63.01   0.000     27.90928    29.70143
	       		     ratio2 |  -39.35272   .5636884   -69.81   0.000    -40.45753   -38.24791
	       		     medInc |   .0124012   .0047311     2.62   0.009     .0031285     .021674
	       		     medInc2 |  -.0002096   .0000581    -3.61   0.000    -.0003234   -.0000958
	       		     bp |   14.90247   .3495975    42.63   0.000     14.21727    15.58767
	       		     bp2 |  -15.78452   .3031258   -52.07   0.000    -16.37863    -15.1904
	       		     hp |  -9.306205   .4794391   -19.41   0.000    -10.24589   -8.366522
	       		     hp2 |   2.509415   .3323768     7.55   0.000     1.857968    3.160861
	       		     ap |  -1.252509    .614171    -2.04   0.041    -2.456262   -.0487557
	       		     ap2 |  -10.45178    1.06885    -9.78   0.000    -12.54669   -8.356873
	       		     wp2 |  -10.03043   .4135346   -24.26   0.000    -10.84094   -9.219913
	       		     offset |   (offset)
	       		     */   
	       		    utility = Math.exp( 
	       		            28.80536*ratio -39.35272*ratio*ratio
	       		            +.0124012*(neighborhoodMedianIncome/1000) -.0002096*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
	       		            + 14.90247*pctBlackInNeighborhood-15.78452*pctBlackInNeighborhood*pctBlackInNeighborhood
	       		            -9.306205*pctHispanicInNeighborhood+2.509415*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       		            +-1.252509*pctAsianInNeighborhood+-10.45178*pctAsianInNeighborhood*pctAsianInNeighborhood
	       		            -10.03043*pctWhiteInNeighborhood);
	       		} else if(a.getRace()==Agent.HISPANIC){
	       		    /* 	 
	       		     HISPANIC
	       		     ratio |   10.49675    .146674    71.57   0.000     10.20927    10.78423
	       		     ratio2 |  -17.73934   .1879713   -94.37   0.000    -18.10776   -17.37093
	       		     medInc |   .0016539   .0015037     1.10   0.271    -.0012933    .0046011
	       		     medInc2 |  -.0000782   .0000177    -4.41   0.000     -.000113   -.0000435
	       		     bp |  -1.759831   .1222127   -14.40   0.000    -1.999363   -1.520298
	       		     bp2 |   .4704964   .1690864     2.78   0.005     .1390931    .8018997
	       		     hp |   3.553886   .1805333    19.69   0.000     3.200047    3.907725
	       		     hp2 |  -1.733281   .1081743   -16.02   0.000    -1.945299   -1.521263
	       		     ap |   .5541233   .1467466     3.78   0.000     .2665051    .8417414
	       		     ap2 |   .6330551   .2015418     3.14   0.002     .2380404     1.02807
	       		     wp2 |  -.9076131    .125293    -7.24   0.000    -1.153183   -.6620433
	       		     offset |   (offset) 
	       		     */  
	       		    utility = Math.exp( 
	       		            10.49675*ratio-17.73934*ratio*ratio
	       		            +.0016539*(neighborhoodMedianIncome/1000) -.0000782*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
	       		            +-1.759831*pctBlackInNeighborhood+.4704964*pctBlackInNeighborhood*pctBlackInNeighborhood
	       		            +3.553886*pctHispanicInNeighborhood-1.733281*pctHispanicInNeighborhood*pctHispanicInNeighborhood
	       		            +.5541233*pctAsianInNeighborhood+.6330551*pctAsianInNeighborhood*pctAsianInNeighborhood
	       		            +-.9076131  *this.getPctWhiteInNeighborhood(b));		} 
		        	} else if(tenure==Agent.OWNER){
		        	    if(a.getRace()==Agent.ASIAN){
		        	        /* ASIAN
		        	         -------------+----------------------------------------------------------------
		        	         ratio |    41.4081   .9278486    44.63   0.000     39.58955    43.22665
		        	         ratio2 |  -50.79082   1.275268   -39.83   0.000     -53.2903   -48.29134
		        	         medInc |   .0003712   .0040314     0.09   0.927    -.0075301    .0082725
		        	         medInc2 |    .000021   .0000222     0.94   0.346    -.0000226    .0000646
		        	         bp |   1.879937   1.139814     1.65   0.099    -.3540565    4.113931
		        	         bp2 |  -14.30295   3.393129    -4.22   0.000    -20.95336   -7.652543
		        	         hp |   2.530468   .7788418     3.25   0.001     1.003966     4.05697
		        	         hp2 |  -2.654589   .5729554    -4.63   0.000     -3.77756   -1.531617
		        	         ap |   13.56877   .8320284    16.31   0.000     11.93802    15.19952
		        	         ap2 |   -4.19505   .8097312    -5.18   0.000    -5.782094   -2.608006
		        	         wp2 |  -2.539022   .5540852    -4.58   0.000    -3.625009   -1.453035
		        	         */
		        	        utility = Math.exp( 
		        	                41.4081*ratio -50.79082*ratio*ratio
		        	                +.0003712*(neighborhoodMedianIncome/1000) +.000021*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
		        	                + 1.879937*pctBlackInNeighborhood-14.30295*pctBlackInNeighborhood*pctBlackInNeighborhood
		        	                +2.530468*pctHispanicInNeighborhood -2.654589*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		        	                +13.56877*pctAsianInNeighborhood+-4.19505*pctAsianInNeighborhood*pctAsianInNeighborhood
		        	                +-2.539022*pctWhiteInNeighborhood);
		        	    }	else if(a.getRace()==Agent.WHITE) {
		        	        /* WHITE      
		        	         ratio |   31.91786   .3603015    88.59   0.000     31.21168    32.62404
		        	         ratio2 |  -45.41915   .5659845   -80.25   0.000    -46.52846   -44.30984
		        	         medInc |   .0185751   .0015357    12.10   0.000     .0155651    .0215851
		        	         medInc2 |  -.0000874   7.75e-06   -11.28   0.000    -.0001026   -.0000722
		        	         bp |  -.6461822   .4921126    -1.31   0.189    -1.610705    .3183408
		        	         bp2 |  -13.44504   .8217032   -16.36   0.000    -15.05555   -11.83453
		        	         hp |  -5.963862   .5318626   -11.21   0.000    -7.006294    -4.92143
		        	         hp2 |   -2.89897   .3730259    -7.77   0.000    -3.630087   -2.167853
		        	         ap |  -13.60793   .5083565   -26.77   0.000    -14.60429   -12.61156
		        	         ap2 |   7.575933   .5089695    14.88   0.000     6.578371    8.573495
		        	         wp2 |  -5.403888   .3033748   -17.81   0.000    -5.998492   -4.809285
		        	         offset |   (offset) 
		        	         */
		        	        utility = Math.exp( 
		        	                31.91786*ratio -45.41915*ratio*ratio
		        	                +.0185751*(neighborhoodMedianIncome/1000) -.0000874*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
		        	                + -.6461822*pctBlackInNeighborhood-13.44504*pctBlackInNeighborhood*pctBlackInNeighborhood
		        	                +-5.963862*pctHispanicInNeighborhood-2.89897*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		        	                +-13.60793*pctAsianInNeighborhood+7.575933*pctAsianInNeighborhood*pctAsianInNeighborhood
		        	                +-5.403888*pctWhiteInNeighborhood);		
		        	    } else if(a.getRace()==Agent.BLACK) {
		        	        /* 
		        	         /*
		        	          BLACK     
		        	          ratio |   97.51121   2.198777    44.35   0.000     93.20168    101.8207
		        	          ratio2 |  -119.2507   3.057823   -39.00   0.000    -125.2439   -113.2575
		        	          medInc |   .3355175   .0238136    14.09   0.000     .2888438    .3821912
		        	          medInc2 |  -.0030947   .0002205   -14.03   0.000     -.003527   -.0026625
		        	          bp |   29.57548   1.566052    18.89   0.000     26.50608    32.64489
		        	          bp2 |  -35.62544   1.250575   -28.49   0.000    -38.07652   -33.17435
		        	          hp |  -33.75439   2.134514   -15.81   0.000    -37.93796   -29.57082
		        	          hp2 |   9.625405   1.481656     6.50   0.000     6.721413     12.5294
		        	          ap |  -29.23912   2.322873   -12.59   0.000    -33.79187   -24.68637
		        	          ap2 |  -.9898796   3.091281    -0.32   0.749    -7.048679     5.06892
		        	          wp2 |  -37.37623   1.869553   -19.99   0.000    -41.04049   -33.71198
		        	          offset |   (offset)       
		        	          */ 
		        	        utility = Math.exp( 
		        	                97.51121 *ratio -119.2507*ratio*ratio
		        	                +.3355175*(neighborhoodMedianIncome/1000) +-.0030947*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
		        	                +29.57548*pctBlackInNeighborhood-35.62544*pctBlackInNeighborhood*pctBlackInNeighborhood
		        	                +-33.75439*pctHispanicInNeighborhood+9.625405*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		        	                +-29.23912*pctAsianInNeighborhood+-.9898796*pctAsianInNeighborhood*pctAsianInNeighborhood
		        	                +-37.37623*pctWhiteInNeighborhood);				
                            } else if(a.getRace()==Agent.HISPANIC){
		        	                    /* 
		        	                     HISP
		        	                     ratio |   23.51575   .3568434    65.90   0.000     22.81635    24.21515
		        	                     ratio2 |  -28.08815   .4738149   -59.28   0.000    -29.01681   -27.15949
		        	                     medInc |   .0595098   .0038286    15.54   0.000      .052006    .0670137
		        	                     medInc2 |  -.0004245   .0000339   -12.51   0.000     -.000491    -.000358
		        	                     bp |   3.623307   .3500446    10.35   0.000     2.937232    4.309382
		        	                     bp2 |  -5.671086   .4814002   -11.78   0.000    -6.614613   -4.727559
		        	                     hp |   7.710153   .4970096    15.51   0.000     6.736032    8.684274
		        	                     hp2 |  -3.771794   .2923683   -12.90   0.000    -4.344825   -3.198762
		        	                     ap |   -2.53052   .4189787    -6.04   0.000    -3.351703   -1.709336
		        	                     ap2 |    1.78119   .6060696     2.94   0.003     .5933156    2.969065
		        	                     wp2 |  -1.748021   .3308424    -5.28   0.000     -2.39646   -1.099582
		        	                     offset |   (offset)   
		        	                     */ 
		        	                    utility = Math.exp( 
		        	                            23.51575*ratio -28.08815*ratio*ratio
		        	                            -.0060357*(neighborhoodMedianIncome/1000) +-.0004245*(neighborhoodMedianIncome/1000)*(neighborhoodMedianIncome/1000)        		
		        	                            + 3.623307*pctBlackInNeighborhood-5.671086*pctBlackInNeighborhood*pctBlackInNeighborhood
		        	                            +7.710153*pctHispanicInNeighborhood+-3.771794*pctHispanicInNeighborhood*pctHispanicInNeighborhood
		        	                            + -2.53052*pctAsianInNeighborhood+1.78119*pctAsianInNeighborhood*pctAsianInNeighborhood
		        	                            +-1.748021*pctWhiteInNeighborhood);			} 
		        	} else {
		        	    System.out.println("no tenure");
		        	}
                    if(utility==0.0){ // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
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

	

