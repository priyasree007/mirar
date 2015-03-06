
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
       // System.out.println("new blockGroup: " + this.censusTract.getCensusTractNum() + " "+ this.blockGroupNum);
    }

    public BlockGroup(int blockGroupNum, int tractNum){ 
        this.blockGroupNum = blockGroupNum;
        this.censusTractNum = tractNum;
       // raceIncomeList = new DoubleArrayList();
        //System.out.println("new blockGroup - c2: " + this.censusTract.getCensusTractNum() + " "+ this.blockGroupNum);
    }

  /*  public void addRaceIncomeList(String [] list) {
        for (int i=0; i<list.length; i++) {
            raceIncomeList.add(convertRawData(Double.parseDouble(list[i])));
        }
    }
    */
    
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
        //System.out.println("BG update: num blocks : " + numBlocks );
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
        
       // System.out.println("BG getRacePct: total p: " + numTotalPeople );
        // white is highest
        if (numWhite > numBlack && numWhite > numAsian
                && numWhite > numHispanic) {
          /* pct =  getPctWhite();
           if (pct == 1.0) {
               return (pct - 0.001); 
           }
           else return pct;*/
            return ((numWhite*1.0)/(numTotalPeople*1.0));
        }
        // black is highest
        else if (numBlack > numWhite && numBlack > numAsian
                && numBlack > numHispanic) {
            
         /*   pct =  getPctBlack();
            if (pct == 1.0) {
                return (1 + (pct - 0.001)); 
            }
            else return (1 + pct);*/
           return (1 + ((numBlack*1.0)/(numTotalPeople*1.0)));
        }
        // asian is highest
        else if (numAsian > numWhite && numAsian > numBlack
                && numAsian > numHispanic) {
            
         //   pct =  getPctAsian();
           /* if (pct == 1.0) {
                return (2 + (pct - 0.001)); 
            }
            else return (2 + pct);*/
           return (2 + ((numAsian*1.0)/(numTotalPeople*1.0)));
        }
        //hispanic is highest
        else if (numHispanic > numWhite && numHispanic > numBlack
                && numHispanic > numAsian) {
           // pct =  getPctHispanic();
          /*  if (pct == 1.0) {
                return (3 + (pct - 0.001)); 
            }
            else return (3 + pct);*/
            return (3 + ((numHispanic*1.0)/(numTotalPeople*1.0)));
        }
        // all are equal
        else
            return -1.0;

    }

   
    public void initializeRenterHousingUnits(DoubleArrayList raceIncomeRent) throws FileNotFoundException { 
        
        int numBlocks = blockList.size();
     //  System.out.println("BlockGroup#initHU: num Blocks   " + numBlocks);
        if (numBlocks <= 0) {
             System.out.println("BlockGroup: " + getBlockGroupNum() + " has no blocks");
             
         }
   //   System.out.println("BlockGroup#initializeHousingUnits --  MirarUtils.NUM_RACES  " + MirarUtils.NUM_RACES + "   MirarUtils.NUM_INCOMES  " + MirarUtils.NUM_INCOMES);
        raceIncomeList = new int[MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES];
       // double unitsPerBlock = numHousingUnits/blockList.size();
       // double occupiedUnitsPerBlock = numOccupiedUnits / blockList.size();
        // the raceIncomeRent list is a list of Doubles 
        // they need to be probabilisticically Interpolated to integers
    //    PrintWriter testDataOut = new PrintWriter(new FileOutputStream("testData.txt", true));
        
   //     PrintWriter blockOut = new PrintWriter(new FileOutputStream("blockAgentInitData.txt", true));
   //     blockOut.println("race income rent (rir) num,  numBlocks,  (rirNum/numBlocks),   rirValue");
        
      //  PrintWriter blockGroupOut = new PrintWriter(new FileOutputStream("blockGroupAgentInitData.txt", true));        
    //    blockGroupOut.println("CensusTract #,  " + " BlockGroup # , " + " Num Blocks , " + " total number agents, " + " vacancy Rate");

       // PrintWriter agentTypeOut = new PrintWriter(new FileOutputStream("agentTypeInitData.txt", true));
       // StringBuffer agentTypeBuff = new StringBuffer();

       // PrintWriter blockVacanciesOut = new PrintWriter(new FileOutputStream("blockVacanciesData.txt", true));        
//        blockVacanciesOut.println("block stfid, num vacant units, total num units per block, vacancy rate");
        
       /// PrintWriter blockRentsOut = new PrintWriter(new FileOutputStream("blockRentsData.txt", true));   
   //     blockRentsOut.println("block stfid, num rents per category - 50, 124.5, 174.5, 224.5, 274.5, 324.5, 374.5, 424.5, 474.5, 524.5, 574.5, 624.5, 674.5, 724.5, 874.5, 1000");
        
        
        //agentTypeOut.println("CensusTract : " + this.getCensusTract().getCensusTractNum() + " BlockGroup:  " + this.getBlockGroupNum() + " Num blocks: " + this.getBlockList().size());
     
        /*for (int i=0; i<raceIncomeRent.size(); i++) {
            agentTypeBuff.append(raceIncomeRent.get(i) + ", ");
        }
        agentTypeBuff.deleteCharAt(agentTypeBuff.length()-1);
        agentTypeBuff.append("\n");*/
       // agentTypeOut.println(raceIncomeRent.toString());
     //   StringBuffer dataStringBuffer = new StringBuffer();
        // rir is the array of probabilistically interpolated integers holding values for the block
      //  System.out.println("BlockGroup#initializeHousingUnits - raceIncomeRent.size(): " + raceIncomeRent.size());
        int rirSize = raceIncomeRent.size();
        int [] rirList = new int[rirSize];
        
        int bgTotalAgents = 0;
        int totalAgents = 0;
        int numAgents = 0;
        int vacantHousingUnitsToAdd = 0;
        
        // for testing rents
       // int [] vacantRentTest = new int[MirarUtils.NUM_RENTS_RENTERS];
        //int [] occupiedRentTest = new int[MirarUtils.NUM_RENTS_RENTERS];
        
        for (int blockIndex =0; blockIndex<numBlocks; blockIndex++) {
         //   for (int i=0; i<rirSize; i++) {
                // interpolate rir's
                //       double rirValue = ((Double)raceIncomeRent.get(i)).doubleValue();
                // set value in race income list
         //   System.out.println("BG#intiHU: getBlock " + blockIndex);
            Block b = (Block) blockList.get(blockIndex);
            
            // this adds the blocks to the global list -  only needs to be done once (not in renter and owner)
            MirarUtils.BLOCKS.add(b);
          //  System.out.println("BG inti HU: block stfid: " + b.getSTFID());
   //         agentTypeBuff.append("block:" + b.getSTFID() + "\n");
           // agentTypeBuff.append("[");
           // try {
               
                //mirarOut.println("NumSteps " + MirarUtils.STEP_NUM);
           
            
            
                int raceIncomeSum = 0;
                int currentIndex = 0;
                int raceIncomeIndex = 0;
                int rirValue = 0;
            //     System.out.println("MirarUtils.NUM_RACES : " + MirarUtils.NUM_RACES +  "MirarUtils.NUM_RENTS_RENTERS : " + MirarUtils.NUM_RENTS_RENTERS + "MirarUtils.NUM_INCOMES : " + MirarUtils.NUM_INCOMES);
            //     System.out.println("raceIncomeRent.size()  RENTER  : " + raceIncomeRent.size() );
                for (int raceIndex=0; raceIndex< MirarUtils.NUM_RACES; raceIndex++) {
                    for (int incomeIndex=0; incomeIndex<MirarUtils.NUM_INCOMES; incomeIndex++) {
                        raceIncomeIndex = (raceIndex*MirarUtils.NUM_INCOMES) + incomeIndex;;//((raceIndex +1) + (incomeIndex + 1) - 1); // Priyasree_Audit: Extra semicolon_Delete the extra semicolon.
                        for (int rentIndex=0; rentIndex<MirarUtils.NUM_RENTS_RENTERS; rentIndex++) {
                            currentIndex = ((raceIndex*(MirarUtils.NUM_INCOMES*MirarUtils.NUM_RENTS_RENTERS)) + (incomeIndex*MirarUtils.NUM_RENTS_RENTERS)) + rentIndex;//(((raceIndex +1) * (incomeIndex + 1)) -1);
                            
                            // set the race income rent number using the data sample parameter
                            double rirNum = (raceIncomeRent.get(currentIndex) * MirarUtils.RENTER_DATA_SAMPLE);
                            
                            rirValue = 0;
                            if (rirNum == 0.0) continue; //Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
                         //   if (rirNum != 0) 
                            else {    
                                rirValue = MirarUtils.probabilisticInterpolation(  (rirNum/numBlocks) );   
                                
                        //    System.out.print((rirNum/numBlocks)+"----"+rirValue+ "  ");
              
                     //           blockOut.println(rirNum + ", " + numBlocks + ", " + (rirNum/numBlocks) + ", " +  rirValue);
                                //( ((Double)raceIncomeRent.get(currentIndex)).doubleValue() / numBlocks ) );
                       //   System.out.println(" \t\t\t BG init HU figure num Agents  block " + b.getSTFID() + " rirNum " + raceIncomeRent.get(currentIndex) + "  :  numBlocks  " + numBlocks + " (rirNum/numBlocks) " + (rirNum/numBlocks) + "  rirVal : " + rirValue);        
                                rirList[currentIndex] = rirValue;
                     //           agentTypeBuff.append(rirValue + ", ");
                                raceIncomeSum += rirValue;
                                
                                for (int j=0; j<rirValue;j++) {
                                    // add housing unit
                                    
                  //                  System.out.println("BlockGroup#initializeHousingUnits - Agenthandler - addAgent");
                                    // add tenure
                                    Agent a = AgentHandler.getInstance().addAgent(raceIndex,incomeIndex, b.getSTFID(), Agent.RENTER);
                                    
                                    // add hu set rent and agent
                  //                  System.out.println("BlockGroup#initializeHousingUnits: - block add HousingUnit");
                                    if (a == null) System.out.println("3333  block group init renter hu - agent is null");
                                    HousingUnit hu = b.addOccupiedHousingUnit(MirarUtils.RENTER_RENTS[rentIndex], a);
                                    
                                    // for rent test
                                //    occupiedRentTest[rentIndex] += 1;
                                    
                     //               System.out.println("BlockGroup#initializeHousingUnits - get housingUnit by NUM");
                                    a.setHousingUnitNum(hu.getHousingUnitNum());
                        //            System.out.println("BlockGroup#initializeHousingUnits - set Housing Unit in AGent");
                                    a.setHousingUnit(hu);
                                    if (hu.getAgent() == null) {
                                        System.out.println("\niiiiiiiii\n iiiiiiiiiiii    ==-----   init Renters hu agent is NULL!!!!! \n iiiiiiiiii\n");
                                    }
                                    // allocate appropriate agent type 
                                    
                                    //      System.out.println("Blcok Gropup allocate HUs: agent Num " + a.getAgentNum() );
                                    
                                    //    b.populateHousingUnit(j, a);
                                    totalAgents++;
                                }
                            }//~: end add housing units
                                    //      raceIncomeSum += rirList[i];//((Integer)raceIncomeRent.get(currentIndex)).intValue();
                            //System.out.println("Med: race Income Values :  raceIndex " + raceIndex + "  incomeIndex " + incomeIndex  +  " rentIndex " + rentIndex + "    currentIndex " + currentIndex + " -->  " + ((Integer)raceIncomeRent.get(currentIndex)).intValue());
                            //System.out.println();
                                        //System.out.println("current Index: " + currentIndex + "    rirSum  " + raceIncomeSum);
                                        
                        } // ~: end rent loop
                        
                        // set the race and Income value obtained by summing the rent values for the race/Income category
                       // System.out.println("BlockGroup#initializeHousingUnits set raceIncomeList in BG - raceIncomeList.length : " + raceIncomeList.length);
                        raceIncomeList[raceIncomeIndex] = raceIncomeSum;
                        
              
                        // System.out.println("Med: race Income Values :  raceIndex " + raceIndex + "  incomeIndex " + incomeIndex  + "   riiIndex " + raceIncomeIndex + " -->  " + raceIncomeValues[raceIncomeIndex]);
                        raceIncomeSum = 0;
                    } // ~: end income loop
                    
                } // ~: end race loop
               
                
                
                
           //######################
           // add vacant housing units
           //######################
                
            /*number of housing units (occupied and vacant) in block =
             * (number of occupied housing units in block)*1.(vacancy_rate in associated block group)
             */ 
           //  double numHousingUnits = totalAgents*(1.0 + vacancyRate);
              //System.out.println("vacancy rate is: " + vacancyRate);  
              
             //   System.out.println("BlockGroup#initializeHousingUnits - vacantHU to add");
              vacantHousingUnitsToAdd = MirarUtils.probabilisticInterpolation(totalAgents*(1.0+renterVacancyRate))-totalAgents;//*.01));
             // blockVacanciesOut.println(b.getSTFID() + ", " + vacantHousingUnitsToAdd + ", " + totalAgents + ", " + vacancyRate);
              //System.out.println("BG init HU: vac HU to add:  " + vacantHousingUnitsToAdd + " totalAgents: " + totalAgents + " vacancyRate " + vacancyRate);
            for (int i =0; i<vacantHousingUnitsToAdd; i++) {
              //  Block b = (Block) blockList.get(blockIndex);
                
                HousingUnit hu = b.addVacantHousingUnit(HousingUnit.RENTED);
                DoubleArrayList rents = b.getRentList(Agent.RENTER);
                //int rentIndex = 
                double rentVal = rents.get(Uniform.staticNextIntFromTo(0, rents.size()-1));
                hu.setRent(rentVal); // random draw from distribution of rents
                
                // testing rent --
                
                /*for (int rentCheck=0; rentCheck<MirarUtils.NUM_RENTS_RENTERS; rentCheck++) {
                    if (MirarUtils.RENTER_RENTS[rentCheck] == rentVal){
                        vacantRentTest[rentCheck] += 1;
              //          System.out.println("\t\t vaca unit rent check: mu " + MirarUtils.RENTER_RENTS[rentCheck] + "  rent val "+ rentVal);
                        break;
                    }
                }*/
                
                //  hu.setRent(rents.get(MirarUtils.sampleFromRawDistribution(rents)));
            }
         //   System.out.println("BlockGroup#initializeHousingUnits: block setRaceIncomeRent");
            b.setRaceIncomeRent_Renters(rirList);   
            bgTotalAgents +=totalAgents;
            if (totalAgents ==0 ) {
//PriyasreeComment System.out.println("\t\t ---------------------> block " + b.getSTFID() + " has no agents -- the rest of the block group " + this.getBlockGroupNum() + " has " + bgTotalAgents + " agents");
            }
      //      System.out.println("\t\t +++ BG inithu - block " + b.getSTFID() + " numAgents  " + totalAgents);
            totalAgents = 0;
            rirList = null;
      //      System.out.println("BlockGroup#initializeHousingUnits - rirList = new int[rirSize] " + rirSize);
            rirList = new int[rirSize];
            //System.out.println("BlockGroup end init HU:  STFID: " + b.getSTFID());
  //          agentTypeBuff.deleteCharAt(agentTypeBuff.length()-1);
           // agentTypeBuff.append("]\n");
     //       agentTypeBuff.append("\n");
        /*    blockRentsOut.print(b.getSTFID() + "");
            for (int i=0;i<occupiedRentTest.length; i++) {
                blockRentsOut.print(", " + occupiedRentTest[i]);
            }
            for (int i=0;i<vacantRentTest.length; i++) {
                blockRentsOut.print(", " + vacantRentTest[i]);
            }
            blockRentsOut.println();*/
        } // ~: end block loop
       // blockGroupOut.println(this.getCensusTract().getCensusTractNum() + ", "  +  this.getBlockGroupNum() + ", " + this.blockList.size() + ", " + bgTotalAgents + ", " + vacancyRate );
        bgTotalAgents = 0;
          //  testDataOut.close();
        
        //System.out.println("\t\t ---------  BlockGroup total agents: " + bgTotalAgents);
        
        
        //MirarUtils.addRaceIncomeList(raceIncomeList);
        
        
        
        
        // go through add hu's for each agent - totalAgents
        // add vacant hu's based on interpolation of (totalAgents*vacancyRate)
        
         //else {
        
        
         // each block - allocate the correct number of housing units
         // assign agents for each occupied unit
        /*agentTypeOut.println(agentTypeBuff.toString());
        blockOut.flush();
        blockGroupOut.flush();
        blockOut.close();
        blockGroupOut.close();
        agentTypeOut.flush();
        agentTypeOut.close();
        blockVacanciesOut.flush();
        blockVacanciesOut.close();
        
        blockRentsOut.flush();
        blockRentsOut.close();*/
    }
    
    public void initializeOwnerHousingUnits(DoubleArrayList raceIncomeRent) throws FileNotFoundException { 
        
        int numBlocks = blockList.size();
     //   System.out.println("BlockGroup#initHU  owner   : num Blocks   " + numBlocks);
        if (numBlocks <= 0) {
      //      System.out.println("BlockGroup: " + getBlockGroupNum() + " has no blocks");
            
        }
        //     System.out.println("BlockGroup#initializeHousingUnits --  MirarUtils.NUM_RACES  " + MirarUtils.NUM_RACES + "   MirarUtils.NUM_INCOMES  " + MirarUtils.NUM_INCOMES);
        raceIncomeList = new int[MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES];
       
        // rir is the array of probabilistically interpolated integers holding values for the block
        //  System.out.println("BlockGroup#initializeHousingUnits - raceIncomeRent.size(): " + raceIncomeRent.size());
        int rirSize = raceIncomeRent.size();
        int [] rirList = new int[rirSize];
        
        int bgTotalAgents = 0;
        int totalAgents = 0;
//        int numAgents = 0;
        int vacantHousingUnitsToAdd = 0;
        
        // for testing rents
        // int [] vacantRentTest = new int[MirarUtils.NUM_RENTS_RENTERS];
        //int [] occupiedRentTest = new int[MirarUtils.NUM_RENTS_RENTERS];
        
        for (int blockIndex =0; blockIndex<numBlocks; blockIndex++) {
            //   for (int i=0; i<rirSize; i++) {
            // interpolate rir's
            //       double rirValue = ((Double)raceIncomeRent.get(i)).doubleValue();
            // set value in race income list
            //   System.out.println("BG#intiHU: getBlock " + blockIndex);
            Block b = (Block) blockList.get(blockIndex);
            
            //taking this out for trial ---- Sep 19, 2005
        //    MirarUtils.BLOCKS.add(b);
         
            
            
            //  System.out.println("BG inti HU: block stfid: " + b.getSTFID());
            //         agentTypeBuff.append("block:" + b.getSTFID() + "\n");
            // agentTypeBuff.append("[");
            // try {
            
            //mirarOut.println("NumSteps " + MirarUtils.STEP_NUM);
            
            
            
            int raceIncomeSum = 0;
            int currentIndex = 0;
            int raceIncomeIndex = 0;
            int rirValue = 0;
       //     System.out.println("MirarUtils.NUM_RACES : " + MirarUtils.NUM_RACES +  "MirarUtils.NUM_RENTS_OWNERS : " + MirarUtils.NUM_RENTS_OWNERS + "MirarUtils.NUM_INCOMES : " + MirarUtils.NUM_INCOMES);
       //     System.out.println("raceIncomeRent.size()  owner   : " + raceIncomeRent.size() );
            for (int raceIndex=0; raceIndex< MirarUtils.NUM_RACES; raceIndex++) {
                for (int incomeIndex=0; incomeIndex<MirarUtils.NUM_INCOMES; incomeIndex++) {
                    raceIncomeIndex = (raceIndex*MirarUtils.NUM_INCOMES) + incomeIndex;;//((raceIndex +1) + (incomeIndex + 1) - 1); // Priyasree_Audit: Extra semicolon_Delete the extra semicolon.
                    for (int rentIndex=0; rentIndex<MirarUtils.NUM_RENTS_OWNERS; rentIndex++) {
                        currentIndex = ((raceIndex*(MirarUtils.NUM_INCOMES*MirarUtils.NUM_RENTS_OWNERS)) + (incomeIndex*MirarUtils.NUM_RENTS_OWNERS)) + rentIndex;//(((raceIndex +1) * (incomeIndex + 1)) -1);
                        
                        // set the race income rent number using the data sample parameter
                       // System.out.println("8888     raceIncomeRent  size  ---  "  + raceIncomeRent.size());
                        double rirNum = (raceIncomeRent.get(currentIndex) * MirarUtils.OWNER_DATA_SAMPLE);
                        
                        
                        rirValue = 0;
                        if (rirNum == 0.0) continue; //Priyasree_Audit: Cannot compare floating-point values using the equals (==) operator_Compare the two float values to see if they are close in value.
                        //   if (rirNum != 0) 
                        else {    
                            rirValue = MirarUtils.probabilisticInterpolation(  (rirNum/numBlocks) );   
                            
                            //                   System.out.print((rirNum/numBlocks)+"----"+rirValue+ "  ");
                            
                            //           blockOut.println(rirNum + ", " + numBlocks + ", " + (rirNum/numBlocks) + ", " +  rirValue);
                            //( ((Double)raceIncomeRent.get(currentIndex)).doubleValue() / numBlocks ) );
                            //              System.out.println(" \t\t\t BG init HU figure num Agents  block " + b.getSTFID() + " rirNum " + raceIncomeRent.get(currentIndex) + "  :  numBlocks  " + numBlocks + " (rirNum/numBlocks) " + (rirNum/numBlocks) + "  rirVal : " + rirValue);        
                            rirList[currentIndex] = rirValue;
                            //           agentTypeBuff.append(rirValue + ", ");
                            raceIncomeSum += rirValue;
                            
                            for (int j=0; j<rirValue;j++) {
                                // add housing unit
                                
                                //                  System.out.println("BlockGroup#initializeHousingUnits - Agenthandler - addAgent");
                                // add tenure
                                Agent a = AgentHandler.getInstance().addAgent(raceIndex,incomeIndex, b.getSTFID(), Agent.OWNER);
                                
                                if (a == null) System.out.println("5555  block group init owner hu - agent is null");
                                // add hu set rent and agent
                                //                  System.out.println("BlockGroup#initializeHousingUnits: - block add HousingUnit");
                                HousingUnit hu = b.addOccupiedHousingUnit(MirarUtils.OWNER_RENTS[rentIndex], a);
                                
                                // for rent test
                                //    occupiedRentTest[rentIndex] += 1;
                                
                                //               System.out.println("BlockGroup#initializeHousingUnits - get housingUnit by NUM");
                                a.setHousingUnitNum(hu.getHousingUnitNum());
                                //            System.out.println("BlockGroup#initializeHousingUnits - set Housing Unit in AGent");
                                a.setHousingUnit(hu);
                                if (hu.getAgent() == null) {
                                    System.out.println("\n ooooooooo \n  ooooooooo    ==-----   init Owners hu agent is NULL!!!!! \n ooooooo\n\n");
                                }
                                // allocate appropriate agent type 
                                
                                //      System.out.println("Blcok Gropup allocate HUs: agent Num " + a.getAgentNum() );
                                
                                //    b.populateHousingUnit(j, a);
                                totalAgents++;
                            }
                        }//~: end add housing units
                        //      raceIncomeSum += rirList[i];//((Integer)raceIncomeRent.get(currentIndex)).intValue();
                        //System.out.println("Med: race Income Values :  raceIndex " + raceIndex + "  incomeIndex " + incomeIndex  +  " rentIndex " + rentIndex + "    currentIndex " + currentIndex + " -->  " + ((Integer)raceIncomeRent.get(currentIndex)).intValue());
                        //System.out.println();
                        //System.out.println("current Index: " + currentIndex + "    rirSum  " + raceIncomeSum);
                        
                    } // ~: end rent loop
                    
                    // set the race and Income value obtained by summing the rent values for the race/Income category
                    // System.out.println("BlockGroup#initializeHousingUnits set raceIncomeList in BG - raceIncomeList.length : " + raceIncomeList.length);
                    raceIncomeList[raceIncomeIndex] = raceIncomeSum;
                    
                    
                    // System.out.println("Med: race Income Values :  raceIndex " + raceIndex + "  incomeIndex " + incomeIndex  + "   riiIndex " + raceIncomeIndex + " -->  " + raceIncomeValues[raceIncomeIndex]);
                    raceIncomeSum = 0;
                } // ~: end income loop
                
            } // ~: end race loop
            
            
            
            
            
            /*number of housing units (occupied and vacant) in block =
             * (number of occupied housing units in block)*1.(vacancy_rate in associated block group)
             */ 
            //  double numHousingUnits = totalAgents*(1.0 + vacancyRate);
            //System.out.println("vacancy rate is: " + vacancyRate);  
            
      //      System.out.println("BlockGroup#initializeHousingUnits - vacantHU to add");
            vacantHousingUnitsToAdd = MirarUtils.probabilisticInterpolation(totalAgents*(1.0+ownerVacancyRate))-totalAgents;//*.01));
            // blockVacanciesOut.println(b.getSTFID() + ", " + vacantHousingUnitsToAdd + ", " + totalAgents + ", " + vacancyRate);
            //System.out.println("BG init HU: vac HU to add:  " + vacantHousingUnitsToAdd + " totalAgents: " + totalAgents + " vacancyRate " + vacancyRate);
            for (int i =0; i<vacantHousingUnitsToAdd; i++) {
                //  Block b = (Block) blockList.get(blockIndex);
                
                HousingUnit hu = b.addVacantHousingUnit(HousingUnit.OWNED);
                DoubleArrayList rents = b.getRentList(Agent.OWNER);
                //int rentIndex = 
                double rentVal = rents.get(Uniform.staticNextIntFromTo(0, rents.size()-1));
                hu.setRent(rentVal); // random draw from distribution of rents
                
                // testing rent --
                
                /*for (int rentCheck=0; rentCheck<MirarUtils.NUM_RENTS_RENTERS; rentCheck++) {
                 if (MirarUtils.RENTER_RENTS[rentCheck] == rentVal){
                 vacantRentTest[rentCheck] += 1;
                 //          System.out.println("\t\t vaca unit rent check: mu " + MirarUtils.RENTER_RENTS[rentCheck] + "  rent val "+ rentVal);
                  break;
                  }
                  }*/
                
                //  hu.setRent(rents.get(MirarUtils.sampleFromRawDistribution(rents)));
            }
   //         System.out.println("BlockGroup#initializeHousingUnits: block setRaceIncomeRent");
            b.setRaceIncomeRent_Owners(rirList);   
            bgTotalAgents +=totalAgents;
            if (totalAgents ==0 ) {
          //      System.out.println("\t\t ---------------------> block " + b.getSTFID() + " has no agents -- the rest of the block group " + this.getBlockGroupNum() + " has " + bgTotalAgents + " agents");
            }
          //  System.out.println("\t\t +++ BG inithu - block " + b.getSTFID() + " numAgents  " + totalAgents);
            totalAgents = 0;
            rirList = null;
       //     System.out.println("BlockGroup#initializeHousingUnits - rirList = new int[rirSize] " + rirSize);
            rirList = new int[rirSize];
            //System.out.println("BlockGroup end init HU:  STFID: " + b.getSTFID());
            //          agentTypeBuff.deleteCharAt(agentTypeBuff.length()-1);
            // agentTypeBuff.append("]\n");
            //       agentTypeBuff.append("\n");
            /*    blockRentsOut.print(b.getSTFID() + "");
             for (int i=0;i<occupiedRentTest.length; i++) {
             blockRentsOut.print(", " + occupiedRentTest[i]);
             }
             for (int i=0;i<vacantRentTest.length; i++) {
             blockRentsOut.print(", " + vacantRentTest[i]);
             }
             blockRentsOut.println();*/
        } // ~: end block loop
        // blockGroupOut.println(this.getCensusTract().getCensusTractNum() + ", "  +  this.getBlockGroupNum() + ", " + this.blockList.size() + ", " + bgTotalAgents + ", " + vacancyRate );
        bgTotalAgents = 0;
        //  testDataOut.close();
        
        //System.out.println("\t\t ---------  BlockGroup total agents: " + bgTotalAgents);
        
        
        //MirarUtils.addRaceIncomeList(raceIncomeList);
        
        
        
        
        // go through add hu's for each agent - totalAgents
        // add vacant hu's based on interpolation of (totalAgents*vacancyRate)
        
        //else {
        
        
        // each block - allocate the correct number of housing units
        // assign agents for each occupied unit
        /*agentTypeOut.println(agentTypeBuff.toString());
         blockOut.flush();
         blockGroupOut.flush();
         blockOut.close();
         blockGroupOut.close();
         agentTypeOut.flush();
         agentTypeOut.close();
         blockVacanciesOut.flush();
         blockVacanciesOut.close();
         
         blockRentsOut.flush();
         blockRentsOut.close();*/
    }
    
    /*public void allocateHousingUnits() {
        int numBlocks = blockList.size();
       if (numBlocks <= 0) {
            System.out.println("BlockGroup: " + getBlockGroupNum() + " has no blocks");
            
        }
        //else {
        double unitsPerBlock = numHousingUnits/blockList.size();
        double occupiedUnitsPerBlock = numOccupiedUnits / blockList.size();
       // System.out.println("BlockGroup " + this.blockGroupNum + " allocateHU: " + numOccupiedUnits);
        
        // each block - allocate the correct number of housing units
        // assign agents for each occupied unit
        for (int i=0; i<blockList.size();i++) {
          //  Block b = (Block)censusUnitHandler.getBlock(((Integer)blockList.get(i)).intValue());
            Block b = (Block) blockList.get(i);
            // add vacant housing units to each block in list
            for (int j=0; j<unitsPerBlock; j++) {
              //  HousingUnit h = new HousingUnit();
                //b.addVacantHousingUnit();
                b.addHousingUnit();
            }
        }
        
        for (int i=0; i<blockList.size();i++) {
            //Block b = (Block)this.getBlock(((Integer)blockList.get(i)).intValue());
            Block b = (Block) blockList.get(i);
            // add vacant housing units to each block in list
        //    System.out.println("BlockGroup allocateHU: occupied units: " + occupiedUnitsPerBlock + " blockList size: " + blockList.size());
            for (int j=0; j<occupiedUnitsPerBlock; j++) {
              //  HousingUnit h = new HousingUnit();
                System.out.println("Block Group: allocate: " + b.getSTFID());
              //  System.out.println("Block Group: ct: " + b.getSTFID().substring(0,6));
               // System.out.println("Block Group: bg: " + b.getSTFID().substring(6,7));
               // System.out.println("Block Group: b: " + b.getSTFID().substring(6));
                Agent a = AgentHandler.getInstance().addAgent( b.getSTFID(),b.getVacantHousingUnit(), this.raceIncomeList);
               System.out.println("Blcok Gropup allocate HUs: agent Num " + a.getAgentNum() );
               b.populateHousingUnit(j, a);
                       //j)));
            }
        }
        //}
        // remainder
        // choose 
    }
    */
    public void addBlock(int blockNum) { 
       // if (blockList == null) blockList = new ArrayList();
        Block b = this.getBlock(blockNum);
        if (b == null) {
            b = new Block(blockNum, this);
        }
        blockList.add(b);//new Block(blockNum, this));
        
    }
    
    public void addBlock(int blockNum, String stfid) { 
       // if (blockList == null) blockList = new ArrayList();
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
        // if (totalIncome > 0.0) {
             return (totalIncome/(numTotalPeople*1.0));
        // }
        // else return ((totalIncomeCategory*1.0)/(numTotalPeople*1.0));
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
