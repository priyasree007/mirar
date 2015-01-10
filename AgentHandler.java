/** 
 * 
 * MirarLA
 * AgentHandler
 * 	handles the agents
 * 	contains the renterAgentList, has access to agents, etc
 *  @author Robert Najlis
 */
package mirar;

import java.util.*;

import uchicago.src.sim.util.Random;

/**
 *  The AgentHandler class holds all of the agentsin the model.  It also handles the moving of agents,
 *  as well as the updating of block utilities based on agent moves.
 *  
 *  @author Robert Najlis
 *  @version 1.0 
 */
public class AgentHandler {

 // private ArrayList allAgentList = new ArrayList(); 
    private ArrayList renterAgentList = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    private ArrayList ownerAgentList = new ArrayList();
    private ArrayList renterTestAgents = new ArrayList(); // arraylist of theoretical agents used for utility testing // Priyasree_DeadCode : Unreachable code_
    private ArrayList ownerTestAgents = new ArrayList(); // arraylist of theoretical agents used for utility testing
    private HashSet blocksToUpdate_Renter = new HashSet(); // Priyasree_DeadCode : Unreachable code
 // private HashSet blocksToUpdate_All = new HashSet(); // Priyasree_DeadCode : Unreachable code_
 // private HashSet blocksToUpdate_Owner = new HashSet(); // Priyasree_DeadCode : Unreachable code_
   
 
    private static AgentHandler instance = new AgentHandler(); 

    /*private AgentHandler() { // Priyasree_DeadCode : Unreachable code_
    	super();
    }*/

    public static AgentHandler getInstance() { 
      return instance;
    }
    
   /* public void moveAgents() {
        if (MirarUtils.TENURE_MATTERS==true) {
        moveRenterAgents();
        moveOwnerAgents();
        } else {
        	moveAllAgents(); 
        }
    }
    */
    /**
     *  goes through each of the renter units and moves them into one of the vacant (renter) housing units
     *  
     * calls the Agent#move function for each renter agent, passing in the list of vacant housing units
     */
    
   /* public void moveAllAgents() { // Priyasree_DeadCode : Unreachable code_
        blocksToUpdate_All.clear(); // clear class list of blocks to update
        
        ArrayList agents =   new ArrayList();
        agents.addAll(this.sampleAllAgents());
        
        ArrayList vacantHousingUnitList =   new ArrayList();
        
        
        // get all vacant housing units - agents can only move into vacant housing units
        
 
        // note that if the move rules are "race only" all agents can move into all vacant housing units
        vacantHousingUnitList.addAll(CensusUnitHandler.getInstance().sampleVacantHousingUnits());
        
        vacantHousingUnitList.trimToSize();
        System.out.println("AgentHandler#MoveAllAgents: total number of housing units is :" + vacantHousingUnitList.size());
        
        ArrayList selectedAgents = new ArrayList();
        
        ArrayList updateHousingUnits = new ArrayList();
        HousingUnit h = null;
        for (int i=0; i<agents.size(); i++) {
            
            Agent agent = (Agent)agents.get(i);
            if (agent.getHousingUnit().getAgent() == null) ErrorLog.getInstance().logError("Agent#moveAllAgents -- agent.getHousingUnit().getAgent() == null");
            if (selectedAgents.contains(agent) ) continue;
            selectedAgents.add(agent);
            h = agent.getHousingUnit();
            if (h == null) {
                ErrorLog.getInstance().logError("Agent#moveAllAgents    ;   pre AgentDecision could not find HU: " + agent.getSTFID() + " " +  agent.getHousingUnitNum());
            }
            updateHousingUnits.add(h);
            
            HousingUnit selectedHU = agent.move(vacantHousingUnitList);
            h = CensusUnitHandler.getInstance().getHousingUnit(agent.getSTFID(), agent.getHousingUnitNum(), agent.getTenure());
            if (h == null) {
                ErrorLog.getInstance().logError("Agent#moveAllAgents ;  post AgentDecision could not find HU: " + agent.getSTFID() + " " +  agent.getHousingUnitNum());
            }
            updateHousingUnits.add(selectedHU);
            vacantHousingUnitList.remove(selectedHU);
        }
        
        // have to get the list of hu's (blocks) that agents moved into and out of
        blocksToUpdate_All.addAll(getBlocksToUpdate(updateHousingUnits));
        updateBlocks(blocksToUpdate_All);
        vacantHousingUnitList.clear();
        agents.clear();
        selectedAgents.clear();
    }
    */
    public void moveRenterAgents() { // Priyasree_DeadCode : Unreachable code_
        blocksToUpdate_Renter.clear(); // clear class list of blocks to update
        
        ArrayList agents =   new ArrayList();
        agents.addAll(this.sampleRenterAgents());
        
        ArrayList vacantHousingUnitList =   new ArrayList();
        
        
        // get all vacant housing units - agents can only move into vacant housing units
        
 
        // note that if the move rules are "race only" all agents can move into all vacant housing units
        vacantHousingUnitList.addAll(CensusUnitHandler.getInstance().sampleVacantHousingUnits(Agent.RENTER));
        
        vacantHousingUnitList.trimToSize();
        
        ArrayList selectedAgents = new ArrayList();
        
        ArrayList updateHousingUnits = new ArrayList();
        HousingUnit h = null;
        for (int i=0; i<agents.size(); i++) {
            
            Agent agent = (Agent)agents.get(i);
            if (agent.getHousingUnit().getAgent() == null) ErrorLog.getInstance().logError("Agent#moveRenterAgents -- agent.getHousingUnit().getAgent() == null");
            if (selectedAgents.contains(agent) ) continue;
            selectedAgents.add(agent);
            h = agent.getHousingUnit();
            if (h == null) {
                ErrorLog.getInstance().logError("Agent#moveRenterAgents    ;   pre AgentDecision could not find HU: " + agent.getSTFID() + " " +  agent.getHousingUnitNum());
            }
            updateHousingUnits.add(h);
            
            HousingUnit selectedHU = agent.move(vacantHousingUnitList);
            h = CensusUnitHandler.getInstance().getHousingUnit(agent.getSTFID(), agent.getHousingUnitNum(), agent.getTenure());
            if (h == null) {
                ErrorLog.getInstance().logError("Agent#moveRenterAgents ;  post AgentDecision could not find HU: " + agent.getSTFID() + " " +  agent.getHousingUnitNum());
            }
            updateHousingUnits.add(selectedHU);
            vacantHousingUnitList.remove(selectedHU);
        }
        
        // have to get the list of hu's (blocks) that agents moved into and out of
        blocksToUpdate_Renter.addAll(getBlocksToUpdate(updateHousingUnits));
        updateBlocks(blocksToUpdate_Renter, Agent.RENTER);
        vacantHousingUnitList.clear();
        agents.clear();
        selectedAgents.clear();
    }
    
    /**
     *  goes through each of the renter units and moves them into one of the vacant (owner) housing units
     *  
     * calls the Agent#move function for each owner agent, passing in the list of vacant housing units
     */
   /* public void moveOwnerAgents() { // Priyasree_DeadCode : Unreachable code_
        blocksToUpdate_Renter.clear(); // clear class list of blocks to update
        
        ArrayList agents =   this.sampleOwnerAgents();
        
        ArrayList vacantHousingUnitList =   new ArrayList();
        
        
        // get all vacant housing units - agents can only move into vacant housing units
        vacantHousingUnitList.addAll(CensusUnitHandler.getInstance().sampleVacantHousingUnits(Agent.OWNER));
        vacantHousingUnitList.trimToSize();
        
        ArrayList selectedAgents = new ArrayList();
        
        ArrayList updateHousingUnits = new ArrayList();
        HousingUnit h = null;
        for (int i=0; i<agents.size(); i++) {
            
            Agent agent = (Agent)agents.get(i);
            if (agent.getHousingUnit().getAgent() == null) ErrorLog.getInstance().logError("AgentHandler#moveOwnerAgents - agent.getHousingUnit().getAgent() == null");
            
            if (selectedAgents.contains(agent) ) continue;
            selectedAgents.add(agent);
            h = agent.getHousingUnit();
            if (h == null) {
                ErrorLog.getInstance().logError("AgentHandler#moveOwnerAgents;  pre AgentDecision could not find HU: " + agent.getSTFID() + " " +  agent.getHousingUnitNum());
            }
            updateHousingUnits.add(h);
            
            HousingUnit selectedHU = agent.move(vacantHousingUnitList);
            
            h = CensusUnitHandler.getInstance().getHousingUnit(agent.getSTFID(), agent.getHousingUnitNum(), agent.getTenure());
            if (h == null) {
                ErrorLog.getInstance().logError("AgentHandler#moveOwnerAgents;  post AgentDecision could not find HU: " + agent.getSTFID() + " " +  agent.getHousingUnitNum());
            }
            updateHousingUnits.add(selectedHU);
            
            // remove chiosen unit from list
            vacantHousingUnitList.remove(selectedHU);
        }
        
        // have to get the list of hu's (blocks) that agents moved into and out of
        blocksToUpdate_Owner.addAll(getBlocksToUpdate(updateHousingUnits));
        updateBlocks(blocksToUpdate_Owner, Agent.OWNER);
        vacantHousingUnitList.clear();
        agents.clear();
        selectedAgents.clear();
        
    }
    */
    public HashSet getBlocksToUpdate_Renter(){ // Priyasree_DeadCode : Unreachable code_
        return blocksToUpdate_Renter; 
    }
    
    /*public HashSet getBlocksToUpdate_Owner(){ // Priyasree_DeadCode : Unreachable code_
        return blocksToUpdate_Owner; 
    }*/
    
    /*public HashSet getBlocksToUpdate_All(){ // Priyasree_DeadCode : Unreachable code_
        return blocksToUpdate_All; 
    }*/
    
    /**
     * creates a list of theoretical or test agents.  
     * One agent for each race and class combo - for each agent decision type , for renters and owners
     *
     */
    public void createTestAgents() { 
        if (renterTestAgents == null) { 
            renterTestAgents = new ArrayList();
        }
        if (ownerTestAgents == null) { 
            ownerTestAgents = new ArrayList();
        }
        
        
        // renters

        // for each race 0-3
        for (int i=0; i<MirarUtils.NUM_RACES; i++) {
            // for each income category 0-15
            for (int j=0; j<MirarUtils.NUM_INCOMES; j++) {
                renterTestAgents.add(new Agent(i,j, false, Agent.RENTER));
                
            }
        }

        //Owners
        
        // for each race 0-3
        for (int i=0; i<MirarUtils.NUM_RACES; i++) {
            // for each income category 0-15
            for (int j=0; j<MirarUtils.NUM_INCOMES; j++) {
                ownerTestAgents.add(new Agent(i,j, false, Agent.OWNER));
                
            }
        }
   
    }
    
    
    /**
     *  calls updateRenterUtilities and
     *   updateOwnerUtilities
     *  
     *  called by Mediator#setupRents
     *
     */
    public void updateUtilities() { // Priyasree_DeadCode : Unreachable code_
        updateRenterUtilities();
       // updateOwnerUtilities();
    }
     
    
    /**
     * updates utilities for renter agents using the test agents to compute utilities 
     *
     */
    public void updateRenterUtilities()  { // Priyasree_DeadCode : Unreachable code_
   
       ArrayList testAgents = this.getRenterTestAgents();
       int numTestAgents = testAgents.size();
       Iterator blockIter = MirarUtils.BLOCKS.iterator();
       while (blockIter.hasNext() ) {
       
           Block b = (Block) blockIter.next();
           if(b.getNumHousingUnits()>0){
           for (int j=0; j<numTestAgents; j++) {
               Agent a = (Agent)testAgents.get(j);
               double util = a.computeUtility(b, Agent.RENTER);
               b.addUtility(util, a);
           }
           }
       }
      
   }
    
    
    /**
     * updates utilities for owner agents using the test agents to compute utilities 
     *
     */
    /*public void updateOwnerUtilities()  { // Priyasree_DeadCode : Unreachable code_
       
        ArrayList testAgents = this.getRenterTestAgents();
        // go through the list and compute utilities for each agent type
        int numTestAgents = testAgents.size();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext() ) {
            Block b = (Block) blockIter.next();
            for (int j=0; j<numTestAgents; j++) {
                Agent a = (Agent)testAgents.get(j);
                double util = a.computeUtility(b, Agent.OWNER);
                b.addUtility(util, a);
            }
        }
     
    }*/
    
    /**
     * determines what blocks need to have their utilities updated
     *  this means the neighbors for each of the housing units passed in as the argument
     *  -- the function creates a list and adds each block for the given housing unit, and also
     *  adds the neighbors for that block
     *  
     *   
     * @param housingUnits
     * @return
     */
    public HashSet getBlocksToUpdate(ArrayList housingUnits) { // Priyasree_DeadCode : Unreachable code_
        // get all of theblocks from the housing units
        HashSet blockSet = new HashSet();
        int numHousingUnits = housingUnits.size();
        Block b= null;
        
        for (int i =0; i<numHousingUnits; i++) {
          
            b= ((HousingUnit)housingUnits.get(i)).getBlock();
            blockSet.add(b);
            ArrayList neighbors = new ArrayList();
            neighbors.addAll(b.getNeighbors());
            for (int j=0; j< neighbors.size(); j++) {
                if (neighbors.get(j) == null) {
                }
                else {
                    blockSet.add((Block) neighbors.get(j));
                }
            }
        }
        return blockSet;
    }
    
    /**
     * calls either updateRenterBlocks or updateOwnerBlocks
     *  depending on the value of tenure
     *  
     * @param blocks
     * @param tenure
     */
    public void updateBlocks(HashSet blocks, int tenure) { // Priyasree_DeadCode : Unreachable code_
        if (tenure == Agent.RENTER)  
            updateRenterBlocks(blocks);
        else 
            updateOwnerBlocks(blocks);
    }
    
    /*public void updateBlocks(HashSet blocks) { // Priyasree_DeadCode : Unreachable code_
            updateRenterBlocks(blocks); // only need to update renter utilities since renters and owners have same utility 
    }*/
    
    
    /**
     * updates the utilities for the given blocks based on the test agents
     * @param blocks
     */
    public void updateOwnerBlocks(HashSet blocks) { // Priyasree_DeadCode : Unreachable code_
        
        // go through the list and compute utilities for each agent type
        
        int numTestAgents = ownerTestAgents.size();
        
        Iterator i = blocks.iterator();
        
        while (i.hasNext()) {
            Block b = (Block) i.next();
            Agent a = new Agent(0);
            double util = 0.0;
            for (int j=0; j<numTestAgents; j++) {
                a = (Agent)ownerTestAgents.get(j);
                util = a.computeUtility(b, a.getTenure());
            }
            b.update(util, a);
        }
    }
    
    
    /**
     * updates the utilities for the given blocks based on the test agents
     * @param blocks
     */
    public void updateRenterBlocks(HashSet blocks) { // Priyasree_DeadCode : Unreachable code_
        
            // go through the list and compute utilities for each agent type
            
            int numTestAgents = renterTestAgents.size();
            
            Iterator i = blocks.iterator();
            while (i.hasNext()) {
                Block b = (Block) i.next();
                Agent a = new Agent(0);
                double util = 0.0;
                    for (int j=0; j<numTestAgents; j++) {
                         a = (Agent)renterTestAgents.get(j);
                         util = a.computeUtility(b, a.getTenure());
                    }
                    b.update(util, a);
                }
    }
  
    /**
     * created a new agent and adds it to the appropriate list (renter or owner)
     * 
     * @param race
     * @param incomeCategory
     * @param stfid
     * @param tenure
     * @return
     */
    public Agent addAgent(int race, int incomeCategory, String stfid, int tenure) { 

        Agent agent = null;
         if (tenure == Agent.RENTER) {
             if (renterAgentList == null) {
                 this.renterAgentList = new ArrayList();
             }
              agent = new Agent(renterAgentList.size(), race, incomeCategory, stfid, tenure);
             renterAgentList.add(agent);
             }
             else {// tenure == Agent.OWNER
                 if (ownerAgentList == null) {
                     this.ownerAgentList = new ArrayList();
                 }
                  agent = new Agent(ownerAgentList.size(), race, incomeCategory, stfid, tenure);
                 ownerAgentList.add(agent);
             }
             return agent;
         
     }
    
  
    public int getNumAgentsOfRaceIncomeType(int raceIncomeType, int tenure) { 
        int total = 0;
        Iterator iter = null;
        if ( tenure == Agent.RENTER) {
            iter = this.renterAgentList.iterator();
        }
        else iter = this.ownerAgentList.iterator();
        while (iter.hasNext()) {
            Agent agent = (Agent) iter.next();
            if (agent.getRaceIncomeType() == raceIncomeType) {
                total++;
            }
        }
        return total;
    }
    
    /*public ArrayList sampleAllAgents() { // Priyasree_DeadCode : Unreachable code_
        ArrayList units = new ArrayList(); 
        units.addAll(renterAgentList);
        units.addAll(ownerAgentList); 
        int probInt = MirarUtils.probabilisticInterpolation(units.size(), MirarUtils.RENTER_AGENT_SAMPLE); // RENTER = OWNER samples
        int sampleNum = Math.min(probInt, units.size());
        
        ArrayList result = new ArrayList(sampleNum);
        for (int i=0; i<sampleNum;i++) {
            int choice = Random.uniform.nextIntFromTo(0, units.size()-1);
            result.add(units.get(choice));
            units.remove(choice);
        }
        return result;
    }*/
    
    /**
     * gets a sample of the Renter agents based on MirarUtils.RENTER_AGENT_SAMPLE
     * @return list of sampled agents
     */
    public ArrayList sampleRenterAgents() { // Priyasree_DeadCode : Unreachable code_
        ArrayList units = new ArrayList(); 
        units.addAll(renterAgentList);
        int probInt = MirarUtils.probabilisticInterpolation(units.size(), MirarUtils.RENTER_AGENT_SAMPLE);
        int sampleNum = Math.min(probInt, units.size());
        
        ArrayList result = new ArrayList(sampleNum);
        for (int i=0; i<sampleNum;i++) {
            int choice = Random.uniform.nextIntFromTo(0, units.size()-1);
            result.add(units.get(choice));
            units.remove(choice);
        }
        return result;
    }
    
    /**
     * gets a sample of the Owner agents based on MirarUtils.RENTER_AGENT_SAMPLE
     * @return list of sampled agents
     */
    /*public ArrayList sampleOwnerAgents() { // Priyasree_DeadCode : Unreachable code_
        ArrayList units = new ArrayList(); 
        units.addAll(ownerAgentList);
        int probInt = MirarUtils.probabilisticInterpolation(units.size(), MirarUtils.OWNER_AGENT_SAMPLE);
        int sampleNum = Math.min(probInt, units.size());
        
        ArrayList result = new ArrayList(sampleNum);
        for (int i=0; i<sampleNum;i++) {
            int choice = Random.uniform.nextIntFromTo(0, units.size()-1);
            result.add(units.get(choice));
            units.remove(choice);
        }
        return result;
    }*/
    
    /*public int getNumAgents(int tenure) { // Priyasree_DeadCode : Unreachable code_
        if (tenure == Agent.RENTER) {
            return renterAgentList.size();
        }
        else return ownerAgentList.size();
    }*/
    
    public ArrayList getRenterAgentList() { 
        return renterAgentList;
    } // end getAgentList

    /*public void setRenterAgentList(ArrayList agentList) { // Priyasree_DeadCode : Unreachable code_
        this.renterAgentList = agentList;
    } // end setAgentList
*/
    
    
    
    public ArrayList getRenterTestAgents() { 
        return renterTestAgents;
    }
    public void setRenterTestAgents(ArrayList testAgents) { 
        this.renterTestAgents = testAgents;
    }
    
  /* not used : RN
    public String memoriesToString() {
        StringBuffer s = new StringBuffer();
        int numAgents = renterAgentList.size();
        for (int i=0; i<numAgents; i++) {
            s.append(((Agent)renterAgentList.get(i)).memoryToString());
        }
        return s.toString();
    }
*/
    public ArrayList getOwnerAgentList() { 
        return ownerAgentList;
    }
 
    public void setOwnerAgentList(ArrayList ownerAgentList) { 
        this.ownerAgentList = ownerAgentList;
    }

    public ArrayList getOwnerTestAgents() {
        return ownerTestAgents;
    }

   /* public void setOwnerTestAgents(ArrayList ownerTestAgents) { // Priyasree_DeadCode : Unreachable code_
        this.ownerTestAgents = ownerTestAgents;
    }*/
} // end AgentHandler
