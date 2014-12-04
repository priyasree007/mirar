/*
 *  @author Robert Najlis
 *  Created on Apr 1, 2004
 *
 *
 */
package mirar;

import uchicago.src.sim.util.Random;

import java.util.*;

/**
 * To do:
 * need to test that when agent declares that it is moving to a certain block, that block
 * is updated appropriately.  
 * 
 * 
 */
public class MirarTest {

    Mediator mediator; // Priyasree_DeadCode : Unreachable code_
    //List block
    
    public MirarTest(Mediator mediator) { // Priyasree_DeadCode : Unreachable code_
        this.mediator = mediator;
    }
    
    public void replaceModelRuns() { // Priyasree_DeadCode : Unreachable code_
        CensusUnitHandler censusUnitHandler = mediator.getCensusUnitHandler();
        AgentHandler agentHandler = mediator.getAgentHandler();
        JUMPHandler jumpHandler = mediator.getJUMPHandler();
        
        ArrayList vacantHousingUnits = censusUnitHandler.getAllVacantHousingUnits();
       // HousingUnit moveInUnit = (HousingUnit)housingUnits.get(0);
       // HousingUnit moveOutUnit = (HousingUnit)housingUnits.get(100);
        
        ArrayList agents = agentHandler.getRenterAgentList();
        // randomly pick some agents - move them into random block/hu's
        int numAgentsToMove = 10;
        List agentsToMove = new ArrayList(numAgentsToMove);
        for (int i=0; i<numAgentsToMove; i++) {
            agentsToMove.add(agents.get(Random.uniform.nextIntFromTo(0, agents.size() - 1)));
        }
        
        System.out.println("\n\n Step Num: " + MirarUtils.STEP_NUM);
        for (int j=0; j<numAgentsToMove; j++) {
            Agent agent = (Agent)agentsToMove.get(j);
            System.out.println("Agent num: " + agent.getAgentNum() + " race: " + agent.raceToString() 
                    + " income: " + agent.getIncome() + " incomeCategory: "+ agent.getIncomeCategory());
            // move out
            Block moveOutBlock = censusUnitHandler.getBlock(agent.getSTFID());
            System.out.println("move out block  " + moveOutBlock.getSTFID() + " num agents: " + moveOutBlock.getNumAgents()
                    + " num agent Race: " + moveOutBlock.getNumRace(agent.getRace()));
            agent.moveOut();
            System.out.println("move out block  " + moveOutBlock.getSTFID() + " num agents: " + moveOutBlock.getNumAgents()
                    + " num agent Race: " + moveOutBlock.getNumRace(agent.getRace()));
            
            // move in
            HousingUnit miHU = (HousingUnit)vacantHousingUnits.get(Random.uniform.nextIntFromTo(0, vacantHousingUnits.size() - 1));
            Block moveInBlock = miHU.getBlock();//censusUnitHandler.getBlock(miHU.getSTFID());
            System.out.println("move in block  " + moveInBlock.getSTFID() + " num agents: " + moveInBlock.getNumAgents()
                    + " num agent Race: " + moveInBlock.getNumRace(agent.getRace()));
            agent.moveIn(miHU);
            
            System.out.println("move in block  " + moveInBlock.getSTFID() + " num agents: " + moveInBlock.getNumAgents()
                    + " num agent Race: " + moveInBlock.getNumRace(agent.getRace()));
           
         // refresh vacant HU list   
            vacantHousingUnits = censusUnitHandler.getAllVacantHousingUnits();
            
            System.out.println("\n\n");
        }
       // int miuNum = moveInUnit.getHousingUnitNum();
        //String miuSTFID = moveInUnit.getBlock().getSTFID();
        
        
        //int mouNum = moveOutUnit.getHousingUnitNum();
        //String mouSTFID = moveOutUnit.getBlock().getSTFID();
        
        //Block mib = censusUnitHandler.getBlock(miuSTFID);
        //Block mob = censusUnitHandler.getBlock(mouSTFID);
        
        
        //Agent a0 =  (Agent) agents.get(0);
       /* 
        System.out.println(a0);
        
        System.out.println("move out block  " + mob.getSTFID() + " num agents: " + mob.getNumTotalPeople()
                + " num agent Race: " + mob.getNumRace(a0.getRace()));
        System.out.println("move in block " + mib.getSTFID() + "num agents: " + mib.getNumTotalPeople()
                + " num agent Race: " + mib.getNumRace(a0.getRace()));
        
        // get agents to test.
       
        a0.moveIn(moveInUnit);   
        a0.moveOut(moveOutUnit);
        
        Block mib2 = censusUnitHandler.getBlock(miuSTFID);
        Block mob2 = censusUnitHandler.getBlock(mouSTFID);
        
        System.out.println("move out block num agents: " + mob2.getNumTotalPeople()  + " num agent Race: " + mob.getNumRace(a0.getRace()));
        System.out.println("move in block num agents: " + mib2.getNumTotalPeople() + " num agent Race: " + mib.getNumRace(a0.getRace()));
        */
     //   jumpHandler.updateBlockLayer(censusUnitHandler.getAllBlocks());
    }
    
    
}
