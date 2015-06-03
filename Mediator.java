/** 
 * 
 * MirarLA
 * Mediator
 */
package mirar;

import java.util.*;

import javax.swing.*; // Priyasree_Audit: Unnecessary import: null 

import java.awt.*; // Priyasree_Audit: Unnecessary import: null
import java.awt.event.WindowAdapter; // Priyasree_Audit: Unnecessary import: Delete the import.
import java.awt.event.WindowEvent; // Priyasree_Audit: Unnecessary import: Delete the import.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.*;

import javax.swing.*; //Priyasree_Audit: Duplicate import: import javax.swing.*;_Delete the duplicate import. // Priyasree_Audit: Unnecessary import: null 

import java.io.*;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive; // Priyasree_Audit: Unnecessary import: Delete the import.

import com.vividsolutions.jump.io.datasource.*; // Priyasree_Audit: Unnecessary import: null
import com.vividsolutions.jump.io.*; // Priyasree_Audit: Unnecessary import: null
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.workbench.*;
import com.vividsolutions.jump.task.*; // Priyasree_Audit: Unnecessary import: null
import com.vividsolutions.jump.workbench.ui.images.IconLoader; // Priyasree_Audit: Unnecessary import: Delete the import.
import com.vividsolutions.jump.workbench.ui.*;
import com.vividsolutions.jump.workbench.ui.plugin.*; // Priyasree_Audit: Unnecessary import: null
import com.vividsolutions.jump.workbench.model.*;
import com.vividsolutions.jump.workbench.ui.plugin.test.*; // Priyasree_Audit: Unnecessary import: null
import com.vividsolutions.jump.workbench.plugin.*; // Priyasree_Audit: Unnecessary import: null
import com.vividsolutions.jump.workbench.ui.renderer.style.*; // Priyasree_Audit: Unnecessary import: null
import com.vividsolutions.jump.util.Range.RangeTreeMap; // Priyasree_Audit: Unnecessary import: Delete the import.
import com.vividsolutions.jump.util.*; // Priyasree_Audit: Unnecessary import: null

import uchicago.src.sim.analysis.Sequence; // Priyasree_Audit: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.BasicAction; // Priyasree_Audit: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.Controller; // Priyasree_Audit: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.Schedule; // Priyasree_Audit: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.SimModelImpl; // Priyasree_Audit: Unnecessary import: Delete the import.
import uchicago.src.sim.gui.*; // Priyasree_Audit: Unnecessary import: null
import uchicago.src.sim.util.Random; // Priyasree_Audit: Unnecessary import: Delete the import.

/**
 *  
 */
public class Mediator {
   // public Mirar mirar; // Priyasree_DeadCode : Unreachable code_
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
    Layer blockLayer;

    private JUMPHandler jumpHandler;
    CensusUnitHandler censusUnitHandler;
    private MirarData mirarData;
    
    
    public Mediator() {
    	//System.out.println("Mediator_Mediator()"); //PriyaUnderStand
        censusUnitHandler = CensusUnitHandler.getInstance();
        jumpHandler = JUMPHandler.getInstance();
        agentHandler = AgentHandler.getInstance();
        mirarData = MirarData.getInstance();
        housingMarket = HousingMarket.getInstance();
    }

    public void checkHU() { // PriyasreeNotUsed
    	//System.out.println("Mediator_checkHU"); //PriyaUnderStand
        Iterator iter = MirarUtils.BLOCKS.iterator();
        ArrayList huList = new ArrayList();
        while (iter.hasNext()) {
            Block b = (Block) iter.next();
            huList.addAll(b.getHousingUnitsByTenure(Agent.RENTER));
                for (int i=0; i<huList.size(); i++) {
                    HousingUnit hu = (HousingUnit)huList.get(i);
                    if (hu.getTenure() != Agent.RENTER)
                        System.out.println("--------------     Mediator check HU ---   tenure does not match ");
                    
                    if (hu.isOccupied() == true  && hu.getAgent() == null) // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                        System.out.println("--------------     Mediator check HU ---   agent is null");
                    
                  
                    
                }
                
                huList.clear();
                huList.addAll(b.getHousingUnitsByTenure(Agent.OWNER));
                for (int i=0; i<huList.size(); i++) {
                    HousingUnit hu = (HousingUnit)huList.get(i);
                    if (hu.getTenure() != Agent.OWNER)
                        System.out.println("--------------     Mediator check HU ---   tenure does not match ");
                    
                    if (hu.isOccupied() == true  && hu.getAgent() == null) // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                        System.out.println("--------------     Mediator check HU ---   agent is null");
                    
                    
                    
                }
                
                huList.clear();
            }
        }
    
    public void setupRents() { // Priyasree_DeadCode : Unreachable code_
    	//System.out.println("Mediator_setupRents"); //PriyaUnderStand
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
    
    public void step() { // Priyasree_DeadCode : Unreachable code_
    	//System.out.println("Mediator_step"); //PriyaUnderStand
        MirarUtils.STEP_NUM++;
        
        if (MirarUtils.STEP_NUM<=1) {
            this.setupRents();
        }
           agentHandler.moveRenterAgents();
           this.blocksForUpdating_Renter.addAll(agentHandler.getBlocksToUpdate_Renter());
           
           //***********************************
           // update rents
           //***********************************
                    if (MirarUtils.AGENT_DECISION.toString() != "PSID_RaceOnly" & (MirarUtils.STEP_NUM % MirarUtils.RENTS_UPDATE_INTERVAL == 1 || MirarUtils.RENTS_UPDATE_INTERVAL == 1))  { //Priyasree_Audit: Cannot compare strings using the not equals (!=) operator_Replace the comparison with equals().
                        System.out.println("-------->>>>     updating rents"); //PriyaRemovePrints
                        if (MirarUtils.RENT_TYPE.equalsIgnoreCase("HomogeneousRentWithinBlocks")) {
                            housingMarket.computeHomogeneousRentWithinBlock(this.blocksForUpdating_Renter, Agent.RENTER);
                        }
                        else if (MirarUtils.RENT_TYPE.equalsIgnoreCase("HeterogeneousRentWithinBlocks")) {
                            housingMarket.computeHeterogeneousRentWithinBlock(this.blocksForUpdating_Renter, Agent.RENTER);
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
                            } catch (IOException ioe) { // Priyasree_Audit: Empty catch clause for exception ioe_Delete the empty catch clause.
                                
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
                            } catch (IOException ioe) { // Priyasree_Audit: Empty catch clause for exception ioe_Delete the empty catch clause.
                                
                            }
                        }
                        agentHandler.updateUtilities();//here
                    }
           //****************************
           // end update rents
           //*********************
           
             
            if (MirarUtils.STEP_NUM % MirarUtils.DISPLAY_UPDATE_INTERVAL == 0) {
               if (MirarUtils.NO_GUI == false) { // Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
                   // update the display every 20 turns
                   jumpHandler.updateBlockLayer();
                   jumpHandler.updateBlockGroupLayer(CensusUnitHandler.getInstance().getAllBlockGroups());
                   jumpHandler.updateCensusTractLayer(CensusUnitHandler.getInstance().getCensusTractList());
               }
           }
           
            if (MirarUtils.STEP_NUM % MirarUtils.PRINT_INTERVAL== 0) {
                System.out.println("Step: " + MirarUtils.STEP_NUM + "   print out data"); //PriyaRemovePrints
                    MirarData.getInstance().processData();
            }
            
            
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
            System.out.println("Total Number of Agents:   " + (numOwnerAgents_AH + numRenterAgents_AH));
            System.out.println("numOwnerAgents_AH:   " + numOwnerAgents_AH  + "   numRenterAgents_AH:   " +  numRenterAgents_AH);
            System.out.println("numOwners_B:   " + numOwners_B  + "   numRenters_B:   " +  numRenters_B);
            System.out.println("numWhites:   " + numWhites_B  + "   numBlacks:   " +  numBlacks_B
            		+ "   numHisps:   " +  numHisps_B + "   numAsians:   " +  numAsians_B); //PriyaRemovePrints
            }
            System.out.println("BlockAgents  : " + MirarData.BlockAgents);
            System.out.println("TOTAL BLACK % Estimation_B : " + (Block.totBlackInNeighborhood_B));
            System.out.println("TOTAL WHITE % Estimation_B : " + (Block.totWhiteInNeighborhood_B)); //PriyaRemovePrints
            if(MirarData.BlockAgents != 0) {
                System.out.println("BLACK % Estimation_B : " + (Block.totBlackInNeighborhood_B/MirarData.BlockAgents));
                System.out.println("WHITE % Estimation_B : " + (Block.totWhiteInNeighborhood_B/MirarData.BlockAgents)); //PriyaRemovePrints
                MirarData.BlockAgents = 0;
                }
            Block.totBlackInNeighborhood_B = 0;
            Block.totWhiteInNeighborhood_B = 0;
                        
            System.out.println("Agent sample prop. is: " + MirarUtils.RENTER_AGENT_SAMPLE + " -- " + "Vacant unit sample prop. is " + MirarUtils.RENTER_VACANT_HOUSING_UNIT_SAMPLE); //PriyaRemovePrints
            //}
            
            this.blocksForUpdating_Renter.clear();
            this.blocksForUpdating_Owner.clear();
           System.out.println("Step: " + MirarUtils.STEP_NUM); //PriyaRemovePrints
          
       }
    

    public void updateBlockLayer() { // PriyasreeNotUsed
    	//System.out.println("Mediator_updateBlockLayer"); //PriyaUnderStand
            jumpHandler.updateBlockLayer();
    }

    //public void setup() {
    public void setupJUMPHandler() { //PriyasreeSetup
    	//System.out.println("Mediator_setup"); //PriyaUnderStand
        // read in shapefiles

        // loadShapefiles();
        // create blocks
        // get list of blocks from jh
        //assign agents to blocks 
    	
        if (MirarUtils.NO_GUI == false) { // Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
            //jumpHandler.setup();
            jumpHandler.setupJUMPWB(); //PriyasreeSetup
        }
        else {
            jumpHandler.initializeSTFIDData();
        }
        
        //add race pct info - m/jh
        //display - jh
        //   prepareJUMPWorkbench(); // this function adds

    }

    public void prepareCensusUnits() {
    	//System.out.println("Mediator_prepareCensusUnits"); //PriyaUnderStand
        censusUnitHandler.prepareCensusUnits(jumpHandler.getSTFIDList());
    }

    /**
     * read in agents from a file and add them to the model
     */
    public void agentsFromFile() {
    }

    /**
     * load data from dbf, shp and other files
     *  
     */
    public void loadData() {
    }

    public void loadAgentDataHeader() {
    	//System.out.println("Mediator_loadAgentDataHeader"); //PriyaUnderStand
        loadRenterAgentDataHeader();
        loadOwnerAgentDataHeader();
    }
    
    public void loadRenterAgentDataHeader() {
    	//System.out.println("Mediator_loadRenterAgentDataHeader"); //PriyaUnderStand
        BufferedReader in;
        try {
            Pattern p = Pattern.compile(",");
            
         
            ArrayList raceList = new ArrayList();
            DoubleArrayList incomeList = new DoubleArrayList();
            DoubleArrayList rentList = new DoubleArrayList();

            
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
                //System.out.println("race list " + i + "  " + race[i]);
            }
            
            s = in.readLine();
            String [] income = p.split(s);
            for (int i=1; i<income.length; i++) {
                incomeList.add(new Double(income[i]).doubleValue()); // "Priyasree_Audit:  Expression can be replaced by Double.parseDouble(income[i])_Replace the expression with the more efficient code."
               // System.out.println("IncomeList " + i + "  " + income[i]);
               // MirarUtils.INCOME_TYPES.add(incomeList.get(i));
            }
           

            s = in.readLine();
            String [] rent = p.split(s);
            for (int i=1; i<rent.length; i++) {
                rentList.add(new Double(rent[i]).doubleValue()); // "Priyasree_Audit:  Expression can be replaced by Double.parseDouble(rent[i])_Replace the expression with the more efficient code."
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
           //System.out.println("MirarUtils.NUM_AGENT_TYPES " + MirarUtils.NUM_AGENT_TYPES + " MirarUtils.RACE_INCOME_SIZE  " + MirarUtils.RACE_INCOME_SIZE); 
        
            in.close();
        } catch (IOException e) { // Priyasree_Audit: Empty catch clause for exception e_Delete the empty catch clause.
       
            
        }
    }
    
    public void loadOwnerAgentDataHeader() {
    	//System.out.println("Mediator_loadOwnerAgentDataHeader"); //PriyaUnderStand
        BufferedReader in;
        try {
            Pattern p = Pattern.compile(",");
            
            DoubleArrayList incomeList = new DoubleArrayList();
            DoubleArrayList rentList = new DoubleArrayList();
            
            if (MirarUtils.OWNER_DATA_FILE !=null)   in = new BufferedReader(new FileReader(MirarUtils.OWNER_DATA_FILE));//"Data"
            else   in = new BufferedReader(new FileReader("Data"
                    + File.separator + "1990extract_agent_data_subset_owners.txt"));
            
            String s = new String();
            
            
            // read in race income and rent
            // add to lists - without first element - which is simply the name of the list
            s = in.readLine();
            s = in.readLine();
            String [] income = p.split(s);
            for (int i=1; i<income.length; i++) {
                incomeList.add(new Double(income[i]).doubleValue()); // "Priyasree_Audit:  Expression can be replaced by Double.parseDouble(income[i])_Replace the expression with the more efficient code."
            }
            
            
            s = in.readLine();
            String [] rent = p.split(s);
            for (int i=1; i<rent.length; i++) {
                rentList.add(new Double(rent[i]).doubleValue()); // "Priyasree_Audit:  Expression can be replaced by Double.parseDouble(rent[i])_Replace the expression with the more efficient code."
            }
            MirarUtils.setOwnerRents(rentList);
            MirarUtils.setOwnerIncomes(incomeList);
            MirarUtils.NUM_RENTS_OWNERS = rentList.size();
            incomeList = null;
            rentList = null;
            
            in.close();
        } catch (IOException e) { // Priyasree_Audit: Empty catch clause for exception e_Delete the empty catch clause.
            
            
        }
    }
    
    public void loadAgentData() throws FileNotFoundException { // file not found exception just for testing
    	//System.out.println("Mediator_loadAgentData"); //PriyaUnderStand
        
       // testing code
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
        
        loadRenterAgentData();
        loadOwnerAgentData();
        MirarUtils.NUM_HOUSING_UNITS = CensusUnitHandler.getInstance().getAllHousingUnits().size();
    }
    
    
    public void loadRenterAgentData() {
    	//System.out.println("Mediator_loadRenterAgentData"); //PriyaUnderStand
        try {
            Pattern p = Pattern.compile(",");            
            DoubleArrayList raceIncomeRent;// = new ArrayList();
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
            
            s = in.readLine();// ignore the header
 
            int i = 0;
            String[] result = null;
            while ((s = in.readLine()) != null) {
                raceIncomeRent = new DoubleArrayList();
                result = p.split(s);
                if (result == null) System.out.println("Load Agent Data:: result string for data is NULL");
               
//PriyasreeComment System.out.println("Mediator#loadAgentData:  stfid " + result[0] + "  bg:  " + result[1] + "  get block group"); 
                BlockGroup bg = censusUnitHandler.getBlockGroup(Integer
                        .parseInt(result[0]), Integer.parseInt(result[1]));

                if (bg == null)
                	continue;
                	/*System.out   //PriyasreeComment
                            .println("load agent data - can't find blockgroup");*/
                else {
                    bg.setRenterVacancyRate(Double.parseDouble(result[5]));
                   
                    for(int index = 10; index<result.length; index++) {
                        raceIncomeRent.add(new Double(result[index]).doubleValue()); // "Priyasree_Audit:  Expression can be replaced by Double.parseDouble(result[index])_Replace the expression with the more efficient code."
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
           
            //#### 
            // add to block history
            if (MirarUtils.BLOCK_HISTORY == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                Iterator blockIter = MirarUtils.BLOCKS.iterator();
                while (blockIter.hasNext()) {
                    ((Block)blockIter.next()).addToHistory(Agent.RENTER);
                }
            }
            
            
         
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void loadOwnerAgentData() {
    	//System.out.println("Mediator_loadOwnerAgentData"); //PriyaUnderStand
        try {
            Pattern p = Pattern.compile(",");           
            DoubleArrayList raceIncomeRent;// = new ArrayList();
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
                result = p.split(s);
                if (result == null) System.out.println("Load Agent Data:: result string for data is NULL");
               
//PriyasreeComment System.out.println("Mediator#loadAgentData:  stfid " + result[0] + "  bg:  " + result[1] + "  get block group");
                BlockGroup bg = censusUnitHandler.getBlockGroup(Integer
                        .parseInt(result[0]), Integer.parseInt(result[1]));

                if (bg == null)
                	continue;
                    /*System. //PriyasreeComment
                    out.println("load agent data - can't find blockgroup");*/
                else {
                    bg.setOwnerVacancyRate(Double.parseDouble(result[5]));

                    for(int index = 10; index<result.length; index++) {
                        raceIncomeRent.add(new Double(result[index]).doubleValue()); // "Priyasree_Audit:  Expression can be replaced by Double.parseDouble(result[index])_Replace the expression with the more efficient code."
                    }

                  
                    bg.initializeOwnerHousingUnits(raceIncomeRent);
                    result = null;
                    raceIncomeRent = null;
                    
                    i++;

                }
            }
            in.close();
           
            //#### 
            // add to block history
            if (MirarUtils.BLOCK_HISTORY == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                Iterator blockIter = MirarUtils.BLOCKS.iterator();
                while (blockIter.hasNext()) {
                    ((Block)blockIter.next()).addToHistory(Agent.OWNER);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }     
    }
    
    public void loadBlockNeighborData() {
    	//System.out.println("Mediator_loadBlockNeighborData"); //PriyaUnderStand
        // load block neighbor file
        try {            
            Pattern p = Pattern.compile(",");
            ArrayList strings = new ArrayList();
            BufferedReader in;
            if (MirarUtils.NEIGHBORHOOD_DATA_FILE !=null)   in = new BufferedReader(new FileReader(MirarUtils.NEIGHBORHOOD_DATA_FILE));//"Data"
            
            else  { in = new BufferedReader(new FileReader("Data" + File.separator + "LACity"  + File.separator + "neighboring_blocks.txt"));
            System.out.println("!!! Mediator#Neighborhood data file no found !!!");
            }
            String s = new String();

            ArrayList neighborList = new ArrayList();
            int tractNum = 0;
            int blockNum = 0;

         
            String[] data;// = new String();
            
            // ##############################
            // for each line in the block neighbor file 
            // see if the block and the nighbor exist
            // if both do, add the neighbor to the block
            // ################################
            while ((s = in.readLine()) != null) {
                data = p.split(s);            
                   tractNum = Integer.parseInt(data[1]);
                   blockNum = Integer.parseInt(data[2]);
                   String stfid = data[3] + data[4];
                   
                   if (MirarUtils.SUBSET_DATA == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                           if (CensusUnitHandler.getInstance().hasBlock(tractNum, blockNum) && CensusUnitHandler.getInstance().hasBlock(stfid)) {
                               ((Block) censusUnitHandler.getBlock(tractNum, blockNum)).addNeighbor(CensusUnitHandler.getInstance().getBlock(stfid)); // Priyasree_Audit: Unnecessary type cast to Block_Delete the unnecessary cast.
                       }
                   }
                   else {
                       ((Block) censusUnitHandler.getBlock(tractNum, blockNum)).addNeighbor(CensusUnitHandler.getInstance().getBlock(stfid)); // Priyasree_Audit: Unnecessary type cast to Block_Delete the unnecessary cast.                  
                   }
            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadShapefiles() {// PriyasreeNotUsed 
    	//System.out.println("Mediator_loadShapefiles"); //PriyaUnderStand
        jumpHandler.loadShapefiles();//blockShpFile, blockGroupShpFile,
    } // end loadData

    /**
     * JUMP requires some brute force methods to add attributes to
     * features.
     * May need to add attributes related to race pct and
     * income here
     *  
     */

    public void prepareData() {
    } // end prepareData

    public void prepareMap() {
    }

    /**
     * add the map to the display. @precondition prepareJUMPWorkbench has
     * run(called from setup) and is visible on display this is required for
     * the JUMP map layer to find the task window to attach to
     * 
     * @postcondition map is added to the JUMP display
     */
    //public void buildDisplay() {
    public void loadFilesToBuildDisplay() { //PriyasreeBuildDisplay
    	//System.out.println("Mediator_loadFilesToBuildDisplay"); //PriyaUndrstand
        if (MirarUtils.BLOCK_SHP_FILE == null) {
        	MirarUtils.BLOCK_SHP_FILE = "Data" + File.separator  + "subset" + File.separator + "blk_subset.shp";
        	System.out.println("Block File Missing... ");
        }
        
         if (MirarUtils.BLOCK_GROUP_SHP_FILE == null) {
        	 MirarUtils.BLOCK_GROUP_SHP_FILE = "Data" + File.separator + "subset" + File.separator + "grp_subset.shp";
         	System.out.println("Blockgroup File Missing... ");
         }
         
         if (MirarUtils.CENSUS_TRACT_SHP_FILE == null) {
        	 MirarUtils.CENSUS_TRACT_SHP_FILE = "Data" + File.separator + "subset" + File.separator + "tract_subset.shp";
         	System.out.println("Tract File Missing... ");
         }

         loadAgentDataHeader();
         if (MirarUtils.NO_GUI == false) { // Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
             jumpHandler.loadShapefiles();//Mirar, blockGroupShpFile, censusTractShpFile);
             jumpHandler.prepareData();
         }
        prepareCensusUnits();


        loadBlockNeighborData(); 
        try { 
            loadAgentData();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (MirarUtils.NO_GUI == false) { // Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
            // prepare display
            //jumpHandler.buildDisplay();
            jumpHandler.buildDisplayGUI(); //PriyasreeBuildDisplay
            jumpHandler.updateBlockLayer();
            jumpHandler.updateBlockGroupLayer(censusUnitHandler.getAllBlockGroups());
            jumpHandler.updateCensusTractLayer(censusUnitHandler.getCensusTractList());
        }
        
        setupUtilities(); 
    }

    public void setupUtilities()  {
    	 //System.out.println("Mediator_setupUtilities"); //PriyaUndrstand
    	 PrintWriter setupOut;
     // get the blocks
        agentHandler.createTestAgents();
 
        
        //  renters
        
        ArrayList testAgents = agentHandler.getRenterTestAgents();
        // go through the list and compute utilities for each agent type
        int numTestAgents = testAgents.size();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext() ) { 
            Block b = (Block) blockIter.next();
            if(b.getNumHousingUnits()>0) {
            	for (int j=0; j<numTestAgents; j++) {
                Agent a = (Agent)testAgents.get(j);
                //double util = a.computeUtility(b, Agent.RENTER); PriyasreeDR
                double util = a.computeUtilityDR(b, Agent.RENTER); //PriyasreeDR
                b.addUtility(util, a);
            	}
            }
        }
    }
    
   
    public CensusUnitHandler getCensusUnitHandler() {
        return censusUnitHandler;
    }

    public AgentHandler getAgentHandler() {
        return agentHandler;
    }


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