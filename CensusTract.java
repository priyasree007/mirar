
/** CensusTract
 *  has own CensusTract number
 * 	has a list with the BlockGroupNumbers of the Blocks in the group
 */
package mirar;

import java.util.*;


public class CensusTract {

    private int numWhite = 0;
    private int numBlack = 0; // Priyasree_DeadCode : Unreachable code_
    private int numAsian = 0;
    private int numHispanic = 0; // Priyasree_DeadCode : Unreachable code_
    private int numTotalPeople = 0;
    
    private double totalIncome; // Priyasree_DeadCode : Unreachable code_

    int censusTractNum; // Priyasree_DeadCode : Unreachable code_
    private ArrayList blockGroupList = new ArrayList();  // Priyasree_DeadCode : Unreachable code_

    public CensusTract(int tractNum) { 
        this.censusTractNum = tractNum;
        blockGroupList = new ArrayList();
   //     System.out.println("new census Tract: " + this.censusTractNum);
    }
     
    
    
    /**
     * @return the racial percentage of the majority race group + race num (see Agent for num for each race)
     */
    public void updateBlockGroupsInfo() { 
        
        
        numWhite = 0;
        numBlack = 0;
        numAsian = 0;
        numHispanic = 0;
        numTotalPeople = 0;
        totalIncome = 0;
//       System.out.println("CT update:");
        // go throught the blockList and add up the num of each race typ in each block
        int numBlockGroups = blockGroupList.size();
        for (int i=0; i<numBlockGroups; i++) {
            BlockGroup bg = (BlockGroup)blockGroupList.get(i);
       
            numWhite += bg.getNumWhite();
            numBlack += bg.getNumBlack();
            numAsian += bg.getNumAsian();
            numHispanic += bg.getNumHispanic();
            numTotalPeople += (bg.getNumWhite() + bg.getNumBlack() + bg.getNumAsian()+ bg.getNumHispanic());
            totalIncome += bg.getTotalIncome();
        }
        
    }
    
    
    public double getRacePct() { 
        // white == 0
        //black == 1
        // asian == 2
        // hispanic ==3
        double pct = 0.0;
        
       // System.out.println("BG getRacePct: total p: " + numTotalPeople );
        // white is highest
        if (numWhite > numBlack && numWhite > numAsian
                && numWhite > numHispanic) {
          /* pct =  getPctWhite();
           if (pct == 1.0) {
               return (pct - 0.001); 
           }
           else return pct;*/
            return ((numWhite*1.0)/(numTotalPeople*1.0));
        }
        // black is highest
        else if (numBlack > numWhite && numBlack > numAsian
                && numBlack > numHispanic) {
            
         /*   pct =  getPctBlack();
            if (pct == 1.0) {
                return (1 + (pct - 0.001)); 
            }
            else return (1 + pct);*/
           return (1 + ((numBlack*1.0)/(numTotalPeople*1.0)));
        }
        // asian is highest
        else if (numAsian > numWhite && numAsian > numBlack
                && numAsian > numHispanic) {
            
         //   pct =  getPctAsian();
           /* if (pct == 1.0) {
                return (2 + (pct - 0.001)); 
            }
            else return (2 + pct);*/
           return (2 + ((numAsian*1.0)/(numTotalPeople*1.0)));
        }
        //hispanic is highest
        else if (numHispanic > numWhite && numHispanic > numBlack
                && numHispanic > numAsian) {
           // pct =  getPctHispanic();
          /*  if (pct == 1.0) {
                return (3 + (pct - 0.001)); 
            }
            else return (3 + pct);*/
            return (3 + ((numHispanic*1.0)/(numTotalPeople*1.0)));
        }
        // all are equal
        else
            return -1.0;

    }
    
    public double getMedianIncome(){ 
        // if (totalIncome > 0.0) {
             return (totalIncome/(numTotalPeople*1.0));
        // }
        // else return ((totalIncomeCategory*1.0)/(numTotalPeople*1.0));
     }
    
    public void addBlockGroup(int groupNum) { 
        BlockGroup bg = getBlockGroup(groupNum);
        if (bg == null) {
            blockGroupList.add(new BlockGroup(groupNum, this));
        }
    }
    
    /*public void addBlock(int blockGroupNum, int blockNum) { // Priyasree_DeadCode : Unreachable code_
        ((BlockGroup)getBlockGroup(blockGroupNum)).addBlock(blockNum); // Priyasree_Audit: Unnecessary type cast to BlockGroup_Delete the unnecessary cast.  
    }*/
    
    public void addBlock(int blockGroupNum, int blockNum, String stfid) { 
        ((BlockGroup)getBlockGroup(blockGroupNum)).addBlock(blockNum, stfid); // Priyasree_Audit: Unnecessary type cast to BlockGroup_Delete the unnecessary cast.
    }
    
    /*public Block getBlock(int blockGroupNum, int blockNum) { // Priyasree_DeadCode : Unreachable code_
        return  ((BlockGroup)getBlockGroup(blockGroupNum)).getBlock(blockNum); // Priyasree_Audit: Unnecessary type cast to BlockGroup_Delete the unnecessary cast.
    }*/
    
    public boolean hasBlockGroup(int blockGroupNum) { 
        for (int i=0; i<blockGroupList.size(); i++) {
            if ( ((BlockGroup)blockGroupList.get(i)).getBlockGroupNum() == blockGroupNum ) {
                return true;
            }
        }
        return false;
    }
    
    public BlockGroup getBlockGroup(int num) { 
        try {
        for (int i=0; i<blockGroupList.size(); i++) {
            if ( ((BlockGroup)blockGroupList.get(i)).getBlockGroupNum() == num) {
                return (BlockGroup)blockGroupList.get(i);
            }
        }
       
        } catch (NullPointerException npe) {//Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
            System.out.println(" CensusTract#getBlockGroup  blockgroupList is null");
        }
        return null;
    }
    
    
    public int getCensusTractNum() { 
        return censusTractNum;
    }

    public ArrayList getAllBlocks() { 
        ArrayList blocks = new ArrayList();
        for (int i=0; i<blockGroupList.size(); i++) {
            blocks.addAll( ((BlockGroup)blockGroupList.get(i)).getBlockList() );
        }
        return blocks;
    }
    
    public ArrayList getBlockGroupList() { 
        return blockGroupList;
    }
    /*public void setBlockGroupList(ArrayList blockGroupList) { // Priyasree_DeadCode : Unreachable code_
        this.blockGroupList = blockGroupList;
    }*/

    /*public void setCensusTractNum(int censusTractNum) { // Priyasree_DeadCode : Unreachable code_
        this.censusTractNum = censusTractNum;
    }*/
    public int getNumAsian() { 
        return numAsian;
    }
    /*public void setNumAsian(int numAsian) { // Priyasree_DeadCode : Unreachable code_
        this.numAsian = numAsian;
    }*/
    public int getNumBlack() { 
        return numBlack;
    }
    /*public void setNumBlack(int numBlack) { // Priyasree_DeadCode : Unreachable code_
        this.numBlack = numBlack;
    }*/
    public int getNumHispanic() { 
        return numHispanic;
    }
    /*public void setNumHispanic(int numHispanic) { // Priyasree_DeadCode : Unreachable code_
        this.numHispanic = numHispanic;
    }*/
    /*public int getNumTotalPeople() { // Priyasree_DeadCode : Unreachable code_
        return numTotalPeople;
    }*/
    /*public void setNumTotalPeople(int numTotalPeople) { // Priyasree_DeadCode : Unreachable code_
        this.numTotalPeople = numTotalPeople;
    }*/
    public int getNumWhite() { 
        return numWhite;
    }
    /*public void setNumWhite(int numWhite) { // Priyasree_DeadCode : Unreachable code_
        this.numWhite = numWhite;
    }*/
    /*public double getTotalIncome() { // Priyasree_DeadCode : Unreachable code_
        return totalIncome;
    }*/
    /*public void setTotalIncome(double totalIncome) { // Priyasree_DeadCode : Unreachable code_
        this.totalIncome = totalIncome;
    }*/
 } // end CensusTract







