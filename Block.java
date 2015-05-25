
package mirar;

import java.util.ArrayList;
import java.util.Iterator;

import uchicago.src.sim.util.Random;
import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.jet.stat.Descriptive;

/** Block 
 * 
 *  has a list of housing units (owned and rented)
 *  knows which types of agents are in the block
 *  knows stats related to the block class (transition probablility, utility list)
 * 
 * @author Robert Najlis, Elizabeth Bruch
 * @version 1.0
 */

public class Block {
	//Priyasree_B$WStatistics===========================//
	public static double pctWhiteInNeighborhood_B;
	public static double pctBlackInNeighborhood_B;
	public static double totBlackInNeighborhood_B = 0.0; 
	public static double totWhiteInNeighborhood_B = 0.0; 
	//Priyasree_B$WStatistics===========================//
	private ArrayList neighborList = new ArrayList(); // Priyasree_DeadCode : Unreachable code_

	private int blockNum; // Priyasree_DeadCode : Unreachable code_
	private int blockGroupNum; // Priyasree_DeadCode : Unreachable code_
	private int censusTractNum; // Priyasree_DeadCode : Unreachable code_
	private String stfid; // Priyasree_DeadCode : Unreachable code_
	private BlockGroup blockGroup;

	private int numHousingUnits = 0; // Priyasree_DeadCode : Unreachable code_

	private int numWhite; // Priyasree_DeadCode : Unreachable code_
	private int numBlack; // Priyasree_DeadCode : Unreachable code_
	private int numAsian; // Priyasree_DeadCode : Unreachable code_
	private int numHispanic; // Priyasree_DeadCode : Unreachable code_
	private int numAgents; // Priyasree_DeadCode : Unreachable code_
	private double expectedNumAgents; // Priyasree_DeadCode : Unreachable code_

	private double totalIncome; // Priyasree_DeadCode : Unreachable code_

	private boolean update = false; // Priyasree_DeadCode : Unreachable code_

	public ArrayList housingUnitList_Renters = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
	public ArrayList housingUnitList_Owners = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
	public ArrayList housingUnitList_All = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
	
	private DoubleArrayList incomeList = new DoubleArrayList();  // Priyasree_DeadCode : Unreachable code_
	private DoubleArrayList utilityList_Owners = new DoubleArrayList(MirarUtils.NUM_AGENT_TYPES); // Priyasree_DeadCode : Unreachable code_
	private DoubleArrayList utilityList_Renters = new DoubleArrayList(MirarUtils.NUM_AGENT_TYPES); // Priyasree_DeadCode : Unreachable code_
	private DoubleArrayList utilityList_All = new DoubleArrayList(MirarUtils.NUM_AGENT_TYPES); // Priyasree_DeadCode : Unreachable code_
    
	private DoubleArrayList transitionProbabilityList_Owners = new DoubleArrayList(MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES); // Priyasree_DeadCode : Unreachable code_
	private DoubleArrayList transitionProbabilityList_Renters = new DoubleArrayList(MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES); // Priyasree_DeadCode : Unreachable code_
	private DoubleArrayList transitionProbabilityList_All = new DoubleArrayList(MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES); // Priyasree_DeadCode : Unreachable code_
		
	public CensusUnitHandler censusUnitHandler; // Priyasree_DeadCode : Unreachable code_
	private IntArrayList raceAndIncomeList = new IntArrayList(MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES); // Priyasree_DeadCode : Unreachable code_
	private int [] raceIncomeRent_Renters= null; // Priyasree_DeadCode : Unreachable code_
	private int [] raceIncomeRent_Owners = null; // Priyasree_DeadCode : Unreachable code_
	private int [] raceIncomeRent_All = null; // Priyasree_DeadCode : Unreachable code_
	
    private ArrayList renterHistory = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    private BlockHistory carryOverHistory_Renter = null; // Priyasree_DeadCode : Unreachable code_
   
    private ArrayList ownerHistory = new ArrayList(); // Priyasree_DeadCode : Unreachable code_
    private BlockHistory carryOverHistory_Owner = null; // Priyasree_DeadCode : Unreachable code_
    
	
	public Block() {
		int raceAndIncomeListSize = MirarUtils.NUM_RACES*MirarUtils.NUM_INCOMES;
        
		for (int i = 0; i < raceAndIncomeListSize; i++) {
		    raceAndIncomeList.add(0);
		    transitionProbabilityList_Renters.add(0.0);
		    utilityList_Renters.add(0.0);
		    utilityList_Owners.add(0.0);
		    utilityList_All.add(0.0);
		}
		
	}

	public Block(int empty) { 
        // an empty constructor that does nothing
	}

	public Block(int blockNum, BlockGroup bg) { 
		this();
		this.blockNum = blockNum;
		this.blockGroup = bg;
	}

	public Block(String stfid, CensusUnitHandler censusUnitHandler) { 
		this();
		this.censusUnitHandler = censusUnitHandler;
		this.stfid = stfid;
		neighborList = new ArrayList();
		this.stfid = stfid;
		// parse stfid into census tract, block group and block num
		censusTractNum = Integer.parseInt(stfid.substring(5, 11));
		this.blockGroupNum = Integer.parseInt(stfid.substring(11, 12));
		this.blockNum = Integer.parseInt(stfid.substring(11));
		censusUnitHandler.addBlockToBlockGroup(this);
	}

	/**
	 * 
	 * @param tenure 
	 * @return sum of transitionProbabilityList - Renters or Owners
	 */
    public double sumExpectedNumAgents(int tenure){ 
        if (tenure == Agent.OWNER) return sumExpectedNumAgents_Owners();
        else return sumExpectedNumAgents_Renters();
    }
    
    /*public double sumExpectedNumAgents(){ // Priyasree_DeadCode : Unreachable code_
        return sumExpectedNumAgents_All();
    }
    */
    /**
     * 
     * @return sum of transitionProbabilityList_Renters
     */
    public double sumExpectedNumAgents_Renters(){ // Priyasree_DeadCode
        double total = 0.0;
        int listSize = transitionProbabilityList_Renters.size();
        for (int i=0; i<listSize; i++) {
            total += transitionProbabilityList_Renters.get(i);
        }
        return total;
    }
    
    /**
     * 
     * @return sum of transitionProbabilityList_Owners
     */
    public double sumExpectedNumAgents_Owners(){ // Priyasree_DeadCode
        double total = 0.0;
        int listSize = transitionProbabilityList_Owners.size();
        for (int i=0; i<listSize; i++) {
            total += transitionProbabilityList_Owners.get(i);
        }
        return total;
    }
    
    /*public double sumExpectedNumAgents_All(){ // Priyasree_DeadCode : Unreachable code_
        double total = 0.0;
        int listSize = transitionProbabilityList_All.size();
        for (int i=0; i<listSize; i++) {
            total += transitionProbabilityList_All.get(i);
        }
        return total;
    }*/
    
    /**
     * Bayer's contraction
     *	compare with total number of housing units
     *	double nHat = expectedNumAgents/housingUnits.size()*1.0;
     *	retur	n:
     *	1.0 if  nHat <= 1   expected num agents <= num housingUnits
     *	else return nHat (nHat >1)
     * @param tenure 
     *
     * @return nHat
     */
	public double computeOffset(double occRate, int tenure){ // Priyasree_DeadCode : Unreachable code_
	    /*
	     * Note: Offset returns NaN for blocks with no housing units. 
	    */ 
	    double offset = 1.0;
	    double expectedNumAgents = sumExpectedNumAgents(tenure);
	    ArrayList units = this.getHousingUnitsByTenure(tenure);
	    if(units.size()!=0){
	        offset = (expectedNumAgents/(units.size()*occRate));
	    }
	    if ( Double.isNaN( offset ) ) ErrorLog.getInstance().logError("Block#computeOffset offset NaN");
	    return offset;
	}
	
	public IntArrayList getRaceAndIncomeList() { 
		return raceAndIncomeList;
	}

	public void setUpdate(boolean update) { // Priyasree_DeadCode : Unreachable code_
		this.update = update;
	}

	public void update(double utility, Agent agent) { // Priyasree_DeadCode : Unreachable code_

		this.addUtility(utility, agent);
		this.addToHistory(agent.getTenure());
	}

	 /**
     * Adds info for agent to the block. Updates total number of people, 
     * total income, and total number of agent's race group. 
     * 
     * @param agent 
     */
	//public void addAgent(Agent agent) { //PriyasreeAddAgent
	public void addAgentToBlock(Agent agent) { //PriyasreeAddAgent	
		//System.out.println("Add Agent from Block");//PriyasreeAddAgent
		numAgents++;
		int position = ((agent.getRace() * MirarUtils.NUM_INCOMES) + agent.getIncomeCategory());
		if (raceAndIncomeList == null ) System.out.println("Block  - raceAndIncomeList is NULL");
		int num = raceAndIncomeList.get(position);
		num += 1;
		raceAndIncomeList.set(position, num);
		//add race
		int race = agent.getRace();
		if (race == 0)
			numWhite++;
		else if (race == 1)
			numBlack++;
		else if (race == 2)
			numAsian++;
		else if (race == 3)
			numHispanic++;
//priyaComm: create a hash table with index vs race, ask for their race n increment the corresponding entry 
		//add income
		this.totalIncome += agent.getIncome();
		incomeList.add((double) agent.getIncome()); //Priyasree_Audit: Unnecessary type cast to double_Delete the unnecessary cast.
	}

    public void addToHistory(int tenure) { 
        if (MirarUtils.BLOCK_HISTORY == true) {//Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
            if (tenure == Agent.RENTER) {
                this.renterHistory.add(new BlockHistory(this.getNumVacantHousingUnits(), this.getRaceAndIncomeList(), 
                        this.getMedianRent(Agent.RENTER), this.getPctBlkInNeighborhood(), this.getPctWhtInNeighborhood(), 
                        this.getPercentileRent(.1, Agent.RENTER), this.getPercentileRent(.3, Agent.RENTER), this.getPercentileRent(.5, Agent.RENTER), this.getPercentileRent(.6, Agent.RENTER), this.getPercentileRent(.9, Agent.RENTER)));
                //Priyasree_B$WStatistics===========================//
                pctBlackInNeighborhood_B = this.getPctBlkInNeighborhood();
                totBlackInNeighborhood_B = totBlackInNeighborhood_B + pctBlackInNeighborhood_B;
                
                pctWhiteInNeighborhood_B = this.getPctWhtInNeighborhood();
                totWhiteInNeighborhood_B = totWhiteInNeighborhood_B + pctWhiteInNeighborhood_B;
              //Priyasree_B$WStatistics===========================//
            }
            else {
                this.ownerHistory.add(new BlockHistory(this.getNumVacantHousingUnits(), this.getRaceAndIncomeList(), 
                        this.getMedianRent(Agent.OWNER), this.getPctBlkInNeighborhood(), this.getPctWhtInNeighborhood(), 
                        this.getPercentileRent(.1, Agent.OWNER), this.getPercentileRent(.3, Agent.OWNER), this.getPercentileRent(.5, Agent.OWNER), this.getPercentileRent(.6, Agent.OWNER), this.getPercentileRent(.9, Agent.OWNER)));
            }
            }
        }
    
   
	
	/**
	 * Puts specified agent in specified housing unit. 
	 * 
	 * @param HousingUnit housing unit 
	 *            housing unit in the block
	 * @param Agent agent
	 * 			  agent to be put in housing unit
	 */
	/*public void populateHousingUnit(HousingUnit hu, Agent agent) { // Priyasree_DeadCode : Unreachable code_

		hu.setOccupied(true);
         hu.setTenure(agent.getTenure());
		this.addAgent(agent);
	}*/
	
	
    /**
     * called by BlockGroup#initializeHousingUnits
     */
	public HousingUnit addOccupiedHousingUnit(double rent, Agent agent) { 
		HousingUnit unit = new HousingUnit(this.numHousingUnits, this, agent.getTenure());
        this.numHousingUnits++;
		unit.setRent(rent);
		//unit.addAgent(agent); //PriyasreeAddAgent
		unit.addHousingUnitToAgent(agent); //PriyasreeAddAgent
		unit.setOccupied(true);
			if (agent.getTenure() == Agent.RENTER) {
				housingUnitList_Renters.add(unit);    
			}
			else if (agent.getTenure() == Agent.OWNER) {
				housingUnitList_Owners.add(unit);    
			}
			else System.out.println("--- Block - add Occupied HU - tenure not 0 or 1");			
			housingUnitList_All.add(unit); 

        if (unit == null) System.out.println("*******    block -- add  occupied housing unit  -- unit is null !!!!1");
        if (unit.getAgent() == null) System.out.println("" +
                "878" +
                "87877         block -- add  occupied housing unit  -- Agent is null !!!!1");
		return unit;
	}
	
	/**
	 * Adds a new vacant housing unit to the block, updates
	 * list of vacant housing units. 
	 * @param tenure 
	 * 
	 */
	public HousingUnit addVacantHousingUnit(int tenure) { 
        HousingUnit unit = new HousingUnit(this.numHousingUnits, this, tenure);
        this.numHousingUnits++;

        	if (tenure == Agent.RENTER) {
        		housingUnitList_Renters.add(unit);    
        	}
        	else if (tenure == Agent.OWNER) {
        		housingUnitList_Owners.add(unit);    
        	}
        	else System.out.println("--- Block - add vacant HU - tenure not 0 or 1");
        	housingUnitList_All.add(unit);
		return unit;
	}
	

    /**
	 * Adds a utility for the appropriate agent type to the list of
	 * utilities for the block. 
	 * 
	 * @param utility
	 * 		value of utility to be added
	 * @param agent
	 * 		appropriate agent type for utility
	 * 
	 */
	public void addUtility(double utility, Agent agent) {
		int race = agent.getRace();
		int incomeCategory = agent.getIncomeCategory();
		int position = ((race * MirarUtils.NUM_INCOMES) + incomeCategory);
			if (agent.getTenure() == Agent.OWNER) {
				utilityList_Owners.set(position, utility);     
			}
			else {
				utilityList_Renters.set(position, utility);   
			}	

	}


	/**
	 * Gets the utility for the appropriate agent type from the list of
	 * utilities for the block. 
	 * 
	 * @param agent
	 * 		agent type
	 * 
	 * @return double utility
	 * 
	 */
	public double getUtility(Agent agent) { 
	    if (agent.getTenure() == Agent.OWNER ) {       
	        return utilityList_Owners.get(((agent.getRace() * MirarUtils.NUM_INCOMES) + agent.getIncomeCategory()));
	    }
        else {
            return utilityList_Renters.get(((agent.getRace() * MirarUtils.NUM_INCOMES) + agent.getIncomeCategory()));
	
        }
    }

	
	public void setTransitionProbability(Agent agent, double probability) { 
        if (agent.getTenure() == Agent.RENTER)
            transitionProbabilityList_Renters.set( ((agent.getRace() * MirarUtils.NUM_INCOMES )+ agent.getIncomeCategory()), probability);
        
        else
	    transitionProbabilityList_Owners.set( ((agent.getRace() * MirarUtils.NUM_INCOMES )+ agent.getIncomeCategory()), probability);
}

	/**
	 * Samples vacant housing units from all available vacancies in block, 
	 * using vacancy sample proportion in MirarUtils. 
	 * @param tenure 
	 * 
	 * @return array of sampled vacancies
	 * 
	 */
	public ArrayList sampleVacantHousingUnits(int tenure) { // Priyasree_DeadCode : Unreachable code_
		ArrayList units = new ArrayList();

		units.addAll(getVacantHousingUnits(tenure));

		int probInt = 0;
        double sample = 0.0;
        if (tenure == Agent.RENTER) sample = MirarUtils.RENTER_VACANT_HOUSING_UNIT_SAMPLE; 
        else sample= MirarUtils.OWNER_VACANT_HOUSING_UNIT_SAMPLE;
        probInt =  MirarUtils.probabilisticInterpolation(units.size(), sample);
		int sampleNum = Math.min(probInt, units.size());

		ArrayList result = new ArrayList(sampleNum);

		for (int i = 0; i < sampleNum; i++) {
			int choice = Random.uniform.nextIntFromTo(0, units.size() - 1);
			result.add(units.get(choice));
			units.remove(choice);
		}

		return result;
	}
	
	public ArrayList sampleVacantHousingUnits() { 
		ArrayList units = new ArrayList();

		units.addAll(getVacantHousingUnits());

		int probInt = 0;
        double sample = 0.0;
        sample = MirarUtils.ALL_VACANT_HOUSING_UNIT_SAMPLE; 
        probInt =  MirarUtils.probabilisticInterpolation(units.size(), sample);
		int sampleNum = Math.min(probInt, units.size());

		ArrayList result = new ArrayList(sampleNum);

		for (int i = 0; i < sampleNum; i++) {
			int choice = Random.uniform.nextIntFromTo(0, units.size() - 1);
			result.add(units.get(choice));
			units.remove(choice);
		}

		return result;
	}
	/**
	 * Samples vacant housing units from all available vacancies in block, 
	 * using vacancy sample proportion in MirarUtils. 
	 * 
	 * @param proportion
	 * 		proportion of vacancies to sample from block
	 * @param tenure 
	 * @return ArrayList 
	 * 		array of sampled vacancies
	 * 
	 */
	public ArrayList sampleVacantHousingUnits(double proportion, int tenure) { // Priyasree_DeadCode : Unreachable code_	
		ArrayList units = new ArrayList();
		units.addAll(getVacantHousingUnits(tenure));
		int sampleNum = MirarUtils.probabilisticInterpolation(units.size(),
				proportion);
		ArrayList result = new ArrayList(sampleNum);
		for (int i = 0; i < sampleNum; i++) {
			result.add(units.get(i));
			units.remove(i);
		}
		return result;
	}

	public ArrayList sampleVacantHousingUnits(double proportion) { 	
		ArrayList units = new ArrayList();
		units.addAll(getVacantHousingUnits());
		int sampleNum = MirarUtils.probabilisticInterpolation(units.size(),
				proportion);
		ArrayList result = new ArrayList(sampleNum);
		for (int i = 0; i < sampleNum; i++) {//priyaComm: may be random selection is req, chk how used
			System.out.println("I am here " + i); //Priyasree_Test
			result.add(units.get(i));
			units.remove(i);
		}
		return result;
	}

    
    public HousingUnit getHousingUnit(int unitNum, int tenure) { 
        if (tenure == Agent.RENTER)
            return getHousingUnit_Renters(unitNum, tenure);
        else  if (tenure == Agent.OWNER)
            return getHousingUnit_Owners(unitNum, tenure);
        else {
         System.out.println("----  Block get HU -- tenure not 0 or 1");
         return null;
        }
    }
    
	/*public HousingUnit getHousingUnit_All(int unitNum) { // Priyasree_DeadCode : Unreachable code_
		for (int i = 0; i < housingUnitList_All.size(); i++) {
			if (((HousingUnit) housingUnitList_All.get(i)).getHousingUnitNum() == unitNum) {
				return (HousingUnit) housingUnitList_All.get(i);
			}
		}
		return null;
	}*/
	
	public HousingUnit getHousingUnit_Renters(int unitNum, int tenure) {
		for (int i = 0; i < housingUnitList_Renters.size(); i++) {
			if (((HousingUnit) housingUnitList_Renters.get(i)).getHousingUnitNum() == unitNum) {
				return (HousingUnit) housingUnitList_Renters.get(i);
			}
		}
		return null;
	}
    
	public HousingUnit getHousingUnit_Owners(int unitNum, int tenure) { 
	    for (int i = 0; i < housingUnitList_Owners.size(); i++) {
	        if (((HousingUnit) housingUnitList_Owners.get(i)).getHousingUnitNum() == unitNum) {
	            return (HousingUnit) housingUnitList_Owners.get(i);
	        }
	    }
	    return null;
	}

    
    
	/*public void removeHousingUnitFromList(HousingUnit hu, ArrayList list) { // Priyasree_DeadCode : Unreachable code_
		for (int i = 0; i < list.size(); i++) {
			if (((HousingUnit) list.get(i)).getHousingUnitNum() == hu
					.getHousingUnitNum()) {
				list.remove(i);
			}
		}
	}*/


/**
 * Removes agent from block, and updates total number of people, total number of
 * agent's race and income group, income distribution. 
 * 
 * @param agent
 */
	public void removeAgent(Agent agent) { // Priyasree_DeadCode : Unreachable code_
		numAgents--;
		if (agent == null)System.out.println("66666  Block  removeAgent  agent is null");
		int position = ((agent.getRace() *MirarUtils.NUM_INCOMES) + agent.getIncomeCategory());
		int num = raceAndIncomeList.get(position);
		num -= 1;
		raceAndIncomeList.set(position, num);

		//subtract from race total
		int race = agent.getRace();
		if (race == Agent.WHITE) {
		    setNumWhite(getNumWhite()-1);
		} else if (race == Agent.BLACK) {
			setNumBlack(getNumBlack() - 1);
		} else if (race == Agent.ASIAN) {
		    setNumAsian(getNumAsian() - 1);
		} else if (race == Agent.HISPANIC) {
			setNumHispanic(getNumHispanic() - 1);;
		}

		//subtract income
		totalIncome -= agent.getIncome();
		incomeList.delete((double) agent.getIncome()); //Priyasree_Audit: Unnecessary type cast to double_Delete the unnecessary cast.

	}
/**
 * Updates housing unit with index huNum. If param occupied=true, set 
 * housing unit to occupied and add the agent to the housing unit. Otherwise, 
 * if occupied=false, remove the agent from the housing unit. 
 * 
 * @param huNum
 * @param occupied
 * @param agent
 */

	/*public void updateHousingUnit(HousingUnit hu) { // Priyasree_DeadCode : Unreachable code_

	}
*/
    /** deprecated
     * 
     * @param neighbor
     */
	/*public void addNeighborBlock(int neighbor) { // Priyasree_DeadCode : Unreachable code_
		neighborList.add(new Integer(neighbor));
	}*/

	public void addNeighbor(Block b) { 
		neighborList.add(b);
	}
	
	
		public ArrayList getNeighbors() { 
	      return neighborList;
	    
	}
	
	/*public Block getNeighbor(int index) { // Priyasree_DeadCode : Unreachable code_
		return (Block) neighborList.get(index);
	}*/
	

	public int getNumWhite() {
		return this.numWhite;
	} // end getNumWhite
 
	public void setNumWhite(int num) { // Priyasree_DeadCode : Unreachable code_
	    if (num < 0) num = 0;
		this.numWhite = num;
	} // end setNumWhite

	public int getNumBlack() { 
		return this.numBlack;
	} // end getNumBlack

	public void setNumBlack(int num) { // Priyasree_DeadCode : Unreachable code_
	    if (num < 0) num = 0;
		this.numBlack = num;
	} // end setNumBlack

	public int getNumAsian() { 
		return this.numAsian;
	} // end getNumAsian

	public void setNumAsian(int num) { // Priyasree_DeadCode : Unreachable code_
	    if (num < 0) num = 0;
		numAsian = num;
	} // end setNumAsian

	public int getNumHispanic() {
		return this.numHispanic;
	} // end getNumHispanic

	public void setNumHispanic(int num) { // Priyasree_DeadCode : Unreachable code_
	    if (num < 0) num = 0;
		this.numHispanic = num;
	} // end setNumHispanic

	public int getNumAgents() { 
		return this.numAgents;
	} // end getNumTotal

	
	
	/*public void setNumAgents(int num) { // Priyasree_DeadCode : Unreachable code_
	    if (num < 0) num = 0;
		this.numAgents = num;
	}*/ // end setNumTotal
/**
 * Gets number of agents in block of specified race. 
 * 
 * @param race
 * @return double
 * 		returns appropriate race total
 */
	

	/*public double getNumRace(int race) { // Priyasree_DeadCode : Unreachable code_
		if (race == Agent.WHITE) {
			return getNumWhite();
		} else if (race == Agent.BLACK) {
			return getNumBlack();
		} else if (race == Agent.ASIAN) {
			return getNumAsian();
		} else { // (race == Agent.HISPANIC) {
			return getNumHispanic();
		}
	}*/

/*public double getPctRace(int race){ // Priyasree_DeadCode : Unreachable code_
	if (race == Agent.WHITE) {
		return getPctWhite();
	} else if (race == Agent.BLACK) {
		return getPctBlack();
	} else if (race == Agent.ASIAN) {
		return getPctAsian();
	} else { // (race == Agent.HISPANIC) {
		return getPctHispanic();
		}
}*/

	public double getPctWhite() { 
		if (numAgents > 0) {
			return ((numWhite * 1.0) / (numAgents * 1.0));
		} else
			return 0.0;
	} // end pctWhite

	public double getPctBlack() { 
		if (numAgents > 0) {
			return ((numBlack * 1.0) / (numAgents * 1.0));
		} else
			return 0.0;
	} // end pctBlack

	public double getPctHispanic() { 
		if (numAgents > 0) {
			return ((numHispanic * 1.0) / (numAgents * 1.0));
		} else
			return 0.0;
	} // end pctHispanic

	public double getPctAsian() { 
		if (numAgents > 0) {
			return ((numAsian * 1.0) / (numAgents * 1.0));
		} else
			return 0.0;
	} // end pctAsian
	
	/**
	 * Returns information about the dominant race group in the block. For example, if
	 * blacks are the dominant race group, the method returns 1.(black %). If Asians 
	 * are the dominant race group, the method returns 2.(Asian %). If Hispanics are the
	 * dominant race group, the method returns 3.(Hispanic %). 
	 * 
	 * @return double
	 * 		indicator of the majority group, and the proportion of that group in block
	 * 	
	 */
	public double getRacePct() { 
	    
	    // white is highest
	    if (numWhite > numBlack && numWhite > numAsian
	            && numWhite > numHispanic) {
	        
	        return getPctWhite();
	    }
	    // black is highest
	    else if (numBlack > numWhite && numBlack > numAsian
	            && numBlack > numHispanic) {
	        
	        
	        return (1 + getPctBlack());
	    }
	    // asian is highest
	    else if (numAsian > numWhite && numAsian > numBlack
	            && numAsian > numHispanic) {
	        
	        
	        return (2 + getPctAsian());
	    }
	    //hispanic is highest
	    else if (numHispanic > numWhite && numHispanic > numBlack
	            && numHispanic > numAsian) {
	        
	        return (3 + getPctHispanic());
	    }
	    // all are equal
	    else
	        return -1.0;
	    
	}
/**
 * Returns the proportion of the specified race group
 * 
 * @param race
 * @return
 */
	/*public double getRacePct(int race) { // Priyasree_DeadCode : Unreachable code_
		if (race == Agent.WHITE) {
			return getPctWhite();
		} else if (race == Agent.BLACK) {
			return getPctBlack();
		} else if (race == Agent.ASIAN) {
			return getPctAsian();
		} else { // (race == Agent.HISPANIC) {
			return getPctHispanic();
		}
	}*/

	public int getBlockNum() { 
		return this.blockNum;
	}

	/*public void setBlockNum(int num) { // Priyasree_DeadCode : Unreachable code_
		this.blockNum = num;
	}*/

	/*public double getMeanRent(int tenure) { // Priyasree_DeadCode : Unreachable code_
	    double totalRent = 0.0;
	    int numHousingUnits = 0;
	    Iterator huIter = getHousingUnitsByTenure(tenure).iterator();
		while (huIter.hasNext()) {
		    HousingUnit hu =  (HousingUnit)huIter.next();
		    totalRent += hu.getRent();
		    numHousingUnits++;
		    
		}
		return (totalRent / (numHousingUnits*1.0));	
	}*/
	
	/*public double getMeanRent() { // Priyasree_DeadCode : Unreachable code_
	    double totalRent = 0.0;
	    int numHousingUnits = 0;
	    Iterator  huIter = housingUnitList_All.iterator();
		while (huIter.hasNext()) {
		    HousingUnit hu =  (HousingUnit)huIter.next();
		    totalRent += hu.getRent();
		    numHousingUnits++;
		    
		}
		return (totalRent / (numHousingUnits*1.0));	
	}*/
	
	
	public double getMedianRent(int tenure) { 
	    Iterator huIter = getHousingUnitsByTenure(tenure).iterator();
	    DoubleArrayList rents = new DoubleArrayList();
	  	while (huIter.hasNext()) {
		    HousingUnit hu =  (HousingUnit)huIter.next();
		    rents.add(hu.getRent());
		}
		rents.sort();
		return Descriptive.median(rents);
	}
	
	/*public double getMedianRent() { // Priyasree_DeadCode : Unreachable code_
        Iterator huIter = null;
        huIter = housingUnitList_All.iterator();
        
	    DoubleArrayList rents = new DoubleArrayList();
	    while (huIter.hasNext()) {
	        HousingUnit hu =  (HousingUnit)huIter.next();
	        rents.add(hu.getRent());
	        //   System.out.println("is this unit occupied? " + hu.isOccupied());
	        //   System.out.println("this is a rent: " + hu.getRent());
	    }
        
        rents.sort();
	    return Descriptive.median(rents);
	}*/
	
	public double getPercentileRent(double centile, int tenure) { 

        Iterator huIter = null;
        if (tenure == Agent.RENTER)
         huIter = housingUnitList_Renters.iterator();
        else huIter = housingUnitList_Owners.iterator();
        
        
	    DoubleArrayList rents = new DoubleArrayList();
		while (huIter.hasNext()) {
		    HousingUnit hu =  (HousingUnit)huIter.next();
		    rents.add(hu.getRent());
		}
		rents.sort();		
		return Descriptive.quantile(rents, centile);
	}
	
    /**
     * computes the Percentile Rent for all agents in the housing units 
     * (owners and renters)
     * 
     * @param centile
     * @return
     */
	/*public double getPercentileRent(double centile) { // Priyasree_DeadCode : Unreachable code_
	    
	    Iterator huIter = null;
	        huIter = housingUnitList_All.iterator();

	    DoubleArrayList rents = new DoubleArrayList();
	    while (huIter.hasNext()) {
	        HousingUnit hu =  (HousingUnit)huIter.next();
	        rents.add(hu.getRent());
	        //System.out.println("is this unit occupied? " + hu.isOccupied());
	        //System.out.println("this is a rent: " + hu.getRent());
	    }
         
	    rents.sort();		
	    return Descriptive.quantile(rents, centile);
	}*/
	
	public double getMedianIncome(){ 
		DoubleArrayList incomes = this.getIncomes();
	incomes.sort();
		return Descriptive.median(incomes);
}
	
	
	/*public DoubleArrayList getIncomeList() { // Priyasree_DeadCode : Unreachable code_
	    return incomeList;
	}*/
	
	public DoubleArrayList getIncomes() { 
        Iterator huIter = null;
	    DoubleArrayList incomes = new DoubleArrayList();
        // first get renter incomes
         huIter = housingUnitList_Renters.iterator();
        
		while (huIter.hasNext()) {//priyaComm: use java streams
		    HousingUnit hu =  (HousingUnit)huIter.next();
		    if(hu.isOccupied()){
				incomes.add(hu.getIncome());
		    }
		} 
        // 2nd get owner incomes
        huIter = housingUnitList_Owners.iterator();
        while (huIter.hasNext()) {
            HousingUnit hu =  (HousingUnit)huIter.next();
            if(hu.isOccupied()){
                incomes.add(hu.getIncome());
            }
        }
		return incomes;
	}
	
    public ArrayList getOccupiedHousingUnitsByTenure(int tenure) { // Priyasree_DeadCode : Unreachable code_
        Iterator huIter = null;
        
        
        if (tenure == Agent.RENTER)
            huIter = housingUnitList_Renters.iterator();
        else huIter = housingUnitList_Owners.iterator();
        
        
        ArrayList huList = new ArrayList();
        while (huIter.hasNext()) {
            HousingUnit hu =  (HousingUnit)huIter.next();
            if(hu.isOccupied()){
                huList.add(hu);
            }
        }
        return huList;
    }
    
    /*public ArrayList getOccupiedHousingUnitsByRace(int race) { // Priyasree_DeadCode : Unreachable code_
        Iterator huIter = null;
        
        
        // first get renter incomes
         huIter = housingUnitList_Renters.iterator();
      //  else huIter = housingUnitList_Owners.iterator();
        
        
        ArrayList huList = new ArrayList();
        while (huIter.hasNext()) {
            HousingUnit hu =  (HousingUnit)huIter.next();
            if(hu.isOccupied() && hu.getAgent().getRace() == race){
                huList.add(hu);
            }
        }
        
        // 2nd get owner incomes
        huIter = housingUnitList_Owners.iterator();
        while (huIter.hasNext()) {
            HousingUnit hu =  (HousingUnit)huIter.next();
            if(hu.isOccupied() && hu.getAgent().getRace() == race){
                huList.add(hu);
            }
        }
        return huList;
        
    }*/
    
	/*public DoubleArrayList getWhiteIncomes() { // Priyasree_DeadCode : Unreachable code_
	    ArrayList huList = this.getOccupiedHousingUnitsByRace(Agent.WHITE);
	    Iterator huIter = huList.iterator();
	    DoubleArrayList incomes = new DoubleArrayList();
	    HousingUnit hu;
	    while (huIter.hasNext()) {
	        hu = (HousingUnit)huIter.next();
	        incomes.add(hu.getAgent().getIncome());
	    }
	    return incomes;
	}*/
	
	/*public DoubleArrayList getBlackIncomes() { // Priyasree_DeadCode : Unreachable code_
        ArrayList huList = this.getOccupiedHousingUnitsByRace(Agent.BLACK);
        Iterator huIter = huList.iterator();
        DoubleArrayList incomes = new DoubleArrayList();
        HousingUnit hu;
        while (huIter.hasNext()) {
            hu = (HousingUnit)huIter.next();
            incomes.add(hu.getAgent().getIncome());
        }
        return incomes;
	}*/
	 
	/*public DoubleArrayList getAsianIncomes() { // Priyasree_DeadCode : Unreachable code_
        ArrayList huList = this.getOccupiedHousingUnitsByRace(Agent.ASIAN);
        Iterator huIter = huList.iterator();
        DoubleArrayList incomes = new DoubleArrayList();
        HousingUnit hu;
        while (huIter.hasNext()) {
            hu = (HousingUnit)huIter.next();
            incomes.add(hu.getAgent().getIncome());
        }
        return incomes;
	}*/
	
	/*public DoubleArrayList getHispanicIncomes() { // Priyasree_DeadCode : Unreachable code_
        ArrayList huList = this.getOccupiedHousingUnitsByRace(Agent.HISPANIC);
        Iterator huIter = huList.iterator();
        DoubleArrayList incomes = new DoubleArrayList();
        HousingUnit hu;
        while (huIter.hasNext()) {
            hu = (HousingUnit)huIter.next();
            incomes.add(hu.getAgent().getIncome());
        }
        return incomes;
	}*/
	
	public double getMeanIncome() { 
		// if (totalIncome > 0.0) {
		return (totalIncome / (numAgents * 1.0));
		// }
		// else return ((totalIncomeCategory*1.0)/(numAgents*1.0));
	}
	
    /*public double getMeanIncomeByRace(int race) { // Priyasree_DeadCode : Unreachable code_
        double totalIncome = 0.0; 
        int totalAgents = 0;
        ArrayList huList = this.getOccupiedHousingUnitsByRace(Agent.WHITE);
        Iterator huIter = huList.iterator();
        HousingUnit hu;
        while (huIter.hasNext()) {
            hu = (HousingUnit)huIter.next();
            totalIncome += hu.getAgent().getIncome();
            totalAgents++;
        }
        return (totalIncome / (totalAgents * 1.0));
    }*/
    
	/*public double getMeanWhiteIncome() { // Priyasree_DeadCode : Unreachable code_
	    return getMeanIncomeByRace(Agent.WHITE);
	}*/
	
	/*public double getMeanBlackIncome() { // Priyasree_DeadCode : Unreachable code_
        return getMeanIncomeByRace(Agent.BLACK);
	}*/
	
	/*public double getMeanAsianIncome() { // Priyasree_DeadCode : Unreachable code_
        return getMeanIncomeByRace(Agent.ASIAN);
	}*/
	
	/*public double getMeanHispanicIncome() { // Priyasree_DeadCode : Unreachable code_
        return getMeanIncomeByRace(Agent.HISPANIC);
	}*/
	
	 public int getTotalAgentsInNeighborhood() { 
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumAgents();
            
        }
        return totalAgents;
    }
	
	     
    /*public double getNeighborhoodMedianRent(int tenure) { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
		DoubleArrayList rents  = new DoubleArrayList();
		Iterator iter = neighborhood.iterator();
		while(iter.hasNext()) {
			Block block = (Block)iter.next();
			rents.addAllOf(block.getRentList(tenure));         
		}
		rents.sort();
		return Descriptive.median(rents);
	}*/
    
   /* public double getNeighborhoodPercentileRent(double centile, int tenure) { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
		DoubleArrayList rents  = new DoubleArrayList();
		Iterator iter = neighborhood.iterator();
		while(iter.hasNext()) {
			Block block = (Block)iter.next();
			rents.addAllOf(block.getRentList(tenure));         
		}
		rents.sort();
		return Descriptive.quantile(rents, centile);
	}*/
	
	
    /*public double getNeighborhoodMedianWhiteIncome() { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        DoubleArrayList incomes  = new DoubleArrayList();
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            incomes.addAllOf(block.getWhiteIncomes());
            
        }
        incomes.sort();
        return Descriptive.median(incomes);
    }*/
    
    /*public double getNeighborhoodTotalHousingUnits(){ // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        int count=0; 
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            count += block.getNumHousingUnits(); 
            
        }
        return count;    	
    }*/
    
    /*public double getNeighborhoodTotalNumAgents(){ // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        int count=0; 
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            count += block.getNumAgents();
            
        }
        return count;    	
    }*/
    
    /*public double getNeighborhoodMedianBlackIncome() { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        DoubleArrayList incomes  = new DoubleArrayList();
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            incomes.addAllOf(block.getBlackIncomes());
            
        }
        incomes.sort();
        return Descriptive.median(incomes);
    }*/
    
    /*public double getNeighborhoodMedianAsianIncome() { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        DoubleArrayList incomes  = new DoubleArrayList();
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            incomes.addAllOf(block.getAsianIncomes());
            
        }
        incomes.sort();
        return Descriptive.median(incomes);
    }*/
    
    /*public double getNeighborhoodMedianHispanicIncome() { // Priyasree_DeadCode : Unreachable code_
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        DoubleArrayList incomes  = new DoubleArrayList();
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            incomes.addAllOf(block.getHispanicIncomes());
            
        }
        incomes.sort();
        return Descriptive.median(incomes);
    }*/
	 
    public double getPctBlkInNeighborhood() { 
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumBlack();            
        }
        if(this.getTotalAgentsInNeighborhood()!=0) {
            return ( (totalAgents*1.0)/ (this.getTotalAgentsInNeighborhood()*1.0)) ;           
        } else {
        	return 0.0; 
        }
    }
    
    public double getPctAsnInNeighborhood() { 
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumAsian();          
        }
        if(this.getTotalAgentsInNeighborhood()!=0) {
            return ( (totalAgents*1.0)/ (this.getTotalAgentsInNeighborhood()*1.0)) ;           
        } else {
        	return 0.0; 
        }
    }
    
    public double getPctHispInNeighborhood() {
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumHispanic();          
        }
        if(this.getTotalAgentsInNeighborhood()!=0) {
            return ( (totalAgents*1.0)/ (this.getTotalAgentsInNeighborhood()*1.0)) ;           
        } else {
        	return 0.0; 
        }
    }
    
    
	
    public double getPctWhtInNeighborhood() {
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        int totalAgents = 0;
        Iterator iter = neighborhood.iterator();
        while(iter.hasNext()) {
            Block block = (Block)iter.next();
            totalAgents += block.getNumWhite(); 
        }
        if(this.getTotalAgentsInNeighborhood()!=0) {
            return ( (totalAgents*1.0)/ (this.getTotalAgentsInNeighborhood()*1.0)) ;           
        } else {
        	return 0.0; 
        }
    }
    
	
	/**
    * @return Returns the blockGroupNum.
    */
	public int getBlockGroupNum() { 
		return blockGroupNum;
	}
	/**
    * @param blockGroupNum
    *                The blockGroupNum to set.
    */
	/*public void setBlockGroupNum(int blockGroupNum) { // Priyasree_DeadCode : Unreachable code_
		this.blockGroupNum = blockGroupNum;
	}*/
	/**
    * @return Returns the censusTractNum.
    */
	public int getCensusTractNum() { 
		return censusTractNum;
	}
	/**
    * @param censusTractNum
    *                The censusTractNum to set.
    */
	/*public void setCensusTractNum(int censusTractNum) { // Priyasree_DeadCode : Unreachable code_
		this.censusTractNum = censusTractNum;
	}*/
	public ArrayList getHousingUnitList_Renters() { 
		return housingUnitList_Renters;
	}
    
    public ArrayList getHousingUnitList_Owners() {
        return housingUnitList_Owners;
    }

    /*public void setHousingUnitList_Owners(ArrayList housingUnitList_Owners) { // Priyasree_DeadCode : Unreachable code_
        this.housingUnitList_Owners = housingUnitList_Owners;
    }*/

    public int getNumHousingUnits() { 
        return numHousingUnits;
    }

    /*public void setNumHousingUnits(int numHousingUnits) { // Priyasree_DeadCode : Unreachable code_
        this.numHousingUnits = numHousingUnits;
    }*/

    public ArrayList getHousingUnitsByTenure(int tenure) { 
        if (tenure == Agent.RENTER) return housingUnitList_Renters;
        else if (tenure == Agent.OWNER) return housingUnitList_Owners;
        else { 
            System.out.println("Block -- getHousingUnitsByTenure tenure not 0 or 1");
            return null;
        }
        
    }
    
    /*public void setHousingUnitList_Renters(ArrayList housingUnitList) { // Priyasree_DeadCode : Unreachable code_
		this.housingUnitList_Renters = housingUnitList;
	}*/
	/*public BlockGroup getBlockGroup() { // Priyasree_DeadCode : Unreachable code_
		return blockGroup;
	}*/
	/*public void setBlockGroup(BlockGroup blockGroup) { // Priyasree_DeadCode : Unreachable code_
		this.blockGroup = blockGroup;
	}*/
	
	/**
	 * returns list of vacant housing units. 
	 * @param tenure 
	 * 
	 * @return
	 */
	public ArrayList getVacantHousingUnits(int tenure) { 
		ArrayList units = new ArrayList();
        
        if (tenure == Agent.RENTER) {
        
            for (int i = 0; i < housingUnitList_Renters.size(); i++) {
                
                if (((HousingUnit) housingUnitList_Renters.get(i)).isOccupied() == false && ((HousingUnit) housingUnitList_Renters.get(i)).getTenure() == tenure) { //Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
                    units.add(housingUnitList_Renters.get(i));
                }
            }
        }
        else if (tenure == Agent.OWNER) {
            
            for (int i = 0; i < housingUnitList_Owners.size(); i++) {
                
                if (((HousingUnit) housingUnitList_Owners.get(i)).isOccupied() == false && ((HousingUnit) housingUnitList_Owners.get(i)).getTenure() == tenure) { //Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
                    units.add(housingUnitList_Owners.get(i));
                }
            }
        }
        else 
		System.out.println("Block : get Vacant HU tenure not 0 or 1");
		return units;
	}
	
	public ArrayList getVacantHousingUnits() { 
		ArrayList units = new ArrayList();
       
            for (int i = 0; i < housingUnitList_All.size(); i++) {
                
                if (((HousingUnit) housingUnitList_All.get(i)).isOccupied() == false) { //Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
                    units.add(housingUnitList_All.get(i));
                }
            }
        
		return units;
	}


	public int getNumVacantHousingUnits() { 
        int num = 0;
        num += getVacantHousingUnits(Agent.RENTER).size();
        num += getVacantHousingUnits(Agent.OWNER).size();
        
	   return num;
	}

	public DoubleArrayList getIncomeDistribution() { // E.E.B. 
																		   // added
																		   // 2/26
		return this.incomeList;
	}
	
    public double getNeighborhoodMedianIncome() { 
        ArrayList neighborhood = new ArrayList();
        neighborhood.addAll(neighborList);
        neighborhood.add(this);
        DoubleArrayList incomes  = new DoubleArrayList();
        Iterator iter = neighborhood.iterator();
        
        while(iter.hasNext()) {         
            Block block = (Block)iter.next();
         if (block !=null) {
                incomes.addAllOf(block.getIncomes());
            }
        }
        incomes.sort();
        return Descriptive.median(incomes);
       

    }
	public String getSTFID() { 
		return stfid;
	}
	public void setSTFID(String stfid) { 
		this.stfid = stfid;
	}


	/*public String toString() { // Priyasree_DeadCode : Unreachable code_
		return this.stfid;

	}*/

	   public String historyToString_Renter() { 
           
           if (carryOverHistory_Renter == null) {               
               return createHistoryString_Renter(MirarUtils.STEP_NUM );
           }
           else {  // there is a carryOverHistory_Renter
              
               if (this.renterHistory.size() == 0) { // there are not histories in the list
                   renterHistory.add(carryOverHistory_Renter);
                   return createHistoryString_Renter(MirarUtils.PRINT_INTERVAL);
               }
               else {
                 
                   if (((BlockHistory)this.renterHistory.get(0)).getStepNum() == carryOverHistory_Renter.getStepNum()) {
                       return createHistoryString_Renter(MirarUtils.PRINT_INTERVAL);
                   }
                   else { 
                       // need to use the carry over renterHistory
                       this.renterHistory.add(0, carryOverHistory_Renter);
                       return createHistoryString_Renter(MirarUtils.PRINT_INTERVAL);
                   }
               }
           }
          
       }
       private String createHistoryString_Renter(int finalStepNum) { 
	        StringBuffer result = new StringBuffer();
	        int totalHistory = 0;
	        // add race-income type
	        //add renterHistory from step 0 - initialization
	        result.append(this.getSTFID() + ", " );
    		result.append(((BlockHistory)renterHistory.get(0)).toString());
    		result.append("\n"); //Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.

            this.carryOverHistory_Renter = new BlockHistory((BlockHistory)this.renterHistory.get(this.renterHistory.size()-1),MirarUtils.STEP_NUM+1 );
                        
            this.renterHistory.clear();
            
            return result.toString();
	    }
	    
	
  public String historyToString_Owner() { 
           
           if (carryOverHistory_Owner == null) {
               return createHistoryString_Owner(MirarUtils.STEP_NUM );
           }
           else {  // there is a carryOverHistory_Renter
              
               if (this.ownerHistory.size() == 0) { // there are not histories in the list
                   ownerHistory.add(carryOverHistory_Owner);
                   return createHistoryString_Owner(MirarUtils.PRINT_INTERVAL);
               }
               else {
                 
                   if (((BlockHistory)this.ownerHistory.get(0)).getStepNum() == carryOverHistory_Owner.getStepNum()) {
                       return createHistoryString_Owner(MirarUtils.PRINT_INTERVAL);
                   }
                   else { 
                       // need to use the carry over renterHistory
                       this.ownerHistory.add(0, carryOverHistory_Owner);
                       return createHistoryString_Owner(MirarUtils.PRINT_INTERVAL);
                   }
               }
           }
          
       }
       private String createHistoryString_Owner(int finalStepNum) { 
            StringBuffer result = new StringBuffer();
            int totalHistory = 0;
            // add race-income type
            //add renterHistory from step 0 - initialization
            result.append(this.getSTFID() + ", " );
            result.append(((BlockHistory)ownerHistory.get(0)).toString());
            result.append("\n"); //Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.
         
            // add remaining memory STFIDs - for 1 through last set of model
            // if missing any memories in between steps, have to insert them
            // if missing any memories from last memory to last step of model - have to add them
            int numHistories = ownerHistory.size();
            int currStep = 0;
            int prevStep = 0;//((BlockHistory)renterHistory.get(0)).getStepNum();
            int currHistory = 1;
            int prevHistory = 0;
            
            for (int i=1; i< numHistories; i++) {
                prevStep = ((BlockHistory)ownerHistory.get(i-1)).getStepNum();
                 currStep =  ((BlockHistory)ownerHistory.get(i)).getStepNum();
                if ( (currStep - prevStep) > 1) {
                    // there are missing steps in between this step and previous one
                    // add those missing onese
                    // then add the current one
                    int insertHistories = ((currStep - prevStep) - 1);
                    int stepToAdd = ((BlockHistory)ownerHistory.get(prevHistory)).getStepNum();
                    for (int j=0; j<insertHistories; j++) {
                        result.append(this.getSTFID() + ", ");
                        
                        result.append(new BlockHistory( (BlockHistory)ownerHistory.get(prevHistory), (stepToAdd + (j+1))) );
                        result.append("\n"); //Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.
                        totalHistory++;
                    }
                    result.append(this.getSTFID() + ", ");
                    result.append(((BlockHistory)ownerHistory.get(currHistory)));
                    result.append("\n"); //Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.
                    totalHistory++;
                    prevHistory = currHistory;
                    currHistory++;
                }
                else {
                    // don't need to insert any steps
                    result.append(this.getSTFID() + ", ");
                    result.append(((BlockHistory)ownerHistory.get(currHistory)));
                    result.append("\n"); //Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.
                    totalHistory++;
                    prevHistory = currHistory;
                    currHistory++;
                }
            }
        // check to make sure there are not histories needed to get to last step of model
            if ( (finalStepNum - totalHistory) > 0 ) {
                int lastHistory = (ownerHistory.size() - 1);
                int lastStep = ((BlockHistory)ownerHistory.get(lastHistory)).getStepNum();
                int incrementor = 1;
                for (int i=lastStep+1; i<=MirarUtils.STEP_NUM; i++) {
                    result.append(this.getSTFID() + ", ");
                    result.append(new BlockHistory( (BlockHistory)ownerHistory.get(lastHistory), (lastStep + incrementor)) );
                    result.append("\n"); //Priyasree_Audit: String literal can be replaced by a character literal_Replace the string literal with a character literal.
                     totalHistory++;
                     incrementor++;
                }
            }
            result.deleteCharAt(result.length()-1);
            
            this.carryOverHistory_Owner = new BlockHistory((BlockHistory)this.ownerHistory.get(this.ownerHistory.size()-1),MirarUtils.STEP_NUM+1 );
            this.ownerHistory.clear();
            
            return result.toString();
        }
        
    
    public double getTotalIncome() { 
        System.out.println("block# total income: " + this.totalIncome);
		return this.totalIncome;
	}
	/*public void setTotalIncome(double totalIncome) { // Priyasree_DeadCode : Unreachable code_
		this.totalIncome = totalIncome;
	}*/

	/*public int getNumOccupiedHousingUnits() { // Priyasree_DeadCode : Unreachable code_
	    int numOccupiedUnits = 0;
	   
	    Iterator huIter = null;
        
        // first get renter incomes
         huIter = housingUnitList_Renters.iterator();
      //  else huIter = housingUnitList_Owners.iterator();
        
        
        while (huIter.hasNext()) {
            HousingUnit hu =  (HousingUnit)huIter.next();
            if(hu.isOccupied() ){
                numOccupiedUnits++;
            }
        }
        
        // 2nd get owner incomes
        huIter = housingUnitList_Owners.iterator();
        while (huIter.hasNext()) {
            HousingUnit hu =  (HousingUnit)huIter.next();
            if(hu.isOccupied() ){
                numOccupiedUnits++;
            }
        }
        return numOccupiedUnits;
        
	}*/

	
	/**
    * @param probablityList
    *                The probablityList to set.
    */
	/*public double getExpectedNumAgents() { // Priyasree_DeadCode : Unreachable code_
		return expectedNumAgents;
	}*/
	/*public void setExpectedNumAgents(double expectedNumAgents) { // Priyasree_DeadCode : Unreachable code_
		this.expectedNumAgents = expectedNumAgents;
	}*/
	
	/*public int [] getRaceIncomeRent_Renters() { // Priyasree_DeadCode : Unreachable code_
	    return this.raceIncomeRent_Renters;
	}*/
	
	public void setRaceIncomeRent_Renters(int [] list) { 
	    if (raceIncomeRent_Renters == null) raceIncomeRent_Renters = new int[list.length];
	    for (int i=0; i<list.length; i++) {//Priyasree_Audit: Loop used to copy an array_Use the method arraycopy defined in java.lang.System instead.
	        raceIncomeRent_Renters[i] = list[i];
	    }
	}

    /*public int [] getRaceIncomeRent_Owners() { // Priyasree_DeadCode : Unreachable code_
	    return this.raceIncomeRent_Owners;
	}*/
	
	public void setRaceIncomeRent_Owners(int [] list) { 
	    if (raceIncomeRent_Owners == null) raceIncomeRent_Owners = new int[list.length];
	    for (int i=0; i<list.length; i++) {//Priyasree_Audit: Loop used to copy an array_Use the method arraycopy defined in java.lang.System instead.
	        raceIncomeRent_Owners[i] = list[i];
	    }
	}
	
	public DoubleArrayList getRentList(int tenure) { 
	    DoubleArrayList rents = new DoubleArrayList();
	    ArrayList housingUnits = new ArrayList();
	    housingUnits.addAll(this.getHousingUnitsByTenure(tenure));
            
	    int numHU = housingUnits.size();
	    for (int i=0; i<numHU; i++) {
	            rents.add(((HousingUnit)housingUnits.get(i)).getRent());
	    }
	    return rents;
	}
	
	public void setRentListByBlock(double rent, int tenure) { 
	    /**
	     * This method updates prices assuming utilities are computed
	     * at the block level. Thus, all the housing units in the block
	     * get the same market price.
	     */
	    ArrayList housingUnits = new ArrayList();
        
        if (tenure == Agent.RENTER) housingUnits.addAll(this.getHousingUnitList_Renters());
        else housingUnits.addAll(this.getHousingUnitList_Owners());
        
	    HousingUnit h = new HousingUnit();
	    int numHU = housingUnits.size();
	    for(int i=0;i<numHU;i++){
	        h = (HousingUnit) housingUnits.get(i);
            if (h.getTenure() == tenure) {
                h.setRent(rent);
            }
	    }
	}
	
    /*public DoubleArrayList getTransitionProbabilityList_Renters() { // Priyasree_DeadCode : Unreachable code_
        return transitionProbabilityList_Renters;
    }*/
    /*public void setTransitionProbabilityList_Renters(
            DoubleArrayList transitionProbabilityList) { // Priyasree_DeadCode : Unreachable code_
        this.transitionProbabilityList_Renters = transitionProbabilityList;
    }*/
    /*public DoubleArrayList getTransitionProbabilityList_Owners() { // Priyasree_DeadCode : Unreachable code_
        return transitionProbabilityList_Owners;
    }*/
    /*public void setTransitionProbabilityList_Owners(
            DoubleArrayList transitionProbabilityList) { // Priyasree_DeadCode : Unreachable code_
        this.transitionProbabilityList_Owners = transitionProbabilityList;
    }*/
    
    /*public DoubleArrayList getTransitionProbabilityList_All() { // Priyasree_DeadCode : Unreachable code_
        return transitionProbabilityList_All;
    }*/
    /*public void setTransitionProbabilityList_All(
            DoubleArrayList transitionProbabilityList) { // Priyasree_DeadCode : Unreachable code_
        this.transitionProbabilityList_All = transitionProbabilityList;
    }*/
} // end Block

