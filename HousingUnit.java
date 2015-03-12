/** HousingUnit
 */
package mirar;

import java.util.*; // Priyasree_Audit: Unnecessary import: import java.util.*;_Unnecessary import: null

/**
* Housing units are nested within Census blocks. They can be empty 
 * (unoccupied), or contain agents. 
 * 
 * Housing units have rents associated with them. These rents are functions 
 * of the income distribution of agents in the neighborhood. 
 * 
 */


public class HousingUnit {
  //private int blockNum; // Priyasree_DeadCode : Unreachable code_
    private int housingUnitNum; // Priyasree_DeadCode : Unreachable code_
    private Agent agent; // Priyasree_DeadCode : Unreachable code_
  //private int agentNum; // Priyasree_DeadCode : Unreachable code_

  //private int race; // Priyasree_DeadCode : Unreachable code_
  //private int income; // Priyasree_DeadCode : Unreachable code_
    private double rent; // Priyasree_DeadCode : Unreachable code_
    private boolean occupied; // Priyasree_DeadCode : Unreachable code_
    private int  tenure; // Priyasree_DeadCode : Unreachable code_
    public static final int OWNED= 0; // Priyasree_DeadCode : Unreachable code_
    public static final int RENTED = 1; // Priyasree_DeadCode : Unreachable code_
    
    public Block block; // Priyasree_DeadCode : Unreachable code_
    public HousingUnit() { 
        this.occupied = false;    
    }
    
   /* public HousingUnit(int blockNum) { // Priyasree_DeadCode : Unreachable code_
        this.blockNum = blockNum;
    }*/

    /*public HousingUnit(boolean occupied) { // Priyasree_DeadCode : Unreachable code_
        this.occupied = occupied;
    }*/

    public HousingUnit(int housingUnitNum, Block block, int tenure) {
        this.housingUnitNum = housingUnitNum;
        this.block = block;
        this.occupied = false;
        this.tenure = tenure;
     //   System.out.println("new HU  block #: " + block.getBlockNum());
    }
    
    
    
   
    public Agent getAgent() { 
        return agent;
    }

    public void setAgent(Agent agent) { 
        if (agent == null ) System.out.println("11111 2222222  Housing Unit set AGent --  agent is nULL");
        this.agent = agent;
    }

    public void addAgent(Agent agent) { 	
        if (agent == null) System.out.println("000010101 Housing Unit - add Agent - agent is null");
        this.setOccupied(true);
        //this.setAgentNum(agent.getAgentNum());
        this.setAgent(agent);
//        this.setIncome(agent.getIncome)
        this.block.addAgent(agent);
        this.setTenure(agent.getTenure());
        //this.block.setUpdate(true);
       // this.block.updateHousingUnit(this);
       // System.out.println("add agent HU  block #: " + block.getBlockNum());
    }
    
    public void removeAgent() { // Priyasree_DeadCode : Unreachable code_
        
        this.setOccupied(false);
      //  this.setAgentNum(-1);
        
        if (this.agent == null) {
            System.out.println("4444  Housing Unit - remove agent - agent is null  CRAP");
            //return;
        }
        this.block.removeAgent(this.agent);
        this.block.setUpdate(true);
       // this.block.updateHousingUnit(this);
        this.agent = null;
    }
    
    
    /*public int getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(int agentNum) {
        this.agentNum = agentNum;
    }
*/
    public Block getBlock() {
        return block;
    }

    /*public void setBlock(Block block) { // Priyasree_DeadCode : Unreachable code_
        this.block = block;
    }*/

   /* public int getBlockNum() { // Priyasree_DeadCode : Unreachable code_
        return blockNum;
    }*/

    /*public void setBlockNum(int blockNum) { // Priyasree_DeadCode : Unreachable code_
        this.blockNum = blockNum;
    }*/

    public double getIncome() { 
        if (this.agent == null) return 0.0;
    //    System.out.println("\t ##  HU - agent.get Income : " + agent.getIncome());
        return this.agent.getIncome();
    }

   /* public void setIncome(int income) {
        this.income = income;
    }
*/
    /*public int getRace() { // Priyasree_DeadCode : Unreachable code_
        if (this.agent == null) return  -1;
        return this.agent.getRace();
    }*/

    //public void setRace(int race) {
     //   this.race = race;
   // }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) { 
        this.rent = rent;
    }
    public boolean isOccupied() { 
        return occupied;
    }
    public void setOccupied(boolean occupied) { 
        this.occupied = occupied;
     
    }
    public int getHousingUnitNum() { 
        return housingUnitNum;
    }
    /*public void setHousingUnitNum(int housingUnitNum) { // Priyasree_DeadCode : Unreachable code_
        this.housingUnitNum = housingUnitNum;
    }*/
    
    /*public boolean equals(HousingUnit hu) {// Priyasree_Audit: Wrong parameter type_Change the name of the method. // Priyasree_DeadCode : Unreachable code_
        
      //  System.out.println("====== compare houing units:");
      //  System.out.println("\tthis housing unit: " + this.block.getSTFID());
                
               // block.getBlockGroup().getCensusTractNum() + " " + 
               // this.block.getBlockGroupNum() + "  " + this.blockNum  + " " + housingUnitNum);
       // System.out.println("\tother housing unit: " + hu.getBlock().getBlockGroup().getCensusTractNum() + " " + 
         //       hu.getBlock().getBlockGroupNum() + "  " + hu.getBlockNum()  + " " + hu.getHousingUnitNum());
        
        if (this.block.getSTFID().equals(hu.getBlock().getSTFID()) && this.housingUnitNum == hu.getHousingUnitNum() ) { // Priyasree_Audit: An if-statement always return true or false_Replace the if-statement with a return-statement whose return value is the expression tested in the if-statement.
                //this.housingUnitNum == hu.getHousingUnitNum() &&
                //this.blockNum == hu.getBlockNum() &&
                //this.block.getBlockGroupNum() == hu.getBlock().getBlockNum() &&
                //this.block.getBlockGroup().getCensusTractNum() == hu.getBlock().getBlockGroup().getCensusTractNum() )
            return true;
        }
        else return false;
    }*/

    public int getTenure() { 
        return tenure;
    }

    public void setTenure(int tenure) { 
        this.tenure = tenure;
    }

    
} // end HousingUnit
