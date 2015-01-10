package mirar;

import java.util.*;
import javax.swing.*;
import java.awt.*; // Priyasree_Audit: Unnecessary import: null 
import java.awt.event.WindowAdapter; // Priyasree_Audit: Unnecessary import: Delete the import.
import java.awt.event.WindowEvent; // Priyasree_Audit: Unnecessary import: Delete the import.
import java.io.*;


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
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Controller; // Priyasree_Audit: Unnecessary import: Delete the import.
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.*; // Priyasree_Audit: Unnecessary import: null 
import uchicago.src.sim.engine.*;
//import uchicago.src.sim.util.*;
import uchicago.src.sim.util.Random;
import uchicago.src.reflector.ListPropertyDescriptor;

/**
 * 
 */
public class Mirar extends SimModelImpl {
    
    private Schedule schedule;	
    
   private Mediator mediator = new Mediator();
    
    
    public Mirar() {
        
        /**
         * These vectors are the information that go into the REPAST Gui
         */
        
        Vector v = new Vector();
        v.add("PSID_IncomeOnly");
        v.add("PSID_RaceOnly");
        v.add("PSID_Test"); //Priyasree_Test
        v.add("PSID_RaceIncome");
        /*v.add("MCSUIdecision");
        v.add("ThresholdRace");
        v.add("StaircaseRace");
        v.add("ContsRace");
        v.add("ThresholdIncome");
        v.add("RaceAndIncome");
        v.add("RaceAndIncomeNeighborhood");
        v.add("RaceAndIncomeNeighborhoodOptimal");
        v.add("Random");
        v.add("CensusPumsIncOnly");
        v.add("CensusPumsRaceInc");
        v.add("CensusPumsRaceOnly");*/
        ListPropertyDescriptor pd = new ListPropertyDescriptor("AgentDecisionType", v);
        
        
        Vector rv = new Vector();
        rv.add("HomogeneousRentWithinBlocks");
        rv.add("HeterogeneousRentWithinBlocks");
        rv.add("MarketClearingRent");
        ListPropertyDescriptor rpd = new ListPropertyDescriptor("RentType", rv);
       
        descriptors.put("AgentDecisionType", pd);
        descriptors.put("RentType", rpd);
        
        Random.createUniform();
    }
    
    public void reset() { // Priyasree_DeadCode : Unreachable code_
        this.schedule = null;	
     
        System.gc();

        // get the new input paramenters
        this.getInitParam();
        
        Random.createUniform();
        Random.createNormal(0.0, 1.0);
    }
    
    public void buildModel() {
 
    }
    
    public void buildDisplay()  {
        mediator.buildDisplay();
    }
 
    private void buildSchedule() {
         schedule.scheduleActionBeginning(1.0, this, "step");
         
         class CollectData extends BasicAction {
             public void execute() {
                 try {
                     finishUp();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }; // Priyasree_Audit: Extra semicolon_Delete the extra semicolon.
         CollectData collectData = new CollectData();
         schedule.scheduleActionAtEnd(collectData);

    }
    
    public void finishUp() throws IOException  {
        
        MirarData.getInstance().processData();
        ErrorLog.getInstance().finishUp();
    }
    
    public void begin() {

        buildModel();
        buildDisplay();
        buildSchedule();
        MirarData.getInstance().initialize();
        if (MirarUtils.OWNER_DATA_FILE.contains("LA")) {
        	MirarUtils.CITY = "LosAngeles";
        } else if (MirarUtils.OWNER_DATA_FILE.contains("ATL")) {
        	MirarUtils.CITY = "Atlanta";
        } else {
        	MirarUtils.CITY = "LosAngeles";
        }
    }
    
    public void step() { // Priyasree_DeadCode : Unreachable code_
        
        if (MirarUtils.NO_GUI == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
            if (MirarUtils.STEPS_TO_RUN == MirarUtils.STEP_NUM) {
                this.stop();
            }
            else mediator.step();
        }
        else mediator.step();
	}
    
    
    public void setup() {
        
    	
        MirarUtils.RENTER_AGENT_SAMPLE = 0.001;
        MirarUtils.RENTER_VACANT_HOUSING_UNIT_SAMPLE = 0.01;
        MirarUtils.OWNER_AGENT_SAMPLE = 0.001;
        MirarUtils.OWNER_VACANT_HOUSING_UNIT_SAMPLE = 0.01;
        // MirarUtils.AGENT_DECISION_STRING = "RaceAndIncomeNeighborhood";
        MirarUtils.DISPLAY_UPDATE_INTERVAL = 5;
        schedule = null;
        schedule = new Schedule();

        AbstractGUIController.CONSOLE_ERR = false;
        AbstractGUIController.CONSOLE_OUT = false;
        AbstractGUIController.ALPHA_ORDER = false;
        MirarUtils.STEP_NUM = 0;
        mediator.setup();
 }
    
    public String getName() {
        
        return "Model of Race, Income, And Residence (MIRAR) ";
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    /**
     * Gets parameters for model from mirarParams.pf file
     */
    public String[] getInitParam() {
        String[] params =
    {
                "AgentDecisionType", "RentType", "renterAgent_SampleProportion",  "ownerAgent_SampleProportion",  "renterVacantHousingUnit_SampleProportion",  
                "ownerVacantHousingUnit_SampleProportion", "renterData_SampleProportion", "ownerData_SampleProportion", "DisplayUpdateInterval" , "StepsToRun", "PrintInterval", 
                "RENTS_UPDATE_INTERVAL", "SubsetData", "AgentMemory", "BlockHistory"
    };
       
        return params;
    }
        
    public double getRenterData_SampleProportion() {
        return MirarUtils.RENTER_DATA_SAMPLE;
    }
    
    public void setRenterData_SampleProportion(double p) {
        MirarUtils.RENTER_DATA_SAMPLE = p;
    }
    
    public double getRenterAgent_SampleProportion() {
        return MirarUtils.RENTER_AGENT_SAMPLE;
    }
    
    public void setRenterAgent_SampleProportion(double p) {
        MirarUtils.RENTER_AGENT_SAMPLE = p;
    }
    
    public double getRenterVacantHousingUnit_SampleProportion() {
        return MirarUtils.RENTER_VACANT_HOUSING_UNIT_SAMPLE;
    }
    
    public void setRenterVacantHousingUnit_SampleProportion(double p) {
        MirarUtils.RENTER_VACANT_HOUSING_UNIT_SAMPLE = p;
    }
    
    
    public double getOwnerData_SampleProportion() {
        return MirarUtils.OWNER_DATA_SAMPLE;
    }
    
    
    public void setOwnerData_SampleProportion(double p) {
        MirarUtils.OWNER_DATA_SAMPLE = p;
    }
    
    public double getOwnerAgent_SampleProportion() {
        return MirarUtils.OWNER_AGENT_SAMPLE;
    }
    
    public void setOwnerAgent_SampleProportion(double p) {
        MirarUtils.OWNER_AGENT_SAMPLE = p;
    }
    
    public double getOwnerVacantHousingUnit_SampleProportion() {
        return MirarUtils.OWNER_VACANT_HOUSING_UNIT_SAMPLE;
    }
    
    public void setOwnerVacantHousingUnit_SampleProportion(double p) {
        MirarUtils.OWNER_VACANT_HOUSING_UNIT_SAMPLE = p;
    }
    
    public String getAgentDecisionType() {
        return MirarUtils.AGENT_DECISION_STRING;
    }
    
    public void setAgentDecisionType(String type) { // Priyasree_DeadCode : Unreachable code_
        MirarUtils.AGENT_DECISION_STRING = type;
        if (MirarUtils.AGENT_DECISION == null ) {
  /*          if (type.equalsIgnoreCase("ThresholdRace")) {
                MirarUtils.AGENT_DECISION = new ThresholdRace();
            }
            else if (type.equalsIgnoreCase("RaceAndIncomeNeighborhood")) {
                MirarUtils.AGENT_DECISION = new RaceAndIncomeNeighborhood();
            }
            else if (type.equalsIgnoreCase("RaceAndIncomeNeighborhoodOptimal")) {
                MirarUtils.AGENT_DECISION = new RaceAndIncomeNeighborhoodOptimal();
            }
            else if(type.equalsIgnoreCase("ContsRace")){
                MirarUtils.AGENT_DECISION = new ContsRace();
            }
            
            else if(type.equalsIgnoreCase("StaircaseRace")){
                MirarUtils.AGENT_DECISION = new StaircaseRace();
            }
            
            else if(type.equalsIgnoreCase("ThresholdIncome")){
                MirarUtils.AGENT_DECISION = new ThresholdIncome();
                
            }
            else */ if(type.equalsIgnoreCase("PSID_RaceOnly")){
                MirarUtils.AGENT_DECISION = new PSID_RaceOnly();
                
            }
            else if(type.equalsIgnoreCase("PSID_Test")){ //Priyasree_Test
                MirarUtils.AGENT_DECISION = new PSID_Test();
                
            }
            else if(type.equalsIgnoreCase("PSID_IncomeOnly")){
                MirarUtils.AGENT_DECISION = new PSID_IncomeOnly();
                
            }
            else if(type.equalsIgnoreCase("PSID_RaceIncome")){
                MirarUtils.AGENT_DECISION = new PSID_RaceIncome();
                
            }/*
            else if(type.equalsIgnoreCase("MCSUIdecision")){
                MirarUtils.AGENT_DECISION = new MCSUIdecision(); 
                
            }
            else if(type.equalsIgnoreCase("CensusPumsRaceOnly")) {
                MirarUtils.AGENT_DECISION = new CensusPumsRaceOnly();	
            }
            else if(type.equalsIgnoreCase("CensusPumsRaceInc")) {
                MirarUtils.AGENT_DECISION = new CensusPumsRaceInc();	
            }
            else if(type.equalsIgnoreCase("CensusPumsIncOnly")) {
                MirarUtils.AGENT_DECISION = new CensusPumsIncOnly();	
            }*/
            else {
                ErrorLog.getInstance().logError("Mirar#setAgentDeccisionType:  new Agent: no agent specified; random agent");
            }
        }
    }

    
    public String getRentType() {
        return MirarUtils.RENT_TYPE;
    }
    
    public void setRentType(String type) {
        MirarUtils.RENT_TYPE = type;
    }
    
    public int getDisplayUpdateInterval() {
        return MirarUtils.DISPLAY_UPDATE_INTERVAL;
    }
    
    public void setDisplayUpdateInterval(int interval) {
        MirarUtils.DISPLAY_UPDATE_INTERVAL = interval;
    }
    
    
    public int getRENTS_UPDATE_INTERVAL() {
        return MirarUtils.RENTS_UPDATE_INTERVAL;
    }
    
    public void setRENTS_UPDATE_INTERVAL(int interval) {
        if (interval ==0) interval = 1;  //interval cannot == 0, will throw divide by zero error
        MirarUtils.RENTS_UPDATE_INTERVAL = interval;
    }
    
    
    public void setStepsToRun(int steps) {
        MirarUtils.STEPS_TO_RUN = steps;
    }
    
    public int getStepsToRun() {
        return MirarUtils.STEPS_TO_RUN;
    }
    
  
    
    
    public int getPrintInterval() {
        return MirarUtils.PRINT_INTERVAL;
    }
    
    public void setPrintInterval(int interval) {
        MirarUtils.PRINT_INTERVAL = interval;
    }
    
    public boolean getSubsetData() {
        return MirarUtils.SUBSET_DATA;
    }
    
    public void setSubsetData(boolean data) {
        MirarUtils.SUBSET_DATA = data;
    }
    
    public boolean getBlockHistory() {
        return MirarUtils.BLOCK_HISTORY;
    }
    
    public void setBlockHistory(boolean data) {
        MirarUtils.BLOCK_HISTORY = data;
    }
    
    public boolean getAgentMemory() {
        return MirarUtils.AGENT_MEMORY;
    }
    
    public void setAgentMemory(boolean data) {
        MirarUtils.AGENT_MEMORY = data;
    }
    
        
    public static void main(String[] args) {
        
        
        uchicago.src.sim.engine.SimInit init = new uchicago.src.sim.engine.SimInit();
        
		Mirar model = new Mirar();

        // setup the Error Log class
        ErrorLog.getInstance().setup();
		if (args.length > 0) {
		    MirarUtils.RENTER_DATA_FILE = args[0];
		}
		if (args.length > 1) {
		    MirarUtils.OWNER_DATA_FILE = args[1];
		}
		if (args.length >2) {
		    MirarUtils.NEIGHBORHOOD_DATA_FILE = args[2];
		}
		if (args.length >3) {
		    MirarUtils.BLOCK_SHP_FILE = args[3];
		}
		if (args.length >4) {
		    MirarUtils.BLOCK_GROUP_SHP_FILE = args[4];
		}
		if (args.length >5) {
		    MirarUtils.CENSUS_TRACT_SHP_FILE = args[5];
		}
       
      
        if (args.length > 6) {
            if (MirarUtils.AGENT_DECISION == null) {
                
                /*if (args[6].equalsIgnoreCase("ThresholdRace")) {
                    MirarUtils.AGENT_DECISION = new ThresholdRace();
                }
                else if (args[6].equalsIgnoreCase("RaceAndIncomeNeighborhood")) {
                    MirarUtils.AGENT_DECISION = new RaceAndIncomeNeighborhood();
                }
                else if (args[6].equalsIgnoreCase("RaceAndIncomeNeighborhoodOptimal")) {
                    MirarUtils.AGENT_DECISION = new RaceAndIncomeNeighborhoodOptimal();
                }
                else if (args[6].equalsIgnoreCase("ContsRace")) {
                   MirarUtils.AGENT_DECISION = new ContsRace();
                }
                else if (args[6].equalsIgnoreCase("StaircaseRace")) {
                    MirarUtils.AGENT_DECISION = new StaircaseRace();
                }
                else if (args[6].equalsIgnoreCase("ThresholdIncome")) {
                    MirarUtils.AGENT_DECISION = new ThresholdIncome();
                } 
                else*/ if (args[6].equalsIgnoreCase("PSID_RaceIncome")) {
                    MirarUtils.AGENT_DECISION = new PSID_RaceIncome();
                } 
                else if (args[6].equalsIgnoreCase("PSID_IncomeOnly")) {
                    MirarUtils.AGENT_DECISION = new PSID_IncomeOnly();
                } 
                else if (args[6].equalsIgnoreCase("PSID_RaceOnly")) {
                    MirarUtils.AGENT_DECISION = new PSID_RaceOnly();             
                    
                }
                else if (args[6].equalsIgnoreCase("PSID_Test")) { //Priyasree_Test
                    MirarUtils.AGENT_DECISION = new PSID_Test();
                }
                /*else if(args[6].equalsIgnoreCase("MCSUIdecision")){
                	MirarUtils.AGENT_DECISION = new MCSUIdecision();
                	
                } else if(args[6].equalsIgnoreCase("CensusPumsRaceOnly")){
                	MirarUtils.AGENT_DECISION = new CensusPumsRaceOnly();
               
                } else if(args[6].equalsIgnoreCase("CensusPumsRaceInc")){
                	MirarUtils.AGENT_DECISION = new CensusPumsRaceInc();
               
                } else if(args[6].equalsIgnoreCase("CensusPumsIncOnly")){
                	MirarUtils.AGENT_DECISION = new CensusPumsIncOnly();
                }*/  else {
                    ErrorLog.getInstance().logError("Mirar#main could not find agent decision class to intialize model with");
                }
            }
        }

         
    if (args.length >7) {
        if (args[7].equalsIgnoreCase("batch")) {
            MirarUtils.NO_GUI = true;
        }
    }
        if (MirarUtils.NO_GUI == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
            init.loadModel(model, "./mirarParams.pf", true);
        }
        else{ // using the GUI
            try {
                String nativeLF = UIManager.getSystemLookAndFeelClassName();
                UIManager.setLookAndFeel(nativeLF);
            } catch (Exception e) { // Priyasree_Audit: Empty catch clause for exception e_Delete the empty catch clause. // Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
            }
            init.loadModel(model, null, false);
        }
       
    }
    
}// end Model



