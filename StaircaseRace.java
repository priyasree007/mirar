
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
public class StaircaseRace extends AgentDecision {

	public StaircaseRace() {
		super();
		// TODO Auto-generated constructor stub
	}

    public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
        
        PrintWriter testSelect = null;
        try {
            testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", false));
            
        } catch (IOException ioe) { // Priyasree: Empty catch clause for exception ioe_Delete the empty catch clause. // Priyasree: Caught exception not logged_Use one of the logging methods to log the exception.
            
        }
        
        
        
      //  this.possibleHousingUnitList.addAll(possibleHousingUnitList);
        
        ArrayList availableUnits = new ArrayList();
        availableUnits.addAll(agent.getPossibleHousingUnitList());

        if (currUnit == null) {
            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
        }
        //System.out.println("-------   AgentDecision select currBlock: " + currUnit.getBlock().getSTFID());
        
    // availableUnits.add(currUnit);

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
    
    public double computeUtility(Block b, Agent a, int tenure) {
        double utility=0.0;
        ArrayList blocks = new ArrayList();
        if (b == null) {
        System.out.println("computeUtility Block is null");
        }
        else {
            int race = a.getRace();
            if(b.getPctRace(race)<0.10) {
                utility = 1.0;
            } else if(b.getPctRace(race)>=0.10 & b.getPctRace(race)<.20){
                utility = 2.0; 
            } else if(b.getPctRace(race)>=.20 & b.getPctRace(race)<.30){
            	utility = 3.0; 
            } else if(b.getPctRace(race)>=.30 & b.getPctRace(race)<0.40){
            	utility = 4.0; 
            } else if(b.getPctRace(race)>=.40 & b.getPctRace(race)<.50){
            	utility = 5.0; 
            } else if(b.getPctRace(race)>=.50 & b.getPctRace(race)<.60){
            	utility = 6.0;
            } else if(b.getPctRace(race)>=.60 & b.getPctRace(race)<.70){
            	utility = 7.0;
            } else if(b.getPctRace(race)>=.70 & b.getPctRace(race)<.80){
            	utility = 8.0;
            } else if(b.getPctRace(race)>=.80 & b.getPctRace(race)<.90) {
            	utility = 9.0;
            } else if(b.getPctRace(race)>=.9){
            	utility = 10.0;
        } else {
                System.out.println("!!!!!! Could not find race !!!!!! ");
            }
        }
      return utility;
    }
    
    public double computeUtilitityForOwnUnit(HousingUnit currUnit, Agent a) {
            Block b = currUnit.getBlock();  //  block; 
            double utility=0.0;
            int race = a.getRace();
            if(b.getPctRace(race)<0.10) {
                utility = 1.0;
            } else if(b.getPctRace(race)>=0.10 & b.getPctRace(race)<.20){
                utility = 2.0; 
            } else if(b.getPctRace(race)>=.20 & b.getPctRace(race)<.30){
            	utility = 3.0; 
            } else if(b.getPctRace(race)>=.30 & b.getPctRace(race)<0.40){
            	utility = 4.0; 
            } else if(b.getPctRace(race)>=.40 & b.getPctRace(race)<.50){
            	utility = 5.0; 
            } else if(b.getPctRace(race)>=.50 & b.getPctRace(race)<.60){
            	utility = 6.0;
            } else if(b.getPctRace(race)>=.60 & b.getPctRace(race)<.70){
            	utility = 7.0;
            } else if(b.getPctRace(race)>=.70 & b.getPctRace(race)<.80){
            	utility = 8.0;
            } else if(b.getPctRace(race)>=.80 & b.getPctRace(race)<.90) {
            	utility = 9.0;
            } else if(b.getPctRace(race)>=.9){
            	utility = 10.0;
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
        return "StaircaseRace";
    }
    
}
