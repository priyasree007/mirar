
/** CensusUnitHandler
 */
package mirar;


import java.util.*;

/**
 * Hanldes Census Units</p>
 * <p>Able to directly access blocks (Block2000) Block Groups and Census Tracts (TRACT2000).&nbsp; So there is no need to go through the geographic hierarchy.&nbsp; </p>
 * <p></p>
 * <p>To get blockGroups associated with a CensusTract: ((CensusTract)getCensusTract).getBlockGroups()</p>
 * <p>same idea for BlockGroups getting Blocks and Blocks getting HousingUnitss</p>
 * <p></p>
 * <p>
 */
public class CensusUnitHandler {

  /**
   * proportion of STF data to use to create agents. 
   */
    
    private ArrayList censusTractList = new ArrayList(); 
    private ArrayList blockGroupList = new ArrayList(); 
    private ArrayList blockList = null; 

    private static CensusUnitHandler instance = new CensusUnitHandler();

    private CensusUnitHandler() {
    	super();
    }

    public static CensusUnitHandler getInstance() {
      return instance;
    }

    
    public void prepareCensusUnits(ArrayList stfidList) {
        if (censusTractList == null) censusTractList = new ArrayList();
        for (int i=0; i<stfidList.size(); i++) {
            int censusTractNum = Integer.parseInt(((String)stfidList.get(i)).substring(5, 11));
            CensusTract ct = this.getCensusTract(censusTractNum);
            
            if ( ct == null) {
                ct = new CensusTract(censusTractNum);
                censusTractList.add(ct);
            }
            int blockGroupNum = Integer.parseInt(((String)stfidList.get(i)).substring(11, 12));
            ct.addBlockGroup(blockGroupNum);
            ct.addBlock(blockGroupNum,Integer.parseInt(((String)stfidList.get(i)).substring(11)), ((String)stfidList.get(i)).substring(5));
            
            
        }
    }
    
    public ArrayList getCensusTractList() {        
        return censusTractList;
    } // end getCensusTracts        


    public void setCensusTracts(ArrayList CensusTracts) {        
    	censusTractList = CensusTracts;
    } // end setCensusTracts        


  public ArrayList getBlockGroupList() {  
        return blockGroupList;
    } // end getBlockGroups        

    public void setBlockGroups(ArrayList blockGroups) {        
        blockGroupList = blockGroups;
    } // end setBlockGroups        

    
    
    public ArrayList getAllBlockGroups() {
        ArrayList blockGroups = new ArrayList();
        for (int i=0; i<censusTractList.size(); i++) {
            blockGroups.addAll( ((CensusTract)censusTractList.get(i)).getBlockGroupList() );
        }
        return blockGroups;
    }
    
    public ArrayList getAllBlocks() {
        ArrayList blocks = new ArrayList();
        for (int i=0; i<censusTractList.size(); i++) {
            blocks.addAll( ((CensusTract)censusTractList.get(i)).getAllBlocks() );
        }
    
    return blocks;
        
    }
    
    public ArrayList getBlocks(ArrayList stfidList) {
        int listSize = stfidList.size();
        ArrayList blocks = new ArrayList(listSize);
        
        for (int i=0; i<listSize; i++) {
            blocks.add(getBlock((String)stfidList.get(i)));
        }
        return blocks;
    }

    
    public ArrayList getAllAgents() {
        ArrayList housingUnitList = new ArrayList();
        ArrayList agentList = new ArrayList();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext()) {
            housingUnitList.addAll( ((Block)blockIter.next()).getHousingUnitList_Renters() );
        }
        for (int i=0; i<housingUnitList.size(); i++) {
            if ( ((HousingUnit)housingUnitList.get(i)).isOccupied() == true ) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                agentList.add( ((HousingUnit)housingUnitList.get(i)).getAgent() );
            }
        }
        
        return agentList;
    }
    
    public ArrayList sampleVacantHousingUnits(int tenure) {
        ArrayList housingUnitList = new ArrayList();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext()) {
            housingUnitList.addAll( ((Block)blockIter.next() ).sampleVacantHousingUnits(tenure) );
        }
        return housingUnitList;
    }
    
    public ArrayList sampleVacantHousingUnits() {
        ArrayList housingUnitList = new ArrayList();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext()) {
            housingUnitList.addAll( ((Block)blockIter.next() ).sampleVacantHousingUnits() );
        }
        return housingUnitList;
    }
    
    public ArrayList getAllVacantHousingUnits() {
        
        ArrayList housingUnitList = new ArrayList();
        ArrayList vacantHousingUnitList = new ArrayList();
        housingUnitList.addAll(getAllHousingUnits());
        
        Iterator iter = housingUnitList.iterator();
        // add only vacant units 
        while (iter.hasNext()) {
            HousingUnit hu = (HousingUnit)iter.next();
            if ( hu.isOccupied() == false ) { // Priyasree_Audit: Equality test with boolean literal: false_ Remove the comparison with false.
                vacantHousingUnitList.add(hu);
            }
        }
       
        return vacantHousingUnitList;
    }
    
    public ArrayList getAllOccupiedHousingUnits() {
        
        ArrayList housingUnitList = new ArrayList();
        ArrayList occupiedHousingUnitList = new ArrayList();
        housingUnitList.addAll(getAllHousingUnits());
        
        Iterator iter = housingUnitList.iterator();
        // add only vacant units 
        while (iter.hasNext()) {
            HousingUnit hu = (HousingUnit)iter.next();
            if ( hu.isOccupied() == true ) { // Priyasree_Audit: Equality test with boolean literal: true_ Remove the comparison with true.
                occupiedHousingUnitList.add(hu);
            }
        }
        return occupiedHousingUnitList;
    }
    
    public ArrayList getAllHousingUnits() {
        ArrayList blocks = this.getAllBlocks();
        ArrayList housingUnitList = new ArrayList();
        
       for (int i=0; i<blocks.size(); i++) {
        housingUnitList.addAll( ((Block)blocks.get(i)).getHousingUnitList_Renters() );
        housingUnitList.addAll( ((Block)blocks.get(i)).getHousingUnitList_Owners() );
        }
        return housingUnitList;
    }
    
    public void setBlocks(ArrayList list) {        
        this.blockList = list;
    } // end setBlocks        

    
    public void addBlock(String stfid) {
        if (blockList == null) {
            this.blockList = new ArrayList();
        }
        else // check to make sure this block does not exist
        {
            for (int i=0; i<blockList.size(); i++) {
                if (stfid.equals(((Block)blockList.get(i)).getSTFID())) {
                    return;
                    
                }
            }
        }
        
        blockList.add(new Block(stfid, this));
    }
    
    public void addBlock(Block block) {
        if (blockList == null) {
            this.blockList = new ArrayList();
        }
        else // check to make sure this block does not exist
        {
            for (int i=0; i<blockList.size(); i++) {
                if (block.getSTFID().equals(((Block)blockList.get(i)).getSTFID())) {
                    return;
                    
                }
            }
        }
        
        blockList.add(block);
    }
    
    public void addBlockToBlockGroup(Block block) {
        BlockGroup bg = getBlockGroup(block.getCensusTractNum(), block.getBlockGroupNum());
        if (bg == null) {
            bg = new BlockGroup(block.getCensusTractNum(), block.getBlockGroupNum());
            addBlockGroup(block.getCensusTractNum(), block.getBlockGroupNum());
        }
        bg.addBlock(block.getBlockNum());
    }
    
    public void addBlockGroup(int tractNum, int groupNum) {
        if (blockGroupList == null) {
            this.blockGroupList = new ArrayList();
        }
        blockGroupList.add(new BlockGroup(tractNum, groupNum));
    }
    
    public void addBlockGroup(BlockGroup bg) {
        if (blockGroupList == null) {
            this.blockGroupList = new ArrayList();
        }
        blockGroupList.add(bg);
    }
    
    public void addCensusTract(CensusTract ct) {
        this.censusTractList.add(ct);
    }
/**
 * returns the requested block (BLOCK2000) in the form a a Block object
 * 
 * 
 * @return Block
 * @param blockNum block number requested corresponds to BLOCK2000 in shp file and features
 */
    public Block getBlock(int blockNum) {       
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        Block b = null;
        while (blockIter.hasNext()) {   
            b = (Block) blockIter.next();
            if ( b.getBlockNum() == blockNum ) {
                return b;
            }
        }
        return null;
    } // end getBlock  
    
    public boolean hasCensusTract(int censusTractNum) {
        for (int i=0; i<censusTractList.size(); i++) {
            if ( ((CensusTract)censusTractList.get(i)).getCensusTractNum() == censusTractNum ) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasBlock(int censusTractNum, int blockGroupNum, int blockNum) {
        if ( getBlock(censusTractNum, blockGroupNum, blockNum) == null) {
            return false;
        }
        return true;
    }
    
    public boolean hasBlock(int censusTractNum,  int blockNum) {
        if ( getBlock(censusTractNum, blockNum) == null) {
            return false;
        }
        return true;
    }
    
    public boolean hasBlock(String stfid) {
        if ( getBlock(stfid) == null) {
            return false;
        }
        return true;
    }
    
    public Block getBlock(int censusTractNum, int blockGroupNum, int blockNum) {
        if (this.hasCensusTract(censusTractNum) ) {
            CensusTract ct = (CensusTract)this.getCensusTract(censusTractNum); //Priyasree_Audit: Unnecessary type cast to CensusTract_Delete the unnecessary cast.
            if (ct.hasBlockGroup(blockGroupNum) ) {
                BlockGroup bg = (BlockGroup)ct.getBlockGroup(blockGroupNum); //Priyasree_Audit: Unnecessary type cast to BlockGroup_Delete the unnecessary cast.
                if (bg.hasBlock(blockNum) ) {
                     return bg.getBlock(blockNum);
                }
            }
        }
        return null;
    }
    
    public Block getBlock(int censusTractNum, int blockNum) {
        String bgString = Integer.toString(blockNum);
        int blockGroupNum = Integer.parseInt(bgString.substring(0,1));
        Block b =  this.getBlock(censusTractNum, blockGroupNum, blockNum);
        return b;
    }
    
    public Block getBlock(String stfid) {
        return this.getBlock(Integer.parseInt(stfid.substring(0,6)), Integer.parseInt(stfid.substring(6,7)), Integer.parseInt(stfid.substring(6)) );
    }
    
    
    public HousingUnit getHousingUnit(String stfid, int housingUnitNum, int tenure) {
        Block b = this.getBlock(stfid);
        return b.getHousingUnit(housingUnitNum, tenure);
    }

    
    public BlockGroup getBlockGroup(String tractString, String groupString) {
        try {
            return getBlockGroup(Integer.getInteger(tractString).intValue(), Integer.getInteger(groupString).intValue());
        } catch (NullPointerException e) { // Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
            return null;
        }
    }
/**
 * 
 * 
 * @return BlockGroup
 * @param groupNum 
 */
    public BlockGroup getBlockGroup(int tractNum, int groupNum) { 
        
        CensusTract ct = this.getCensusTract(tractNum);
        if (ct == null) return null;
        
        BlockGroup bg = ct.getBlockGroup(groupNum);
        
        if (bg != null) 
            return bg; 
        else return null;
    } // end getBlockGroup        

    public CensusTract getCensusTract(String tractString){ 
        try {
            return getCensusTract(Integer.getInteger(tractString).intValue());
        }catch (NullPointerException e) { // Priyasree_Audit: Caught exception not logged_Use one of the logging methods to log the exception.
            return null;
        }
    }
    
/**
 * 
 * 
 * @return CensusTract
 * @param tractNum 
 */
    public CensusTract getCensusTract(int tractNum) {           
            //      search censusTractLIst for correct tractNum
            for (int i=0; i<censusTractList.size(); i++) {
                if ( ((CensusTract)censusTractList.get(i)).getCensusTractNum() == tractNum ) {
                    return (CensusTract)censusTractList.get(i);
                }
            }
        return null;
    } // end getCensusTract        


    public void setBlockGroupList(ArrayList blockGroupList) {
        this.blockGroupList = blockGroupList;
    }
    public void setBlockList(ArrayList blockList) {
        this.blockList = blockList;
    }
    public void setCensusTractList(ArrayList censusTractList) {
        this.censusTractList = censusTractList;
    }
    
    public String blockHistoriesToString_Renters() {
        StringBuffer s = new StringBuffer();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext()) {
            s.append( ((Block)blockIter.next()).historyToString_Renter());
        }
        return s.toString();
     }
    
    public String blockHistoriesToString_Owners() {
        StringBuffer s = new StringBuffer();
        Iterator blockIter = MirarUtils.BLOCKS.iterator();
        while (blockIter.hasNext()) {
            s.append( ((Block)blockIter.next()).historyToString_Owner());
        }
        return s.toString();
     }
    public ArrayList getBlockList() {
        return blockList;
    }
 } // end CensusUnitHandler



