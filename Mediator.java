/** 
 * 
 * MirarLA
 * Mediator
 */
package mirar;

import java.util.*;

import javax.swing.*; // Priyasree: Unnecessary import: null 
import java.awt.*; // Priyasree: Unnecessary import: null
import java.awt.event.WindowAdapter; // Priyasree: Unnecessary import: Delete the import.
import java.awt.event.WindowEvent; // Priyasree: Unnecessary import: Delete the import.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.*;
import javax.swing.*; //Priyasree: Duplicate import: import javax.swing.*;_Delete the duplicate import. // Priyasree: Unnecessary import: null 
import java.io.*;


import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive; // Priyasree: Unnecessary import: Delete the import.

import com.vividsolutions.jump.io.datasource.*; // Priyasree: Unnecessary import: null
import com.vividsolutions.jump.io.*; // Priyasree: Unnecessary import: null
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.workbench.*;
import com.vividsolutions.jump.task.*; // Priyasree: Unnecessary import: null
import com.vividsolutions.jump.workbench.ui.images.IconLoader; // Priyasree: Unnecessary import: Delete the import.
import com.vividsolutions.jump.workbench.ui.*;
import com.vividsolutions.jump.workbench.ui.plugin.*; // Priyasree: Unnecessary import: null
import com.vividsolutions.jump.workbench.model.*;
import com.vividsolutions.jump.workbench.ui.plugin.test.*; // Priyasree: Unnecessary import: null
import com.vividsolutions.jump.workbench.plugin.*; // Priyasree: Unnecessary import: null
import com.vividsolutions.jump.workbench.ui.renderer.style.*; // Priyasree: Unnecessary import: null
import com.vividsolutions.jump.util.Range.RangeTreeMap; // Priyasree: Unnecessary import: Delete the import.
import com.vividsolutions.jump.util.*; // Priyasree: Unnecessary import: null

import uchicago.src.sim.analysis.Sequence; // Priyasree: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.BasicAction; // Priyasree: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.Controller; // Priyasree: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.Schedule; // Priyasree: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.SimModelImpl; // Priyasree: Unnecessary import: Delete the import.
import uchicago.src.sim.gui.*; // Priyasree: Unnecessary import: null
import uchicago.src.sim.util.Random; // Priyasree: Unnecessary import: Delete the import.

/**
 *  
 */
public class Mediator {

    public Mirar mirar;
    public AgentHandler agentHandler;
    public HousingMarket housingMarket;
    public ArrayList blocksForUpdating_All = new ArrayList();
    public ArrayList blocksForUpdating_Renter = new ArrayList();
    public ArrayList blocksForUpdating_Owner = new ArrayList();
    
    JUMPWorkbench wb;
    WorkbenchFrame frame;
    WorkbenchContext workbenchContext;
    IndexedFeatureCollection blockShpData;
    FeatureDataset blockData;

    IndexedFeatureCollection blockGroupShpData;
    IndexedFeatureCollection tractShpData;
    //ArrayList featureList;
  //  int stepNum;
    Layer blockLayer;

    private String blockShpFile = "Data" + File.separator  + "LACounty" + File.separator  + "tgr06037blk00.shp";
    private String blockGroupShpFile = "Data" + File.separator + "LACounty" + File.separator  
            + "tgr06037grp00.shp";
    private String censusTractShpFile = "Data" + File.separator + "LACounty" + File.separator 
            + "tgr06037trt00.shp";

    double sampleProportion = .1;

    private JUMPHandler jumpHandler;
    CensusUnitHandler censusUnitHandler;
    private MirarData mirarData;
    
    
    public Mediator() {
        censusUnitHandler = CensusUnitHandler.getInstance();
        jumpHandler = JUMPHandler.getInstance();
        agentHandler = AgentHandler.getInstance();
        mirarData = MirarData.getInstance();
        housingMarket = HousingMarket.getInstance();
    }

    public void checkHU() {
        Iterator iter = MirarUtils.BLOCKS.iterator();
        ArrayList huList = new ArrayList();
        while (iter.hasNext()) {
            Block b = (Block) iter.next();
            huList.addAll(b.getHousingUnitsByTenure(Agent.RENTER));
                for (int i=0; i<huList.size(); i++) {
                    HousingUnit hu = (HousingUnit)huList.get(i);
                    if (hu.getTenure() != Agent.RENTER)
                        System.out.println("--------------     Mediator check HU ---   tenure does not match ");
                    
                    if (hu.isOccupied() == true  && hu.getAgent() == null) // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
                        System.out.println("--------------     Mediator check HU ---   agent is null");
                    
                  
                    
                }
                
                huList.clear();
                huList.addAll(b.getHousingUnitsByTenure(Agent.OWNER));
                for (int i=0; i<huList.size(); i++) {
                    HousingUnit hu = (HousingUnit)huList.get(i);
                    if (hu.getTenure() != Agent.OWNER)
                        System.out.println("--------------     Mediator check HU ---   tenure does not match ");
                    
                    if (hu.isOccupied() == true  && hu.getAgent() == null) // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
                        System.out.println("--------------     Mediator check HU ---   agent is null");
                    
                    
                    
                }
                
                huList.clear();
            }
        }
    
    public void setupRents() {
        if (MirarUtils.RENT_TYPE.equalsIgnoreCase("HomogeneousRentWithinBlocks")) {
            housingMarket.computeHomogeneousRentWithinBlock(MirarUtils.BLOCKS, Agent.RENTER);
            housingMarket.computeHomogeneousRentWithinBlock(MirarUtils.BLOCKS, Agent.OWNER);
        }
        else if (MirarUtils.RENT_TYPE.equalsIgnoreCase("HeterogeneousRentWithinBlocks")) {
            housingMarket.computeHeterogeneousRentWithinBlock(MirarUtils.BLOCKS, Agent.RENTER);
            housingMarket.computeHeterogeneousRentWithinBlock(MirarUtils.BLOCKS, Agent.OWNER);
        }
        else if (MirarUtils.RENT_TYPE.equalsIgnoreCase("MarketClearingRent")) {
            housingMarket.computeMarketClearingRent(Agent.RENTER);
            housingMarket.computeMarketClearingRent(Agent.OWNER);
        }            
        agentHandler.updateUtilities();
    }
    
    public void step() {
     //   checkHU();
/*    	
    	try {
            PrintWriter RentsTest = new PrintWriter(new FileOutputStream("RentsTest.txt", true));
            PrintWriter IncomeDist = new PrintWriter(new FileOutputStream("IncomesTest.txt", true));
            if(MirarUtils.STEP_NUM<11){
            if(MirarUtils.STEP_NUM==0) {
            	RentsTest.println("stfid, numHouseList, numVacant, tick, Rent, centile90, agentsInNeigh, numIncomesInList");
            	IncomeDist.println("stfid, tick, income");
            }
            ArrayList BlocksBefore = new ArrayList();
            BlocksBefore.addAll(CensusUnitHandler.getInstance().getAllBlocks());
            Iterator iter = BlocksBefore.iterator();
            Block b = null;
            while (iter.hasNext()) {
                b = (Block)iter.next();
                Iterator huIter = b.housingUnitList.iterator();
        	    DoubleArrayList rents = new DoubleArrayList();
        		while (huIter.hasNext()) {
        		    HousingUnit hu =  (HousingUnit)huIter.next();
        		    rents.add(hu.getRent());
        		}
        		
        		ArrayList neighborhood = new ArrayList();
                DoubleArrayList incomeDistribution = new DoubleArrayList();
                neighborhood.addAll(b.getNeighbors());
                neighborhood.add(b);
                int numNeighbors = neighborhood.size();
                  for (int j=0; j<numNeighbors; j++) {
                        incomeDistribution.addAllOf( ((Block)neighborhood.get(j)).getIncomeDistribution());
                    }
                  neighborhood.clear();
        		for(int i=0; i<rents.size(); i++){
        			RentsTest.println(b.getSTFID() + "," + b.housingUnitList.size() + "," + b.getVacantHousingUnits().size() + "," + b.getNumOccupiedHousingUnits() + "," + MirarUtils.STEP_NUM + "," + rents.get(i) + "," + b.getPercentileRent(.9) + "," + b.getTotalAgentsInNeighborhood() + "," + incomeDistribution.size());
        	      		
        		}
        		for(int i=0; i<incomeDistribution.size(); i++){
        		IncomeDist.println(b.getSTFID() + "," + MirarUtils.STEP_NUM + "," + incomeDistribution.get(i));
            }
}
            }
            RentsTest.close();
            IncomeDist.close();
        } catch (IOException ioe) {
        
        }
  */      
        MirarUtils.STEP_NUM++;
        // call moveAgents in agenthandler - with blockList
        //agentHandler.moveAgents(censusUnitHandler.getAllHousingUnits());
        
        if (MirarUtils.STEP_NUM<=1) {
            this.setupRents();
        }
           agentHandler.moveRenterAgents();
           this.blocksForUpdating_Renter.addAll(agentHandler.getBlocksToUpdate_Renter());
           
         //  agentHandler.moveOwnerAgents();
         //  this.blocksForUpdating_Owner.addAll(agentHandler.getBlocksToUpdate_Owner());
           
           //***********************************
           // update rents
           //***********************************
          // else {
               //     System.out.println("Mediator rent type: " + MirarUtils.RENT_TYPE);
                    if (MirarUtils.AGENT_DECISION.toString() != "PSID_RaceOnly" & (MirarUtils.STEP_NUM % MirarUtils.RENTS_UPDATE_INTERVAL == 1 || MirarUtils.RENTS_UPDATE_INTERVAL == 1))  { //Priyasree: Cannot compare strings using the not equals (!=) operator_Replace the comparison with equals().
                        System.out.println("-------->>>>     updating rents");
                        if (MirarUtils.RENT_TYPE.equalsIgnoreCase("HomogeneousRentWithinBlocks")) {
                            housingMarket.computeHomogeneousRentWithinBlock(this.blocksForUpdating_Renter, Agent.RENTER);
                          //  housingMarket.computeHomogeneousRentWithinBlock(this.blocksForUpdating_Owner, Agent.OWNER);
                        }
                        else if (MirarUtils.RENT_TYPE.equalsIgnoreCase("HeterogeneousRentWithinBlocks")) {
                            housingMarket.computeHeterogeneousRentWithinBlock(this.blocksForUpdating_Renter, Agent.RENTER);
                         //   housingMarket.computeHeterogeneousRentWithinBlock(this.blocksForUpdating_Owner, Agent.OWNER);
                        }
                        
                        else if (MirarUtils.RENT_TYPE.equalsIgnoreCase("MarketClearingRent")) {
                            
                            
                            // print market clearing rent data
                            try {
                                PrintWriter mcRentsBefore = new PrintWriter(new FileOutputStream("mcRentsBefore.txt", true));
                                mcRentsBefore.println("stfid , medianRent, neighmedianIncome, numHousingUnits, pctBlkNeigh");
                                ArrayList mcBlocksBefore = new ArrayList();
                                mcBlocksBefore.addAll(CensusUnitHandler.getInstance().getAllBlocks());
                                Iterator iter = mcBlocksBefore.iterator();
                                Block b = null;
                                while (iter.hasNext()) {
                                    b = (Block)iter.next();
                                    mcRentsBefore.println(b.getSTFID() +", " + b.getMedianRent(Agent.RENTER) + ", " + b.getNeighborhoodMedianIncome()+ "," + b.getHousingUnitList_Renters().size() + "," + b.getPctBlkInNeighborhood());
                                    mcRentsBefore.println(b.getSTFID() +", " + b.getMedianRent(Agent.OWNER) + ", " + b.getNeighborhoodMedianIncome()+ "," + b.getHousingUnitList_Renters().size() + "," + b.getPctBlkInNeighborhood());
                                }
                                mcRentsBefore.close();
                            } catch (IOException ioe) { // Priyasree: Empty catch clause for exception ioe_Delete the empty catch clause.
                                
                            }
                            
                            
                            
                            
                          System.out.println("\t%%%%% " + MirarUtils.STEP_NUM + "  MirarUtils.MC_RENTS_UPDATE_INTERVAL");
                            housingMarket.computeMarketClearingRent(Agent.RENTER);
                            housingMarket.computeMarketClearingRent(Agent.OWNER);
                            
                            
                            // print market clearin rent data
                            try {
                                PrintWriter mcRentsAfter = new PrintWriter(new FileOutputStream("mcRentsAfter.txt", true));
                                mcRentsAfter.println("stfid , medianRent, neighmedianIncome, numHousingUnits, pctblkneigh");
                               
                                ArrayList mcBlocksAfter = new ArrayList();
                                mcBlocksAfter.addAll(CensusUnitHandler.getInstance().getAllBlocks());
                                Iterator iter = mcBlocksAfter.iterator();
                                Block b = null;
                                while (iter.hasNext()) {
                                    b = (Block)iter.next();
                                    mcRentsAfter.println(b.getSTFID() +", " + b.getMedianRent(Agent.RENTER) + ", " + b.getNeighborhoodMedianIncome() + "," + b.getHousingUnitList_Renters().size() + ", " + b.getPctBlkInNeighborhood());
                                    mcRentsAfter.println(b.getSTFID() +", " + b.getMedianRent(Agent.OWNER) + ", " + b.getNeighborhoodMedianIncome() + "," + b.getHousingUnitList_Renters().size() + ", " + b.getPctBlkInNeighborhood());
                                }
                                mcRentsAfter.close();
                            } catch (IOException ioe) { // Priyasree: Empty catch clause for exception ioe_Delete the empty catch clause.
                                
                            }
                        }
                        agentHandler.updateUtilities();
                    }
           //****************************
           // end update rents
           //*********************
           
             
            if (MirarUtils.STEP_NUM % MirarUtils.DISPLAY_UPDATE_INTERVAL == 0) {
               if (MirarUtils.NO_GUI == false) { // Priyasree: Equality test with boolean literal: false_ Remove the comparison with false.
                   // update the display every 20 turns
                   //   System.out.println("setpNum; " + stepNum);
                   jumpHandler.updateBlockLayer();
                   jumpHandler.updateBlockGroupLayer(CensusUnitHandler.getInstance().getAllBlockGroups());
                   jumpHandler.updateCensusTractLayer(CensusUnitHandler.getInstance().getCensusTractList());
               }
           }
           
            if (MirarUtils.STEP_NUM % MirarUtils.PRINT_INTERVAL== 0) {
                System.out.println("Step: " + MirarUtils.STEP_NUM + "   print out data");
                    MirarData.getInstance().processData();
            }
       /*   Iterator blockIter = MirarUtils.BLOCKS.iterator();
          while (blockIter.hasNext() ) {
              System.out.println("\t **** -->  num neighbors: "  + ((Block)blockIter.next()).getNeighbors().size());
          }*/
            
            //########################
            //
            //  print some test blocks to test block history output
            //
            //#########################
            /*
            Block b = CensusUnitHandler.getInstance().getBlock("1034001000");
            try {
                PrintWriter block1034001000 = new PrintWriter(new FileOutputStream("block1034001000.txt", true));
              //  block1034001000.println("Step " + MirarUtils.STEP_NUM );

                
                StringBuffer s = new StringBuffer();
                s.append(b.getSTFID() + ", ");
                s.append(MirarUtils.STEP_NUM + ",");
                s.append(" ");
                s.append(b.getNumVacantHousingUnits() + ",");
                s.append(" ");
                s.append(b.getMedianRent() + ", ");
                int listSize = b.getRaceAndIncomeList().size();
                for (int i=0; i<listSize; i++) {        
                    s.append(b.getRaceAndIncomeList().get(i)); 
                    s.append(", ");
                }
                // delete space
                s = s.deleteCharAt(s.length() - 1);
                // delete comma
                s = s.deleteCharAt(s.length() - 1);
                block1034001000.println( s.toString());
                block1034001000.close();
                
                
            } catch (IOException ioe) {
                
            }
            
         
            
             b = CensusUnitHandler.getInstance().getBlock("1041032000");
            try {
                PrintWriter block1041032000 = new PrintWriter(new FileOutputStream("block1041032000.txt", true));
//                block1041032000.println("Step " + MirarUtils.STEP_NUM );

                
                StringBuffer s = new StringBuffer();
                s.append(b.getSTFID() + ", ");
                s.append(MirarUtils.STEP_NUM + ",");
                s.append(" ");
                s.append(b.getNumVacantHousingUnits() + ",");
                s.append(" ");
                s.append(b.getMedianRent() + ", ");
                int listSize = b.getRaceAndIncomeList().size();
                for (int i=0; i<listSize; i++) {        
                    s.append(b.getRaceAndIncomeList().get(i)); 
                    s.append(", ");
                }
                // delete space
                s = s.deleteCharAt(s.length() - 1);
                // delete comma
                s = s.deleteCharAt(s.length() - 1);
                block1041032000.println( s.toString());
        
                block1041032000.close();
                
            } catch (IOException ioe) {
                
            }
            
            
            b = CensusUnitHandler.getInstance().getBlock("1034003005");
            try {
                PrintWriter block1034003005 = new PrintWriter(new FileOutputStream("block1034003005.txt", true));
              //  block1034003005.println("Step " + MirarUtils.STEP_NUM );

                
                StringBuffer s = new StringBuffer();
                s.append(b.getSTFID() + ", ");
                s.append(MirarUtils.STEP_NUM + ",");
                s.append(" ");
                s.append(b.getNumVacantHousingUnits() + ",");
                s.append(" ");
                s.append(b.getMedianRent() + ", ");
                int listSize = b.getRaceAndIncomeList().size();
                for (int i=0; i<listSize; i++) {        
                    s.append(b.getRaceAndIncomeList().get(i)); 
                    s.append(", ");
                }
                // delete space
                s = s.deleteCharAt(s.length() - 1);
                // delete comma
                s = s.deleteCharAt(s.length() - 1);
                block1034003005.println( s.toString());
                block1034003005.close();
                
                
            } catch (IOException ioe) {
                
            }
            */
            
            //###############################
            // test num agents in the population
            //################################
            Iterator blockIterator = MirarUtils.BLOCKS.iterator();
            int numOwners_B = 0;
            int numRenters_B = 0;
            int numBlacks_B=0;
            int numWhites_B=0; 
            int numHisps_B=0; 
            int numAsians_B=0; 
            while (blockIterator.hasNext()) {
                Block b = (Block)blockIterator.next();
                numRenters_B += b.getOccupiedHousingUnitsByTenure(Agent.RENTER).size();
                numOwners_B += b.getOccupiedHousingUnitsByTenure(Agent.OWNER).size();
                numBlacks_B +=b.getNumBlack();
                numWhites_B +=b.getNumWhite();
                numHisps_B += b.getNumHispanic();
                numAsians_B += b.getNumAsian();
            }
            
            int numAgentsInBlocks =0;
            int numOwnerAgents_AH = AgentHandler.getInstance().getOwnerAgentList().size();
            int numRenterAgents_AH = AgentHandler.getInstance().getRenterAgentList().size();
            
            if(MirarUtils.STEP_NUM==1){
            	System.out.println("The city is... " + MirarUtils.CITY);
            System.out.println("numOwnerAgents_AH:   " + numOwnerAgents_AH  + "   numRenterAgents_AH:   " +  numRenterAgents_AH);
            System.out.println("numOwners_B:   " + numOwners_B  + "   numRenters_B:   " +  numRenters_B);
            System.out.println("numWhites:   " + numWhites_B  + "   numBlacks:   " +  numBlacks_B
            		+ "   numHisps:   " +  numHisps_B + "   numAsians:   " +  numAsians_B);
            
            System.out.println("Agent sample prop. is: " + MirarUtils.RENTER_AGENT_SAMPLE + " -- " + "Vacant unit sample prop. is " + MirarUtils.RENTER_VACANT_HOUSING_UNIT_SAMPLE);
            }
            
            this.blocksForUpdating_Renter.clear();
            this.blocksForUpdating_Owner.clear();
           System.out.println("Step: " + MirarUtils.STEP_NUM);
          
       }
    

    public void updateBlockLayer() {
//            jumpHandler.updateBlockLayer(censusUnitHandler.getAllBlocks());
            jumpHandler.updateBlockLayer();
    }

    public void setup() {

        // read in shapefiles

        // loadShapefiles();
        // create blocks
        // get list of blocks from jh
        //assign agents to blocks 
    	
        if (MirarUtils.NO_GUI == false) { // Priyasree: Equality test with boolean literal: false_ Remove the comparison with false.
            jumpHandler.setup();
        }
        else {
            jumpHandler.initializeSTFIDData();
        }
        

        // System.out.println("check");
        //add race pct info - m/jh

        ///display - jh

        //   prepareJUMPWorkbench(); // this function adds

    }

    public void prepareCensusUnits() {
        // get list of blocks from - jh?
        //  ArrayList stfidList = jumpHandler.getSTFIDList();
        // create blocks
        //  for (int i = 0; i < stfidList.size(); i++) {
        // need to change this to construct all units
        censusUnitHandler.prepareCensusUnits(jumpHandler.getSTFIDList());

        //}
    }

    /**
     * read in agents from a file and add them to the model
     */
    public void agentsFromFile() {

    }

    /*
     * public void addAgents() { // for now randomly add some agents to blocks -
     * test adding/displaying // agents // get list of blocks 10 or so)
     * ArrayList blockList = censusUnitHandler.getAllBlocks(); // pick a block //
     * create each agent 10; i++) { Block block = (Block)
     * blockList.get(Random.uniform.nextIntFromTo(0, blockList.size() - 1));
     * int blockNum = block.getBlockNum();
     * 
     * Agent agent = new Agent(Random.uniform.nextIntFromTo(0, 3),
     * Random.uniform.nextIntFromTo(0, 4), "OptimizeIncomeSameRace",
     * this.agentHandler, blockNum); // update block info
     * block.addAgent(agent);
     * 
     * if (blockNum != agent.getBlockNum()) { System.out.println("add agents:
     * blockNUm: " + blockNum); System.out.println("add agents: agentBlockNum: " +
     * agent.getBlockNum()); }
     * 
     * agentHandler.addAgent(agent); }
     *  
     */

    public void addAgent(int race, double income, String agentDecision) {

    }

    public void addAgent(int race, int income, String agentDecision) {

    }

    /**
     * load data from dbf, shp and other files
     *  
     */
    public void loadData() {
        // loadBlockNeighborData()
        // loadAgentData();

    }

    public void loadAgentDataHeader() {
        loadRenterAgentDataHeader();
        loadOwnerAgentDataHeader();
    }
    
    public void loadRenterAgentDataHeader() {
        BufferedReader in;
        try {
            Pattern p = Pattern.compile(",");
            
         
            ArrayList raceList = new ArrayList();
            DoubleArrayList incomeList = new DoubleArrayList();
            DoubleArrayList rentList = new DoubleArrayList();
            
            
  //          DoubleArrayList raceIncomeRent;// = new ArrayList();
            
     //       ArrayList strings = new ArrayList();

         //   BufferedReader in = new BufferedReader(new FileReader("Data"
          //          + File.separator + "blockgrp_data.txt")); //"1990extract_agent_data_subset.txt"
            
           if (MirarUtils.RENTER_DATA_FILE !=null)   in = new BufferedReader(new FileReader(MirarUtils.RENTER_DATA_FILE));//"Data"
           
           else   in = new BufferedReader(new FileReader("Data"
                   + File.separator + "1990extract_agent_data_subset_renters.txt"));
           
           String s = new String();
            
            
            // read in race income and rent
            // add to lists - without first element - which is simply the name of the list
            s = in.readLine();
            String [] race = p.split(s);
            for (int i=1; i<race.length; i++) {
                raceList.add(race[i]);
            //    System.out.println("race list " + i + "  " + race[i]);
            }
            
            s = in.readLine();
            String [] income = p.split(s);
            for (int i=1; i<income.length; i++) {
                incomeList.add(new Double(income[i]).doubleValue()); // "Priyasree:  Expression can be replaced by Double.parseDouble(income[i])_Replace the expression with the more efficient code."
               // System.out.println("IncomeList " + i + "  " + income[i]);
               // MirarUtils.INCOME_TYPES.add(incomeList.get(i));
            }
           

            s = in.readLine();
            String [] rent = p.split(s);
            for (int i=1; i<rent.length; i++) {
                rentList.add(new Double(rent[i]).doubleValue()); // "Priyasree:  Expression can be replaced by Double.parseDouble(rent[i])_Replace the expression with the more efficient code."
            }
            MirarUtils.setRenterRents(rentList);
            MirarUtils.setRenterIncomes(incomeList);
            MirarUtils.NUM_RACES = raceList.size();
            MirarUtils.NUM_INCOMES = incomeList.size();
            MirarUtils.NUM_RENTS_RENTERS = rentList.size();
            raceList = null;
            incomeList = null;
            rentList = null;
            
            MirarUtils.NUM_AGENT_TYPES = MirarUtils.NUM_RACES * MirarUtils.NUM_INCOMES;
            MirarUtils.RACE_INCOME_SIZE = MirarUtils.NUM_RACES * MirarUtils.NUM_INCOMES;
           // System.out.println("MirarUtils.NUM_AGENT_TYPES " + MirarUtils.NUM_AGENT_TYPES + " MirarUtils.RACE_INCOME_SIZE  " + MirarUtils.RACE_INCOME_SIZE);
        
            in.close();
        } catch (IOException e) { // Priyasree: Empty catch clause for exception e_Delete the empty catch clause.
       
            
        }
    }
    
    public void loadOwnerAgentDataHeader() {
        BufferedReader in;
        try {
            Pattern p = Pattern.compile(",");
            
            
         //   ArrayList raceList = new ArrayList();
            DoubleArrayList incomeList = new DoubleArrayList();
            DoubleArrayList rentList = new DoubleArrayList();
            
            
    //        DoubleArrayList raceIncomeRent;// = new ArrayList();
            
   //         ArrayList strings = new ArrayList();
            
            //   BufferedReader in = new BufferedReader(new FileReader("Data"
            //          + File.separator + "blockgrp_data.txt")); //"1990extract_agent_data_subset.txt"
            
            if (MirarUtils.OWNER_DATA_FILE !=null)   in = new BufferedReader(new FileReader(MirarUtils.OWNER_DATA_FILE));//"Data"
            else   in = new BufferedReader(new FileReader("Data"
                    + File.separator + "1990extract_agent_data_subset_owners.txt"));
            
            String s = new String();
            
            
            // read in race income and rent
            // add to lists - without first element - which is simply the name of the list
           s = in.readLine();
           /* String [] race = p.split(s);
            for (int i=1; i<race.length; i++) {
                raceList.add(race[i]);
                System.out.println("race list " + i + "  " + race[i]);
            }*/
           
            s = in.readLine();
            String [] income = p.split(s);
            for (int i=1; i<income.length; i++) {
                incomeList.add(new Double(income[i]).doubleValue()); // "Priyasree:  Expression can be replaced by Double.parseDouble(income[i])_Replace the expression with the more efficient code."
             //   System.out.println("IncomeList " + i + "  " + income[i]);
                // MirarUtils.INCOME_TYPES.add(incomeList.get(i));
            }
            
            
            s = in.readLine();
            String [] rent = p.split(s);
            for (int i=1; i<rent.length; i++) {
                rentList.add(new Double(rent[i]).doubleValue()); // "Priyasree:  Expression can be replaced by Double.parseDouble(rent[i])_Replace the expression with the more efficient code."
            }
            MirarUtils.setOwnerRents(rentList);
            MirarUtils.setOwnerIncomes(incomeList);
      //      MirarUtils.NUM_RACES = raceList.size();
         //   MirarUtils.NUM_INCOMES = incomeList.size();
            MirarUtils.NUM_RENTS_OWNERS = rentList.size();
       //     raceList = null;
            incomeList = null;
            rentList = null;
            
         //   MirarUtils.NUM_AGENT_TYPES = MirarUtils.NUM_RACES * MirarUtils.NUM_INCOMES;
         //   MirarUtils.RACE_INCOME_SIZE = MirarUtils.NUM_RACES * MirarUtils.NUM_INCOMES;
         //   System.out.println("MirarUtils.NUM_AGENT_TYPES " + MirarUtils.NUM_AGENT_TYPES + " MirarUtils.RACE_INCOME_SIZE  " + MirarUtils.RACE_INCOME_SIZE);
            
            in.close();
        } catch (IOException e) { // Priyasree: Empty catch clause for exception e_Delete the empty catch clause.
            
            
        }
    }
    
    public void loadAgentData() throws FileNotFoundException { // file not found exception just for testing
        
       // testing code
        
    /*    File deleteBlockOut = new File("blockAgentInitData.txt");
        if (deleteBlockOut.exists()) {
            deleteBlockOut.delete();
        }
        File deleteBlockGroupOut = new File("blockGroupAgentInitData.txt");
        if (deleteBlockGroupOut.exists()) {
            deleteBlockGroupOut.delete();
        }
        File deleteAgentTypeOut = new File("agentTypeInitData.txt");
        if (deleteAgentTypeOut.exists()) {
            deleteAgentTypeOut.delete();
        }
        File deleteBlockVacanciesOut = new File("blockVacanciesData.txt");
        if (deleteBlockVacanciesOut.exists()) {
            deleteBlockVacanciesOut.delete();
        }
        
        File deleteBlockRentsOut = new File("blockRentsData.txt");
        if (deleteBlockRentsOut.exists()) {
            deleteBlockRentsOut.delete();
        }
        
        File deleteRentTestOut = new File("rentTestOut.txt");
        if (deleteRentTestOut.exists()) {
        	deleteRentTestOut.delete();
        }
       */ 
        File deleteMCRentBefore = new File("mcRentsBefore.txt");
        if (deleteMCRentBefore.exists()) {
            deleteMCRentBefore.delete();
        }
        
        File deleteMCRentAfter = new File("mcRentsAfter.txt");
        if (deleteMCRentAfter.exists()) {
            deleteMCRentAfter.delete();
        }
       
        File deleteTestSelect = new File("testSelect.txt");
        if (deleteTestSelect.exists()) {
            deleteTestSelect.delete();
        }
        
        File deleteTestUtil = new File("testUtil.txt");
        if (deleteTestUtil.exists()) {
            deleteTestUtil.delete();
        }
        
        File deleteNeighborsFromCode = new File("neighborsFromCode.txt");
        if (deleteNeighborsFromCode.exists()) {
            deleteNeighborsFromCode.delete();
        }
       
        
        File deleteHomoRent = new File("homoRent.txt");
        if (deleteHomoRent.exists() && MirarUtils.RENT_TYPE.equalsIgnoreCase("HomogeneousRentWithinBlocks")) {
            deleteHomoRent.delete();
        }
        File deleteHeteroRent = new File("heteroRent.txt");
        if (deleteHeteroRent.exists() && MirarUtils.RENT_TYPE.equalsIgnoreCase("HeterogeneousRentWithinBlocks")) {
            deleteHeteroRent.delete();
        }
        
        //PrintWriter mcRentsBefore = new PrintWriter(new FileOutputStream("mcRentsBefore.txt", true));
        //mcRentsBefore.println("STFID, rent, pct_black, numHousingUnits");
        //mcRentsBefore.close();
        
       // PrintWriter mcRentsAfter = new PrintWriter(new FileOutputStream("mcRentsAfter.txt", true));
       // mcRentsAfter.println("STFID, rent, pct_black, numHousingUnits");
       // mcRentsAfter.close();
        /*
        PrintWriter blockOut = new PrintWriter(new FileOutputStream("blockAgentInitData.txt", true));
        blockOut.println("race_income_rent_num, numBlocks, rirNum/numBlocks, rirValue");
        
        PrintWriter blockGroupOut = new PrintWriter(new FileOutputStream("blockGroupAgentInitData.txt", true));        
        blockGroupOut.println("CensusTract, BlockGroup , Num Blocks, total_number_agents, vacancyRate");

        PrintWriter agentTypeOut = new PrintWriter(new FileOutputStream("agentTypeInitData.txt", true));
        StringBuffer agentTypeBuff = new StringBuffer();

        PrintWriter blockVacanciesOut = new PrintWriter(new FileOutputStream("blockVacanciesData.txt", true));        
        blockVacanciesOut.println("block_stfid, num_vacant_units, numAgents, vacancyRate");
        
        PrintWriter blockRentsOut = new PrintWriter(new FileOutputStream("blockRentsData.txt", true));   
        blockRentsOut.println("block_stfid, numRentsPerCategory_Occupied: 50, 124.5, 174.5, 224.5, 274.5, 324.5, 374.5, 424.5, 474.5, 524.5, 574.5, 624.5, 674.5, 724.5, 874.5, 1000, numRentsPerCategory_Vacant: 50, 124.5, 174.5, 224.5, 274.5, 324.5, 374.5, 424.5, 474.5, 524.5, 574.5, 624.5, 674.5, 724.5, 874.5, 1000");
        
        
        
        PrintWriter RentTestOut = new PrintWriter(new FileOutputStream("rentTestOut.txt", false));   

        blockOut.close();
        blockGroupOut.close();
        agentTypeOut.close();
        blockVacanciesOut.close();
        blockRentsOut.close();*/
      //  housingMarketOut.close();
        // load agent and housing unit data file
        loadRenterAgentData();
        loadOwnerAgentData();
        MirarUtils.NUM_HOUSING_UNITS = CensusUnitHandler.getInstance().getAllHousingUnits().size();
    }
    
    
    public void loadRenterAgentData() {
        try {
            Pattern p = Pattern.compile(",");
            
         
           // ArrayList raceList = new ArrayList();
          //  DoubleArrayList incomeList = new DoubleArrayList();
         //   DoubleArrayList rentList = new DoubleArrayList();
            
            
            DoubleArrayList raceIncomeRent;// = new ArrayList();
            
         //   ArrayList strings = new ArrayList();

         //   BufferedReader in = new BufferedReader(new FileReader("Data"
          //          + File.separator + "blockgrp_data.txt")); //"1990extract_agent_data_subset.txt"
            BufferedReader in;
           if (MirarUtils.RENTER_DATA_FILE !=null)   in = new BufferedReader(new FileReader(MirarUtils.RENTER_DATA_FILE));//"Data"
           else  {
        	   in = new BufferedReader(new FileReader("Data"  + File.separator + "subset"  + File.separator + "1990extract_agent_data_subset_renters.txt"));
        	   System.out.println("Renter data file not found...");
           }
           String s = new String();
            
            
            // read in race income and rent
            // add to lists - without first element - which is simply the name of the list
            s = in.readLine();
            s = in.readLine();
            s = in.readLine();
          //  int [] raceIncomeValues = new int[MirarUtils.NUM_AGENT_TYPES];
            
            s = in.readLine();// ignore the header
            //tract2000,bg,occupied_hu,total_hu,vacant_hu,vacancy_rate,blk_hh,wht_hh,asn_hh,hisp_hh,
            /*
             * variable 1 = 2000 tract ID
             variable 2 = 2000 block group ID (the data are sorted by tracts, and block groups within tracts)
             variable 3 = total number of occupied housing units in the block group
             variable 4 = total number of housing units in the block group
             variable 5 = total number of vacant housing units in the block group
             variable 6 = the vacancy rate in the block group (average is 10%)
             variables  7-10  = total number of black, white, Asian, and Hispanic households in the block group
             */
            int i = 0;
            String[] result = null;
            while ((s = in.readLine()) != null) {
                raceIncomeRent = new DoubleArrayList();
             //   String[] raceIncomeList = new String[64];
                result = p.split(s);
        //        System.out.println("result length: " + result.length );
                if (result == null) System.out.println("Load Agent Data:: result string for data is NULL");
             //   else System.out.println("Load Agent Data:: result string length  "  + result.length);
               
                  System.out.println("Mediator#loadAgentData:  stfid " + result[0] + "  bg:  " + result[1] + "  get block group"); 
           //     System.out.println("Mediator#loadAgentData: censusUnitHandler.getBlockGroup");
                BlockGroup bg = censusUnitHandler.getBlockGroup(Integer
                        .parseInt(result[0]), Integer.parseInt(result[1]));

                //                    new BlockGroup(Integer.parseInt(result[1]),
                //                        Integer.parseInt(result[0]), censusUnitHandler);

                if (bg == null)
                    System.out
                            .println("load agent data - can't find blockgroup");
                else {
//                    System.out.println("Mediator#loadAgentData: set block group data");
                 //   bg.setNumOwnerOccupiedUnits( Double.parseDouble(result[2]));
                //    bg.setNumOwnerHousingUnits( Double.parseDouble(result[3]));
              //      bg.setNumOwnerVacantUnits( Double.parseDouble(result[4]));
                	//System.out.println("result that is choked on is: " + result[5]);
                    bg.setRenterVacancyRate(Double.parseDouble(result[5]));
                    
                 //   bg.initializeNumWhite(Double.parseDouble(result[6]));
              //      bg.initializeNumBlack( Double.parseDouble(result[7]));
              //      bg.initializeNumAsian( Double.parseDouble(result[8]));
             //       bg.initializeNumHispanic( Double.parseDouble(result[9]));
                   
   //                 System.out.println("Mediator#loadAgentData:  raceIncomeRent.add");
                    for(int index = 10; index<result.length; index++) {
                    	//System.out.print("result count is " + result[index]);
                        raceIncomeRent.add(new Double(result[index]).doubleValue()); // "Priyasree:  Expression can be replaced by Double.parseDouble(result[index])_Replace the expression with the more efficient code."
//                                new Integer(MirarUtils.probabilisticInterpolation( Double.parseDouble(result[index]))));
//                                new Integer(result[index]));
                    }
                 
                    bg.initializeRenterHousingUnits(raceIncomeRent);
                    result = null;
                    raceIncomeRent = null;
                    
                    i++;

                }
         //       Runtime rt = Runtime.getRuntime();
            //    System.out.println("Runtime.totalMemory()-Runtime.freeMemory():  " + (rt.totalMemory() - rt.freeMemory()));
            //    System.out.println("System.gc");
            //    System.gc();
             //   System.out.println("Runtime.totalMemory()-Runtime.freeMemory():  " + (rt.totalMemory() - rt.freeMemory()));
            }
            // should we update the blocks here, so they will have block history set for time 0?
            in.close();
//            System.out.println("Mediator#loadAgentDAta :  CensusUnitHandler -  getAllBlocks();");
          //  ArrayList allBlocks = CensusUnitHandler.getInstance().getAllBlocks();
           
            //#### 
            // add to block history
            if (MirarUtils.BLOCK_HISTORY == true) { // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
                Iterator blockIter = MirarUtils.BLOCKS.iterator();
                while (blockIter.hasNext()) {
                    ((Block)blockIter.next()).addToHistory(Agent.RENTER);
                }
            }
            
            
         
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    /*    ArrayList occupiedUnitsList = this.censusUnitHandler.getAllOccupiedHousingUnits();
        ArrayList vacantUnitsList = this.censusUnitHandler.getAllVacantHousingUnits();
        DoubleArrayList occRents = new DoubleArrayList();
        DoubleArrayList vacantRents = new DoubleArrayList();
        
        for(int i=0; i<occupiedUnitsList.size(); i++){
        	double rentO = ((HousingUnit)occupiedUnitsList.get(i)).getRent();
        	occRents.add(rentO);
        }
        for(int i=0; i<vacantUnitsList.size(); i++){
        	double rentV = ((HousingUnit)vacantUnitsList.get(i)).getRent();
        	vacantRents.add(rentV);
        }
*/
        
     //   RentTestOut.println("median occupied rent is: " + Descriptive.median(occRents));    
       // RentTestOut.println("median vacant rent is: " + Descriptive.median(vacantRents));
       // RentTestOut.close();
    }

    public void loadOwnerAgentData() {
        try {
            Pattern p = Pattern.compile(",");
            
         
         //   ArrayList raceList = new ArrayList();
      //      DoubleArrayList incomeList = new DoubleArrayList();
      //      DoubleArrayList rentList = new DoubleArrayList();
            
            
            DoubleArrayList raceIncomeRent;// = new ArrayList();
            
      //      ArrayList strings = new ArrayList();

         //   BufferedReader in = new BufferedReader(new FileReader("Data"
          //          + File.separator + "blockgrp_data.txt")); //"1990extract_agent_data_subset.txt"
            BufferedReader in;
           if (MirarUtils.OWNER_DATA_FILE !=null)   in = new BufferedReader(new FileReader(MirarUtils.OWNER_DATA_FILE));//"Data"
           else   in = new BufferedReader(new FileReader("Data"
                   + File.separator + "subset"  + File.separator + "1990extract_agent_data_subset_owners.txt"));
           
           String s = new String();
            
            
            // read in race income and rent
            // add to lists - without first element - which is simply the name of the list
            s = in.readLine();
            s = in.readLine();
            s = in.readLine();
     //       int [] raceIncomeValues = new int[MirarUtils.NUM_AGENT_TYPES];
            
            s = in.readLine();// ignore the header
            //tract2000,bg,occupied_hu,total_hu,vacant_hu,vacancy_rate,blk_hh,wht_hh,asn_hh,hisp_hh,
            /*
             * variable 1 = 2000 tract ID
             variable 2 = 2000 block group ID (the data are sorted by tracts, and block groups within tracts)
             variable 3 = total number of occupied housing units in the block group
             variable 4 = total number of housing units in the block group
             variable 5 = total number of vacant housing units in the block group
             variable 6 = the vacancy rate in the block group (average is 10%)
             variables  7-10  = total number of black, white, Asian, and Hispanic households in the block group
             */
            int i = 0;
            String[] result = null;
            while ((s = in.readLine()) != null) {
                raceIncomeRent = new DoubleArrayList();
             //   String[] raceIncomeList = new String[64];
                result = p.split(s);
        //        System.out.println("result length: " + result.length );
                if (result == null) System.out.println("Load Agent Data:: result string for data is NULL");
             //   else System.out.println("Load Agent Data:: result string length  "  + result.length);
               
                System.out.println("Mediator#loadAgentData:  stfid " + result[0] + "  bg:  " + result[1] + "  get block group");
           //     System.out.println("Mediator#loadAgentData: censusUnitHandler.getBlockGroup");
                BlockGroup bg = censusUnitHandler.getBlockGroup(Integer
                        .parseInt(result[0]), Integer.parseInt(result[1]));

                //                    new BlockGroup(Integer.parseInt(result[1]),
                //                        Integer.parseInt(result[0]), censusUnitHandler);

                if (bg == null)
                    System.out
                            .println("load agent data - can't find blockgroup");
                else {
//                    System.out.println("Mediator#loadAgentData: set block group data");
               //     bg.setNumOccupiedUnits( Double.parseDouble(result[2]));
               //     bg.setNumHousingUnits( Double.parseDouble(result[3]));
              //      bg.setNumVacantUnits( Double.parseDouble(result[4]));
                    bg.setOwnerVacancyRate(Double.parseDouble(result[5]));
                    
                  //  bg.initializeNumWhite(Double.parseDouble(result[6]));
                  //  bg.initializeNumBlack( Double.parseDouble(result[7]));
                 //   bg.initializeNumAsian( Double.parseDouble(result[8]));
                //    bg.initializeNumHispanic( Double.parseDouble(result[9]));
                   
   //                 System.out.println("Mediator#loadAgentData:  raceIncomeRent.add");
                    for(int index = 10; index<result.length; index++) {
                        raceIncomeRent.add(new Double(result[index]).doubleValue()); // "Priyasree:  Expression can be replaced by Double.parseDouble(result[index])_Replace the expression with the more efficient code."
//                                new Integer(MirarUtils.probabilisticInterpolation( Double.parseDouble(result[index]))));
//                                new Integer(result[index]));
                    }

                  
                    bg.initializeOwnerHousingUnits(raceIncomeRent);
                    result = null;
                    raceIncomeRent = null;
                    
                    i++;

                }
         //       Runtime rt = Runtime.getRuntime();
            //    System.out.println("Runtime.totalMemory()-Runtime.freeMemory():  " + (rt.totalMemory() - rt.freeMemory()));
            //    System.out.println("System.gc");
            //    System.gc();
             //   System.out.println("Runtime.totalMemory()-Runtime.freeMemory():  " + (rt.totalMemory() - rt.freeMemory()));
            }
            // should we update the blocks here, so they will have block history set for time 0?
            in.close();
           
            //#### 
            // add to block history
            if (MirarUtils.BLOCK_HISTORY == true) { // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
                Iterator blockIter = MirarUtils.BLOCKS.iterator();
                while (blockIter.hasNext()) {
                    ((Block)blockIter.next()).addToHistory(Agent.OWNER);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    /*    ArrayList occupiedUnitsList = this.censusUnitHandler.getAllOccupiedHousingUnits();
        ArrayList vacantUnitsList = this.censusUnitHandler.getAllVacantHousingUnits();
        DoubleArrayList occRents = new DoubleArrayList();
        DoubleArrayList vacantRents = new DoubleArrayList();
        
        for(int i=0; i<occupiedUnitsList.size(); i++){
            double rentO = ((HousingUnit)occupiedUnitsList.get(i)).getRent();
            occRents.add(rentO);
        }
        for(int i=0; i<vacantUnitsList.size(); i++){
            double rentV = ((HousingUnit)vacantUnitsList.get(i)).getRent();
            vacantRents.add(rentV);
        }
*/
        
     
    }
    
    public void loadBlockNeighborData() {
        // load block neighbor file
        try {
            
           //     PrintWriter neighborsFromFile = new PrintWriter(new FileOutputStream("neighborsFromFile.txt", false));

            
            
            Pattern p = Pattern.compile(",");
            ArrayList strings = new ArrayList();
            BufferedReader in;
            if (MirarUtils.NEIGHBORHOOD_DATA_FILE !=null)   in = new BufferedReader(new FileReader(MirarUtils.NEIGHBORHOOD_DATA_FILE));//"Data"
            
            else  { in = new BufferedReader(new FileReader("Data" + File.separator + "LACity"  + File.separator + "neighboring_blocks.txt"));
            System.out.println("!!! Mediator#Neighborhood data file no found !!!");
            }
            String s = new String();

           // int i = 0;
            ArrayList neighborList = new ArrayList();
            int tractNum = 0;
            int blockNum = 0;
           // int counter = 0;

         
            String[] data;// = new String();
            
            // ##############################
            // for each line in the block neighbor file 
            // see if the block and the nighbor exist
            // if both do, add the neighbor to the block
            // ################################
            while ((s = in.readLine()) != null) {
               // System.out.println("counter: " + counter);
                // counter--;
               // index++;
                data = p.split(s);            
                   
                   tractNum = Integer.parseInt(data[1]);
                   blockNum = Integer.parseInt(data[2]);
                   String stfid = data[3] + data[4];
              // System.out.println("Mediator#loadBlockNeighborData: num blocks  " + CensusUnitHandler.getInstance().getAllBlocks().size());
                 //  System.out.println("\t compare tract num + block num " + tractNum +"" +blockNum + "  stfid  " + stfid );
                   if (MirarUtils.SUBSET_DATA == true) { // Priyasree: Equality test with boolean literal: true_ Remove the comparison with true.
                   // check to see if the neighbor is in the block set, otherwise don't add
                  //     ArrayList blocks = censusUnitHandler.getAllBlocks();
                      // boolean add = false;
                     //  for (int i=0; i<blocks.size(); i++) {
                          // if (censusUnitHandler.hasBlock(stfid)) {
                           if (CensusUnitHandler.getInstance().hasBlock(tractNum, blockNum) && CensusUnitHandler.getInstance().hasBlock(stfid)) {
//                           if ( ((Block)blocks.get(i)).getSTFID().equals(stfid) ) {
                          //     System.out.println("add neighbor " + stfid);
                               ((Block) censusUnitHandler.getBlock(tractNum, blockNum)).addNeighbor(CensusUnitHandler.getInstance().getBlock(stfid)); // Priyasree: Unnecessary type cast to Block_Delete the unnecessary cast.
                   //            neighborsFromFile.println(((Block) censusUnitHandler.getBlock(tractNum, blockNum)).getSTFID() + ", " + stfid);
                               
                          //     break;
                        //   }
                       }
                   }
                   else {
                       ((Block) censusUnitHandler.getBlock(tractNum, blockNum)).addNeighbor(CensusUnitHandler.getInstance().getBlock(stfid)); // Priyasree: Unnecessary type cast to Block_Delete the unnecessary cast.
                      // neighborsFromFile.println(((Block) censusUnitHandler.getBlock(tractNum, blockNum)).getSTFID() + ", " + stfid);
                   
                   }
                //String[]

                


            }
            in.close();
     //       neighborsFromFile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadShapefiles() {
        //IconLoader iconLoader = new IconLoader();
        jumpHandler.loadShapefiles();//blockShpFile, blockGroupShpFile,
                //censusTractShpFile);
    } // end loadData

    /**
     * JUMP requires some brute force methods to add attributes to
     * features.
     * May need to add attributes related to race pct and
     * income here
     *  
     */

    public void prepareData() {
        // your code here
    } // end prepareData

    public void prepareMap() {

    }

    /*
     * public void prepareJUMPWorkbench() { //IconLoader iconLoader = new
     * IconLoader(); //ImageIcon imageIcon = IconLoader.icon("World.gif"); //
     * test ShapefileReader
     * 
     * try { // add feature and value for it to feature list - this will be
     * used // for the range value // for (int i=0; i <featureList.size(); i++) { //
     * add feature -- racePct FeatureSchema schema =
     * blockShpData.getFeatureSchema(); int blockShpDataNumAttributes =
     * schema.getAttributeCount(); schema.addAttribute("racePct",
     * AttributeType.DOUBLE); int blockDataNumAttributes =
     * schema.getAttributeCount(); // blockData = new
     * IndexedFeatureCollection(fc1); // for (Iterator i = fc1.iterator();
     * i.hasNext();) { // BasicFeature f1 = (BasicFeature) i.next(); //
     * blockData.add(new BasicFeature(i.next())); // } //
     * fc1.setSchema(schema);
     * //((Feature)featureList.get(i)).setSchema(schema); //for (int j=0; j
     * <schema.getAttributeCount(); j++) { // System.out.println("j: " + j); //
     * System.out.println(schema.getAttributeName(j)); //
     * System.out.println(schema.getAttributeType(j)); //
     * System.out.println(((Feature)featureList.get(i)).getString(j)); //} //
     * FeatureSchema schema = // ((Feature)featureList.get(0)).getSchema(); // } //
     * System.out.println("featurelist size: " + featureList.size());
     * 
     * /// for (int k=0; k <featureList.size(); k++) { // execute(context,
     * colorScheme); // } // System.out.println("featurelist num: " + k);
     * 
     * ArrayList newFeatures = new ArrayList(); int k = 0; for (Iterator i =
     * blockShpData.iterator(); i.hasNext();) { k++;
     * 
     * BasicFeature f1 = (BasicFeature) i.next(); BasicFeature f11 = new
     * BasicFeature(schema); for (int m = 0; m < blockShpDataNumAttributes;
     * m++) { f11.setAttribute(m, f1.getAttribute(m)); } // Object numAttrs [] =
     * f1.getAttributes(); // System.out.println("f1.getAttributes(): " + //
     * numAttrs.length);
     * 
     * 
     * if (k < featureList.size()/6) { //set value to .5 or less //
     * ((Feature)featureList.get(k)). Double d1 = new Double(0.1);
     * f11.setAttribute(6, (Object)d1); } else if (k > featureList.size()/6 &&
     * k < featureList.size()/3) { //set value to .5 or less //
     * ((Feature)featureList.get(k)). Double d2 = new Double(0.4);
     * f11.setAttribute(6, (Object)d2); } else {
     * 
     * //set value to higher than .5 //((Feature)featureList.get(i)). Double d3 =
     * new Double(1.0); f11.setAttribute(6, (Object)d3); }
     */
    /*
     * newFeatures.add(f11); // create block based on block number
     * //censusUnitHandler.addBlock(Integer.parseInt((String)f11.getAttribute("BLOCK2000")));
     * censusUnitHandler.addBlock((String) f11.getAttribute("STFID")); }
     * blockData = new FeatureDataset(newFeatures, schema); // // FeatureSchema
     * schema = // ((Feature)featureList.get(0)).getSchema();
     * System.out.println("schema:num attributes: " +
     * schema.getAttributeCount()); //for (int j=0; j <featureList.size(); j++) {
     * for (int i = 0; i < schema.getAttributeCount(); i++) {
     * System.out.println(schema.getAttributeName(i));
     * System.out.println(schema.getAttributeType(i)); //
     * System.out.println(((Feature)featureList.get(0)).getString(i)); }
     * 
     * //} // test gui jumpHandler.setup();
     * 
     * String [] empty = {}; wb = new JUMPWorkbench("test",empty, imageIcon,
     * new JWindow(), new DummyTaskMonitor()); //final
     * 
     * workbenchContext = wb.getContext(); new
     * JUMPConfiguration().setup(workbenchContext);
     * 
     * frame = new WorkbenchFrame("test", imageIcon, workbenchContext);
     */

    // new
    // FirstTaskFramePlugIn().initialize(workbenchContext.createPlugInContext());
    //  Container contentPane = frame.getContentPane();
    /*
     * Task task = new Task(); TaskFrame taskFrame = new TaskFrame(task,
     * context); JDesktopPane desktop = frame.getDesktopPane();
     * desktop.add(taskFrame); // contentPane.add(taskFrame);
     */
    //   LayerManager layerManager = taskFrame.getLayerManager();
    //frame.createTask();
    //  frame.addTaskFrame();
    //  WorkbenchFrame frame= new WorkbenchFrame("test", imageIcon,
    // wb.getContext());
    // frame.addWindowListener(new WindowAdapter() {
    // public void windowOpened(WindowEvent e) {
    //    s.setVisible(false);
    //    }
    //});
    // wb.getFrame().setVisible(true);
    //   } catch( com.vividsolutions.jump.io.IllegalParametersException
    // e) {
    //     System.out.println(" didn't read file");
    /*
     * } catch (Exception e) { e.printStackTrace(); //
     * System.out.println("could not read file"); } }
     */
    /**
     * add the map to the display. @precondition prepareJUMPWorkbench has
     * run(called from setup) and is visible on display this is required for
     * the JUMP map layer to find the task window to attach to
     * 
     * @postcondition map is added to the JUMP display
     */
    public void buildDisplay() {
        
        if (MirarUtils.BLOCK_SHP_FILE == null) {
        	MirarUtils.BLOCK_SHP_FILE = "Data" + File.separator  + "subset" + File.separator + "blk_subset.shp";
        	System.out.println("Block File Missing... ");
        }
        //"LACounty" + File.separator  + "tgr06037blk00.shp";
        
         if (MirarUtils.BLOCK_GROUP_SHP_FILE == null) {
        	 MirarUtils.BLOCK_GROUP_SHP_FILE = "Data" + File.separator + "subset" + File.separator + "grp_subset.shp";
         	System.out.println("Blockgroup File Missing... ");
         }
         //"LACounty" + File.separator  
         //       + "tgr06037grp00.shp";
         if (MirarUtils.CENSUS_TRACT_SHP_FILE == null) {
        	 MirarUtils.CENSUS_TRACT_SHP_FILE = "Data" + File.separator + "subset" + File.separator + "tract_subset.shp";
         	System.out.println("Tract File Missing... ");
         }
         //"LACounty" + File.separator 
            //    + "tgr06037trt00.shp";
        // prepare data

         loadAgentDataHeader();
         if (MirarUtils.NO_GUI == false) { // Priyasree: Equality test with boolean literal: false_ Remove the comparison with false.
             jumpHandler.loadShapefiles();//Mirar, blockGroupShpFile, censusTractShpFile);
             jumpHandler.prepareData();
         }
        prepareCensusUnits();

        // addAgents();
        //addNeighbors();
        //
        loadBlockNeighborData();
        try { // just for testing remove later

            loadAgentData();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (MirarUtils.NO_GUI == false) { // Priyasree: Equality test with boolean literal: false_ Remove the comparison with false.
            // prepare display
            jumpHandler.buildDisplay();
            jumpHandler.updateBlockLayer();//censusUnitHandler.getAllBlocks());
            jumpHandler.updateBlockGroupLayer(censusUnitHandler.getAllBlockGroups());
            jumpHandler.updateCensusTractLayer(censusUnitHandler.getCensusTractList());
        }
        
        setupUtilities();
        System.out.println("Decision rule is: " + MirarUtils.AGENT_DECISION);
        //System.out.println("Owner Agent Sample is: " + MirarUtils.OWNER_AGENT_SAMPLE);  
        //System.out.println("Renter Agent Sample is: " + MirarUtils.RENTER_AGENT_SAMPLE);    
       // setupRents();
    }

    public void setupUtilities()  {
    	 PrintWriter setupOut;
     // get the blocks
		//try {
		//	setupOut = new PrintWriter(new FileOutputStream("mediatorSetup.txt", false));
		//	setupOut.println("stfid, med_block_rent, med_neigh_block_income, a_income, a_race, utility");
        agentHandler.createTestAgents();
        //ArrayList blocks = censusUnitHandler.getAllBlocks();
        
        
        
        //  renters
        
        ArrayList testAgents = agentHandler.getRenterTestAgents();
        // go through the list and compute utilities for each agent type
        //int numBlocks = blocks.size();
        int numTestAgents = testAgents.size();
        //System.out.println("Mediator: computeUtilities: numTest Agents: " + numTestAgents);
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        //for (int i=0; i<numBlocks; i++) {
        while (blockIter.hasNext() ) { 
//            Block b = (Block) blocks.get(i);
            Block b = (Block) blockIter.next();
            if(b.getNumHousingUnits()>0) {
            	for (int j=0; j<numTestAgents; j++) {
                Agent a = (Agent)testAgents.get(j);
                double util = a.computeUtility(b, Agent.RENTER);
          //      System.out.println("Mediator: computeUtilities: block: " + b.getSTFID() + "  util: " + util);
                b.addUtility(util, a);
            //    setupOut.println(b.getSTFID() + ", " + b.getMedianRent() + ", " + b.getNeighborhoodMedianIncome() + ", " + a.getIncome() + " , " + a.getRace() + ", " + util);
            	}
            }
        }
/*        
   //  owners
        
         testAgents = null;
         testAgents = agentHandler.getOwnerTestAgents();
        // go through the list and compute utilities for each agent type
        //int numBlocks = blocks.size();
         numTestAgents = testAgents.size();
        //System.out.println("Mediator: computeUtilities: numTest Agents: " + numTestAgents);
         blockIter = null;
         blockIter = MirarUtils.BLOCKS.iterator();
        //for (int i=0; i<numBlocks; i++) {
        while (blockIter.hasNext() ) { 
//            Block b = (Block) blocks.get(i);
            Block b = (Block) blockIter.next();
            for (int j=0; j<numTestAgents; j++) {
                Agent a = (Agent)testAgents.get(j);
                double util = a.computeUtility(b, Agent.OWNER);
          //      System.out.println("Mediator: computeUtilities: block: " + b.getSTFID() + "  util: " + util);
                b.addUtility(util, a);
            //    setupOut.println(b.getSTFID() + ", " + b.getMedianRent() + ", " + b.getNeighborhoodMedianIncome() + ", " + a.getIncome() + " , " + a.getRace() + ", " + util);
            }
        }
        */
     //   setupOut.close();
		//} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
    }
    
   
    public CensusUnitHandler getCensusUnitHandler() {
        return censusUnitHandler;
    }

    public AgentHandler getAgentHandler() {
        return agentHandler;
    }

    /*
     * public int getPopulationSize() { return
     * agentHandler.getPopulationSize(); }
     * 
     * public void setPopulationSize(int size) {
     * agentHandler.setPopulationSize(size); }
     * 
     * public double getPctWhite() { return agentHandler.getPctWhite(); }
     * 
     * public void setPctWhite(double pct) { agentHandler.setPctWhite(pct); }
     * 
     * public double getPctBlack() { return agentHandler.getPctBlack(); }
     * 
     * public void setPctBlack(double pct) { agentHandler.setPctBlack(pct); }
     * 
     * public double getPctAsian() { return agentHandler.getPctAsian(); }
     * 
     * public void setPctAsian(double pct) { agentHandler.setPctAsian(pct); }
     * 
     * public double getPctHispanic() { return agentHandler.getPctHispanic(); }
     * 
     * public void setPctHispanic(double pct) {
     * agentHandler.setPctHispanic(pct); }
     */

    public MirarData getMirarData() {
        return mirarData;
    }
    public void setMirarData(MirarData mirarData) {
        this.mirarData = mirarData;
    }
    public JUMPHandler getJUMPHandler() {
        return jumpHandler;
    }
} // end Mediator
