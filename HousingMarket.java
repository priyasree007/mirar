/*
 *  @author Robert Najlis
 *  Created on Apr 14, 2004
 *
 *
 */
package mirar;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//import com.ibm.media.content.application.mvr.Handler;  // what is this for???? does not seem to be used
import cern.colt.list.*;
import cern.jet.stat.*;
import uchicago.src.sim.util.Random;
/**
 * This class updates the housing unit prices in the model. There are three ways the prices can be updated:
 *
 * (1) computeHomogeneousRentsWithinBlocks
 *
 * This method uses a "mechanical" method to update rents as a function of neighborhood income. Prices are constant within blocks.
 *
 * (2) computeHeterogeneousRentsWithinBlocks
 *
 * This method uses a "mechanical" method to update rents as a function of neighborhood income. Prices vary within blocks.
 *
 * (3) computeMarketClearingRents
 *
 * This method uses the market clearing model developed in Bayer 2004 to compute prices such that markets "clear." When prices are set such that markets clear, agents are indifferent to all housing units within neighborhoods.
 */
public class HousingMarket {
    private static HousingMarket instance = new HousingMarket();
    private HousingMarket() {
   super();

    }
    public static HousingMarket getInstance() {
      return instance;
    }


    /** This function returns a list of all agents' incomes in the target block's neighborhood.
     *
     * @param b block
     * @return list of incomes of agents in neighborhood
     */

    public DoubleArrayList getIncomeDistribution(Block b) {
       // int numBlocks = blocks.size();
        ArrayList neighborhood = new ArrayList();
        DoubleArrayList incomeDistribution = new DoubleArrayList();
       // Block b = new Block(0);
        //compute rent for each block
        //for (int i=0; i<numBlocks; i++) {
          //  b = (Block)blocks.get(i);

            neighborhood.addAll(b.getNeighbors());
/*           try {
                PrintWriter neighborsFromCode = new PrintWriter(new FileOutputStream("neighborsFromCode.txt", true));
               Iterator iter = neighborhood.iterator();
               while (iter.hasNext()) {
                   neighborsFromCode.println(b.getSTFID() + ", " + ((Block) iter.next()).getSTFID());
               }
               neighborsFromCode.close();
            } catch (IOException ioe) {

            }
  */
            neighborhood.add(b);

            int numNeighbors = neighborhood.size();
           // System.out.println("-- >    HousingMarket#getIncomeDistribution: neighborhood size:  " + numNeighbors);
            for (int j=0; j<numNeighbors; j++) {
                incomeDistribution.addAllOf( ((Block)neighborhood.get(j)).getIncomeDistribution());
            }
            neighborhood.clear();
            return incomeDistribution;
    }

    /** Function to mechanically compute a rent associated with a given income using the following equation:
     
     * @param income input income
     * @return rent
     */
    public double computePricesAtlanta(double bp, double hp, double ap, 
    			double wp, double medInc) {
        /*
         * * hedonic regression for ATLANTA
------------------------------------------------------------------------------
   lnprice50 |      Coef.   Std. Err.      t    P>|t|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
        bp00 |   1.231207   .0127777    96.36   0.000     1.206163    1.256251
        ap00 |   .1594388   .0263848     6.04   0.000     .1077252    .2111524
        hp00 |   .4857871   .0121716    39.91   0.000     .4619311    .5096432
      bp00_2 |  -.7006716   .0069069  -101.44   0.000     -.714209   -.6871342
      wp00_2 |   .4933503   .0077147    63.95   0.000     .4782296     .508471
      hp00_2 |   .1060546   .0169909     6.24   0.000      .072753    .1393563
      ap00_2 |   6.054856   .1662293    36.42   0.000     5.729051    6.380662
      medinc |   .0381996   .0000614   622.40   0.000     .0380794    .0383199
     medinc2 |  -.0002189   5.56e-07  -393.90   0.000      -.00022   -.0002178
     medinc3 |   4.50e-07   1.47e-09   305.67   0.000     4.47e-07    4.53e-07
       _cons |   4.738236   .0076737   617.47   0.000     4.723196    4.753276
   
          */
    	return Math.exp(1.231207*bp + .4857871*hp + .1594388*ap 
    			+ -.7006716*bp*bp + .1060546*hp*hp + 6.054856*ap*ap + .4933503*wp*wp
    			+ .0381996*(medInc/1000) -.0002189*(medInc/1000)*(medInc/1000) + 0.000000450*(medInc/1000)*(medInc/1000)*(medInc/1000)
    			+ 4.738236) ;
    }
    
    public double computePricesLosAngeles(double bp, double hp, double ap, double wp,
    			double medInc){
/*
	* hedonic regression for LA
------------------------------------------------------------------------------
   lnprice50 |      Coef.   Std. Err.      t    P>|t|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
        bp00 |  -.3034688   .0021211  -143.07   0.000     -.307626   -.2993116
        ap00 |  -.3715374   .0026839  -138.43   0.000    -.3767978    -.366277
        hp00 |   .0765532   .0025119    30.48   0.000       .07163    .0814764
      bp00_2 |   .2902815   .0024994   116.14   0.000     .2853829    .2951802
      wp00_2 |  -.3077938   .0016785  -183.37   0.000    -.3110836    -.304504
      hp00_2 |   -.211079   .0017674  -119.43   0.000    -.2145431   -.2076149
      ap00_2 |   .4218875   .0033985   124.14   0.000     .4152266    .4285483
      medinc |   .0267126   .0000169  1581.31   0.000     .0266795    .0267458
     medinc2 |  -.0000958   1.32e-07  -727.42   0.000    -.0000961   -.0000955
     medinc3 |   1.19e-07   2.82e-10   420.69   0.000     1.18e-07    1.19e-07
       _cons |   5.834416   .0014924  3909.49   0.000     5.831491    5.837341

*/
    	return Math.exp(-.3034688*bp + .0765532*hp -.3715374*ap 
    			+ .2902815*bp*bp + -.211079*hp*hp + .4218875*ap*ap +  -.3077938*wp*wp
    			+ .0267126*(medInc/1000) -.0000958*(medInc/1000)*(medInc/1000) + 0.000000119*(medInc/1000)*(medInc/1000)*(medInc/1000)
    			+ 5.834416) ;
      	}
    
    public double computePricesChicago(double bp, double hp, double ap, double wp,
			double medInc){
/*
 * hedonic regression for chicago
------------------------------------------------------------------------------
   lnprice50 |      Coef.   Std. Err.      t    P>|t|     [95% Conf. Interval]
-------------+----------------------------------------------------------------
        bp00 |  -.4367199   .0053775   -81.21   0.000    -.4472595   -.4261802
        ap00 |  -.6281621   .0072857   -86.22   0.000    -.6424417   -.6138825
        hp00 |   -.223488   .0060883   -36.71   0.000    -.2354209   -.2115551
      bp00_2 |   .1555982   .0028918    53.81   0.000     .1499303    .1612661
      wp00_2 |  -.1269455   .0036164   -35.10   0.000    -.1340336   -.1198574
      hp00_2 |  -.1436268   .0039299   -36.55   0.000    -.1513293   -.1359243
      ap00_2 |   .3865273   .0098091    39.40   0.000     .3673017    .4057529
      medinc |    .019657   .0000204   964.39   0.000      .019617    .0196969
     medinc2 |  -.0000483   8.29e-08  -583.10   0.000    -.0000485   -.0000482
       _cons |    6.02827   .0034885  1728.06   0.000     6.021433    6.035107
------------------------------------------------------------------------------

 */
	return Math.exp(-.4367199*bp + -.6281621*ap -.223488*hp 
			+ .155598*bp*bp + -.1436268*hp*hp + .3865273*ap*ap +  -.1269455*wp*wp
			+ .019657*(medInc/1000)  -.0000483*(medInc/1000)*(medInc/1000) 
			+ 6.02827) ;
  	}

    /** This function computes a rent for each block (all housing units within the same block are assigned the same rent).
     *
     *
     * @param blocks list of blocks to compute homogeneous rents for
     * @param tenure TODO
     */
    public void computeHomogeneousRentWithinBlock(ArrayList blocks, int tenure) {
       // compute Rent for each block - each housing unit has the same rent
      //  PrintWriter homoRent = null;
    /*    try {
             homoRent = new PrintWriter(new FileOutputStream("homoRent.txt", true));
             homoRent.println("step num: " + MirarUtils.STEP_NUM );
        } catch (IOException ioe) {

        }
      
        */
        int numBlocks = blocks.size();
        ArrayList neighborhood = new ArrayList();
        DoubleArrayList incomeDistribution = new DoubleArrayList();
        double inc;
        double bk, h, a, w;
        Block b = new Block(0);
        ArrayList housingUnits = new ArrayList();
        //compute rent for each block
        for (int i=0; i<numBlocks; i++) {
            
            b = (Block)blocks.get(i);
            inc=b.getNeighborhoodMedianIncome();
            bk=b.getPctBlkInNeighborhood();
            w=b.getPctWhtInNeighborhood();
            h=b.getPctHispInNeighborhood();
            a=b.getPctAsnInNeighborhood();
            
            double rent=0; 

            if(tenure==Agent.OWNER) {
            	if(MirarUtils.CITY.equalsIgnoreCase("LosAngeles")) {           	
            		rent = computePricesLosAngeles(bk, h, a, w, inc);
            	} else if(MirarUtils.CITY.equalsIgnoreCase("Atlanta")) {
            		rent = computePricesAtlanta(bk, h, a, w, inc);	
            	} else if(MirarUtils.CITY.equalsIgnoreCase("Chicago")){
            		rent = computePricesChicago(bk, h, a, w, inc);
            	}  else {
                	System.out.println("HousingMarket: TENURE NOT FOUND");
                }
            } else if (tenure==Agent.RENTER) {
            	if(MirarUtils.CITY.equalsIgnoreCase("LosAngeles")) {           	
            		rent = computePricesLosAngeles(bk, h, a, w, inc);
            	} else if(MirarUtils.CITY.equalsIgnoreCase("Atlanta")) {
            		rent = computePricesAtlanta(bk, h, a, w, inc);	
            	} else if(MirarUtils.CITY.equalsIgnoreCase("Chicago")){
            		rent = computePricesChicago(bk, h, a, w, inc);
            	}  else {
                	System.out.println("HousingMarket: TENURE NOT FOUND");
                }
            }
           
            // set the rent in each vacant housing unit in the block
            
            b.setRentListByBlock(rent, tenure);

            /*
            //%%%%%%%%%%
            // print test info
            //%%%%%%%%%%%%

            homoRent.println("Block STFID: " + b.getSTFID() );

            if (incomeDistribution.size() > 0) {
                for (int index=0; index< incomeDistribution.size()-1; index++) {
                    homoRent.print(incomeDistribution.get(index) + ", ");
                }
                homoRent.print("" + incomeDistribution.get(incomeDistribution.size()-1));
                homoRent.println();
            }
                homoRent.println(MirarUtils.HOMOGENEOUS_RENT_QUANTILE + "");
                homoRent.println(income + "");
                homoRent.println(rent + "");

                homoRent.close();
                //   } catch (IOException ioe) {

            //}
*/
            // clean up
            neighborhood.clear();
            housingUnits.clear();
        }
     }



    /** This function computes a rent for housing unit within each block. These rents are based on quantiles of the income distribution in the neighborbhood of the target block.
     *
     * @param blocks list of blocks to compute heterogeneous rents for
     * @param tenure TODO
     */

    public void computeHeterogeneousRentWithinBlock(ArrayList blocks, int tenure) {
/*
         PrintWriter heteroRent = null;
        try {
            heteroRent = new PrintWriter(new FileOutputStream("heteroRent.txt", true));
            heteroRent.println("Step Num: " + MirarUtils.STEP_NUM );
        } catch (IOException ioe) {

        }
  */ 
        int numBlocks = blocks.size();
        ArrayList neighborhood = new ArrayList();
        DoubleArrayList incomeDistribution = new DoubleArrayList();
        Block b = new Block(0);
        ArrayList housingUnits = new ArrayList();
        //compute rent for each block
        for (int i=0; i<numBlocks; i++) {
            b = (Block)blocks.get(i);
            neighborhood.add(b);
            neighborhood.addAll(b.getNeighbors());
            int numNeighbors = neighborhood.size();
            for (int j=0; j<numNeighbors; j++) {
                incomeDistribution.addAllOf( ((Block)neighborhood.get(j)).getIncomeDistribution());
            }
            incomeDistribution.sort();



            //System.out.println("incom Distro Block " + b.getSTFID());
            //for (int m=0; m<incomeDistribution.size(); m++) {
            //    System.out.print(incomeDistribution.get(m) + " ");
            //}
            //System.out.println("");
            // get quantile
            double [] qP = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 };
            DoubleArrayList quantilePoints = new DoubleArrayList(qP);
            DoubleArrayList incomes = Descriptive.quantiles(incomeDistribution, quantilePoints);
            //System.out.println("Housing market compute homo rent - block: " + b.getSTFID() + " income: " + income );
            //double rent = (444.13 + 9.647*(income) - 0.03*(income*income));
           // System.out.println("Housing market compute hetero - quantilePoints size " + quantilePoints.size());
            DoubleArrayList rawRents = new DoubleArrayList(quantilePoints.size());
            //set raw rents based on rent function
           // System.out.println("Housing market compute hetero - raw rents size " + rawRents.size());
           double bk,w,h,as;
            bk=b.getPctBlkInNeighborhood();
            w=b.getPctWhtInNeighborhood();
            h=b.getPctHispInNeighborhood();
            as=b.getPctAsnInNeighborhood();
             
           
            for (int n=0; n<quantilePoints.size(); n++) {
           //     System.out.println("Housing market compute hetero - raw rents");
                double rent= -10; 
                if(tenure==Agent.OWNER) {
                 rent = computePricesLosAngeles(bk, h, as, w, incomes.get(n));
                } else if (tenure==Agent.RENTER) {
                 rent = computePricesLosAngeles(bk, h, as, w, incomes.get(n));
                } else {
                	System.out.println("HM: TENURE NOT FOUND");
                }
                rawRents.add(rent);
            //    System.out.println("Housing market compute hetero - raw rents " + rent);
            }
            DoubleArrayList rents = new DoubleArrayList((rawRents.size() -1));
           // System.out.println("Housing market compute hetero -  rents size " + rents.size() );
            int numRents = rawRents.size() -1;
            for (int p=0; p<numRents; p++) {
                //System.out.println("Housing market compute hetero -  rents");
                double rent = ((rawRents.get(p) + rawRents.get(p+1))/2);
                //double rent = (rawRents.get(p) + (rawRents.get(p+1)/2));
                rents.add(rent);
            }

            // Randomly choose 1 rent for each housing unit in the block
            // set the rent in each vacant housing unit in the block
            //housingUnits = b.getHousingUnitList();
          housingUnits.addAll(b.getHousingUnitsByTenure(tenure));
            //  .getVacantHousingUnits(); get ALL housing units, not just vacant ones
          //  System.out.println("Housing market compute hetero rent - block: " + b.getSTFID() + " num vacant units: " + b.getVacantHousingUnitList().size());
            Agent a = new Agent(0);
            //System.out.println("Housing market compute homo rent - block: " + b.getSTFID() + " rent: " + rent );
            int numUnits = housingUnits.size();
            for (int k=0; k<numUnits; k++) { // set rents for occupied units
                if(((HousingUnit)housingUnits.get(k)).isOccupied()){
                    a = (Agent) ((HousingUnit)housingUnits.get(k)).getAgent() ; //Priyasree: Unnecessary type cast to Agent_Delete the unnecessary cast.
                    int incomePoints = incomes.size()-1; // 11 points but 10 rents
                    for(int p=1; p<incomePoints; p++) { // set agent's rent to fall within same decile as agent's income in neighborhood distribution
                        if(a.getIncome()> incomes.get(p-1) & a.getIncome()<=incomes.get(p)){
                            ((HousingUnit)housingUnits.get(k)).setRent(rents.get(p));
                        }
                    }
                }
                else { // set rents for vacant units
                    int choice = Random.uniform.nextIntFromTo(0, rents.size()-1);
                ((HousingUnit)housingUnits.get(k)).setRent(rents.get(choice));
                }
            //    System.out.println("Housing market compute hetero rent - block: " + b.getSTFID() + "quantile: " + choice + " rent: " + rent );

            }
         
            //@@@@@@@@@@@@
            // print test info
            //################
             /*
            // print block Stfid
            heteroRent.println("Block STFID: " + b.getSTFID());
            heteroRent.println("quantile .3 is " + b.getRentQuantile(.3));
            heteroRent.println("quantile .6 is " + b.getRentQuantile(.6));
            heteroRent.println("quantile .9 is " + b.getRentQuantile(.9));
            heteroRent.println("quantile .5 is " + b.getRentQuantile(.5));
            heteroRent.println("quantile .4 is " + b.getRentQuantile(.4));
            if (incomeDistribution.size() > 0) {
                // print income distro
                for (int index=0; index< incomeDistribution.size()-1; index++) {
                    heteroRent.print(incomeDistribution.get(index) + ", ");
                }

                heteroRent.print("" + incomeDistribution.get(incomeDistribution.size()-1));
                heteroRent.println();
            }
            if (incomes.size() > 0) {
                //print incomes
                for (int index=0; index< incomes.size()-1; index++) {
                    heteroRent.print(incomes.get(index) + ", ");
                }
                heteroRent.print("" + incomes.get(incomes.size()-1));
                heteroRent.println();
            }
            //print rawrents
            if (rawRents.size() > 0) {
                for (int index=0; index< rawRents.size()-1; index++) {
                    heteroRent.print(rawRents.get(index) + ", ");
                }
                heteroRent.print("" + rawRents.get(rawRents.size()-1));
                heteroRent.println();
            }
            // print rents
            if (rents.size() > 0) {
                for (int index=0; index< rents.size()-1; index++) {
                    heteroRent.print(rents.get(index) + ", ");
                }
                heteroRent.print("" + rents.get(rents.size()-1));
                heteroRent.println();
            }
            // print rents for occupied housing units
            ArrayList occupiedHousingUnits = new ArrayList();
            occupiedHousingUnits.addAll(b.getOccupiedHousingUnitList());
            if (occupiedHousingUnits.size() > 0) {
                for (int index=0; index<occupiedHousingUnits.size()-1;index++) {
                    heteroRent.print(((HousingUnit)occupiedHousingUnits.get(index)).getRent() + ", ");
                }

                heteroRent.print("" + ((HousingUnit)occupiedHousingUnits.get(occupiedHousingUnits.size()-1)).getRent());

                heteroRent.println();
            }
//          print rents for vacant housing units
            ArrayList vacantHousingUnits = new ArrayList();
            vacantHousingUnits.addAll(b.getVacantHousingUnits());
            if (vacantHousingUnits.size() > 0) {
                for (int index=0; index<vacantHousingUnits.size()-1;index++) {
                    heteroRent.print(((HousingUnit)vacantHousingUnits.get(index)).getRent() + ", ");
                }

                heteroRent.print("" + ((HousingUnit)vacantHousingUnits.get(vacantHousingUnits.size()-1)).getRent());

                heteroRent.println();
            }
  
          
            vacantHousingUnits.clear();
            occupiedHousingUnits.clear();
            // clean up
          */  
             
            neighborhood.clear();
            housingUnits.clear();
        }
        //heteroRent.close();
        
    }

    /** This function computes the transition probabilities for each agent type associated with each block. The function:
     *
     * for each agent type
     *  1. creates a list of utilities associated with all blocks for that agent type
     *  2. utilities ==> probabilities; set the probability value into the slot for the appropriate agent type in the block
     *  3. go on to the next agent
     *
     * Note that we multiply the utility associated with each block by the number
     * of housing units in that block in order to allow areas with more housing
     * units to have a higher probabiliity of in-migration.
     * @param tenure TODO
     */
    public void computeTransitionProbabilities(int tenure) {
        
     ArrayList testAgents;
     if (tenure == Agent.RENTER) {
         testAgents = AgentHandler.getInstance().getRenterTestAgents();
     }
     else {
         testAgents = AgentHandler.getInstance().getOwnerTestAgents();
     }
     
   //System.out.println("this is number of test agents " + testAgents.size());
                // for each block compute list of utilities
        Iterator blocksIter = MirarUtils.BLOCKS .iterator();
        DoubleArrayList probabilities = new DoubleArrayList();
        DoubleArrayList utilities = new DoubleArrayList();

        //int numBlocks = blocks.size();
        Iterator agentsIter = testAgents.iterator();

        while (agentsIter.hasNext()) { // looping over agent types
            Agent agent = (Agent)agentsIter.next();
            // compute utilites list for all blocks
            int index =0;
            Iterator blockIter = MirarUtils.BLOCKS.iterator();
            while (blockIter.hasNext() ) {
                Block b = (Block)blockIter.next();
//                double totHU = b.getHousingUnitList().size();
   
                // changing this for tenure
                double totHU = b.getHousingUnitsByTenure(tenure).size();
                utilities.add(agent.computeUtility((Block)MirarUtils.BLOCKS.get(index), tenure)*totHU);
                index++;
            }

            //convert utilities to probabilities
            probabilities = MirarUtils.convertToProbabilities(utilities);
        
            int numProbs = probabilities.size();
            int numAgents = AgentHandler.getInstance().getNumAgentsOfRaceIncomeType(agent.getRaceIncomeType(), tenure);
            for (int i=0; i<numProbs; i++) {
            	probabilities.set(i, (probabilities.get(i)*numAgents));
                  }
           Iterator bIter = MirarUtils.BLOCKS.iterator();
            int bIndex = 0;
            while (bIter.hasNext() ) {
            	((Block)bIter.next()).setTransitionProbability(agent, probabilities.get(bIndex));
            	bIndex++;
            }
            utilities.clear();
            probabilities.clear();
        } // ~: end agents iteration

    }



    /** This function iterates through the following steps to update housing prices:
     *
     * (1) compute the expected number of agents moving into each block (sum over the expected number of agents of each type, within blocks)
     *
     * (2) compute the marginal utility associated with moving to each block (this value is the same for all agent types)
     *
     * (3) compares the expected number of agents to the block capacity (total number of housing units in block/average occupancy rate in city) and uses Bayer's contraction to adjust marginal utility accordingly
     *
     * (4) solve for new prices
     *
     * (5) repeat steps until prices converge
     *
     *
     * Note: since utilities are currently computed for each block (so all housing units within the block have the same utility), market clearing prices are computed over blocks as well (because all housing units within each block will have the same price).
     */
    public void computeMarketClearingRent(int tenure){
        
        try {
            PrintWriter HMOut = new PrintWriter(new FileOutputStream("housingMarket.txt", true));
            //     blockOut.println("race income rent (rir) num,  numBlocks,  (rirNum/numBlocks),   rirValue");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        double totalOccupancyRate = (AgentHandler.getInstance().getRenterAgentList().size()*1.0)/(MirarUtils.NUM_HOUSING_UNITS*1.0);
    
        System.out.println("-----   MirarUtils.NUM_HOUSING_UNITS:  " + MirarUtils.NUM_HOUSING_UNITS);
       double difference = 7.0;
       int numBlocks=MirarUtils.BLOCKS.size();
        Block b = new Block();
        Agent a = new Agent();
        // random agent, used for computing AgentDecision functions that don't depend on specific race and income
        double updateDelta=0.0;
        double marginalUtil = 0.0;
        double price = 0.0;
        int iter = 0;
        
  while(difference>0.001) {
        //for(int i=0;i<5;i++) {
        System.out.println("------ HM#computeMCR:  Iterations within Market Clearing Rent Update Loop: ------ :" + iter);

        difference = 0.0; // set back to zero
            computeTransitionProbabilities(tenure);
  double sumTot=0.0;
            
                Iterator blockIter = MirarUtils.BLOCKS.iterator();
                while (blockIter.hasNext() ) {
        
                b = (Block) blockIter.next();
                sumTot=sumTot + b.sumExpectedNumAgents(tenure);
                if(b.getHousingUnitList_Renters().size()!=0) {
                double nHat = b.computeOffset(totalOccupancyRate, tenure);
        
                marginalUtil = a.computeMarginalUtility(b, tenure); // check to make sure this is right
     
                updateDelta=marginalUtil - Math.log(nHat);
              // System.out.println("old marginalUtil is: " + marginalUtil + " and new MarginalUtil is: " + updateDelta);
        price = a.computePrice(updateDelta, b, tenure); // solve for new prices
       // System.out.println("HM#computeMCR:  this is the new price for housing unit: " + price);
        
        
        b.setRentListByBlock(price, tenure); // assign updated prices to housing units
        difference=+ Math.abs(marginalUtil-updateDelta);
           }
                }
             //   System.out.println("this is total sum over expected agents " + sumTot);
                //        System.out.println("HM#computeMCR:  this is the difference: " + difference);
            iter = iter+1; // number of iterations to convergence
      
    }

    }

}

