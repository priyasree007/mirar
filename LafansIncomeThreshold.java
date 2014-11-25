/**
 * I haven't yet figured out how to run the LA FANS choice models with market clearing rents (I need
 * a measure of neighborhood quality that isn't correlated with measurement error in rent)
 */

package mirar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import cern.colt.list.DoubleArrayList;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LafansIncomeThreshold extends AgentDecision {

	/**
	 * 
	 */
	public LafansIncomeThreshold() {
		super();
	}
	  public HousingUnit select(ArrayList possibleHousingUnitList, HousingUnit currUnit, Agent agent) {
        
        PrintWriter testSelect = null;
        try {
            testSelect = new PrintWriter(new FileOutputStream("testSelect.txt", true));
            
        } catch (IOException ioe) { // Priyasree_Audit: Empty catch clause for exception ioe_Delete the empty catch clause. // Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
            
        }      
        
      //  this.possibleHousingUnitList.addAll(possibleHousingUnitList);
        
        ArrayList availableUnits = new ArrayList();
        //availableUnits = selectAffordableUnits(agent); // use only units with rents <= threshold
      //  System.out.println("number of affordable units is " + availableUnits.size());
        availableUnits.addAll(agent.getPossibleHousingUnitList());

        if (currUnit == null) {
            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
        }
    // availableUnits.add(currUnit);
        
        DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
        
        double utilityCurrentUnit = computeUtilitityForOwnUnit(availableUnits, currUnit, agent); 

        utilities.add(utilityCurrentUnit);
        availableUnits.add(currUnit);
      
        int choice = MirarUtils.sampleFromRawDistribution(utilities);
    // System.out.println("this is agent's income " + agent.getIncome());
    //    for(int i=0; i<availableUnits.size(); i++){
    //    	System.out.println("this is the 20th percentile rent in alternative " + i + " " + ((HousingUnit)availableUnits.get(i)).block.getNeighborhoodPercentileRent(.2));
    //    }
    //    System.out.println("this is 20th centile in chosen neighborhood " + ((HousingUnit)availableUnits.get(choice)).block.getPercentileRent(.2));
    //    System.out.println("is agent staying in own unit? " + ((availableUnits.size()-1)==choice));
        if (choice == -1) {
            
            return null;
        }
        else {
           return (HousingUnit) availableUnits.get(choice);
        }
    }
    
      public double computeUtility(Block b, Agent a, int tenure) {
      	double utility= -12.0;
    //    ArrayList blocks = new ArrayList();
        if (b == null) {
        System.out.println("block is null");
        }   
        else {
        	double ratio = (b.getNeighborhoodPercentileRent(.2, tenure)*12.0)/a.getIncome();
        	utility = Math.exp(13.88038*ratio -33.02009*ratio*ratio);
           	} 
        return utility;
    }
    
    public double computeUtilitityForOwnUnit(ArrayList units, HousingUnit currUnit, Agent a) {
            Block b = currUnit.getBlock();  //  block;
            double utility = -12.0;
            /**
             * Note that I need to rescale d to account for the fact that, in the LA FANS estimates, 
             * dij is the probability of choosing one's own neighborhood instead of another neighborhood. 
             * Since neighborhoods = Census tracts, this coefficient is scaled to be the probability of 
             * choosing one's own Census tract instead of one of the 1651 other Census tracts in LA County.
             * 
             * I think what I want to do is scale the dij coefficient by:
             * 	 (number of alternatives in simulation)/1652
             *  
             */
            double d; 
          if(units.size()>1651){
          	d=10.21492; 
          } else {
            d = 10.21492 - Math.log(1651/units.size()); // Priyasree_Audit: Integer division in a floating-point expression_Cast one of the integers to a float. This will prevent round off error."
          }
            double ratio = (b.getNeighborhoodPercentileRent(.2, a.getTenure())*12.0)/a.getIncome();
        	utility = Math.exp(d + 13.88038*ratio -10.7266*ratio -33.02009*ratio*ratio + 24.59793*ratio*ratio);
           	return utility; 
    }

    
    public double computeMarginalUtility(Block b, int tenure) {
    	/**
    	 * For now, not using market clearing rents with the LA FANS choice
    	 * functions.. so these classes are undefined. 
    	 */
    	double t=0; 
    	return t; 
    }
    
    public double solveForPrice(double marginalUtil, Block b, int tenure) {
    	double t=0; 
    	return t; 
    }
    
    public String toString() {
        return "LafansThresholdIncome";
    }
}
