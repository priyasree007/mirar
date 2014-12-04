
package mirar;

import java.io.FileOutputStream; // Priyasree_Audit: Unnecessary import: import java.io.FileOutputStream;_Delete the import.
import java.io.IOException; // Priyasree_Audit: Unnecessary import: import java.io.java.io.IOException;_Delete the import.
import java.io.PrintWriter; // Priyasree_Audit: Unnecessary import: import  import java.io.PrintWriter;_Delete the import.
import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ContsRace extends AgentDecision {

	/**
	 * 
	 */
	public ContsRace() { // Priyasree_DeadCode : Unreachable code_
		super();
		// TODO Auto-generated constructor stub
	}

	  public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) { // Priyasree_DeadCode : Unreachable code_
/*        
        PrintWriter testSelect = null;
        try {
            testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", false));
            
        } catch (IOException ioe) {
            
        }
  */      
        
        
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

    //  System.out.println("Agent income is " + agent.getIncome() + " and race is " + agent.getRace());
    //  System.out.println("current STFID is " + agent.getSTFID());
//  for(int i=0; i<utilities.size(); i++){
//  System.out.println("utility i from select function is: " + utilities.get(i));
//      }
            //new DoubleArrayList();   
        
        //System.out.println("AgentDecision select availableUnits " + availableUnits.size() + "  utilities: " + utilities.size());
        //return getChosenHousingUnitFromUtilities(utilities, availableBlocks); //choose from available blocks
        int choice = MirarUtils.sampleFromRawDistribution(utilities);
        	
      //    testSelect.println("choice == " + choice);
       //   testSelect.println("ThresholdRace#select:  Agent race is " + agent.getRace());
          //testSelect.println("ThresholdRace#select:  current STFID is " + agent.getSTFID());
          for(int i=0; i<utilities.size(); i++){
  //            testSelect.println("utility " + i + " from select function is: " + utilities.get(i));
              double prop=0;
              if(agent.getRace()==Agent.WHITE){
              	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
              	prop=this.getPctWhiteInNeighborhood(b);
              } else if(agent.getRace()==Agent.BLACK){
              	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
              	prop=this.getPctBlackInNeighborhood(b);
              } else if(agent.getRace()==Agent.HISPANIC){
              	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
              	prop=this.getPctHispanicInNeighborhood(b);
              } else if(agent.getRace()==Agent.ASIAN){
              	Block b=((HousingUnit)availableUnits.get(i)).getBlock();
              	prop=this.getPctAsianInNeighborhood(b);
              }
    //          testSelect.println("proportion own race in neigh is " + prop);
          }
          //testSelect.println("AgentDecision select availableUnits " + availableUnits.size() + "  utilities: " + utilities.size());
       //   testSelect.close();
        
        
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
    
  /*  public DoubleArrayList computeUtilities(ArrayList housingUnits, Agent agent) {
        DoubleArrayList utilities = new DoubleArrayList();
        ArrayList availableUnits = possibleHousingUnitList;
        Iterator e = availableUnits.iterator();

            int i=0;
            while (e.hasNext()) {
                //System.out.println("agent decsion: select : i: " + i + "num hu: " + availableBlocks.size());
               // Block b = (Block)((HousingUnit)e.next()).getBlock();
                HousingUnit hu = (HousingUnit) e.next();
//              Block b = (Block) e.next();
                Block b =hu.getBlock();
                utilities.add(computeUtility(hu.getBlock(), agent));
//                  Block b = (Block)((HousingUnit) availableBlocks.get(i)).getBlock();
               
                i++;
            }
               return utilities;
    }
    */



    public double computeUtility(Block b, Agent a, int tenure) { // Priyasree_DeadCode : Unreachable code_
        double utility=0.0; 
        ArrayList blocks = new ArrayList();
        if (b == null) {
        System.out.println("contsrace neighbor computeUtility Block is null");
        }
        else {
            if(a.getRace()==Agent.WHITE) {
                   utility = Math.exp(50.0*this.getPctWhiteInNeighborhood(b));
            } else if(a.getRace()==Agent.BLACK){
            		utility = Math.exp(50.0*this.getPctBlackInNeighborhood(b));
             } else if (a.getRace()==Agent.ASIAN){
             		utility = Math.exp(50.0*getPctAsianInNeighborhood(b));
               }else if (a.getRace()==Agent.HISPANIC) {
               		utility = Math.exp(50.0*this.getPctHispanicInNeighborhood(b));
               } else {
                System.out.println("!!!!!! Could not find race !!!!!! ");
            }
        }
      return utility;
    }
    
    public double computeUtilitityForOwnUnit(HousingUnit currUnit, Agent a) { // Priyasree_DeadCode : Unreachable code_
            Block b = currUnit.getBlock();  //  block; 
            double utility=0.0;
            if(a.getRace()==Agent.WHITE) {
                utility = Math.exp(50.0*this.getPctWhiteInNeighborhood(b));
         } else if(a.getRace()==Agent.BLACK){
         		utility = Math.exp(50.0*this.getPctBlackInNeighborhood(b));
          } else if (a.getRace()==Agent.ASIAN){
          		utility = Math.exp(50.0*getPctAsianInNeighborhood(b));
            }else if (a.getRace()==Agent.HISPANIC) {
            		utility = Math.exp(50.0*this.getPctHispanicInNeighborhood(b));
            } else {
             System.out.println("!!!!!! Could not find race !!!!!! ");
         }
   return utility; 
    }

        
    public double computeMarginalUtility(Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
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

    public double solveForPrice(double marginalUtil, Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
        //System.out.println("this is marginalUtil within solve method: " + marginalUtil);
        return -1000*(marginalUtil + b.getPctBlack());
    }

    public String toString() { // Priyasree_DeadCode : Unreachable code_
        return "ContsRace";
    }
    
}
