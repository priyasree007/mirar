package mirar;
import java.util.*;

//import codecLib.mpa.i;
import cern.colt.list.*;
import cern.jet.stat.*;

/**
 *  An abstract superclass for all the AgentDecision subclasses.
 *  
 *  This class also defines helper functions for use by the subclasses.
 *
 *
 * @author Robert Najlis
 * @version 1.0 
 */

public abstract class AgentDecision {


    public abstract HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent); // Priyasree_DeadCode : Unreachable code_
    
    public abstract double computeUtility(Block b, Agent agent, int tenure); 
    
    public abstract double computeMarginalUtility(Block b, int tenure); 
    
    public abstract double solveForPrice(double marginalUtil, Block b, int tenure); 
    
	/**
	 * Returns utilities for a given list of housing units. 
	 * 
	 * @param housingUnits
	 *            list of housing units
	 * @return list of utilities associated with housing units
	 */
    public DoubleArrayList getUtilitiesByHousingUnit(ArrayList housingUnits, Agent agent) { 
        DoubleArrayList utilities = new DoubleArrayList();
        int numUnits = housingUnits.size();

        HousingUnit hu;
       
        for (int i=0; i<numUnits; i++) {
            hu = (HousingUnit)housingUnits.get(i);
                utilities.add(getUtility(hu, agent));
        }
        return utilities;
    }
	/**
	 * Returns utility for a given housing unit. 
	 * 
	 * @param housingUnit
	 *            housing unit
	 * @return utility associated with block that contains housing unit
	 */
    public double getUtility(HousingUnit hu, Agent agent) { 
       
            Block b = hu.getBlock();
            return b.getUtility(agent);    
    }
	

    public ArrayList getAllBlocks() { // Priyasree_DeadCode : Unreachable code_
        return MirarUtils.BLOCKS;
    }


	// rriolo version of these *Old versions.
	// the *Old versions would run ok, but use more and more
	// memory , till at some step the y hit some limit
	// and speed drops fast, thenn later depending o n how
	// much mem was allocated via the java parameters,
	// at so me step it crashes/
	// the hope of these new versions is that they
	// wont u se so much ram allocated all these arraylists
	// thar arent needed so slow it down anyway.


    public int getTotalAgentsInNeighborhood(Block b) { 
        int totalAgents = b.getNumAgents();
		for ( Object tb : b.getNeighbors() ) {
			totalAgents += ((Block) tb).getNumAgents();
        }		
		//System.stderr.printf( "getTotalAgentsInNbhood...\n" );
        return totalAgents;
    }
    
}
 // end AgentDecision