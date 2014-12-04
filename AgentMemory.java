package mirar;

/**
 * The AgentMemory class holds the information as to where an agent was 
 * at a particular time step.
 * 
 * @author Robert Najlis
 * @version 1.0 
 */

public class AgentMemory {

    private int stepNum;
    private String stfid;
    private int housingUnitNum;
    
    
    public AgentMemory(int stepNum, String stfid, int housingUnitNum) { // Priyasree_DeadCode : Unreachable code_
        this.stepNum = stepNum;
        this.stfid = stfid;
        this.housingUnitNum = housingUnitNum;
    }
    
    public AgentMemory(AgentMemory memory, int stepNum) { // Priyasree_DeadCode : Unreachable code_
        this.stepNum = stepNum;
        this.stfid = memory.getSTFID();
        this.housingUnitNum = memory.getHousingUnitNum();
        
    }
    
    public int getHousingUnitNum() {
        return housingUnitNum;
    }
    
    public void setHousingUnitNum(int housingUnitNum) {
        this.housingUnitNum = housingUnitNum;
    }
    
    public int getStepNum() { // Priyasree_DeadCode : Unreachable code_
        return stepNum; 
    }
    
    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }
    
    public String getSTFID() {
        return stfid;
    }
    
    public void setSTFID(String stfid) {
        this.stfid = stfid;
    }
    
    public String toString() {
        return (this.stepNum + " " + this.stfid + " " + this.housingUnitNum);
    }
}
