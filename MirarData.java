/*
 * 
 * MirarLA
 *  @author Robert Najlis
 *  Created on Mar 31, 2004
 *
 *
 */
package mirar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * The MirarData class handles data collection and output
 *  
 *  @author Robert Najlis
 * @version 1.0 
 */
public class MirarData {

   // List agents = new ArrayList();
    List agents_renters = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    List agents_owners = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    String agentMemories; // Priyasree_DeadCode : Unreachable code_
    List blocks = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    String blockHistories; // Priyasree_DeadCode : Unreachable code_
    File agentDir; // Priyasree_DeadCode : Unreachable code_
    File blockDir; // Priyasree_DeadCode : Unreachable code_
    int numOutputs = 0; // Priyasree_DeadCode : Unreachable code_
    
    private static MirarData instance = new MirarData(); // Priyasree_DeadCode : Unreachable code_

    private MirarData() { // Priyasree_DeadCode : Unreachable code_
    	super();
    }

    public static MirarData getInstance() { // Priyasree_DeadCode : Unreachable code_
      return instance;
    }
    
    
    /**
     * calls: collectData, outputData 
     *
     */
    public void processData() { // Priyasree_DeadCode : Unreachable code_
        collectData();
        outputData();
    }
    
    /**
     * collects all agents (owner and renter) from AgentHandler 
     *
     */
    public void collectData() { // Priyasree_DeadCode : Unreachable code_
        // collect all needed data
        AgentHandler ah = AgentHandler.getInstance();
        agents_owners.addAll(ah.getOwnerAgentList());
        agents_renters.addAll(ah.getRenterAgentList());
    }
    
    /**
     * calls outputAgentData and outputBlockData if corresponding memory is set to true
     *
     */
    public void outputData() { // Priyasree_DeadCode : Unreachable code_
        if (MirarUtils.AGENT_MEMORY == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
            outputAgentData();
        }
        if (MirarUtils.BLOCK_HISTORY == true) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
            outputBlockData();
        }
        numOutputs++;
    }
    
    /**
     * outputs Agent Data
     * creates two new files located in the directory for the current model run:
     *  AgentData/agentData_Renters + {STEP_NUM}.txt
     *  AgentData/agentData_Owners + {STEP_NUM}.txt
     *
     */
    public void outputAgentData() { // Priyasree_DeadCode : Unreachable code_
        
        //print data to file
        try {
            
            
            File agentFile_Renters = new File(agentDir, "agentData_Renters" + MirarUtils.STEP_NUM + ".txt");
            File agentFile_Owners = new File(agentDir, "agentData_Owners" + MirarUtils.STEP_NUM + ".txt");
            
            
            PrintWriter agentOut_Renters = new PrintWriter(new FileOutputStream(agentFile_Renters, true));
            PrintWriter agentOut_Owners = new PrintWriter(new FileOutputStream(agentFile_Owners, true));
            
            
            agentOut_Renters.print("agent_type,");
            agentOut_Owners.print("agent_type,");
            StringBuffer label = new StringBuffer();
           
            int startingPoint = 0;
            if (numOutputs == 0) startingPoint = numOutputs*MirarUtils.PRINT_INTERVAL;
            else startingPoint = (numOutputs*MirarUtils.PRINT_INTERVAL) + 1;
            
            for (int i=startingPoint; i<MirarUtils.STEP_NUM+1; i++) {
                label.append("tick" + i + ",");
            }
      
            if (label.length() > 0) 
            {
              label.deleteCharAt(label.length() - 1);
            }
            
            agentOut_Renters.println(label.toString());
            agentOut_Owners.println(label.toString());
            int numAgents_Renters = agents_renters.size();
            int numAgents_Owners = agents_owners.size();
            Agent a = null;
            for (int i=0; i<numAgents_Renters; i++) {
                a = (Agent)agents_renters.get(i);
                agentOut_Renters.println(a.memoryToString());
            }
            a = null;
            for (int i=0; i<numAgents_Owners; i++) {
                a = (Agent)agents_owners.get(i);
                agentOut_Owners.println(a.memoryToString());
            }
            a = null;
            
            // close files
            agentOut_Renters.close();
            agentOut_Owners.close();
            
            // clear all lists
            agents_owners.clear();
            agents_renters.clear();
            
          
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
		
    /**
     * output block data in the format:
     * creates two new files located in the directory for the current model run:
     *  BlockData/blockData_Renters + {STEP_NUM}.txt
     *  BlockData/blockData_Owners + {STEP_NUM}.txt 
     * 
     * outputs in the  format
     *  stfid,tick,vacancies,medianRent,neighPctBlk, neighPctWht, race_income_,
     *   -- where race_income repeats for each race/income combination
     */
    public void outputBlockData() { // Priyasree_DeadCode : Unreachable code_
        try {
            File blockFile_Renters = new File(blockDir, "blockData_Renters" + MirarUtils.STEP_NUM + ".txt");
            File blockFile_Owners = new File(blockDir, "blockData_Owners" + MirarUtils.STEP_NUM + ".txt");
            PrintWriter blockOut_Renters = new PrintWriter(new FileOutputStream(blockFile_Renters, true));
            PrintWriter blockOut_Owners = new PrintWriter(new FileOutputStream(blockFile_Owners, true));
        
           
            //********************
            // make sure that a header is needed
            // only needed for each new file
            //************************************
            
            File dataFile = new File(MirarUtils.RENTER_DATA_FILE);
            System.out.println("block data file :   " + dataFile.getName().substring(0,4));
            if (dataFile.getName().substring(0,4).equalsIgnoreCase("1990") ){
                blockOut_Renters.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, wht_0100,wht_0150,wht_0200,wht_0250,wht_0300,wht_0350,wht_0400,wht_0450,wht_0500,wht_0600,wht_0750,wht_01000,wht_01250,wht_01500,wht_02000,wht_0m200,blk_0100,blk_0150,blk_0200,blk_0250,blk_0300,blk_0350,blk_0400,blk_0450,blk_0500,blk_0600,blk_0750,blk_01000,blk_01250,blk_01500,blk_02000,blk_0m200,asn_0100,asn_0150,asn_0200,asn_0250,asn_0300,asn_0350,asn_0400,asn_0450,asn_0500,asn_0600,asn_0750,asn_01000,asn_01250,asn_01500,asn_02000,asn_0m200,hsp_0100,hsp_0150,hsp_0200,hsp_0250,hsp_0300,hsp_0350,hsp_0400,hsp_0450,hsp_0500,hsp_0600,hsp_0750,hsp_01000,hsp_01250,hsp_01500,hsp_02000,hsp_0m200,quantile1, quantile3, quantile5, quantile6, quantile9");                          
                blockOut_Owners.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, wht_0100,wht_0150,wht_0200,wht_0250,wht_0300,wht_0350,wht_0400,wht_0450,wht_0500,wht_0600,wht_0750,wht_01000,wht_01250,wht_01500,wht_02000,wht_0m200,blk_0100,blk_0150,blk_0200,blk_0250,blk_0300,blk_0350,blk_0400,blk_0450,blk_0500,blk_0600,blk_0750,blk_01000,blk_01250,blk_01500,blk_02000,blk_0m200,asn_0100,asn_0150,asn_0200,asn_0250,asn_0300,asn_0350,asn_0400,asn_0450,asn_0500,asn_0600,asn_0750,asn_01000,asn_01250,asn_01500,asn_02000,asn_0m200,hsp_0100,hsp_0150,hsp_0200,hsp_0250,hsp_0300,hsp_0350,hsp_0400,hsp_0450,hsp_0500,hsp_0600,hsp_0750,hsp_01000,hsp_01250,hsp_01500,hsp_02000,hsp_0m200,quantile1, quantile3, quantile5, quantile6, quantile9");                          
            }

            /*if (dataFile.getName().substring(0,4).equalsIgnoreCase("1990") ){ 
                blockOut_Renters.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, white_0_4999,white_5000_9999,white_10000_14999,white_15000_19999,white_20000_24999,white_25000_29999,white_30000_34999,white_35000_39999,white_40000_49999,white_50000_59999,white_60000_74999,white_75000_99999,white_100000,black_0_4999,black_5000_9999,black_10000_14999,black_15000_19999,black_20000_24999,black_25000_29999,black_30000_34999,black_35000_39999,black_40000_49999,black_50000_59999,black_60000_74999,black_75000_99999,black_100000,asian_0_4999,asian_5000_9999,asian_10000_14999,asian_15000_19999,asian_20000_24999,asian_25000_29999,asian_30000_34999,asian_35000_39999,asian_40000_49999,asian_50000_59999,asian_60000_74999,asian_75000_99999,asian_100000,hisp_0_4999,hisp_5000_9999,hisp_10000_14999,hisp_15000_19999,hisp_20000_24999,hisp_25000_29999,hisp_30000_34999,hisp_35000_39999,hisp_40000_49999,hisp_50000_59999,hisp_60000_74999,hisp_75000_99999,hisp_100000, quantile1, quantile3, quantile5, quantile6, quantile9"); 
                blockOut_Owners.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, white_0_4999,white_5000_9999,white_10000_14999,white_15000_19999,white_20000_24999,white_25000_29999,white_30000_34999,white_35000_39999,white_40000_49999,white_50000_59999,white_60000_74999,white_75000_99999,white_100000,black_0_4999,black_5000_9999,black_10000_14999,black_15000_19999,black_20000_24999,black_25000_29999,black_30000_34999,black_35000_39999,black_40000_49999,black_50000_59999,black_60000_74999,black_75000_99999,black_100000,asian_0_4999,asian_5000_9999,asian_10000_14999,asian_15000_19999,asian_20000_24999,asian_25000_29999,asian_30000_34999,asian_35000_39999,asian_40000_49999,asian_50000_59999,asian_60000_74999,asian_75000_99999,asian_100000,hisp_0_4999,hisp_5000_9999,hisp_10000_14999,hisp_15000_19999,hisp_20000_24999,hisp_25000_29999,hisp_30000_34999,hisp_35000_39999,hisp_40000_49999,hisp_50000_59999,hisp_60000_74999,hisp_75000_99999,hisp_100000, quantile1, quantile3, quantile5, quantile6, quantile9"); 
            }*/
            else if(dataFile.getName().substring(0,4).equalsIgnoreCase("theo")){
                blockOut_Renters.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, wht_0100,wht_0150,wht_0200,wht_0250,wht_0300,wht_0350,wht_0400,wht_0450,wht_0500,wht_0600,wht_0750,wht_01000,wht_01250,wht_01500,wht_02000,wht_0m200,blk_0100,blk_0150,blk_0200,blk_0250,blk_0300,blk_0350,blk_0400,blk_0450,blk_0500,blk_0600,blk_0750,blk_01000,blk_01250,blk_01500,blk_02000,blk_0m200,asn_0100,asn_0150,asn_0200,asn_0250,asn_0300,asn_0350,asn_0400,asn_0450,asn_0500,asn_0600,asn_0750,asn_01000,asn_01250,asn_01500,asn_02000,asn_0m200,hsp_0100,hsp_0150,hsp_0200,hsp_0250,hsp_0300,hsp_0350,hsp_0400,hsp_0450,hsp_0500,hsp_0600,hsp_0750,hsp_01000,hsp_01250,hsp_01500,hsp_02000,hsp_0m200,quantile1, quantile3, quantile5, quantile6, quantile9");                          
                blockOut_Owners.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, wht_0100,wht_0150,wht_0200,wht_0250,wht_0300,wht_0350,wht_0400,wht_0450,wht_0500,wht_0600,wht_0750,wht_01000,wht_01250,wht_01500,wht_02000,wht_0m200,blk_0100,blk_0150,blk_0200,blk_0250,blk_0300,blk_0350,blk_0400,blk_0450,blk_0500,blk_0600,blk_0750,blk_01000,blk_01250,blk_01500,blk_02000,blk_0m200,asn_0100,asn_0150,asn_0200,asn_0250,asn_0300,asn_0350,asn_0400,asn_0450,asn_0500,asn_0600,asn_0750,asn_01000,asn_01250,asn_01500,asn_02000,asn_0m200,hsp_0100,hsp_0150,hsp_0200,hsp_0250,hsp_0300,hsp_0350,hsp_0400,hsp_0450,hsp_0500,hsp_0600,hsp_0750,hsp_01000,hsp_01250,hsp_01500,hsp_02000,hsp_0m200,quantile1, quantile3, quantile5, quantile6, quantile9");                          
          }
            else if (dataFile.getName().substring(0,4).equalsIgnoreCase("200 data")) {
                blockOut_Renters.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, wht_0100,wht_0150,wht_0200,wht_0250,wht_0300,wht_0350,wht_0400,wht_0450,wht_0500,wht_0600,wht_0750,wht_01000,wht_01250,wht_01500,wht_02000,wht_0m200,blk_0100,blk_0150,blk_0200,blk_0250,blk_0300,blk_0350,blk_0400,blk_0450,blk_0500,blk_0600,blk_0750,blk_01000,blk_01250,blk_01500,blk_02000,blk_0m200,asn_0100,asn_0150,asn_0200,asn_0250,asn_0300,asn_0350,asn_0400,asn_0450,asn_0500,asn_0600,asn_0750,asn_01000,asn_01250,asn_01500,asn_02000,asn_0m200,hsp_0100,hsp_0150,hsp_0200,hsp_0250,hsp_0300,hsp_0350,hsp_0400,hsp_0450,hsp_0500,hsp_0600,hsp_0750,hsp_01000,hsp_01250,hsp_01500,hsp_02000,hsp_0m200,quantile1, quantile3, quantile5, quantile6, quantile9");                          
                blockOut_Owners.println("stfid,tick,vacancies,medianRent, neighPctBlk, neighPctWht, wht_0100,wht_0150,wht_0200,wht_0250,wht_0300,wht_0350,wht_0400,wht_0450,wht_0500,wht_0600,wht_0750,wht_01000,wht_01250,wht_01500,wht_02000,wht_0m200,blk_0100,blk_0150,blk_0200,blk_0250,blk_0300,blk_0350,blk_0400,blk_0450,blk_0500,blk_0600,blk_0750,blk_01000,blk_01250,blk_01500,blk_02000,blk_0m200,asn_0100,asn_0150,asn_0200,asn_0250,asn_0300,asn_0350,asn_0400,asn_0450,asn_0500,asn_0600,asn_0750,asn_01000,asn_01250,asn_01500,asn_02000,asn_0m200,hsp_0100,hsp_0150,hsp_0200,hsp_0250,hsp_0300,hsp_0350,hsp_0400,hsp_0450,hsp_0500,hsp_0600,hsp_0750,hsp_01000,hsp_01250,hsp_01500,hsp_02000,hsp_0m200,quantile1, quantile3, quantile5, quantile6, quantile9");                          
                }
            Iterator blockIter = MirarUtils.BLOCKS.iterator();
            while (blockIter.hasNext()) {
                blockOut_Renters.println(((Block)blockIter.next()).historyToString_Renter());
                
              //  blockOut_Owners.println(((Block)blockIter.next()).historyToString_Owner());
             
            }
            blockOut_Renters.close();
            blockOut_Owners.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * sets up directory for the output files
     * requires Calendar class to set the directory by current data
     *  directory is set as: AgentDecision + (year.month.day.hour.minutes)
     *
     */
    public void initialize() { // Priyasree_DeadCode : Unreachable code_
        // get unique id for run
        // get year.month.date.time for dir
        Calendar rightNow = Calendar.getInstance();
        String directoryName; 
        
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minutes = rightNow.get(Calendar.MINUTE);

        File mirarOutputDir = new File("MirarOutput");
        if (! mirarOutputDir.exists()) {
            mirarOutputDir.mkdir();
        }
        
        if (MirarUtils.RENTER_DATA_FILE.contains("LA")) {
        	MirarUtils.CITY = "LosAngeles";
        } else if (MirarUtils.RENTER_DATA_FILE.contains("ATL")) {
        	MirarUtils.CITY = "Atlanta";
        } else if (MirarUtils.RENTER_DATA_FILE.contains("Chicago")) {
        	MirarUtils.CITY = "Chicago";
        }
        
        if (MirarUtils.RENTER_DATA_FILE.contains("BWequal")) {
        	directoryName =  MirarUtils.CITY + "_" + MirarUtils.AGENT_DECISION + "_" + "BWequality_" +  year + "." + (month+1) + "." + day + "." + hour + "." + minutes;
        } 
        else if (MirarUtils.RENTER_DATA_FILE.contains("_HWequal")) {
            directoryName =  MirarUtils.CITY + "_" + MirarUtils.AGENT_DECISION + "_" + "HWequality_" +  year + "." + (month+1) + "." + day + "." + hour + "." + minutes;
            } 
        else if (MirarUtils.RENTER_DATA_FILE.contains("_BHWequal")) {
            directoryName =  MirarUtils.CITY + "_" + MirarUtils.AGENT_DECISION + "_" + "BHWequality_" +  year + "." + (month+1) + "." + day + "." + hour + "." + minutes;
            }
        else { 
        	directoryName = MirarUtils.CITY + "_" +  MirarUtils.AGENT_DECISION + "_" +  year + "." + (month+1) + "." + day + "." + hour + "." + minutes;
        }
        
        File outputDir = new File("MirarOutput", directoryName);

        outputDir.mkdir();

        agentDir = new File(outputDir, "AgentData");
        agentDir.mkdir();
        blockDir = new File(outputDir, "BlockData");
        blockDir.mkdir();

    }

}
