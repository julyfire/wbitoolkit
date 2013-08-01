/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.json.JSONArray;
import org.json.JSONException;
import wbitoolkit.common.LibPathSet;
import wbitoolkit.statistic.Description;
import wbitoolkit.common.Matrix;
import wbitoolkit.statistic.Bootstrap;
import wbitoolkit.phylo.BootstrapTree;
import wbitoolkit.phylo.DistanceMatrix;
import wbitoolkit.phylo.NJTree;
import wbitoolkit.phylo.Tree;
import wbitoolkit.phylo.TreeGUI;
import wbitoolkit.phylo.TreeIO;
import wbitoolkit.phylo.TreeNode;
import wbitoolkit.rna.Fold;
import wbitoolkit.rna.MfeFold;
import wbitoolkit.smooth.Loess;

/**
 *
 * @author wb
 */
public class WBIToolkit {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, JSONException {
//        
//        LibPathSet.setLibDir("/home/wb/NetBeansProjects/WBIToolkit/lib");;
//        /**--------------------- test MFE calculation ** ----------------------/
//        String seq="ATGAACAAAACGGAAAAAGACGGTCTCAAAATTGCTCTCAGGC";
//        
//        long start,end,time;
//     
//        start = System.currentTimeMillis();
//        
//        for(int i=0;i<1000;i++)
//            new MfeFold().cal(seq);
//        
//        
//        end = System.currentTimeMillis();
//        time = end - start;
//        System.out.println(time);
////        
//        
////        
//        Fold.loadEnergyParameters();
//        
//        start = System.currentTimeMillis();
//        for(int i=0;i<1000;i++)
//            new Fold().fold(seq, null);
////        
//        end = System.currentTimeMillis();
//        time = end - start;
//        System.out.println(time);
//       
        
//        
//       
//       
//        /**--------------------- test LOESS smooth calculation --------------**/
//        double[] y=new double[]{
//           
//        };
//        double[] sy=new Loess(1).smooth(y);
//        for(int i=0;i<sy.length;i++)
//            System.out.println(sy[i]);
//        

//        /**--------------------- test tree IO -------------------------------**/
//        TreeIO tio=new TreeIO();
//        String ts="((A:0.12,B:0.15)0.98:0.11,(C:0.1,D:0.2)0.97:0.12,E:0.35);";
//        Tree tree=tio.readTree(ts);
        
        /** root at a node **/
//        TreeNode node=tree.getRootNode().getChildNode(0).getChildNode(1);
//        node.setAsRoot();
//        tree.setRootNode(node);
        /** root on branch **/
//        TreeNode from=tree.getRootNode().getChildNode(0);
//        TreeNode to=tree.getRootNode().getChildNode(0).getChildNode(0);
//        tree.setRootAt(from, to, 0.1f);
        /** midpoint root tree **/
//        tree.setRootByMidpoint();
//       
        /**--------------------- test NJ tree building ----------------------**/
//        double[][] matrix=new double[][]{
//            {0,0.27,0.45,0.55,0.58},
//            {0.27,0,0.48,0.58,0.61},
//            {0.45,0.48,0,0.3,0.57},
//            {0.55,0.58,0.3,0,0.67},
//            {0.58,0.61,0.57,0.67,0}
//        };
//        String[] labels=new String[]{"A","B","C","D","E"};
//        NJTree njt=new NJTree(matrix,labels);
////        NJTree njt=new NJTree(matrix);
//        njt.buildTree();
//        Tree tree=njt.getTree();
//        
//        System.out.println(tio.writeTree(tree));
//        TreeNode nD=tree.getLeafNodes().get(0);
//        TreeNode nB=tree.getLeafNodes().get(3);
//        System.out.println(nB.getName()+"-"+nD.getName()+"=>"+nB.distanceTo(nD));
        
        /**---------------------- test bootstrap ----------------------------**/
//        ArrayList<double[]> mfe=new ArrayList<double[]>();
//        BufferedReader br = new BufferedReader(new FileReader("/home/wb/Desktop/1000.mfe"));
//        String line=br.readLine();
//        String[] labels=line.split(",");
//        while((line = br.readLine()) != null){
//            String[] items=line.split(",");
//            double[] c=new double[items.length];
//            for(int i=0;i<c.length;i++){
//                c[i]=Double.parseDouble(items[i]);
//            }
//            mfe.add(c);       
//        }
//        br.close();
//        double[][] mfed=new double[mfe.size()][];
//        for(int i=0;i<mfe.size();i++){
//            mfed[i]=mfe.get(i);
//        }
//        
//        BootstrapTree bootstrap=new BootstrapTree(mfed,100){
//
//            @Override
//            public double[][] getMatrix(Object[] sample) {
////                double[][] data=Matrix.transpose((double[][])sample);
//                double[][] data=new double[sample.length][];
//                for(int i=0;i<sample.length;i++){
//                    data[i]=(double[])sample[i];
//                }
//                data=Matrix.transpose(data);
//                DistanceMatrix m=new DistanceMatrix(){
//
//                    @Override
//                    public double distanceOf(Object a, Object b) {
//                        double[] sa=(double[])a;
//                        double[] sb=(double[])b;
//                        double dis=0;
//                        for(int i=0;i<sa.length;i++){
//                            dis+=Math.abs(sa[i]-sb[i]);
//                        }
//                        return dis/sa.length;
//                    }
//                    
//                };
//                m.setData(data);
//                return m.getMatrix();
//            }
//            
//        };
//        bootstrap.setLabels(labels);
//        bootstrap.test(Bootstrap.H1.NOT_EQUAL);
//        double[] sp=bootstrap.getSp();
//        Tree tree=((BootstrapTree)bootstrap).getBootstrapTree();
//        tree.setRootByMidpoint();
//        System.out.println();
//        
//        /**--------------------- test tree GUI ------------------------------**/
//        JFrame f=new JFrame();
//        f.setSize(400,400);
//        TreeGUI tg=new TreeGUI(tree);
//        f.add(tg);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setLocationRelativeTo(null);
//        f.setVisible(true);
//        
        

    }
    
    
    
}
