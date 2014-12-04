/*
 * Created on Jan 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mirar;

import java.util.ArrayList;
import java.util.Iterator;

import cern.colt.list.DoubleArrayList;

public class SchellingDecision extends AgentDecision {

	public SchellingDecision() { // Priyasree_DeadCode : Unreachable code_
	}

	//public SchellingDecision(Agent agent) {
//		this.agent = agent;
//	}

	public HousingUnit select(ArrayList possibleHousingUnitList,
			HousingUnit currUnit, Agent agent) { // Priyasree_DeadCode : Unreachable code_
		System.out.println("possibleHousingUnitList size is " + possibleHousingUnitList.size());
		possibleHousingUnitList.addAll(possibleHousingUnitList);
			ArrayList availableUnits = agent.getPossibleHousingUnitList();
			if (currUnit == null) {
	            System.out.println("Agetndecision - select: could not get currUnit " + agent.getSTFID() + " " + agent.getHousingUnitNum());
	        }
			System.out.println("-------   AgentDecision select currBlock: " + currUnit.getBlock().getSTFID());
			availableUnits.add(currUnit);
			DoubleArrayList utilities = getUtilitiesByHousingUnit(availableUnits, agent);
			    //new DoubleArrayList();   
			
			System.out.println("AgentDecision select availableUnits " + availableUnits.size() + "  utilities: " + utilities.size());
			//return getChosenHousingUnitFromUtilities(utilities, availableBlocks); //choose from available blocks
			int choice = MirarUtils.sampleFromRawDistribution(utilities);
			if (choice == -1) {
			    possibleHousingUnitList.clear();
			   System.out.println("agent decision randi neighbors: choice is null , choice == " + choice);
			    return null;
			}
			else {
			    possibleHousingUnitList.clear();
			   System.out.println("+++++++   AgentDecision select curBlock: " + ((HousingUnit) availableUnits.get(choice)).getBlock().getSTFID());
			    return (HousingUnit) availableUnits.get(choice);
			}
		}


	public DoubleArrayList computeUtilities(ArrayList housingUnits, Agent agent) { // Priyasree_DeadCode : Unreachable code_
	    DoubleArrayList utilities = new DoubleArrayList();
		ArrayList availableUnits = new ArrayList();
        availableUnits.addAll(housingUnits);
	    Iterator e = availableUnits.iterator();

			int i=0;
			while (e.hasNext()) {
			    //System.out.println("agent decsion: select : i: " + i + "num hu: " + availableBlocks.size());
			   // Block b = (Block)((HousingUnit)e.next()).getBlock();
			    HousingUnit hu = (HousingUnit) e.next();
//			    Block b = (Block) e.next();
			    Block b =hu.getBlock();
			    utilities.add(computeUtility(hu.getBlock(), agent, agent.getTenure()));
//					Block b = (Block)((HousingUnit) availableBlocks.get(i)).getBlock();
			   
			    i++;
			}
			System.out.println("utilities size is " + utilities.size());
			   return utilities;
	}
	
	public double computeUtility(Block b, Agent agent, int tenure) { // Priyasree_DeadCode : Unreachable code_
		double utility=0.0;
		ArrayList blocks = new ArrayList();
		//System.out.println("block pct white " + this.getPctWhiteInNeighborhood(b));
		//System.out.println("block pct blk " + this.getPctBlackInNeighborhood(b));
		//System.out.println("block pct hisp " + this.getPctHispanicInNeighborhood(b));
		//System.out.println("block pct asn " + this.getPctAsianInNeighborhood(b));
		
		
		if (b == null) {
		System.out.println("raceincome neighbor computeUtility Block is null");
		}
		else {
			if(agent.getRace()==Agent.WHITE){
				if(this.getPctWhiteInNeighborhood(b)>=0.50) {
	 			utility=Math.exp(1); 
			} else if(this.getPctWhiteInNeighborhood(b)<0.50){
				utility = Math.exp(0);
			}
			}
			if(agent.getRace()==Agent.BLACK){
				if(this.getPctBlackInNeighborhood(b)>=0.50) {
	 			utility=Math.exp(1); 
			} else if(this.getPctBlackInNeighborhood(b)<0.50){
				utility = Math.exp(0);
			}
		}
		if(agent.getRace()==agent.HISPANIC){
			if(this.getPctHispanicInNeighborhood(b)>=0.50) {
 			utility=Math.exp(1); 
		} else if(this.getPctHispanicInNeighborhood(b)<0.50){
			utility = Math.exp(0);
			}
	}
		if(agent.getRace()==agent.ASIAN){
			if(this.getPctAsianInNeighborhood(b)>=0.50)
 			utility=Math.exp(1); 
		} else if(this.getPctAsianInNeighborhood(b)<0.50){
			utility = Math.exp(0);
		}
		}
	  return utility;
	}

	/* (non-Javadoc)
	 * @see edu.ucla.stat.mirar.AgentDecision#computeMarginalUtility(edu.ucla.stat.mirar.Block)
	 */
	public double computeMarginalUtility(Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.ucla.stat.mirar.AgentDecision#solveForPrice(double, edu.ucla.stat.mirar.Block)
	 */
	public double solveForPrice(double marginalUtil, Block b, int tenure) { // Priyasree_DeadCode : Unreachable code_
		// TODO Auto-generated method stub
		return 0;
	}

}
