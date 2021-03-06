/*
 *  @author Robert Najlis
 *  Created on Mar 29, 2004
 *
 *
 */
package mirar;

import java.util.*;// Priyasree_Audit: Unnecessary import: import java.util.*_Unnecessary import: null

import cern.colt.list.IntArrayList;




/**
 *  BlockHistory
 */


public class BlockHistory {

    String stfid; // Priyasree_DeadCode : Unreachable code_
    int numVacantHousingUnits; // Priyasree_DeadCode : Unreachable code_
    IntArrayList raceAndIncomeList = new IntArrayList(); // Priyasree_DeadCode : Unreachable code_
    int stepNum; // Priyasree_DeadCode : Unreachable code_
    double medianRent; // Priyasree_DeadCode : Unreachable code_
    double neighPctBlk; // Priyasree_DeadCode : Unreachable code_
    double neighPctWht; // Priyasree_DeadCode : Unreachable code_
    
    double quantile1; // Priyasree_DeadCode : Unreachable code_
    double quantile3; // Priyasree_DeadCode : Unreachable code_
    double quantile5; // Priyasree_DeadCode : Unreachable code_
    double quantile6; // Priyasree_DeadCode : Unreachable code_
    double quantile9; // Priyasree_DeadCode : Unreachable code_
    
    
    /**
     * @param numVacantHousingUnits
     * @param raceAndIncomeList
     */
    public BlockHistory(int numVacantHousingUnits, IntArrayList raceAndIncomeList, double medianRent, double neighPctBlk, double neighPctWht) { // Priyasree_DeadCode : Unreachable code_
        super();
        this.numVacantHousingUnits = numVacantHousingUnits;
        this.raceAndIncomeList.addAllOf(raceAndIncomeList);
        this.stepNum = MirarUtils.STEP_NUM;
        this.medianRent = medianRent;
        this.neighPctBlk = neighPctBlk;
        this.neighPctWht = neighPctWht;
    }
    /**
     * @param stfid
     * @param numVacantHousingUnits
     * @param raceAndIncomeList
     */
    public BlockHistory(String stfid, int numVacantHousingUnits,
            IntArrayList raceAndIncomeList, double medianRent, double neighPctBlk, double neighPctWht) { // Priyasree_DeadCode : Unreachable code_
        super();
        this.stfid = stfid;
        this.numVacantHousingUnits = numVacantHousingUnits;
        this.raceAndIncomeList.addAllOf(raceAndIncomeList);
        this.stepNum = MirarUtils.STEP_NUM;
        this.medianRent = medianRent;
        this.neighPctBlk = neighPctBlk;
        this.neighPctWht = neighPctWht;
    }
    
    public BlockHistory(int numVacantHousingUnits,
            IntArrayList raceAndIncomeList, double medianRent, double neighPctBlk, double neighPctWht,
            double quantile1, double quantile3, double quantile5, double quantile6, double quantile9) { 
        super();
        this.numVacantHousingUnits = numVacantHousingUnits;
        this.raceAndIncomeList.addAllOf(raceAndIncomeList);
        this.stepNum = MirarUtils.STEP_NUM;
        this.medianRent = medianRent;
        this.neighPctBlk = neighPctBlk;
        this.neighPctWht = neighPctWht;
        this.quantile1 = quantile1;
        this.quantile3 = quantile3;
        this.quantile5 = quantile5;
        this.quantile6 = quantile6;
        this.quantile9 = quantile9;
    }
    
    public BlockHistory(BlockHistory bh, int stepNum) { 
        super();
        this.stfid = bh.getStfid();
        this.numVacantHousingUnits = bh.getNumVacantHousingUnits();
        this.raceAndIncomeList.addAllOf(bh.getRaceAndIncomeList());
        this.quantile1 = bh.quantile1;
        this.quantile3 = bh.quantile3;
        this.quantile5 = bh.quantile5;
        this.quantile6 = bh.quantile6;
        this.quantile9 = bh.quantile9;       
        this.stepNum = stepNum;
    }
    
    public int getNumVacantHousingUnits() { 
        return numVacantHousingUnits;
    }
   /* public void setNumVacantHousingUnits(int numVacantHousingUnits) { // Priyasree_DeadCode : Unreachable code_
        this.numVacantHousingUnits = numVacantHousingUnits;
    }*/
    public IntArrayList getRaceAndIncomeList() { 
        return raceAndIncomeList;
    }
    /*public void setRaceAndIncomeList(IntArrayList raceAndIncomeList) { // Priyasree_DeadCode : Unreachable code_
        this.raceAndIncomeList = raceAndIncomeList;
    }*/
    
    public String toString() { 
        StringBuffer s = new StringBuffer();
        s.append(stepNum + ",");
        s.append(" "); // Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.
        s.append(this.getNumVacantHousingUnits() + ",");
        s.append(" "); // Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.
        s.append(this.medianRent + ", ");
        s.append(this.neighPctBlk + ", ");
        s.append(this.neighPctWht + ", ");
        int listSize = raceAndIncomeList.size();
        for (int i=0; i<listSize; i++) {       	
            s.append(this.raceAndIncomeList.get(i)); 
            s.append(", ");
        }
        // delete space
       // s = s.deleteCharAt(s.length() - 1);
        // delete comma
        //s = s.deleteCharAt(s.length() - 1);
        
        // add quantiles
        s.append("" + this.quantile1);
        s.append(", " + this.quantile3);
        s.append(", " + this.quantile5);
        s.append(", " + this.quantile6);
        s.append(", " + this.quantile9);
        return s.toString();
    }
    public String getStfid() { 
        return stfid;
    }
    /*public void setStfid(String stfid) { // Priyasree_DeadCode : Unreachable code_
        this.stfid = stfid;
    }*/
    
    
	public int getStepNum() { 
		return stepNum;
	}
	
	/*public double getQuantile1(){ // Priyasree_DeadCode : Unreachable code_
		return quantile1; 
	}*/
	
	/*public double getQuantile3(){ // Priyasree_DeadCode : Unreachable code_
		return quantile3; 
	}*/
	
	/*public double getQuantile5(){ // Priyasree_DeadCode : Unreachable code_
		return quantile5; 
	}*/
	/*public double getQuantile6(){ // Priyasree_DeadCode : Unreachable code_
		return quantile6; 
	}*/
	/*public double getQuantile9(){ // Priyasree_DeadCode : Unreachable code_
		return quantile9; 
	}*/
	/*public void setStepNum(int stepNum) { // Priyasree_DeadCode : Unreachable code_
		this.stepNum = stepNum;
	}*/
}
