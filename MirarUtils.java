/*
 *  @author Robert Najlis
 *  Created on Feb 26, 2004
 *
 *
 */
package mirar;

import uchicago.src.sim.util.Random;
import java.util.*;
import cern.colt.list.DoubleArrayList;


public class MirarUtils {
    public static AgentDecision AGENT_DECISION; 
    public static boolean test = true;
    
    
    // owner
    public static double OWNER_VACANT_HOUSING_UNIT_SAMPLE=1.0;// = 1.0;
    public static double OWNER_AGENT_SAMPLE=0.2; //0.1;
    public static double OWNER_DATA_SAMPLE=0.2 ; // percent of all data used
    
    // renter
    public static double RENTER_VACANT_HOUSING_UNIT_SAMPLE=0.01;
    public static double RENTER_AGENT_SAMPLE=0.001; //0.1;
    public static double RENTER_DATA_SAMPLE = 0.2 ; // percent of all data used
 
    public static double ALL_VACANT_HOUSING_UNIT_SAMPLE = 1.0; 
    
    public static String AGENT_DECISION_STRING;// = new String();
    public static int DISPLAY_UPDATE_INTERVAL;
    public static int PRINT_INTERVAL = 10;
    public static int RENTS_UPDATE_INTERVAL = 2;
    public static boolean NO_GUI = true;
    public static int STEP_NUM;
    public static int STEPS_TO_RUN = 20;
    public static int RACE_INCOME_SIZE;
    public static int NUM_INCOMES;
    public static int NUM_RACES = 4;
    // string identifying which city is being used (for calculating housing market price updates!)
    public static String CITY ; 
    //public static String CITY = "Atlanta" ; 
    public static int NUM_RENTS_RENTERS;
    
    public static int NUM_RENTS_OWNERS;
    
    
    public static boolean AGENT_MEMORY = true;
    public static boolean BLOCK_HISTORY = true;
    
    public static double TOTAL_AGENT_INCOME;  //
    public static HashMap NUM_INCOMES_PER_TYPE;  //owners and renters together
    
    public static int TOTAL_NUM_AGENTS;
    public static DoubleArrayList INCOME_TYPES = new DoubleArrayList();
    
    public static String RENT_TYPE = "HomogeneousRentWithinBlocks";
    public static double HOMOGENEOUS_RENT_QUANTILE = 0.5;
    public static double OCCUPANCY_RATE;
    public static int NUM_AGENT_TYPES;

    
    //renters
    public static int []  RENTER_RACE_INCOME_NUMS = new int[NUM_AGENT_TYPES];  //is this used?
    public static double [] RENTER_RACE_INCOME_RENT;   
    public static double [] RENTER_RENTS;    // called in block group class
    public static double [] RENTER_INCOMES;    
    
    
    // owners
    public static int []  OWNER_RACE_INCOME_NUMS = new int[NUM_AGENT_TYPES];
    public static double [] OWNER_RACE_INCOME_RENT;
    public static double [] OWNER_RENTS;
    public static double [] OWNER_INCOMES;
    
    public static ArrayList BLOCKS = new ArrayList();
    public static int NUM_HOUSING_UNITS;
    
    
    
    // test variables
    public static boolean TEST_AGENTS_MOVE = false;
    public static boolean TEST_BLOCK_NEIGHBORHOOD = false;
    public static boolean TEST_UTILITIES = false;
    public static boolean SUBSET_DATA =true;
    
    public static String RENTER_DATA_FILE = null;
    public static String OWNER_DATA_FILE = null;
    
    
    public static String NEIGHBORHOOD_DATA_FILE = null;
    public static String BLOCK_SHP_FILE = null; 
    public static String BLOCK_GROUP_SHP_FILE = null;
    public static String CENSUS_TRACT_SHP_FILE = null;
    /**
     * This method takes a non-integer double, and computes both the integer
     * portion of that double (chopping off everything after the decimal) and
     * the remainder (everything after the decimal point). We then
     * draw a random uniform [0,1]; if the random uniform is less than or
     * equal to the remainder, we assign the double the integer value + 1. Ir 
     * the random uniform is greater than the remainder, we assign the double 
     * the integer value. 
     * 
     * @param num double
     * @return num.floor or num.ceil integer interpolation of double
     */
    public static int probabilisticInterpolation(double num) {
        int wholeNum = (int) num;
        double remainder = num - wholeNum;
        double randomNum = Random.uniform.nextDoubleFromTo(0, 1);
        if (randomNum <= remainder) {
            return wholeNum+1;
        }
        else
            return wholeNum;
    }
    /**
     * This version of probabilistic interpolation takes as its arguments an integer number of some
     * quantity (e.g., the number of black households) and a double sample proportion (e.g., sampling
     * at the rate of 5 percent). This function then computes 5 percent of the quantity (e.g, 
     * 5 percent of black households) and interpolates appropriately to deal with non integer values.
     * 
     * @param totalNum whole number of some quantity
     * @param sampleProportion proportion of quantity to sample
     * @return interpolated whole number sampled proportion 
     */
    public static int probabilisticInterpolation(int totalNum, double sampleProportion) {
        double num = totalNum*sampleProportion;
        int wholeNum = (int) num;
        double remainder = num - wholeNum;
        double randomNum = Random.uniform.nextDoubleFromTo(0, 1);
        if (randomNum <= remainder) {
            return wholeNum + 1;
        }
        else
            return wholeNum;
    }
/**
 * This function takes a list of probabilities, and samples one item from the
 * list with probabilities given by the list. We use the following algorithm to 
 * transform the multinomial into a uniform distribution (and then sample from
 * the uniform):
 * 
 * 1. create variables t1 and t2, t1[0]=0 and t2[0]=data[0]. next let 
 * each value of t1[i] be a lagged value of t2[i-1], and each value of t2 is 
 * t1[i]+ data[i]. thus the distance between t1[i] and t2[i] cab be interpreted
 * as the amount of probabilitity associated with the ith indexed item in the 
 * probability list. 
 * 
 * 2. generate a random uniform(0,1)
 * 
 * 3. if that random uniform falls between t1[i] and t2[i], return the index i. 
 * in other words, items with larger probabilities will have larger intervals 
 * between t1[i] and t2[i], and will therefore have a larger probability of being
 * sampled. 
 * 
 * @param data vector of transition probabilities
 * @return index of choice
 */
    public static int sampleFromMultinomialDistribution(ArrayList data) {
        double[] t1, t2;
        double x; // random number

        int transLength = data.size();
        t1 = new double[transLength];
        t2 = new double[transLength];
        t1[0] = 0.0;
        t2[0] = ((Double) data.get(0)).doubleValue();
        for (int i = 1; i < transLength; i++) {
            t1[i] = t2[i - 1];
            t2[i] = ((Double) data.get(i)).doubleValue() + t1[i];
        }

        x = Random.uniform.nextDoubleFromTo(0.0, 1.0);

System.out.println("this is x " + x); 
        for (int i = 0; i < transLength; i++) {
        	System.out.println("this is t1 " + t1[i] + " and this is t2: " + t2[i]); 
            if (x > t1[i] && x <= t2[i]) {
                
                return i;
            }
        }
        return -1;
    }
/**
 * 
 * @param data
 * @return index
 */
    public static int sampleFromMultinomialDistribution(DoubleArrayList data) {
        
        if (Double.isNaN(data.get(data.size()-1)) || Double.isNaN(data.get(data.size()-1)) ) {
            ErrorLog.getInstance().logError("MirarUtils#sampleFromMultinomialDistribution:DoubleArrayList -- utilty is infinite.");

            return -1;
        }
        int transLength = data.size();
        if (transLength == 0) return -1;
        
        double[] t1, t2;
        double x; // random number

        t1 = new double[transLength];
        t2 = new double[transLength];
        t1[0] = 0.0;
        t2[0] = data.get(0);
        for (int i = 1; i < transLength; i++) {
            t1[i] = t2[i - 1];
            t2[i] = data.get(i) + t1[i];
        }

        
        x = Random.uniform.nextDoubleFromTo(0, 1);
//System.out.println("this is x " + x); 
        for (int i = 0; i < transLength; i++) {
        //	System.out.println("this is t1 " + t1[i] + " and this is t2: " + t2[i]); 
            if (x > t1[i] && x <= t2[i]) {
            //    System.out.println("this is i " + i); 
                return i;
            }
        }
        ErrorLog.getInstance().logError("MirarUtils#sampleFromMultinomialDistribution:DoubleArrayList -- x did not fall within probability interval");
        return -1;
    }
    /**
     * Same as other multinomial methods, except data are read in as a string. 
     * 
     * @param data
     * @return
     */
    public static int sampleFromMultinomialDistribution(String [] data) {
        double[] t1, t2;
        double x; // random number
        int transLength = data.length;
        t1 = new double[transLength];
        t2 = new double[transLength];
        t1[0] = 0.0;
        t2[0] = Double.parseDouble(data[0]);
        for (int i = 1; i < transLength; i++) {
            t1[i] = t2[i - 1];
            t2[i] = Double.parseDouble(data[i]) + t1[i];
        }


        x = Random.uniform.nextDoubleFromTo(0, 1);            
        for (int i = 0; i < transLength; i++) {          
            if (x > t1[i] && x <= t2[i]) {
                return i;
            }
        }
        return -1;
    }
    
    public static int sampleFromMultinomialDistribution(double [] data) {
        double[] t1, t2;
        double x; // random number
        int transLength = data.length;
        t1 = new double[transLength];
        t2 = new double[transLength];
        t1[0] = 0.0;
        t2[0] = data[0];
        for (int i = 1; i < transLength; i++) {
            t1[i] = t2[i - 1];
            t2[i] = data[i] + t1[i];
           
        }

        x = Random.uniform.nextDoubleFromTo(0, 1);
        for (int i = 0; i < transLength; i++) {            
            if (x > t1[i] && x <= t2[i]) {
                return i;
            }
        }
        return -1;
    }
    
    public static int sampleFromMultinomialDistribution(int [] data) {

        double[] t1, t2;
        double x; // random number

        int transLength = data.length;
        t1 = new double[transLength];
        t2 = new double[transLength];
        t1[0] = 0.0;
        t2[0] = data[0];
        for (int i = 1; i < transLength; i++) {
            t1[i] = t2[i - 1];
            t2[i] = data[i] + t1[i];
           
        }

        x = Random.uniform.nextDoubleFromTo(0, 1);
        for (int i = 0; i < transLength; i++) {            
            if (x > t1[i] && x <= t2[i]) {
                return i;
            }
        }
        return 0;
    }
    
    /**
     * reads in a raw set of data values (e.g., utililities), converts them into 
     * transition probabilities, and then samples from the multinomial distribution. 
     * 
     * @param data string
     * @return index value
     */
    public static int sampleFromRawDistribution(String [] data) {
        double total = 0.0;
        double [] result = new double[data.length];
        for (int i=0;i<result.length;i++) {
            result[i] = Double.parseDouble(data[i]);
            total += result[i];
        }
        for (int i=0;i<result.length;i++) {
            result[i] = result[i]/total;
        }
  
        
        
        return sampleFromMultinomialDistribution(result);
    }
    
    public static int sampleFromRawDistribution(double [] data) {
        double total = 0.0;
        for (int i=0;i<data.length;i++) {           
            total += data[i];
        }
        for (int i=0;i<data.length;i++) {
            data[i] = data[i]/total;
        }
        
        return sampleFromMultinomialDistribution(data);
    }
    
    
    public static int sampleFromRawDistribution(DoubleArrayList data) {
        if (Double.isNaN(data.get(data.size()-1)) || Double.isNaN(data.get(data.size()-1)) ) {
            ErrorLog.getInstance().logError("MirarUtils#sampleFromRawDistribution:DoubleArrayList -- utilty is infinite.");
            return -1;
        }
        double total = 0.0;
        for (int i=0;i<data.size();i++) {           
            total += data.get(i);
        }
        if (MirarUtils.TEST_UTILITIES==true){ // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
        System.out.println("MirarUtils#sampleFromRawDistribution sum of all utilities is: " + total);
        }
        if (Double.isNaN(total) ) {
            ErrorLog.getInstance().logError("MirarUtils#sampleFromRawDistribution:DoubleArrayList -- total sum of utility is infinite.");
            return -1;
        }
        
        for (int i=0;i<data.size();i++) {
            data.set(i, (data.get(i)/total));// = data.get(i)/total;
        }
        
        return sampleFromMultinomialDistribution(data);
    }
    /**
     * randomly selects an index value from a list
     * 
     * @param list
     * @return index value
     */
    public static int randomlySelectIndex(ArrayList list) {
        int index = Random.uniform.nextIntFromTo(0, (list.size()-1));
        return index;
    }
    
   /**
    * Selects item with highest value in list (we should use the colt "max" method here)
    * 
    * 
    * @param data
    * @return index value
    */
    public static int selectOptimal(DoubleArrayList data) {
        double highest = 0.0;
        int selection = -1;
        int dataSize = data.size();
        for (int i=0; i<dataSize; i++) {
            if (data.get(i) > highest) {
                highest = data.get(i);
                selection = i;
            }
        }
        return selection;
    }
    
    
    public static void setRaceIncomeRent(DoubleArrayList rir) {
        MirarUtils.OWNER_RACE_INCOME_RENT = new double[rir.size()];
        for (int i=0; i<MirarUtils.OWNER_RACE_INCOME_RENT.length; i++) {
            MirarUtils.OWNER_RACE_INCOME_RENT[i] = rir.get(i);
        }
    }
    
    public static void setRenterRents(DoubleArrayList rentList) {
        int numRents = rentList.size();
        MirarUtils.RENTER_RENTS = new double[numRents];
        for (int i=0; i<numRents; i++) {
            MirarUtils.RENTER_RENTS[i] = rentList.get(i);
        }
    }
    public static void setOwnerRents(DoubleArrayList rentList) {
        int numRents = rentList.size();
        MirarUtils.OWNER_RENTS = new double[numRents];
        for (int i=0; i<numRents; i++) {
            MirarUtils.OWNER_RENTS[i] = rentList.get(i);
        }
    }
    
    public static void setRenterIncomes(DoubleArrayList incomeList) {
        int numIncomes = incomeList.size();
        MirarUtils.RENTER_INCOMES = new double[numIncomes];
        for (int i=0; i<numIncomes; i++) {
            MirarUtils.RENTER_INCOMES[i] = incomeList.get(i);
        }
        MirarUtils.initNumIncomesPerType();
    }
    public static void setOwnerIncomes(DoubleArrayList incomeList) {
        int numIncomes = incomeList.size();
        MirarUtils.OWNER_INCOMES = new double[numIncomes];
        for (int i=0; i<numIncomes; i++) {
            MirarUtils.OWNER_INCOMES[i] = incomeList.get(i);
        }
    }
    /**
     * Takes a set of raw values and converts them into probabilities. 
     * 
     * @param list of raw values
     * @return list of probabilities
     */
    public static DoubleArrayList convertToProbabilities(DoubleArrayList list) {
        double total = 0.0;
        DoubleArrayList probabilityList = new DoubleArrayList();
       int listSize = list.size();
       for (int i=0; i<listSize; i++) {
           total += list.get(i);
       }
        
       for (int i=0; i<listSize; i++) {
           probabilityList.add( (list.get(i)/ total) );
       } 
       return probabilityList;
    }
    
    /**
     * converts an income category to real valued income. 
     * 
     * @param incomeCategory
     * @param tenure 
     * @return
     */
    public static double incomeCategoryToIncome(int incomeCategory, int tenure) {
        if (tenure == Agent.RENTER) {
            if (MirarUtils.RENTER_INCOMES == null) MirarUtils.RENTER_INCOMES = new double[MirarUtils.NUM_INCOMES];
            return MirarUtils.RENTER_INCOMES[incomeCategory];
        }
        else {
            if (MirarUtils.OWNER_INCOMES == null) MirarUtils.OWNER_INCOMES = new double[MirarUtils.NUM_INCOMES];
            return MirarUtils.OWNER_INCOMES[incomeCategory];
        }
    }
    
    public static int incomeToIncomeCategory(double income, int tenure) {
        if (tenure == Agent.RENTER) {
            for (int i=0; i<MirarUtils.RENTER_INCOMES.length; i++) {
                if (MirarUtils.RENTER_INCOMES[i] == income) { // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
                    return i;
                }
            }
        }
        else {
            for (int i=0; i<MirarUtils.OWNER_INCOMES.length; i++) {
                if (MirarUtils.OWNER_INCOMES[i] == income) { // Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static double getIncomeSquared() {
        return MirarUtils.TOTAL_AGENT_INCOME*MirarUtils.TOTAL_AGENT_INCOME;
    }
    
    
//  this gives both renter and owner
    public static double getTotalAgentIncome() {
        return MirarUtils.TOTAL_AGENT_INCOME;
    }
    
    public static void subtractIncome(double income){
        //## don't need to do subtraction in version 1
        Double incomeObj = new Double(income);
        int numIncomeType = ((Integer)MirarUtils.NUM_INCOMES_PER_TYPE.get(incomeObj)).intValue();
        if (numIncomeType > 0) {
            
            MirarUtils.NUM_INCOMES_PER_TYPE.put(incomeObj, new Integer(numIncomeType - 1));
        }
        
        MirarUtils.TOTAL_AGENT_INCOME -= income;
        
    }
    
    
    // keeps track of total agent income (renter and owner)
    public static void addIncome(double income) {
        MirarUtils.TOTAL_AGENT_INCOME += income;
        Double incomeObj = new Double(income);
        
        // get the number of agents with this income 
        // each income is a unique identifier
        int numIncomeType = ((Integer)MirarUtils.NUM_INCOMES_PER_TYPE.get(incomeObj)).intValue();
        MirarUtils.NUM_INCOMES_PER_TYPE.put(incomeObj, new Integer(numIncomeType + 1));
    }
    
    public static void initNumIncomesPerType() {
        
      
        MirarUtils.NUM_INCOMES_PER_TYPE = new HashMap();
        for (int i=0; i<MirarUtils.RENTER_INCOMES.length; i++) {
            
            MirarUtils.NUM_INCOMES_PER_TYPE.put(new Double(MirarUtils.RENTER_INCOMES[i]), new Integer(0));
        }
       
            
    }
   
    
    // this gives both renter and owner
        public static int getNumAgentsOfIncomeType(double income) {
            return ((Integer)MirarUtils.NUM_INCOMES_PER_TYPE.get(new Double(income))).intValue();
            
        }
        
   
}
