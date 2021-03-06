
package mirar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator; // Priyasree_Audit: Unnecessary import: Delete the import.

import cern.colt.list.DoubleArrayList;

/**
 * @author eeb
 *
 */
/*public class ThresholdRace extends AgentDecision {

    public ThresholdRace() {
        super();
    }

        public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
            
            PrintWriter testSelect = null;
            try {
                testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", false));
                
            } catch (IOException ioe) { // Priyasree_Audit: Empty catch clause for exception ioe_Delete the empty catch clause. // Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
                
            }
            
            ArrayList availableUnits = new ArrayList();
            availableUnits.addAll(agent.getPossibleHousingUnitList());

            if (currUnit == null) {
                System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
            }

            DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
            
            double utilityCurrentUnit = computeUtilitityForOwnUnit(currUnit, agent); 

            utilities.add(utilityCurrentUnit);
            availableUnits.add(currUnit);

            int choice = MirarUtils.sampleFromRawDistribution(utilities);
            	
              testSelect.println("choice == " + choice);
              testSelect.println("ThresholdRace#select:  Agent race is " + agent.getRace());
              //testSelect.println("ThresholdRace#select:  current STFID is " + agent.getSTFID());
              for(int i=0; i<utilities.size(); i++){
                  testSelect.println("utility " + i + " from select function is: " + utilities.get(i));
                  double prop=0;
                  if(agent.getRace()==agent.WHITE){
                  	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
                  	prop=this.getPctWhiteInNeighborhood(b);
                  } else if(agent.getRace()==agent.BLACK){
                  	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
                  	prop=this.getPctBlackInNeighborhood(b);
                  } else if(agent.getRace()==agent.HISPANIC){
                  	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
                  	prop=this.getPctHispanicInNeighborhood(b);
                  } else if(agent.getRace()==agent.ASIAN){
                  	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
                  	prop=this.getPctAsianInNeighborhood(b);
                  }
                  testSelect.println("proportion own race in neigh is " + prop);
              }
              //testSelect.println("AgentDecision select availableUnits " + availableUnits.size() + "  utilities: " + utilities.size());
              testSelect.close();
            
            
            if (choice == -1) {
           //     possibleHousingUnitList.clear();
                System.out.println("agent decision randi neighbors: choice is null , choice == " + choice);
                
                return null;
            }
            else {
               // possibleHousingUnitList.clear();
            //    System.out.println("+++++++   AgentDecision select curBlock: " + ((HousingUnit) availableUnits.get(choice)).getBlock().getSTFID());
                return (HousingUnit) availableUnits.get(choice);
            }
        }
        
        public DoubleArrayList computeUtilities(ArrayList housingUnits, Agent agent) {
            DoubleArrayList utilities = new DoubleArrayList();
            ArrayList availableUnits = possibleHousingUnitList;
            Iterator e = availableUnits.iterator();

                int i=0;
                while (e.hasNext()) {
                    //System.out.println("agent decsion: select : i: " + i + "num hu: " + availableBlocks.size());
                   // Block b = (Block)((HousingUnit)e.next()).getBlock();
                    HousingUnit hu = (HousingUnit) e.next();
//                  Block b = (Block) e.next();
                    Block b =hu.getBlock();
                    utilities.add(computeUtility(hu.getBlock(), agent));
//                      Block b = (Block)((HousingUnit) availableBlocks.get(i)).getBlock();
                   
                    i++;
                }
                   return utilities;
        }
        



        public double computeUtility(Block b, Agent a, int tenure) {
            double utility=0.0;
            ArrayList blocks = new ArrayList();
            if (b == null) {
            System.out.println("thresholdrace neighbor computeUtility Block is null");
            }
            else {
                if(a.getRace()==Agent.WHITE) {
                    if(this.getPctWhiteInNeighborhood(b)>=0.20){
                       utility = Math.exp(1);
                    //	utility=1.0;
                    } else if(this.getPctWhiteInNeighborhood(b)<0.20){
                      utility = Math.exp(-100);
                    //	utility=0.0;
                    }
                } else if(a.getRace()==Agent.BLACK){
                    if(this.getPctBlackInNeighborhood(b)>=0.20){
                       utility = Math.exp(1);
                    //	utility = 1.0;
                    } else if(this.getPctBlackInNeighborhood(b)<0.20){
                     utility = Math.exp(-100);
                    //	utility = 0.0;
                    }
                } else if (a.getRace()==Agent.ASIAN){
                    if(this.getPctAsianInNeighborhood(b)>=0.20){
                        utility = Math.exp(1);
                    //	 utility = 1.0;
                    } else if(this.getPctAsianInNeighborhood(b)<0.20){
                       utility = Math.exp(-100);
                    //	 utility = 0.0;
                    }
                }else if (a.getRace()==Agent.HISPANIC) {
                    if(this.getPctHispanicInNeighborhood(b)>=0.20){
                        utility = Math.exp(1);
                    	//utility = 1.0;
                    } else if(this.getPctHispanicInNeighborhood(b)<0.20){
                        utility = Math.exp(-100);
                    	//utility = 0.0;
                    }
                } else {
                    System.out.println("!!!!!! Could not find race !!!!!! ");
                }
            }
          return utility;
        }
        
        public double computeUtilitityForOwnUnit(HousingUnit currUnit, Agent a) {
                Block b = currUnit.getBlock();  //  block; 
                double utility=0.0;
                if(a.getRace()==Agent.WHITE) {
                    if(this.getPctWhiteInNeighborhood(b)>=0.20){
                        utility = Math.exp(1);
                    	//utility = 1.0;
                    } else if(this.getPctWhiteInNeighborhood(b)<0.20){
                        utility = Math.exp(-100);
                    	//utility = 0.0;
                    }
                } else if(a.getRace()==Agent.BLACK){
                    if(this.getPctBlackInNeighborhood(b)>=0.20){
                        utility = Math.exp(1);
                       //utility = 1.0;
                    } else if(this.getPctBlackInNeighborhood(b)<0.20){
                        utility = Math.exp(-100);
                    	//utility = 0.0;
                    }
                } else if (a.getRace()==Agent.ASIAN){
                    if(this.getPctAsianInNeighborhood(b)>=0.20){
                        utility = Math.exp(1);
                    } else if(this.getPctAsianInNeighborhood(b)<0.20){
                        utility = Math.exp(-100);
                    }
                }else if (a.getRace()==Agent.HISPANIC) {
                    if(this.getPctHispanicInNeighborhood(b)>=0.20){
                        utility = Math.exp(1);
                    } else if(this.getPctHispanicInNeighborhood(b)<0.20){
                        utility = Math.exp(-100);
                    }
                } else {
                    System.out.println("!!!!!! Could not find race !!!!!! ");
                }
            return utility; 
        }

            
        public double computeMarginalUtility(Block b, int tenure) {
            double marginalUtility = 0.0;
            if (b == null) {
            System.out.println("raceincome neighbor computeUtility Block is null");
            }
            else {
          //  double medIncome = this.getNeighborhoodMedianIncome(b);
        //  System.out.println("number of agents: " + b.getNumOccupiedHousingUnits());
        //  System.out.println("number of vacant units: " + b.getNumVacantHousingUnits());
        //  System.out.println("RAIN#computeMarginalUtility -  last price was: " + b.getMedianRent() + "  block " + b.getBlockNum());
        //  System.out.println("RAIN#computeMarginalUtility - last pctblack was: " + b.getPctBlack());
        //  System.out.println("RAIN#computeMarginalUtility - last medianInc was: " + b.getMedianIncome());
        //  System.out.println("RAIN#computeMarginalUtility - total number of housing units is: " + b.getHousingUnitList().size());
            marginalUtility = -b.getMedianRent(tenure)/1000 - b.getPctBlack();
            }
            return marginalUtility;
        }

        public double solveForPrice(double marginalUtil, Block b, int tenure) {
            //System.out.println("this is marginalUtil within solve method: " + marginalUtil);
            return -1000*(marginalUtil + b.getPctBlack());
        }

        public String toString() {
            return "ThresholdRace";
        }
}
*/