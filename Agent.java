/** 
 * 
 * MirarLA
 * Agent
 * @author Robert Najlis
 */
package mirar;

import java.util.*;

/**
 * The Agent class represents a household.  the Agent can be of one of four races:
 * White, Black, Asian, or Hispanic.  Agents can be renters or owners of their housing unit.
 * Income levels vary depending on the data file used (different files have different 
 * income distibutions). 
 *
 * @author Robert Najlis
 * @version 1.0 
 *
 */

public class Agent {

	public static int Block = 0; //Priyasree
    public static int CountComputeUtility = 0; //Priyasree
    private double income = -1.0; // Priyasree_DeadCode : Unreachable code_
    private int incomeCategory = - 1; // Priyasree_DeadCode : Unreachable code_
    private int race; // Priyasree_DeadCode : Unreachable code_
    
    private int housingUnitNum; // Priyasree_DeadCode : Unreachable code_
    private HousingUnit housingUnit; // Priyasree_DeadCode : Unreachable code_
    private ArrayList possibleHousingUnitList = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    private ArrayList neighbors = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    private ArrayList memory = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    private AgentMemory carryOverMemory = null; // Priyasree_DeadCode : Unreachable code_
    
    private int blockNum; // Priyasree_DeadCode : Unreachable code_
    private int agentNum; // Priyasree_DeadCode : Unreachable code_
    private String stfid; // Priyasree_DeadCode : Unreachable code_
  //private boolean sampled;
    private boolean addToIncomeList = true; // Priyasree_DeadCode : Unreachable code_
    
    public static final int WHITE = 0; // Priyasree_DeadCode : Unreachable code_
    public static final int BLACK = 1; // Priyasree_DeadCode : Unreachable code_
    public static final int ASIAN = 2; // Priyasree_DeadCode : Unreachable code_
    public static final int HISPANIC = 3; // Priyasree_DeadCode : Unreachable code_

    private int tenure; // Priyasree_DeadCode : Unreachable code_
    public static final int OWNER = 0; // Priyasree_DeadCode : Unreachable code_
    public static final int RENTER = 1; // Priyasree_DeadCode : Unreachable code_
  

    public Agent() { 
    }

    /**
     * does nothing - just an empty constructor
     * @param empty
     */
    public Agent(int empty) {
    }
    
  
    /**
     * called by AgentHandler#createTestAgents
     * @param race
     * @param incomeCategory
     * @param addIncome
     * @param tenure
     */
    public Agent(int race, int incomeCategory, boolean addIncome, int tenure) { 
        this();
        this.addToIncomeList = addIncome;
        this.race = race;
        this.tenure = tenure;
        this.incomeCategory = incomeCategory;
        this.setIncome(MirarUtils.incomeCategoryToIncome(incomeCategory, tenure));
        this.addToMemory();
    }

    /**
     * called by AgentHandler#addAgent
     * @param agentNum
     * @param race
     * @param incomeCategory
     * @param stfid
     * @param tenure
     */
    public Agent(int agentNum, int race, int incomeCategory, String stfid, int tenure) { 
        this();
        this.agentNum = agentNum;
        this.race = race;
        this.tenure = tenure;
        this.incomeCategory = incomeCategory;
        this.setIncome(MirarUtils.incomeCategoryToIncome(incomeCategory, tenure));
        this.blockNum = Integer.parseInt(stfid.substring(6));
        this.stfid = stfid;
        this.addToMemory();
    }
    
 
    /**
     * 
     * Uses the AgentDecision#select function to 
     * select the destination housing unit for the agent
     * 
     * 
     * Returns the destination housing unit for the agent. 
     *     
     * @param housingUnitList_Renters
     * 		list of possible destination housing units
     * @return HousingUnit
     * 		destination housing unit (may be agent's current unit)
     */
    public HousingUnit move(ArrayList housingUnitList) { // Priyasree_DeadCode : Unreachable code_
        boolean notThere = false;
        if (this.getHousingUnit().getAgent() == null) notThere = true;
       
        possibleHousingUnitList.clear();
        possibleHousingUnitList.addAll(housingUnitList);
        // add own unit to list
  
        if (MirarUtils.TEST_BLOCK_NEIGHBORHOOD == true) { // Priyasree_Audit : Equality test with boolean literal: true_ Remove the comparison with true. 
            for (int i=0; i<housingUnitList.size(); i++) {
                Block b = ((HousingUnit)housingUnitList.get(i)).getBlock();
                
                neighbors.clear();
                neighbors.addAll(b.getNeighbors());
                
                if (neighbors == null) {
                    System.out.println("block " + b.getSTFID());
                    System.out.println("\t\t\t neighbor is NULL");
                }
                else {
                    for (int j=0; j< neighbors.size(); j++) {
                         Block b2 = (Block)neighbors.get(j);
                        if (b2 == null) System.out.println("\t NULL ---  neighbors.get(j)");
                    }
                }
               //
            }
        }
        
        // check to make sure there are no occupied housing units
        // should do something if this does happen - perhaps remove them
        for (int i=0; i<housingUnitList.size(); i++) {
            if ( ((HousingUnit)housingUnitList.get(i)).isOccupied() == true ) { // Priyasree_Audit : Equality test with boolean literal: true_ Remove the comparison with true. 
            }
        }
 
        if (this.getHousingUnit().getAgent() == null) {
            ErrorLog.getInstance().logError("Agent#move  move currUnit  agent is null  tenure -> " + this.getTenure());
            if (notThere == true) { // Priyasree_Audit : Equality test with boolean literal: true_ Remove the comparison with true. 
                ErrorLog.getInstance().logError("Agent#move move currUnit  agent is null  -->  notThere at beginning either");
                
            }
        }
        HousingUnit moveToUnit = MirarUtils.AGENT_DECISION.select(possibleHousingUnitList, this.getHousingUnit(), this);
      
        // if own unit - do nothing
        if (moveToUnit == null ) {
            return this.getHousingUnit();
        }
        else if  (moveToUnit.getHousingUnitNum() == this.getHousingUnitNum() &&
                moveToUnit.getBlock().getSTFID().equals(this.getSTFID()) ) {
           return this.getHousingUnit();
        }
        else {
           
            moveOut();
            moveIn(moveToUnit);
            this.addToMemory();
        }
       // possibleHousingUnitList.clear();
        return moveToUnit;
    }
 
    /**
     * Moves agent into specified housing unit. Sets agent's block and housing unit
     * number to appropriate values. 
     * 
     * @param hu
     */
    public void moveIn(HousingUnit h) { // Priyasree_DeadCode : Unreachable code_
        if (h == null) ErrorLog.getInstance().logError("Agent#MoveIn could not get housing Unit to move in to");
        h.addAgent(this);
        this.setSTFID(h.getBlock().getSTFID());
        this.setHousingUnitNum(h.getHousingUnitNum());
        this.setHousingUnit(h);
        if (h.getAgent() == null ) ErrorLog.getInstance().logError("Agent#MoveIn  agent moveIn HousingUnit.getAgent() agent is null");
        
    }
    
    /**
     * removes agent from current hu
     *
     */
    public void moveOut() { // Priyasree_DeadCode : Unreachable code_
        if (this.getHousingUnit().getAgent() == null)ErrorLog.getInstance().logError("Agent#MoveOut  agent moveIn HousingUnit.getAgent() agent is null");
        this.housingUnit.removeAgent();
    }
    
    public int getRaceIncomeType() { 
        return  ((this.race*MirarUtils.NUM_INCOMES) + this.incomeCategory) ;
    }
    
    
    public void addToMemory() { 
        if (MirarUtils.AGENT_MEMORY == true) { // Priyasree_Audit : Equality test with boolean literal: true_ Remove the comparison with true. 
            memory.add(new AgentMemory(MirarUtils.STEP_NUM,this.stfid, this.housingUnitNum));
        }
    }
    
    public double computeUtility(Block b, int tenure) { 
    	CountComputeUtility++;
        return MirarUtils.AGENT_DECISION.computeUtility(b, this, tenure);
    }
    
    public double computeMarginalUtility(Block b, int tenure){ // Priyasree_DeadCode : Unreachable code_
    	return MirarUtils.AGENT_DECISION.computeMarginalUtility(b, tenure);
    }
    
    public double computePrice(double marginalUtil, Block b, int tenure){ // Priyasree_DeadCode : Unreachable code_
    	return MirarUtils.AGENT_DECISION.solveForPrice(marginalUtil, b, tenure);
    }
    
  

    /**
     * @return Returns the block.
     */
    /*public int getBlockNum() { // Priyasree_DeadCode : Unreachable code_
        return blockNum;
    }*/

    /**
     * @param blockNum
     *            sets the blockNumber of the Block that the agent occupies
     */
    /*public void setBlockNum(int blockNum) { // Priyasree_DeadCode : Unreachable code_
        this.blockNum = blockNum;
    }*/

    public Block getBlock() {     Block++;  // Priyasree_DeadCode : Unreachable code_
        return (Block) CensusUnitHandler.getInstance().getBlock(this.blockNum); // Priyasree_Audit : Unnecessary type cast to Block_Delete the unnecessary cast.
    }
  
    public double getIncome() {
        return income;
    }

    /**
     * not used in version 1
     * this function is here in anticipation of future versions
     * @param income
     */ 
    public void setIncome(double income) { 
      //  MirarUtils.subtractIncome(this.income);  ### don't need to subtract in version 1
        this.income = income;
        if (this.addToIncomeList == true) {  // Priyasree_Audit : Equality test with boolean literal: true_ Remove the comparison with true. 
            MirarUtils.addIncome(income);
        }
    
    }

    
    public int getIncomeCategory() { 
        return incomeCategory;
    }

    
    /*public void setIncomeCategory(int incomeCategory) { // Priyasree_DeadCode : Unreachable code_
        this.incomeCategory = incomeCategory;
    }*/

    
    public int getRace() { 
        return race;
    }

   
    /*public void setRace(int race) { // Priyasree_DeadCode : Unreachable code_
        this.race = race;
    }*/
    public int getHousingUnitNum() { 
        return this.getHousingUnit().getHousingUnitNum();
    }
    public void setHousingUnitNum(int housingUnitNum) { 
        this.housingUnitNum = housingUnitNum;
    }
    public HousingUnit getHousingUnit() { 
            return CensusUnitHandler.getInstance().getHousingUnit(this.stfid, this.housingUnitNum, this.getTenure());
    }
    public void setHousingUnit(HousingUnit housingUnit) {
        this.housingUnit = housingUnit;
       
        if (this.getHousingUnit().getAgent() == null) {
            ErrorLog.getInstance().logError("Agent#getHousingUnit  agent  setHousingUnit (hu)  hu  agent is null,  tenure -> " + this.getTenure());
        
        }
    }
    public ArrayList getPossibleHousingUnitList() { // Priyasree_DeadCode : Unreachable code_
        return possibleHousingUnitList;
    }
    /*public void setPossibleHousingUnitList(ArrayList possibleHousingUnitList) { // Priyasree_DeadCode : Unreachable code_
        this.possibleHousingUnitList = possibleHousingUnitList;
    }*/
    public int getAgentNum() { // Priyasree_DeadCode : Unreachable code_
        return agentNum;
    }
    /*public void setAgentNum(int agentNum) { // Priyasree_DeadCode : Unreachable code_
        this.agentNum = agentNum;
    }*/
    
    public String getSTFID() { 
        return stfid;
    }
    
    public void setSTFID(String stfid) { // Priyasree_DeadCode : Unreachable code_
        this.stfid = stfid;
    }
    
    /*public ArrayList getMemory() { // Priyasree_DeadCode : Unreachable code_
        return memory;
    }*/
    /*public void setMemory(ArrayList memory) { // Priyasree_DeadCode : Unreachable code_
        this.memory = memory;
    }*/
    /*public boolean isSampled() { // Priyasree_DeadCode : Unreachable code_
        return sampled;
    }*/
    /*public void setSampled(boolean sampled) { // Priyasree_DeadCode : Unreachable code_
        this.sampled = sampled;
    }*/
    
    /*public String toString() { // Priyasree_DeadCode : Unreachable code_
        return ("agent num: " + this.agentNum + " STFID: " + this.stfid + " housing unit: " + this.housingUnitNum
                + " race: " + this.raceToString() + " income: " + this.getIncome()); 
    }*/
    
    /**
     * call the createMemoryString function with two parameters:  the current step number and the value
     * to be prepended to the first memory of the print interval
     * the  firstMemoryPrepend param tells if the agent was sampled (1) or not sampled (2) for the first memory 
     * of this printout.  For the first tick of the model run (step 0 ) the agent is considered sampled.
     * The agent is considered sampled after that if the carry over history is not needed.  If the carry over history
     * is used, that means the agent was not sampled, as we are using the carry over history from the previous 
     * tick.
     * @return
     */
    public String memoryToString() { 
       
        // first print interval of the model.  agent is considered sampled for step 0
        if (carryOverMemory == null) {
            return createMemoryString(MirarUtils.STEP_NUM, 1 );
        }
       // else {  // there is a carryOverHistory
           
            // there are no histories in the list thus the agent was not sampled for the 
            // first tick of the interval
        else   if (this.memory.size() == 0) { 
                memory.add(carryOverMemory);
           //    if (carryOverMemory.getStepNum() == (MirarUtils.STEP_NUM +1)) 
           //        System.out.println("*++++  Agent#memoryToString add memory to mem size== 0) : carryOverMem:  " + carryOverMemory.getStepNum() + 
           //                 " cueeStepNum " + MirarUtils.STEP_NUM);
         //       System.out.println("Agent#memoryToString add memory to mem size 0:  carry over StepNum:  " + carryOverMemory.getStepNum()  + " currentStepNUM " + MirarUtils.STEP_NUM);
                return createMemoryString(MirarUtils.PRINT_INTERVAL, 2);
            }
           // else {
              
                //memory.0 is the same step as the carry over history - don't need the carry over memory
                // agent was sampled for the first tick of the print interval
            else    if (((AgentMemory)this.memory.get(0)).getStepNum() == carryOverMemory.getStepNum()) {
                    
                  //  if (carryOverMemory.getStepNum() == (MirarUtils.STEP_NUM +1)) 
                 //       System.out.println("*------  Agent#memoryToString add memory to mem size  0 : carryOverMem:  " + carryOverMemory.getStepNum() + 
                 //               " cueeStepNum " + MirarUtils.STEP_NUM);
                  //  System.out.println("Agent#memoryToString Don;t add memory to mem size (not 0) : " + memory.size() +
                    //        "   carry over StepNum:  " + carryOverMemory.getStepNum()  + " currentStepNUM " +
                    //        MirarUtils.STEP_NUM  + "memory 0 step NUm  "+ ((AgentMemory)this.memory.get(0)).getStepNum());
                    
                    
                    return createMemoryString(MirarUtils.PRINT_INTERVAL, 1);
                }
                else { 
                    // need to use the carry over history
                    //the agent was not sampled for the 
                    // first tick of the interval
                    this.memory.add(0, carryOverMemory);
                //    if (carryOverMemory.getStepNum() == (MirarUtils.STEP_NUM )) 
                //        System.out.println("*++++  Agent#memoryToString add memory to mem size (not 0) : carryOverMem:  " + carryOverMemory.getStepNum() + 
                 //               " cueeStepNum " + MirarUtils.STEP_NUM);
             //       System.out.println("Agent#memoryToString add memory to mem size (not 0) : " + memory.size() + "   carry over StepNum:  " + carryOverMemory.getStepNum()  + " currentStepNUM " + MirarUtils.STEP_NUM);
                    // return createHistoryString();
                    return createMemoryString(MirarUtils.PRINT_INTERVAL, 2);
                }
           // }
       // }
        
       
    }
    
    /**
     * creates the memory string
     * takes the list of memories and adds beginning middle and end memories when needed
     * the memory list only records when an agent was sampled so this functions adds in 
     * the other memories based on that.
     * the  firstMemoryPrepend param tells if the agent was sampled (1) or not (2) for the first memory 
     * of this printout.  For the first tick of the model run (step 0 ) the agent is considered sampled.
     * The agent is considered sampled after that if the carry over history is not needed.  If the carry over history
     * is used, that means the agent was not sampled, as we are using the carry over history from the previous 
     * tick.
     * @param printInterval
     * @param firstMemoryPrepend
     * @return
     */
    private String createMemoryString(int printInterval, int firstMemoryPrepend) { 
        carryOverMemory = null;
        StringBuffer result = new StringBuffer();
        int totalMemory = 0;
        // add race-income type  
        result.append((this.getRace()*MirarUtils.NUM_INCOMES) + this.getIncomeCategory() + ",") ;// Priyasree_Audit :  Accidental Concatenation_Enclose the sub-expression in parentheses. 
        //add stfid from 1st step - initialization
     //  if (((AgentMemory)memory.get(0)).getStepNum() == (MirarUtils.STEP_NUM + 1)) 
          // System.out.println("***********  prepend mem == StepNUm+1 prepend:  " + ((AgentMemory)memory.get(0)).getStepNum() + 
           //        " cueeStepNum " + MirarUtils.STEP_NUM);
        result.append("" + firstMemoryPrepend + "" + ((AgentMemory)memory.get(0)).getSTFID() + "," );
     
        // add remaining memory STFIDs - for 1 through last set of model
        // if missing any memories in between steps, have to insert them
        // if missing any memories from last memory to last step of model - have to add them
        int numMemories = memory.size();
        int prevStep = 0;
        int currStep = 0;
        int currMemory = 1;
        int prevMemory = 0;
        
        for (int i=1; i< numMemories; i++) {
            prevStep = ((AgentMemory)memory.get(i-1)).getStepNum();
             currStep =  ((AgentMemory)memory.get(i)).getStepNum();
            if ( (currStep - prevStep) > 1) {
                // there are missing steps in between this step and previous one
                int insertMemories = ((currStep - prevStep) - 1);
                for (int j=0; j<insertMemories; j++) {
                    result.append("2" + ((AgentMemory)memory.get(prevMemory)).getSTFID() + "," );
                    totalMemory++;
                }
                result.append("1" + ((AgentMemory)memory.get(currMemory)).getSTFID() + ","  );
                totalMemory++;
                prevMemory = currMemory;
                currMemory++;
            }
            else {
                // don't need to insert any steps
                result.append("1" + ((AgentMemory)memory.get(currMemory)).getSTFID() + "," );
                totalMemory++;
                prevMemory = currMemory;
                currMemory++;
            }
        }
        // check to make sure there are not memories needed to get to last step of model
        if ( (printInterval - totalMemory) > 0 ) {
            // need to add memories to end of list
            int lastMemory = (memory.size() - 1);
            int lastStepNum = ((AgentMemory)memory.get(lastMemory)).getStepNum();
           // result.append(" want to add memories to end  of list  ==  step NUm  " + ((AgentMemory)memory.get(lastMemory)).getStepNum() + "  ");
            for (int i=lastStepNum+1; i<=MirarUtils.STEP_NUM; i++) {
                result.append("2" + ((AgentMemory)memory.get(lastMemory)).getSTFID() + "," );
            }
        }
        
        // set carry over memory
        this.carryOverMemory = new AgentMemory((AgentMemory)this.memory.get(this.memory.size()-1),MirarUtils.STEP_NUM+1 );
       // result.append(" set carry over memory  carry Over StepNUM:  " + carryOverMemory.getStepNum() + " -- ");
        this.memory.clear();
        
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
    
    /*public String testMemoryToString() { // Priyasree_DeadCode : Unreachable code_
        StringBuffer result = new StringBuffer();
        result.append("" + MirarUtils.STEP_NUM + ",") ;
        int memoryLength = memory.size();
        int totalMemory = memoryLength;
        int stepNum = 1;
        for (int i=0; i<memoryLength; i++) {
        	int currStep =  ((AgentMemory)memory.get(i)).getStepNum();
        	
        	// check for steps before the initial move of the agents
        	if (i>0) {
        		int prevStep =  ((AgentMemory)memory.get(i-1)).getStepNum();
        		
        		int difference = currStep - prevStep;
        		if (difference > 1) {
        			for(int j=1;j<difference;j++){
        				result.append("2 " + ((AgentMemory)memory.get(i-1)).toString() +  "  stepNUM: " + ((AgentMemory)memory.get(i-1)).getStepNum() + " added first IF  ,");
        				totalMemory++;
        				stepNum++;
        			}
        		}
        	}
        	result.append("1 " + ((AgentMemory)memory.get(i)).toString() +  "  stepNUM: " + ((AgentMemory)memory.get(i)).getStepNum() + " added no IF ,");
        stepNum++;
        }
        
        // this should take care of the remains at the end
        if (totalMemory < MirarUtils.STEP_NUM + 1) {
    		for (int k=totalMemory; k<MirarUtils.STEP_NUM+1; k++) {
    			result.append("2 " + ((AgentMemory)memory.get(memoryLength-1)).toString() +  "  stepNUM: " + ((AgentMemory)memory.get(memoryLength-1)).getStepNum() + " added Last IF , total Mem: " + totalMemory + " ,");
    			stepNum++;
    		}
    	}
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
    */
    /*public String raceToString() { // Priyasree_DeadCode : Unreachable code_
        switch (this.getRace()) {
            case 0 : return "white"; 
            case 1 : return "black"; 
            case 2 : return "asian"; 
            case 3 : return "hispanic"; 
            default : return "race not specified"; 
        }
    }*/

    public int getTenure() { 
        return tenure;
    }

    /*public void setTenure(int tenure) { // Priyasree_DeadCode : Unreachable code_
        this.tenure = tenure;
    }*/
} // end Agent
