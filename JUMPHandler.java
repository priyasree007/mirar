/*
 * Created on Feb 10, 2004
 *
 *
 */
package mirar;

import java.util.*;

import javax.swing.*; 

import java.awt.*;
import java.awt.event.WindowAdapter; // Priyasree_Audit: Unnecessary import: import java.awt.event.WindowAdapter;_Delete the import.
import java.awt.event.WindowEvent; // Priyasree_Audit: Unnecessary import: import java.awt.event.WindowEvent;_Delete the import.

import javax.swing.*; // Priyasree_Audit:  Duplicate import: import javax.swing.*_Delete the duplicate import.

/*import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureType;
*/
import java.io.*;
import java.net.URL; // Priyasree_Audit:  Unnecessary import: import java.net.URL;
import java.nio.channels.FileChannel;

import com.vividsolutions.jump.io.datasource.*; // Priyasree_Audit:  Unnecessary import: import com.vividsolutions.jump.io.datasource.*;_Unnecessary import: null
import com.vividsolutions.jump.io.*;
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.workbench.*;
import com.vividsolutions.jump.task.*;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.*;
import com.vividsolutions.jump.workbench.ui.plugin.*; // Priyasree_Audit:  Unnecessary import: import com.vividsolutions.jump.workbench.ui.plugin.*;_Unnecessary import: null
import com.vividsolutions.jump.workbench.model.*;
import com.vividsolutions.jump.workbench.ui.plugin.test.*; // Priyasree_Audit: Unnecessary import: import com.vividsolutions.jump.workbench.ui.plugin.test.*;_Unnecessary import: null
import com.vividsolutions.jump.workbench.plugin.*; // Priyasree_Audit: Unnecessary import: import com.vividsolutions.jump.workbench.plugin.*;_Unnecessary import: null
import com.vividsolutions.jump.workbench.ui.renderer.style.*;
import com.vividsolutions.jump.util.Range.RangeTreeMap;
import com.vividsolutions.jump.util.*;

import mirar.dbf.DBFReader;
import mirar.dbf.JDBField;

/**
 * @author rnajlis
 * 
 *  
 */
public class JUMPHandler {

    JUMPWorkbench wb; // Priyasree_DeadCode : Unreachable code_JUMPWorkbench wb
    WorkbenchFrame frame; // Priyasree_DeadCode : Unreachable code_
    WorkbenchContext workbenchContext; // Priyasree_DeadCode : Unreachable code_

    //ArrayList featureList;
    int stepNum; // Priyasree_DeadCode : Unreachable code_int stepNum 
    Layer blockLayer; // Priyasree_DeadCode : Unreachable code_
    Layer blockGroupLayer; // Priyasree_DeadCode : Unreachable code_Layer blockGroupLayer
    Layer censusTractLayer; // Priyasree_DeadCode : Unreachable code_Layer censusTractLayer
    ArrayList blockFeatureList; // Priyasree_DeadCode : Unreachable code_
    FeatureSchema blockSchema; // Priyasree_DeadCode : Unreachable code_
    IndexedFeatureCollection blockShpData; // Priyasree_DeadCode : Unreachable code_
    FeatureDataset blockData; // Priyasree_DeadCode : Unreachable code_FeatureDataset blockData
    FeatureDataset blockGroupData; // Priyasree_DeadCode : Unreachable code_FeatureDataset blockGroupData
    FeatureDataset censusTractData;  // Priyasree_DeadCode : Unreachable code_FeatureDataset censusTractData
    IndexedFeatureCollection blockGroupShpData;  // Priyasree_DeadCode : Unreachable code_IndexedFeatureCollection blockGroupShpData
    IndexedFeatureCollection censusTractShpData; // Priyasree_DeadCode : Unreachable code_censusTractShpData

    ArrayList stfidList; // Priyasree_DeadCode : Unreachable code_ArrayList stfidList
    
    private static JUMPHandler instance = new JUMPHandler(); // Priyasree_DeadCode : Unreachable code_JUMPHandler instance 

    private JUMPHandler() { // Priyasree_DeadCode : Unreachable code_
    	super();
    }

    public static JUMPHandler getInstance() { // Priyasree_DeadCode : Unreachable code_JUMPHandler getInstance()
      return instance;
    }

/*    public JUMPHandler(FeatureDataset blockData, 
            IndexedFeatureCollection blockGroupShpData,
            IndexedFeatureCollection censusTractShpData) {
        this.blockData = blockData;
        this.blockGroupShpData = blockGroupShpData;
        this.censusTractShpData = censusTractShpData;
    }

    public JUMPHandler(FeatureDataset blockData,
            FeatureDataset blockGroupData,
            FeatureDataset censusTractData ) {
            //IndexedFeatureCollection blockGroupShpData,
            //IndexedFeatureCollection censusTractShpData) {
        this.blockData = blockData;
        this.blockGroupData = blockGroupData;
        this.censusTractData = censusTractData;
    }
*/
    /*
     * public void updateDisplay() { updateBlockLayer(ArrayList blockList);
     * //updateBlockGroupLayer(); //updateCensusTractLayer();
     */

    
    public void setup() { // Priyasree_DeadCode : Unreachable code_void setup()
     //loadShapefiles( blockShpFile,  blockGroupShpFile,censusTractShpFile);
     //prepareData();
     setupJUMPWorkbench();
    }
    
   // public void loadData
    
    /**
     *  for now this only deals with the block data, we'll see about the blockGroup and
     * 		censusTract later
     *  add racePct field top schema
     *  add racePct data to features (from blockList.getRacePct())
     * 
     *  call before buildDisplay
     * 
     *  @precondition: shp data read in, blocks created and populated with agents
     *  @postcondition: racePct field and data added to block schema
     */
    public void prepareData() { // Priyasree_DeadCode : Unreachable code_void prepareData()
        try {
            
            // for blocks
            
            // add feature and value for it to feature list - this will be used
            // for the range value
            // add feature -- racePct
            FeatureSchema schema = blockShpData.getFeatureSchema();
            int blockShpDataNumAttributes = schema.getAttributeCount();
            schema.addAttribute("racePct", AttributeType.DOUBLE);
            //if (MirarUtils.test == true) {
                schema.addAttribute("numWhite", AttributeType.INTEGER);
                schema.addAttribute("numBlack", AttributeType.INTEGER);
                schema.addAttribute("numAsian", AttributeType.INTEGER);
                schema.addAttribute("numHispanic", AttributeType.INTEGER);
                schema.addAttribute("medianIncome", AttributeType.DOUBLE);
            //}
            int blockDataNumAttributes = schema.getAttributeCount();

            // create new features with new schema
            // copy old data to new feature
            // add racePct data
            ArrayList newFeatures = new ArrayList();
            stfidList = new ArrayList();
            int k = 0;
            for (Iterator i = blockShpData.iterator(); i.hasNext();) {
                k++;

                BasicFeature f1 = (BasicFeature) i.next();
                BasicFeature f11 = new BasicFeature(schema);
                for (int m = 0; m < blockShpDataNumAttributes; m++) {
                    f11.setAttribute(m, f1.getAttribute(m));

                }
                newFeatures.add(f11);
                // create block based on block number
                //censusUnitHandler.addBlock(Integer.parseInt((String)f11.getAttribute("BLOCK2000")));
               // censusUnitHandler.addBlock((String) f11.getAttribute("STFID"));
                stfidList.add((String) f11.getAttribute("STFID"));
                
                
              //  System.out.println((String) f11.getAttribute("STFID"));
            }
      
            blockData = new FeatureDataset(newFeatures, schema);
            
            
            // for blockGroups
            
            // add feature and value for it to feature list - this will be used
            // for the range value
            // add feature -- racePct
            FeatureSchema schemaBG = blockGroupShpData.getFeatureSchema();
            int blockGroupShpDataNumAttributes = schemaBG.getAttributeCount();
            schemaBG.addAttribute("racePct", AttributeType.DOUBLE);
            //if (MirarUtils.test == true) {
                schemaBG.addAttribute("numWhite", AttributeType.INTEGER);
                schemaBG.addAttribute("numBlack", AttributeType.INTEGER);
                schemaBG.addAttribute("numAsian", AttributeType.INTEGER);
                schemaBG.addAttribute("numHispanic", AttributeType.INTEGER);
                schemaBG.addAttribute("medianIncome", AttributeType.DOUBLE);
            //}
            int blockGroupDataNumAttributes = schemaBG.getAttributeCount();

            // create new features with new schema
            // copy old data to new feature
            // add racePct data
            ArrayList newBGFeatures = new ArrayList();
           // stfidList = new ArrayList();
            //int m = 0;
            for (Iterator i = blockGroupShpData.iterator(); i.hasNext();) {
                //m++;

                BasicFeature f1 = (BasicFeature) i.next();
                BasicFeature f11 = new BasicFeature(schemaBG);
                for (int m = 0; m < blockGroupShpDataNumAttributes; m++) {
                    f11.setAttribute(m, f1.getAttribute(m));

                }
                newBGFeatures.add(f11);
                // create block based on block number
                //censusUnitHandler.addBlock(Integer.parseInt((String)f11.getAttribute("BLOCK2000")));
               // censusUnitHandler.addBlock((String) f11.getAttribute("STFID"));
                //stfidList.add((String) f11.getAttribute("STFID"));
            }
      
            blockGroupData = new FeatureDataset(newBGFeatures, schemaBG);
            
            
            
            // for census tracts
            

            
            // add feature and value for it to feature list - this will be used
            // for the range value
            // add feature -- racePct
            FeatureSchema schemaCT = censusTractShpData.getFeatureSchema();
            int censusTractShpDataNumAttributes = schemaCT.getAttributeCount();
            schemaCT.addAttribute("racePct", AttributeType.DOUBLE);
            //if (MirarUtils.test == true) {
                schemaCT.addAttribute("numWhite", AttributeType.INTEGER);
                schemaCT.addAttribute("numBlack", AttributeType.INTEGER);
                schemaCT.addAttribute("numAsian", AttributeType.INTEGER);
                schemaCT.addAttribute("numHispanic", AttributeType.INTEGER);
                schemaCT.addAttribute("medianIncome", AttributeType.DOUBLE);
            //}
            int censusTractDataNumAttributes = schemaCT.getAttributeCount();

            // create new features with new schema
            // copy old data to new feature
            // add racePct data
            ArrayList newCTFeatures = new ArrayList();
           // stfidList = new ArrayList();
            //int m = 0;
            for (Iterator i = censusTractShpData.iterator(); i.hasNext();) {
                //m++;

                BasicFeature f1 = (BasicFeature) i.next();
                BasicFeature f11 = new BasicFeature(schemaCT);
                for (int m = 0; m < censusTractShpDataNumAttributes; m++) {
                    f11.setAttribute(m, f1.getAttribute(m));

                }
                newCTFeatures.add(f11);
                // create block based on block number
                //censusUnitHandler.addBlock(Integer.parseInt((String)f11.getAttribute("BLOCK2000")));
               // censusUnitHandler.addBlock((String) f11.getAttribute("STFID"));
                //stfidList.add((String) f11.getAttribute("STFID"));
            }
      
            censusTractData = new FeatureDataset(newCTFeatures, schemaCT);
            
            
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            //    System.out.println("could not read file");
        }
    }
    
    /**
     *  update block layer with the getRacePct() from each Block in the
     *  BlockList
     *  @precondition blocks are create, map data read in
     */
    public void updateBlockLayer() { // Priyasree_DeadCode : Unreachable code_void updateBlockLayer()
       // ArrayList blockList = new ArrayList();
       // blockList.addAll(blocks);
        // featureList = new ArrayList(fc1.getFeatures());

        // go through and change the block value for all of tract2000 = 104104
        //  fc1

        //  FeatureSchema schema = ((Feature)featureList.get(0)).getSchema();
        //   System.out.println("schema:num attributes: " +
        // schema.getAttributeCount());

        /*
         * for (int i=0; i <fc1.size(); i++) { if
         * (((Feature)featureList.get(i)).getString(3).equals("104104") ) {
         * ((Feature)featureList.get(i)).setAttribute(4, "5023");
         */
        Block b = null;
        int j=0;
        for (Iterator i = blockData.iterator(); i.hasNext(); ) {
            Feature f = (Feature)i.next();
           
            String fSTFID = f.getString(3) + f.getString(4);
            b = (Block)CensusUnitHandler.getInstance().getBlock(fSTFID); // Priyasree_Audit: Unnecessary type cast to Block_Delete the unnecessary cast.
            if (b != null) {
                //if ( ! (  b.getSTFID().equalsIgnoreCase(fSTFID)) ) {
                //     System.out.println("\t\t &&&&&&&&&   Block   " +b.getSTFID() + " does not equal feature stfid : " + f.getString(3) + "" + f.getString(4) + "<" + fSTFID + ">" );
                // }
                double d = b.getRacePct();
                f.setAttribute("racePct", new Double(d));
                
                // if (MirarUtils.test == true) {
                //   System.out.println("test == true");
                int numWhite = b.getNumWhite();
                int numBlack = b.getNumBlack();
                int numAsian = b.getNumAsian();
                int numHispanic = b.getNumHispanic();
//                System.out.println("\t\t &&&&&&&&&   Block   " +b.getSTFID()  + " num white: " + numWhite);
//                System.out.println("\t\t &&&&&&&&&   Block   " +b.getSTFID()  + " num black: " + numBlack);
//                System.out.println("\t\t &&&&&&&&&   Block   " +b.getSTFID()  + " num asian: " + numAsian);
//                System.out.println("\t\t &&&&&&&&&   Block   " +b.getSTFID()  + " num hispanic: " + numHispanic);
//                System.out.println("\t\t &&&&&&&&&   Block   " +b.getSTFID()  + " race pct: " + d);
                f.setAttribute("numWhite", new Integer(numWhite));
                f.setAttribute("numBlack", new Integer(numBlack));
                f.setAttribute("numAsian", new Integer(numAsian));
                f.setAttribute("numHispanic", new Integer(numHispanic));
                
                double medianIncome =  b.getMeanIncome();
                f.setAttribute("medianIncome", new Double(medianIncome));
                //  }
                
                j++;
            }
        }
        //    String city = (String) i.next();
        /*
         * for (int i=0; i <schema.getAttributeCount(); i++) {
         * System.out.println(schema.getAttributeName(i));
         * System.out.println(schema.getAttributeType(i));
         * System.out.println(((Feature)featureList.get(0)).getString(i));
         */
        /*
         *  }*/
         blockLayer.fireLayerChanged(LayerEventType.APPEARANCE_CHANGED);//
         
    }

    
    /**
     *  update block layer with the getRacePct() from each Block in the
     *  BlockList
     *  @precondition blocks are create, map data read in
     */
    public void updateBlockGroupLayer(ArrayList blockGroupList) { // Priyasree_DeadCode : Unreachable code_updateBlockGroupLayer(ArrayList blockGroupList)
       // ArrayList blockList = new ArrayList();
       // blockList.addAll(blocks);
        // featureList = new ArrayList(fc1.getFeatures());

        // go through and change the block value for all of tract2000 = 104104
        //  fc1

        //  FeatureSchema schema = ((Feature)featureList.get(0)).getSchema();
        //   System.out.println("schema:num attributes: " +
        // schema.getAttributeCount());

        /*
         * for (int i=0; i <fc1.size(); i++) { if
         * (((Feature)featureList.get(i)).getString(3).equals("104104") ) {
         * ((Feature)featureList.get(i)).setAttribute(4, "5023");
         */
        BlockGroup bg = null;
        int j=0;
        for (Iterator i = blockGroupData.iterator(); i.hasNext(); ) {
            Feature f = (Feature)i.next();
            bg =  CensusUnitHandler.getInstance().getBlockGroup(f.getString(3), f.getString(4).substring(0,1));// (BlockGroup)blockGroupList.get(j);
            if (bg != null) {
                bg.updateBlocksInfo();
                double d = bg.getRacePct();
                f.setAttribute("racePct", new Double(d));
                
                
                // if (MirarUtils.test == true) {
                //   System.out.println("test == true");
                int numWhite = bg.getNumWhite();
                int numBlack = bg.getNumBlack();
                int numAsian = bg.getNumAsian();
                int numHispanic = bg.getNumHispanic();
                
                f.setAttribute("numWhite", new Integer(numWhite));
                f.setAttribute("numBlack", new Integer(numBlack));
                f.setAttribute("numAsian", new Integer(numAsian));
                f.setAttribute("numHispanic", new Integer(numHispanic));
                
                double medianIncome =  bg.getMedianIncome();
                f.setAttribute("medianIncome", new Double(medianIncome));
                //  }
                
                j++;
            }
        }
        //    String city = (String) i.next();
        /*
         * for (int i=0; i <schema.getAttributeCount(); i++) {
         * System.out.println(schema.getAttributeName(i));
         * System.out.println(schema.getAttributeType(i));
         * System.out.println(((Feature)featureList.get(0)).getString(i));
         */
        /*
         *  }*/
         blockGroupLayer.fireLayerChanged(LayerEventType.APPEARANCE_CHANGED);//
         
    }

 
    public void updateCensusTractLayer(ArrayList censusTractList) { // Priyasree_DeadCode : Unreachable code_void updateCensusTractLayer(ArrayList censusTractList)
        // ArrayList blockList = new ArrayList();
        // blockList.addAll(blocks);
         // featureList = new ArrayList(fc1.getFeatures());

         // go through and change the block value for all of tract2000 = 104104
         //  fc1

         //  FeatureSchema schema = ((Feature)featureList.get(0)).getSchema();
         //   System.out.println("schema:num attributes: " +
         // schema.getAttributeCount());

         /*
          * for (int i=0; i <fc1.size(); i++) { if
          * (((Feature)featureList.get(i)).getString(3).equals("104104") ) {
          * ((Feature)featureList.get(i)).setAttribute(4, "5023");
          */
        CensusTract ct = null;
        int j=0;
        for (Iterator i = censusTractData.iterator(); i.hasNext(); ) {
            Feature f = (Feature)i.next();
            String fTractString = f.getString(3);
            ct = CensusUnitHandler.getInstance().getCensusTract(fTractString);
//          (CensusTract)censusTractList.get(j);
            if (ct != null ) {
                ///#######
                ct.updateBlockGroupsInfo();
                double d = ct.getRacePct();
                f.setAttribute("racePct", new Double(d));
                
                // if (MirarUtils.test == true) {
                //   System.out.println("test == true");
                int numWhite = ct.getNumWhite();
                int numBlack = ct.getNumBlack();
                int numAsian = ct.getNumAsian();
                int numHispanic = ct.getNumHispanic();
                
                f.setAttribute("numWhite", new Integer(numWhite));
                f.setAttribute("numBlack", new Integer(numBlack));
                f.setAttribute("numAsian", new Integer(numAsian));
                f.setAttribute("numHispanic", new Integer(numHispanic));
                
                double medianIncome =  ct.getMedianIncome();
                f.setAttribute("medianIncome", new Double(medianIncome));
                //  }
                
                j++;
            }
        }
             
         //    String city = (String) i.next();
         /*
          * for (int i=0; i <schema.getAttributeCount(); i++) {
          * System.out.println(schema.getAttributeName(i));
          * System.out.println(schema.getAttributeType(i));
          * System.out.println(((Feature)featureList.get(0)).getString(i));
          */
         /*
          *  }*/
          censusTractLayer.fireLayerChanged(LayerEventType.APPEARANCE_CHANGED);//
          
     }

    
    /**
     * load in shapefile data using Strings indicating filenames
     * 
     */
//    public void loadShapefiles(String blockShpFile, String blockGroupShpFile, 
 //           String censusTractShpFile) {
        public void loadShapefiles() { // Priyasree_DeadCode : Unreachable code_
        ImageIcon imageIcon = IconLoader.icon("World.gif");

        ShapefileReader sfReader = new ShapefileReader();
        try {
           
            // load shapefiles

            blockShpData = new IndexedFeatureCollection(sfReader
                    .read(new DriverProperties(MirarUtils.BLOCK_SHP_FILE)));
            blockGroupShpData = new IndexedFeatureCollection(sfReader
                    .read(new DriverProperties(MirarUtils.BLOCK_GROUP_SHP_FILE)));
            censusTractShpData = new IndexedFeatureCollection(sfReader
                    .read(new DriverProperties(MirarUtils.CENSUS_TRACT_SHP_FILE)));

        } catch (com.vividsolutions.jump.io.IllegalParametersException e) {
           // System.out.println(" didn't read file");
            e.printStackTrace();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * setup the JUMP Workbench
     * this mus be done (and completed) before the map can be added to the Workbench
     *
     */
    public void setupJUMPWorkbench() { // Priyasree_DeadCode : Unreachable code_void setupJUMPWorkbench()
        ImageIcon imageIcon = IconLoader.icon("World.gif");
        String[] empty = {};

        try {
            wb = new JUMPWorkbench("Model of Race, Income, And Residence (MIRAR)", empty, imageIcon, new JWindow(),
                    new DummyTaskMonitor());
            //final

            workbenchContext = wb.getContext();
            new JUMPConfiguration().setup(workbenchContext);

            frame = new WorkbenchFrame("Model of Race, Income, And Residence (MIRAR)", imageIcon, workbenchContext);
            wb.getFrame().setVisible(true);

        } catch (com.vividsolutions.jump.io.IllegalParametersException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    /**
     * creates layers and themes and adds them to the JUMP Workbench
     * 
     * @precondition setup and prepareData have been called
     */
    public void buildDisplay() {
        //workbenchContext.getLayerManager().addLayer("MirarLA", "Block Group",
          //      blockGroupShpData);
        //workbenchContext.getLayerManager().addLayer("MirarLA", "Census Tract",
         //       censusTractShpData);

        // ****************************
        //layer style
        //*****************************
      
        censusTractLayer = new Layer("Census Tract Layer", Color.GREEN, censusTractData, workbenchContext.getLayerManager());
        blockGroupLayer = new Layer("Block Group Layer", Color.YELLOW, blockGroupData, workbenchContext.getLayerManager());
        blockLayer = new Layer("Block Layer", Color.RED, blockData,
                workbenchContext.getLayerManager());
        
        
        //   workbenchContext.getLayerManager().addLayer("test", blockLayer);

        // ColorScheme.load();
        // ColorScheme colorScheme = ColorScheme.create("Set 1");

        //******************
        // discrete style based on BLOCK2000 value
        //***********************

        /*
         * ArrayList names = new ArrayList();
         * names.addAll(ColorScheme.discreteColorSchemeNames());
         * System.out.println("names size : " + names.size()); ColorScheme
         * colorScheme = ColorScheme.create(names.get(0).toString());
         * 
         * //ColorScheme colorScheme = new ColorScheme("discrete",
         * ColorScheme.discreteColorSchemeNames());
         *  // if (colorScheme == null) System.out.println("color scheme is
         * null"); // ArrayList colorList = (ArrayList)colorScheme.getColors(); //
         * System.out.println("colorList size : " + colorList.size()); //
         * layer.setName("(" + colorScheme.getColors().size() + ") " +
         * colorSchemeName);
         * 
         *  // create a new map of attributes to colors Map attributeToStyleMap =
         * new HashMap(); // FeatureSchema schema =
         * ((Feature)featureList.get(0)).getSchema();
         * //System.out.println("schema:num attributes: " +
         * schema.getAttributeCount());
         *  // get the features - use them for the attribute to color mapping
         * for (int i=0; i <featureList.size(); i++) { // get the feature value
         * String block = (String)((Feature)featureList.get(i)).getString(4);
         * System.out.println("Block: " + block); // get the next color value
         * in the color scheme BasicStyle tempStyle = new
         * BasicStyle((Color)colorScheme.next()); // add both to the map --
         * note this is discrete attributeToStyleMap.put(block, tempStyle); // }
         * 
         * 
         *  }
         * 
         * ColorThemingStyle themeStyle = new ColorThemingStyle("BLOCK2000",
         * attributeToStyleMap, new BasicStyle((Color)colorScheme.next()));
         *  
         */
        //******************
        // end discrete style based on BLOCK2000 value
        //***********************
        //***********************
        // range style
        //*****************************
        // add feature and value for it to feature list - this will be used for
        // the range value
        /*
         * for (int i=0; i <featureList.size(); i++) { // add feature --
         * racePct FeatureSchema schema =
         * ((Feature)featureList.get(i)).getSchema();
         * schema.addAttribute("racePct", AttributeType.DOUBLE);
         * //((Feature)featureList.get(i)).setSchema(schema); //for (int j=0; j
         * <schema.getAttributeCount(); j++) { // System.out.println("j: " +
         * j); // System.out.println(schema.getAttributeName(j)); //
         * System.out.println(schema.getAttributeType(j)); //
         * System.out.println(((Feature)featureList.get(i)).getString(j)); //}
         *  // FeatureSchema schema =
         * ((Feature)featureList.get(0)).getSchema(); }
         * System.out.println("featurelist size: " + featureList.size());
         * 
         * for (int k=0; k <featureList.size(); k++) {
         * System.out.println("featurelist num: " + k);
         * 
         * if (k < featureList.size()/2) { //set value to .5 or less
         * ((Feature)featureList.get(k)).setAttribute(6, new Double(0.3)); }
         * else { //set value to higher than .5
         * ((Feature)featureList.get(k)).setAttribute(6, new Double(1.0));
         *  } }
         */

        ArrayList names = new ArrayList();
        names.addAll(ColorScheme.rangeColorSchemeNames());
      //  System.out.println("names size : " + names.size());
        //for (int i = 0; i < names.size(); i++) {
        //    System.out.println("color scheme name : " + names.get(i));
       // }

        ColorScheme colorScheme = ColorScheme.create("spectral (ColorBrewer)");//names.get(0).toString());
        // ColorScheme colorScheme = new ColorScheme("spectral", names);

        // create a new map of attributes to colors
        Range.RangeTreeMap attributeRangeToStyleMap = new Range.RangeTreeMap();
        // FeatureSchema schema = ((Feature)featureList.get(0)).getSchema();
        //System.out.println("schema:num attributes: " +
        // schema.getAttributeCount());

        // start off simple: use 0 -.5 and >.5 to 1

        //   Range(java.lang.Object@min, boolean@includingMin,
        // java.lang.Object@max, boolean@includingMax)
        Range whiteLow = new Range(new Double(0.0), false, new Double(0.3), true);
        Range whiteMid = new Range(new Double(0.3), false, new Double(0.7), true);
        Range whiteHigh = new Range(new Double(0.7), false, new Double(1.0), true);
     
        Range blackLow = new Range(new Double(1.0), false, new Double(1.3), true);
        Range blackMid = new Range(new Double(1.3), false, new Double(1.7), true);
        Range blackHigh = new Range(new Double(1.7), false, new Double(2.0), true);
       
        Range asianLow = new Range(new Double(2.0), false, new Double(2.3), true);
        Range asianMid = new Range(new Double(2.3), false, new Double(2.7), true);
        Range asianHigh = new Range(new Double(2.7), false, new Double(3.0), true);
       
        Range hispanicLow = new Range(new Double(3.0), false, new Double(3.3), true);
        Range hispanicMid = new Range(new Double(3.3), false, new Double(3.7), true);
        Range hispanicHigh = new Range(new Double(3.7), false, new Double(4.0), true);

        Range noMajority = new Range(new Double(-1.0), true, new Double(-1.0), true);
        
        attributeRangeToStyleMap.put(whiteLow, new BasicStyle(new Color(109,227,255)));//Color.CYAN));
        attributeRangeToStyleMap.put(whiteMid, new BasicStyle(new Color(8,131,162)));
        attributeRangeToStyleMap.put(whiteHigh, new BasicStyle(new Color(42,55,89)));//Color.BLUE.darker().darker()));
     
       
        attributeRangeToStyleMap.put(blackLow, new BasicStyle(new Color(0,255,0)));
        attributeRangeToStyleMap.put(blackMid, new BasicStyle(new Color(0,160,0))); 
        attributeRangeToStyleMap.put(blackHigh, new BasicStyle(new Color(0,94,0)));

       
        attributeRangeToStyleMap.put(asianLow, new BasicStyle(new Color(255,217,0)));
        attributeRangeToStyleMap.put(asianMid, new BasicStyle(new Color(190,138,0)));
        attributeRangeToStyleMap.put(asianHigh, new BasicStyle(new Color(107,79,0)));
                       
        attributeRangeToStyleMap.put(hispanicLow, new BasicStyle(new Color(255,0,201)));
        attributeRangeToStyleMap.put(hispanicMid, new BasicStyle(new Color(176,0,115)));
        attributeRangeToStyleMap.put(hispanicHigh, new BasicStyle(new Color(104,0,73)));

        attributeRangeToStyleMap.put(noMajority, new BasicStyle(Color.GRAY));
        
        ColorThemingStyle themeStyle = new ColorThemingStyle("racePct",
                attributeRangeToStyleMap, new BasicStyle((Color) colorScheme // Priyasree_Audit: Unnecessary type cast to Color_Delete the unnecessary cast.
                        .next()));

        //***********************
        // end range style
        //*****

        themeStyle.setEnabled(true);
        workbenchContext.getLayerManager().addLayer("MirarLA", censusTractLayer);
        workbenchContext.getLayerManager().addLayer("MirarLA", blockGroupLayer);
        workbenchContext.getLayerManager().addLayer("MirarLA", blockLayer);
        
        // the style needs to be added after the layer has been added...
        censusTractLayer.addStyle(themeStyle);
        blockGroupLayer.addStyle(themeStyle);
        blockLayer.addStyle(themeStyle);

        // ****************************
        //end layer style
        //*****************************
        // blockLayer.addStyle(new BasicStyle());
        //layer.setVisible(false);
        /*
         * Map attributeToStyleMap = new HashMap(); for (Iterator i =
         * cities.iterator(); i.hasNext(); ) { String city = (String) i.next();
         * attributeToStyleMap.put(city, new BasicStyle(colorScheme.next())); }
         *  // layer.getBasicStyle().setEnabled(false); ColorThemingStyle
         * themeStyle = new ColorThemingStyle("City", attributeToStyleMap, new
         * BasicStyle(Color.gray)); themeStyle.setEnabled(true);
         * layer.addStyle(themeStyle); layer.setVisible(false);
         */
        // ((Feature)featureList.get(j)).getString(i))
        /*
         * try {
         * 
         * //PlugInContext pic = workbenchContext.createPlugInContext();
         * TestColorThemingPlugIn testColor = new TestColorThemingPlugIn();
         * testColor.initialize(workbenchContext.createPlugInContext());
         * testColor.execute(workbenchContext.createPlugInContext()); // new
         * TestColorThemingPlugIn().initialize(workbenchContext.createPlugInContext()); }
         * catch (java.lang.Exception e) { e.printStackTrace();
         */
        //omHandler.createDisplay();

        // test showing attributes
        //ViewAttributesPlugIn(
    }

    public ArrayList getSTFIDList() {
        return stfidList;
    }
    public void buildMap() {
    }
    
    public void initializeSTFIDData() {
        stfidList = new ArrayList();
        FileChannel in = null;
        DBFReader dbfFileReader = null;
        try {

            String datasourceNoSHP = MirarUtils.BLOCK_SHP_FILE.substring(0, MirarUtils.BLOCK_SHP_FILE.length() - 3);
          
            String dbfString = datasourceNoSHP + "dbf";

            in = new FileInputStream(dbfString).getChannel();
            dbfFileReader = new DBFReader(dbfString);
            //DbaseFileHeader header = dbfFileReader.getHeader();
          //  int numRecords = header.getNumRecords();
            int numFields = dbfFileReader.getFieldCount();
            for (int i=0; i<numFields; i++) {
                System.out.println("dbf filed: " + ((JDBField)dbfFileReader.getField(i)).getName()); // Priyasree_Audit: Unnecessary type cast to JDBField_Delete the unnecessary cast.
            }
            while (dbfFileReader.hasNextRecord()) {
                Object [] record = dbfFileReader.nextRecord();
              //  System.out.println("record stfid: " + (String)record[4]);
                stfidList.add((String)record[4]);
            }
            
//            for (int i = 0; i < numRecords; i++) {
                //           create one new obj for each feature
               // Object obj = clazz.newInstance();
                //((GisAgent) obj).setGisAgentIndex(i);
                //agentList.add(obj);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dbfFileReader != null) {
                //try {
                 //   dbfFileReader.close();
                //} catch (IOException e) {
                   // e.printStackTrace();
                //}
            }

        }


    }

       /* stfidList = new ArrayList();
            try {
              // get feature info - features, types 
              URL file = new File(MirarUtils.BLOCK_SHP_FILE).toURL();
              org.geotools.data.shapefile.ShapefileDataStore store = new ShapefileDataStore(file);
              String name = store.getTypeNames()[0];  // there is just one - one type per shapefile - type of data in shapefile
              System.out.println("init STFID data - name " + name);
              FeatureSource source =   store.getFeatureSource(name);
              FeatureResults fsShape = source.getFeatures();

              //String[] types = store.getTypeNames();

              //Method[] methods = clazz.getMethods();
                     
                    
              FeatureType ft = source.getSchema();
              FeatureReader reader = fsShape.reader();
              while (reader.hasNext()) {
                  org.geotools.feature.Feature feature = reader.next();   
                //     get the stfid for each feature
                //agentList.add(obj);
                  stfidList.add(feature.getAttribute("STFID"));
                //stfidList.add((String) f11.getAttribute("STFID"));
              }
              reader.close();

              
            } catch (Exception e) {
              e.printStackTrace();
            }

    }*/
}
