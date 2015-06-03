package mirar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.*; //Priyasree

import javax.swing.*;

import java.awt.*; // Priyasree_Audit: Unnecessary import: null 
import java.awt.event.WindowAdapter; // Priyasree_Audit: Unnecessary import: Delete the import.
import java.awt.event.WindowEvent; // Priyasree_Audit: Unnecessary import: Delete the import.
import java.io.*;
import java.lang.reflect.Field;

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
   static long startTime = 0;//PriyasreeTime
   static long endTime = 0;//PriyasreeTime
   static long duration = 0;//PriyasreeTime
   static DateFormat dateFormat = new SimpleDateFormat("HH:mm"); //PriyasreeTime
    
   private Mediator mediator = new Mediator();
    
   public Mirar() {
        //System.out.println("Mirar()"); //PriyaUnderStand
        /**
         * These vectors are the information that go into the REPAST Gui
         */
        Vector v = new Vector();
        v.add("PSID_IncomeOnly");
        v.add("PSID_RaceOnly");
        v.add("PSID_Test"); //Priyasree_Test
        v.add("PSID_RaceIncome");
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
    
    public void reset() { // PriyasreeNotUsed: keep for future use, not used now
        this.schedule = null;	
        System.gc();

        // get the new input paramenters
        this.getInitParam(); 
        Random.createUniform();
        Random.createNormal(0.0, 1.0);
    }
    
    public void buildModel() {
    	//System.out.println("Mirar_buildModel"); //PriyaUnderStand
    }
    
    public void buildDisplay()  {
    	//System.out.println("Mirar_buildDisplay"); //PriyaUnderStand
        //mediator.buildDisplay();
        mediator.loadFilesToBuildDisplay(); //PriyasreeBuildDisplay
    }
 
    private void buildSchedule() {
    	//System.out.println("Mirar_buildSchedule"); //PriyaUnderStand
         schedule.scheduleActionBeginning(1.0, this, "step");
         
         class CollectData extends BasicAction {
             public void execute() {
            	// System.out.println("Mirar_BasicAction Execute"); //PriyaUnderStand
                 try {
                     finishUp();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
         CollectData collectData = new CollectData();
         schedule.scheduleActionAtEnd(collectData);

    }
    
    public void finishUp() throws IOException  {
    	//System.out.println("Mirar_Finish Up"); //PriyaUnderStand
    	endTime = System.currentTimeMillis(); //PriyasreeTime
    	Date end = new Date(endTime); //PriyasreeTime
    	System.out.println("End Time = " + dateFormat.format(end)); //PriyasreeTime //PriyaRemovePrints
    	duration = endTime - startTime;//PriyasreeTime
        System.out.println("DURATION = " + String.format("%d min, %d sec", //PriyaRemovePrints
        												  TimeUnit.MILLISECONDS.toMinutes(duration), 
        												  (TimeUnit.MILLISECONDS.toSeconds(duration) - (TimeUnit.MILLISECONDS.toMinutes(duration) * 60)))); //PriyasreeTime);
        MirarData.getInstance().processData();
        //ErrorLog.getInstance().finishUp();
        ErrorLog.getInstance().finishLoggingError(); //PriyasreefinishUp
    }
    
    public void begin() {
    	//System.out.println("Mirar_Begin"); //PriyaUnderStand
        buildModel();
        buildDisplay();
        buildSchedule();
        MirarData.getInstance().initialize();
        if (MirarUtils.OWNER_DATA_FILE.contains("LA")) {
        	MirarUtils.CITY = "LosAngeles"; //priyaComm : use enum
        } else if (MirarUtils.OWNER_DATA_FILE.contains("ATL")) {
        	MirarUtils.CITY = "Atlanta";
        } else {
        	MirarUtils.CITY = "LosAngeles";
        }
    }
    
    public void step() { 
    	//System.out.println("Mirar_Step"); //PriyaUnderStand
        if (MirarUtils.NO_GUI == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
            if (MirarUtils.STEPS_TO_RUN == MirarUtils.STEP_NUM) {
                this.stop();
            }
            else mediator.step();
        }
        else mediator.step();
	}
    
//priyaComm: use aspects to create a run log which will display the parameter values.    
    public void setup() {
    	//System.out.println("Mirar_SetUp"); //PriyaUnderStand
        schedule = null;
        schedule = new Schedule();

        AbstractGUIController.CONSOLE_ERR = false;
        AbstractGUIController.CONSOLE_OUT = false;
        AbstractGUIController.ALPHA_ORDER = false;
        MirarUtils.STEP_NUM = 0;
        //mediator.setup(); //Priyasree_removed from here to getSchedule 
 }
    
    public String getName() { // Priyasree: keep for future use, not used now
    	//System.out.println("Mirar_GetName"); //PriyaUnderStand
        return "Model of Race, Income, And Residence (MIRAR) ";
    }
    
    public Schedule getSchedule() {
    	//System.out.println("Mirar_GetSchedule"); //PriyaUnderStand
	mediator.setup(); //Priyasree_added
    return schedule;
    }
    
    /**
     * Gets parameters for model from mirarParams.pf file
     */
    public String[] getInitParam() {
    	//System.out.println("Mirar_Get Init Param"); //PriyaUnderStand
        String[] params =
    {
                "AgentDecisionType", "RentType", "renterAgent_SampleProportion",  "ownerAgent_SampleProportion",  "renterVacantHousingUnit_SampleProportion",  
                "ownerVacantHousingUnit_SampleProportion", "renterData_SampleProportion", "ownerData_SampleProportion", "DisplayUpdateInterval" , "StepsToRun", "PrintInterval", 
                "RENTS_UPDATE_INTERVAL", "SubsetData", "AgentMemory", "BlockHistory", "DisplayUpdateInterval", "RenterDataFile", "OwnerDataFile", "NeighborhoodDataFile" ,
                "BlockShpFile" , "BlockGroupShpFile" , "CensusTractShpFile" , "RunMode"
    };
        return params;
    }
        
    public double getRenterData_SampleProportion() {
        return MirarUtils.RENTER_DATA_SAMPLE;
    }
    
    public void setRenterData_SampleProportion(double p) {
    	//System.out.println("Mirar_setRenterData_SampleProportio"); //PriyaUnderStand
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
    
    /*public void setAgentDecisionType(String type) { PriyasreeDR
        MirarUtils.AGENT_DECISION_STRING = type;
        if (MirarUtils.AGENT_DECISION == null ) {
        	if(type.equalsIgnoreCase("PSID_RaceOnly")){
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
                
            }
            else {
                ErrorLog.getInstance().logError("Mirar#setAgentDeccisionType:  new Agent: no agent specified; random agent");
            }
        }
    }*/
  //priyaComm:refactor the above to use these methods once. there sudnt b 2 similar functionality(the set and this one)
    
	public void setAgentDecisionType(String type) { //PriyasreeDR
		MirarUtils.AGENT_DECISION_STRING = type;
		MirarUtils.AGENT_DECISION = new DecisionRule();
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
    
    //Priyasree_Remove scattered Parameters
    public String getRenterDataFile() {
        return MirarUtils.RENTER_DATA_FILE;
    }
    
    public void setRenterDataFile(String file) {
        MirarUtils.RENTER_DATA_FILE = file;
    }
    
    public String getOwnerDataFile() {
        return MirarUtils.OWNER_DATA_FILE;
    }
    
    public void setOwnerDataFile(String file) {
        MirarUtils.OWNER_DATA_FILE = file;
    }
    
    public String getNeighborhoodDataFile() {
        return MirarUtils.NEIGHBORHOOD_DATA_FILE;
    }
    
    public void setNeighborhoodDataFile(String file) {
        MirarUtils.NEIGHBORHOOD_DATA_FILE = file;
    }
    
    public String getBlockShpFile() {
        return MirarUtils.BLOCK_SHP_FILE;
    }
    
    public void setBlockShpFile(String file) {
        MirarUtils.BLOCK_SHP_FILE = file;
    }
    
    public String getBlockGroupShpFile() {
        return MirarUtils.BLOCK_GROUP_SHP_FILE;
    }
    
    public void setBlockGroupShpFile(String file) {
        MirarUtils.BLOCK_GROUP_SHP_FILE = file;
    }
    
    public String getCensusTractShpFile() {
        return MirarUtils.CENSUS_TRACT_SHP_FILE;
    }
    
    public void setCensusTractShpFile(String file) {
        MirarUtils.CENSUS_TRACT_SHP_FILE = file;
    }
    
    public String getRunMode() {
        return MirarUtils.RUN_Mode;
    }
    
    public void setRunMode(String mode) {
        MirarUtils.RUN_Mode = mode;
    }
    
    
    public static void main(String[] args) {
    	startTime = System.currentTimeMillis(); //PriyasreeTime
    	Date start = new Date(startTime); //PriyasreeTime
    	System.out.println("Start Time = " + dateFormat.format(start)); //PriyasreeTime //PriyaRemovePrints
    	//System.out.println("Start Time = " + String.format("%d:%d:%d", TimeUnit.MILLISECONDS.toHours(startTime), TimeUnit.MILLISECONDS.toMinutes(startTime),TimeUnit.MILLISECONDS.toSeconds(startTime)));//PriyasreeTime
    	//System.out.println("Mirar_MAIN"); //PriyaUnderStand
        uchicago.src.sim.engine.SimInit init = new uchicago.src.sim.engine.SimInit();
        
		Mirar model = new Mirar();
		
        // setup the Error Log class
        ErrorLog.getInstance().setup();
 
    
        if (MirarUtils.RUN_Mode == "batch") {
            MirarUtils.NO_GUI = true;
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



