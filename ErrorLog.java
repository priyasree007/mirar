/*
 *  @author Robert Najlis
 *  Created on Jan 22, 2006
 *
 *
 */
package mirar;

import java.io.*;
import java.util.Calendar;

import uchicago.src.sim.engine.*;

public class ErrorLog {

    private File logfile;
    private PrintWriter logWriter;
    
    boolean errorLogged = false;
    /**
     * @param args
     */
    
    private static ErrorLog instance = new ErrorLog();

    private ErrorLog() {
        super();
    
    }

    public void setup() {
         logfile = new File("errorLog.txt");
         try {
         logWriter = new PrintWriter(new FileOutputStream(logfile, true));
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
    public void logError(String msg) {
        
       errorLogged = true;
        
        Calendar rightNow = Calendar.getInstance();
        
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minutes = rightNow.get(Calendar.MINUTE);

        logWriter.println("Error:  ");
        logWriter.println(msg);
        logWriter.println(" Date: " + year + "." + (month+1) + "." + day + "." + hour + "." + minutes); 
        logWriter.println("RENTER_DATA_FILE: " + MirarUtils.RENTER_DATA_FILE  + "\n"
                   + ", OWNER_DATA_FILE: " +  MirarUtils.OWNER_DATA_FILE  + "\n"
                   +  ", NEIGHBORHOOD_DATA_FILE: " + MirarUtils.NEIGHBORHOOD_DATA_FILE  + "\n"
                   +  ", BLOCK_SHP_FILE: " + MirarUtils.BLOCK_SHP_FILE    + "\n"
                   +  ", BLOCK_GROUP_SHP_FILE: " + MirarUtils.BLOCK_GROUP_SHP_FILE   + "\n"
                   +  ", CENSUS_TRACT_SHP_FILE: " + MirarUtils.CENSUS_TRACT_SHP_FILE   + "\n"
                   +  ", AGENT_DECISION: " + MirarUtils.AGENT_DECISION  + "\n"
                   );
       
    }
    
    public static ErrorLog getInstance() {
      return instance;
    }
    
    public void finishUp() {
        if (errorLogged == true) { //Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
            logWriter.flush();
            logWriter.println("\n##########################\n");
        }
        
        logWriter.close();
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
