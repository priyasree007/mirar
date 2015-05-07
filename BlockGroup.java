
/** BlockGroup
 * 	has own BlockGroup number
 * 	has a list with the BlockNumbers of the Blocks in the group
 * 
 * 	@author Robert Najlis
 */

package mirar;
import cern.colt.list.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import cern.jet.random.Uniform;
import cern.jet.random.Normal;

public class BlockGroup {

    private int blockGroupNum; // Priyasree_DeadCode : Unreachable code_
    private int censusTractNum; // Priyasree_DeadCode : Unreachable code_
    
    private double numHousingUnits; // Priyasree_DeadCode : Unreachable code_
    private double numVacantUnits; // Priyasree_DeadCode : Unreachable code_
    private double numOccupiedUnits; // Priyasree_DeadCode : Unreachable code_
    private double vacancyRate; // Priyasree_DeadCode : Unreachable code_
    
    private int population; // Priyasree_DeadCode : Unreachable code_
    private int numWhite = 0; // Priyasree_DeadCode : Unreachable code_
    private int numBlack = 0; // Priyasree_DeadCode : Unreachable code_
    private int numAsian = 0; // Priyasree_DeadCode : Unreachable code_
    private int numHispanic = 0; // Priyasree_DeadCode : Unreachable code_
    private int numTotalPeople = 0; // Priyasree_DeadCode : Unreachable code_
    
    private double initNumWhite = 0.0; // Priyasree_DeadCode : Unreachable code_
    private double initNumBlack = 0.0; // Priyasree_DeadCode : Unreachable code_
    private double initNumAsian = 0.0; // Priyasree_DeadCode : Unreachable code_
    private double initNumHispanic = 0.0; // Priyasree_DeadCode : Unreachable code_
    
    private double totalIncome; // Priyasree_DeadCode : Unreachable code_
    
    private int numBlocks = 0; // Priyasree_DeadCode : Unreachable code_
    private double sampleProportion = .1; // Priyasree_DeadCode : Unreachable code_
    private int [] raceIncomeList; // Priyasree_DeadCode : Unreachable code_
    private ArrayList blockList = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    private CensusTract censusTract; // Priyasree_DeadCode : Unreachable code_
    private double ownerVacancyRate; // Priyasree_DeadCode : Unreachable code_
    private double renterVacancyRate; // Priyasree_DeadCode : Unreachable code_

    /*public BlockGroup() { // Priyasree_DeadCode : Unreachable code_
    }*/
    
    public BlockGroup(int blockGroupNum, CensusTract censusTract) { 
        this.blockGroupNum = blockGroupNum;
        this.censusTract = censusTract;
    }

    public BlockGroup(int blockGroupNum, int tractNum){ 
        this.blockGroupNum = blockGroupNum;
        this.censusTractNum = tractNum;
    }

    
    /**
     * @return the racial percentage of the majority race group + race num (see Agent for num for each race)
     */
    public void updateBlocksInfo() { 
        
        // clear out all variables
        numWhite = 0;
        numBlack = 0;
        numAsian = 0;
        numHispanic = 0;
        numTotalPeople = 0;
        totalIncome = 0;

        // go throught the blockList and add up the num of each race typ in each block
        
        for (int i=0; i<numBlocks; i++) {
            System.out.println("BG update - get block " + i);
            Block b = (Block)blockList.get(i);
            System.out.println("BG update: block: " + b.getSTFID());
            numWhite += b.getNumWhite();
            numBlack += b.getNumBlack();
            numAsian += b.getNumAsian();
            numHispanic += b.getNumHispanic();
            System.out.println("BG update: total p: " + numTotalPeople );
            numTotalPeople += (b.getNumWhite() + b.getNumBlack() + b.getNumAsian()+ b.getNumHispanic());
            System.out.println("BG update: total p: " + numTotalPeople );
            totalIncome += b.getTotalIncome();
        }
        
    }
    
    public double getRacePct() { 
        // white == 0
        //black == 1
        // asian == 2
        // hispanic ==3
        double pct = 0.0;
        
        // white is highest
        if (numWhite > numBlack && numWhite > numAsian
                && numWhite > numHispanic) {
            return ((numWhite*1.0)/(numTotalPeople*1.0));
        }
        // black is highest
        else if (numBlack > numWhite && numBlack > numAsian
                && numBlack > numHispanic) {
           return (1 + ((numBlack*1.0)/(numTotalPeople*1.0)));
        }
        // asian is highest
        else if (numAsian > numWhite && numAsian > numBlack
                && numAsian > numHispanic) {
           return (2 + ((numAsian*1.0)/(numTotalPeople*1.0)));
        }
        //hispanic is highest
        else if (numHispanic > numWhite && numHispanic > numBlack
                && numHispanic > numAsian) {
            return (3 + ((numHispanic*1.0)/(numTotalPeople*1.0)));
        }
        // all are equal
        else
            return -1.0;

    }

   
    public void initializeRenterHousingUnits(DoubleArrayList raceIncomeRent) throws FileNotFoundException { 
        
        int numBlocks = blockList.size();
        if (numBlocks <= 0) {
             System.out.println("BlockGroup: " + getBlockGroupNum() + " has no blocks");
             
         }
        raceIncomeList = new int[MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES];

        int rirSize = raceIncomeRent.size();
        int [] rirList = new int[rirSize];
        
        int bgTotalAgents = 0;
        int totalAgents = 0;
        int numAgents = 0;
        int vacantHousingUnitsToAdd = 0;
        
        for (int blockIndex =0; blockIndex<numBlocks; blockIndex++) {
            Block b = (Block) blockList.get(blockIndex);
            MirarUtils.BLOCKS.add(b);

                int raceIncomeSum = 0;
                int currentIndex = 0;
                int raceIncomeIndex = 0;
                int rirValue = 0;

                for (int raceIndex=0; raceIndex< MirarUtils.NUM_RACES; raceIndex++) {
                    for (int incomeIndex=0; incomeIndex<MirarUtils.NUM_INCOMES; incomeIndex++) {
                        raceIncomeIndex = (raceIndex*MirarUtils.NUM_INCOMES) + incomeIndex;;//((raceIndex +1) + (incomeIndex + 1) - 1); // Priyasree_Audit: Extra semicolon_Delete the extra semicolon.
                        for (int rentIndex=0; rentIndex<MirarUtils.NUM_RENTS_RENTERS; rentIndex++) {
                            currentIndex = ((raceIndex*(MirarUtils.NUM_INCOMES*MirarUtils.NUM_RENTS_RENTERS)) + (incomeIndex*MirarUtils.NUM_RENTS_RENTERS)) + rentIndex;//(((raceIndex +1) * (incomeIndex + 1)) -1);
                            
                            // set the race income rent number using the data sample parameter
                            double rirNum = (raceIncomeRent.get(currentIndex) * MirarUtils.RENTER_DATA_SAMPLE);
                            
                            rirValue = 0;
                            if (rirNum == 0.0) continue; //Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
                            else {    
                                rirValue = MirarUtils.probabilisticInterpolation(  (rirNum/numBlocks) );      
                                rirList[currentIndex] = rirValue;
                                raceIncomeSum += rirValue;
                                
                                for (int j=0; j<rirValue;j++) {
                                    // add tenure
                                    Agent a = AgentHandler.getInstance().addAgent(raceIndex,incomeIndex, b.getSTFID(), Agent.RENTER);
                                    
                                    // add hu set rent and agent
                                    if (a == null) System.out.println("3333  block group init renter hu - agent is null");
                                    HousingUnit hu = b.addOccupiedHousingUnit(MirarUtils.RENTER_RENTS[rentIndex], a);
                                    
                                    // for rent test
                                    a.setHousingUnitNum(hu.getHousingUnitNum());
                                    a.setHousingUnit(hu);
                                    if (hu.getAgent() == null) {
                                        System.out.println("\niiiiiiiii\n iiiiiiiiiiii    ==-----   init Renters hu agent is NULL!!!!! \n iiiiiiiiii\n");
                                    }
                                    totalAgents++;
                                }
                            }
                        } 
                        raceIncomeList[raceIncomeIndex] = raceIncomeSum;
                        raceIncomeSum = 0;
                    } // ~: end income loop                   
                } // ~: end race loop
                
           //######################
           // add vacant housing units
           //######################
                
              vacantHousingUnitsToAdd = MirarUtils.probabilisticInterpolation(totalAgents*(1.0+renterVacancyRate))-totalAgents;//*.01));
            for (int i =0; i<vacantHousingUnitsToAdd; i++) {
             
                HousingUnit hu = b.addVacantHousingUnit(HousingUnit.RENTED);
                DoubleArrayList rents = b.getRentList(Agent.RENTER);
                
                double rentVal = rents.get(Uniform.staticNextIntFromTo(0, rents.size()-1));
                hu.setRent(rentVal); // random draw from distribution of rents
            }
            b.setRaceIncomeRent_Renters(rirList);   
            bgTotalAgents +=totalAgents;
            if (totalAgents ==0 ) {
//PriyasreeComment System.out.println("\t\t ---------------------> block " + b.getSTFID() + " has no agents -- the rest of the block group " + this.getBlockGroupNum() + " has " + bgTotalAgents + " agents");
            }
            totalAgents = 0;
            rirList = null;
            rirList = new int[rirSize];
        } // ~: end block loop
        bgTotalAgents = 0;
    }
    
    public void initializeOwnerHousingUnits(DoubleArrayList raceIncomeRent) throws FileNotFoundException { 
        
        int numBlocks = blockList.size();
        if (numBlocks <= 0) {
        }
        raceIncomeList = new int[MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES];
       
        int rirSize = raceIncomeRent.size();
        int [] rirList = new int[rirSize];
        
        int bgTotalAgents = 0;
        int totalAgents = 0;
        int vacantHousingUnitsToAdd = 0;
        
        // for testing rents
        for (int blockIndex =0; blockIndex<numBlocks; blockIndex++) {
            Block b = (Block) blockList.get(blockIndex);
  
            int raceIncomeSum = 0;
            int currentIndex = 0;
            int raceIncomeIndex = 0;
            int rirValue = 0;
 
            for (int raceIndex=0; raceIndex< MirarUtils.NUM_RACES; raceIndex++) {
                for (int incomeIndex=0; incomeIndex<MirarUtils.NUM_INCOMES; incomeIndex++) {
                    raceIncomeIndex = (raceIndex*MirarUtils.NUM_INCOMES) + incomeIndex;;//((raceIndex +1) + (incomeIndex + 1) - 1); // Priyasree_Audit: Extra semicolon_Delete the extra semicolon.
                    for (int rentIndex=0; rentIndex<MirarUtils.NUM_RENTS_OWNERS; rentIndex++) {
                        currentIndex = ((raceIndex*(MirarUtils.NUM_INCOMES*MirarUtils.NUM_RENTS_OWNERS)) + (incomeIndex*MirarUtils.NUM_RENTS_OWNERS)) + rentIndex;//(((raceIndex +1) * (incomeIndex + 1)) -1);
                        
                        // set the race income rent number using the data sample parameter
                        double rirNum = (raceIncomeRent.get(currentIndex) * MirarUtils.OWNER_DATA_SAMPLE);
                                                
                        rirValue = 0;
                        if (rirNum == 0.0) continue; //Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
                        else {    
                            rirValue = MirarUtils.probabilisticInterpolation(  (rirNum/numBlocks) );          
                            rirList[currentIndex] = rirValue;
                            raceIncomeSum += rirValue;
                            
                            for (int j=0; j<rirValue;j++) {
                                // add tenure
                                Agent a = AgentHandler.getInstance().addAgent(raceIndex,incomeIndex, b.getSTFID(), Agent.OWNER);
                                
                                if (a == null) System.out.println("5555  block group init owner hu - agent is null");
                                // add hu set rent and agent
                                HousingUnit hu = b.addOccupiedHousingUnit(MirarUtils.OWNER_RENTS[rentIndex], a);
                                
                                // for rent test
                                a.setHousingUnitNum(hu.getHousingUnitNum());
                                a.setHousingUnit(hu);
                                if (hu.getAgent() == null) {
                                    System.out.println("\n ooooooooo \n  ooooooooo    ==-----   init Owners hu agent is NULL!!!!! \n ooooooo\n\n");
                                }
                                totalAgents++;
                            }
                        }//~: end add housing units             
                    } // ~: end rent loop
                    
                    // set the race and Income value obtained by summing the rent values for the race/Income category
                    raceIncomeList[raceIncomeIndex] = raceIncomeSum;
                    raceIncomeSum = 0;
                } // ~: end income loop              
            } // ~: end race loop

            vacantHousingUnitsToAdd = MirarUtils.probabilisticInterpolation(totalAgents*(1.0+ownerVacancyRate))-totalAgents;//*.01));
 
            for (int i =0; i<vacantHousingUnitsToAdd; i++) {
                HousingUnit hu = b.addVacantHousingUnit(HousingUnit.OWNED);
                DoubleArrayList rents = b.getRentList(Agent.OWNER);
                double rentVal = rents.get(Uniform.staticNextIntFromTo(0, rents.size()-1));
                hu.setRent(rentVal); // random draw from distribution of rents
            }

            b.setRaceIncomeRent_Owners(rirList);   
            bgTotalAgents +=totalAgents;
            if (totalAgents ==0 ) {
            }
            totalAgents = 0;
            rirList = null;
            rirList = new int[rirSize];
        } // ~: end block loop

        bgTotalAgents = 0;
    }
    
    public void addBlock(int blockNum) { 
        Block b = this.getBlock(blockNum);
        if (b == null) {
            b = new Block(blockNum, this);
        }
        blockList.add(b);//new Block(blockNum, this));
        
    }
    
    public void addBlock(int blockNum, String stfid) { 
        Block b = this.getBlock(blockNum);
        if (b == null) {
            b = new Block(blockNum, this);
            b.setSTFID(stfid);
        }
        blockList.add(b);//new Block(blockNum, this));
        numBlocks++;
        
    }
    
    public Block getBlock(int blockNum) { 
        for (int i=0; i<blockList.size(); i++) {
            if ( ((Block)blockList.get(i)).getBlockNum() == blockNum) {
                return (Block)blockList.get(i);
            }
        }
        return null;
    }
    
    public boolean hasBlock(int blockNum) { 
        for (int i=0; i<blockList.size(); i++) {
            if ( ((Block)blockList.get(i)).getBlockNum() == blockNum ) {
                return true;
            }
        }
        return false;
    }
    public ArrayList getBlockList() { 
        return blockList;
    } // end getBlocks

   
    /**
     * @return Returns the blockGroupNum.
     */
    public int getBlockGroupNum() {
        return blockGroupNum;
    }
    /**
     * @param groupNum The groupNum to set.
     */
    /*public void setBlockGroupNum(int groupNum) { // Priyasree_DeadCode : Unreachable code_
        this.blockGroupNum = blockGroupNum;
    }*/
    
    
    public double getMedianIncome(){ 
             return (totalIncome/(numTotalPeople*1.0));
     }
    
    /**
     * @return Returns the numAsian.
     */
    public int getNumAsian() { 
        return numAsian;
    }
    /**
     * @param numAsian The numAsian to set.
     */
    /*public void setNumAsian(int numAsian) { // Priyasree_DeadCode : Unreachable code_
        this.numAsian = convertRawData(numAsian);
    }*/
    /**
     * @return Returns the numBlack.
     */
    public int getNumBlack() { 
        return numBlack;
    }
    /**
     * @param numBlack The numBlack to set.
     */
    /*public void setNumBlack(int numBlack) { // Priyasree_DeadCode : Unreachable code_
        this.numBlack = convertRawData(numBlack);
    }*/
    /**
     * @return Returns the numHispanic.
     */
    public int getNumHispanic() { 
        return numHispanic;
    }
    /**
     * @param numHispanic The numHispanic to set.
     */
   /* public void setNumHispanic(int numHispanic) { // Priyasree_DeadCode : Unreachable code_
        this.numHispanic = convertRawData(numHispanic);
    }*/
    /**
     * @return Returns the numWhite.
     */
    public int getNumWhite() { 
        return numWhite;
    }
    /**
     * @param numWhite The numWhite to set.
     */
    /*public void setNumWhite(int numWhite) { // Priyasree_DeadCode : Unreachable code_
        this.numWhite = convertRawData(numWhite);
    }*/
    /**
     * @return Returns the population.
     */
    /*public int getPopulation() { // Priyasree_DeadCode : Unreachable code_
        return population;
    }*/
    /**
     * @param population The population to set.
     */
    /*public void setPopulation(int population) { // Priyasree_DeadCode : Unreachable code_
        this.population = convertRawData(population);
    }*/
    /**
     * @return Returns the raceIncomeList.
     */
    /*public int [] getRaceIncomeList() { // Priyasree_DeadCode : Unreachable code_
        return raceIncomeList;
    }*/
    
    
    /**
     * @param raceIncomeList The raceIncomeList to set.
     * deprecated - use setRaceIncomeList(int [] raceIncomeList)
     */
    /*public void setRaceIncomeList(String [] raceIncomeList) { // Priyasree_DeadCode : Unreachable code_
        for (int i=0; i<raceIncomeList.length; i++) {
            this.raceIncomeList[i] = Integer.parseInt(raceIncomeList[i]);
        }
    }*/
    
    /**
     * @param raceIncomeList The raceIncomeList to set.
     */
    /*public void setRaceIncomeList(int [] raceIncomeList) { // Priyasree_DeadCode : Unreachable code_
        this.raceIncomeList = raceIncomeList;
    }*/
    
    /**
     * @return Returns the censusTractNum.
     */
    /*public int getCensusTractNum() { // Priyasree_DeadCode : Unreachable code_
        return censusTractNum;
    }*/
    /**
     * @param censusTractNum The censusTractNum to set.
     */
    /*public void setCensusTractNum(int censusTractNum) { // Priyasree_DeadCode : Unreachable code_
        this.censusTractNum = censusTractNum;
    }*/
    /**
     * @return Returns the numHousingUnits.
     */
    /*public double getNumHousingUnits() { // Priyasree_DeadCode : Unreachable code_
        return numHousingUnits;
    }*/
    /**
     * @param numHousingUnits The numHousingUnits to set.
     */
    /*public void setNumHousingUnits(double numHousingUnits) { // Priyasree_DeadCode : Unreachable code_
        this.numHousingUnits = convertRawData(numHousingUnits);
    }*/
    /**
     * @return Returns the numOccupiedUnits.
     */
    /*public double getNumOccupiedUnits() { // Priyasree_DeadCode : Unreachable code_
        return numOccupiedUnits;
    }*/
    /**
     * @param numOccupiedUnits The numOccupiedUnits to set.
     */
    /*public void setNumOccupiedUnits(double numOccupiedUnits) { // Priyasree_DeadCode : Unreachable code_
        this.numOccupiedUnits = convertRawData(numOccupiedUnits);
       // System.out.println("BlockGroup " + this.blockGroupNum + " setNumOccupiedUnits: " + numOccupiedUnits);
    }*/
    /**
     * @return Returns the numVacantUnits.
     */
    /*public double getNumVacantUnits() { // Priyasree_DeadCode : Unreachable code_
        return numVacantUnits;
    }*/
    /**
     * @param numVacantUnits The numVacantUnits to set.
     */
    /*public void setNumVacantUnits(double numVacantUnits) { // Priyasree_DeadCode : Unreachable code_
        this.numVacantUnits = convertRawData(numVacantUnits);
    }*/
    
    /*public int convertRawData(int data) { // Priyasree_DeadCode : Unreachable code_
        //MirarUtils.show();
      //  double dData = data*censusUnitHandler.getSampleProportion();
       return  MirarUtils.probabilisticInterpolation(data*MirarUtils.OWNER_DATA_SAMPLE);//this.getSampleProportion());
    }*/
    
    /*public int convertRawData(double data) { // Priyasree_DeadCode : Unreachable code_
       return MirarUtils.probabilisticInterpolation(data*MirarUtils.OWNER_DATA_SAMPLE);//censusUnitHandler.getSampleProportion());
    }*/
    /*public CensusTract getCensusTract() { // Priyasree_DeadCode : Unreachable code_
        return censusTract;
    }*/
    /*public void setCensusTract(CensusTract censusTract) { // Priyasree_DeadCode : Unreachable code_
        this.censusTract = censusTract;
    }*/
    /*public double getSampleProportion() { // Priyasree_DeadCode : Unreachable code_
        return sampleProportion;
    }*/
    /*public void setSampleProportion(double sampleProportion) { // Priyasree_DeadCode : Unreachable code_
        this.sampleProportion = sampleProportion;
    }*/
    /*public void setBlockList(ArrayList blockList) { // Priyasree_DeadCode : Unreachable code_
        this.blockList = blockList;
    }*/
    /*public double getNumTotalPeople() { // Priyasree_DeadCode : Unreachable code_
        return (numWhite + numBlack + numAsian + numHispanic);
    }*/
    /*public void setNumTotalPeople(int numTotalPeople) { // Priyasree_DeadCode : Unreachable code_
        this.numTotalPeople = numTotalPeople;
    }*/
    /*public int getNumBlocks() { // Priyasree_DeadCode : Unreachable code_
        return numBlocks;
    }*/
    /*public void setNumBlocks(int numBlocks) { // Priyasree_DeadCode : Unreachable code_
        this.numBlocks = numBlocks;
    }*/
    public double getTotalIncome() { 
        return totalIncome;
    }
    /*public void setTotalIncome(double totalIncome) { // Priyasree_DeadCode : Unreachable code_
        this.totalIncome = totalIncome;
    }*/

    /**
     * @param i
     */
    public void setRenterVacancyRate(double rate) { 
        this.renterVacancyRate = rate;
    }
    public void setOwnerVacancyRate(double rate) { 
        this.ownerVacancyRate = rate;
    }

    /*public void initializeNumWhite(double num) { // Priyasree_DeadCode : Unreachable code_
        initNumWhite = num;
    }*/
   
    /*public void initializeNumBlack(double num) { // Priyasree_DeadCode : Unreachable code_
        initNumBlack = num;
    }*/
    
    /*public void initializeNumHispanic(double num) { // Priyasree_DeadCode : Unreachable code_
        initNumHispanic = num;
    }*/
    
    /*public void initializeNumAsian(double num) { // Priyasree_DeadCode : Unreachable code_
        initNumAsian = num;
    }*/
    
} // end BlockGroup
