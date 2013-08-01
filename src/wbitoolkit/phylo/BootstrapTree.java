/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import wbitoolkit.statistic.Bootstrap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author wb
 */
public abstract class BootstrapTree extends Bootstrap{
    
    private String[] labels;
    private Set leaves;
    
    public BootstrapTree(){}
    public BootstrapTree(double[][] data, int times){
        setData(data);
        setTimes(times);
    }
    
    public Tree getBootstrapTree(){       
        NJTree njt=new NJTree(getMatrix(getData()),labels);
        njt.buildTree();
        TreeNode[] nodes=njt.getInternalNodes();
        double[] bootstrapValue=getSp();
        for(int i=0;i<nodes.length;i++){
            nodes[i].setProb(bootstrapValue[i]*100);
        }
        return njt.getTree();
    }
    
    public void calculateObservedValue(){
        
    }

    @Override
    public TreeNode[] calculateStatistic(Object[] sample) {
        double[][] dm=getMatrix(sample);
        NJTree njt=new NJTree(dm,labels);
        njt.buildNodes();
        TreeNode[] nodes=njt.getInternalNodes();
        
//        njt.buildTree();
//        System.out.println(new TreeIO().writeTree(njt.getTree()));

        
        return nodes;
    }
    
    @Override
    public boolean compareWithObservation(Object os, Object bs, H1 h1) {
        Set osLeaf=((TreeNode)os).getLeafNodesNames();
        Set bsLeaf=((TreeNode)bs).getLeafNodesNames();
        Set cOsLeaf=((HashSet)((HashSet)leaves).clone());
        cOsLeaf.removeAll(osLeaf);
        if(h1==H1.NOT_EQUAL){
            if(osLeaf.equals(bsLeaf) || cOsLeaf.equals(bsLeaf)){
                return true;
            }
        }
        return false;
    }
    
//    /**
//     * all the neighbor nodes leaves sets should be equal 
//     * @param os
//     * @param bs
//     * @param h1
//     * @return 
//     */
//    @Override
//    public boolean compareWithObservation(Object os, Object bs, H1 h1){
//        List<TreeNode> c1=((TreeNode)os).getChildNodes();
//        List<TreeNode> c2=((TreeNode)bs).getChildNodes();
//        if(c1.size()!=c2.size()) 
//            return false;
//        Set[] c1t=new HashSet[c1.size()+1];
//        Set[] c2t=new HashSet[c2.size()+1];
//        int i=0;
//        for(;i<c1.size();i++){
//            c1t[i]=c1.get(i).getLeafNodesNames();
//            c2t[i]=c2.get(i).getLeafNodesNames();
//        }
//        
//        Set osn=((TreeNode)os).getLeafNodesNames();
//        c1t[i]=((HashSet)((HashSet)leaves).clone());
//        c1t[i].removeAll(osn);
//        Set bsn=((TreeNode)bs).getLeafNodesNames();
//        c2t[i]=((HashSet)((HashSet)leaves).clone());
//        c2t[i].removeAll(bsn);
//        
//        
//        for(Set s:c1t){
//            if(s.size()==1 && s.contains("FN429887")){
//                
//                for(Set t:c2t){
//                    if(t.size()==1 && t.contains("FN429887")){
//                        System.out.println("find");
//                    }
//                }
//            }
//            
//        }
//        
//        
//        i=0;
//        for(Set s1:c1t){
//            for(Set s2:c2t){
//                if(s1.equals(s2))
//                    i+=1;
//            }
//        }
//        if(i==c1t.length){
//            return true;
//        }
//        
//        return false;        
//    }
    
    public abstract double[][] getMatrix(Object[] sample);

    /**
     * @return the labels
     */
    public String[] getLabels() {
        return labels;
    }

    /**
     * @param labels the labels to set
     */
    public void setLabels(String[] labels) {
        this.labels = labels;
        leaves=new HashSet();
        for(int i=0;i<labels.length;i++){
            leaves.add(labels[i]);
        }
    }
}
