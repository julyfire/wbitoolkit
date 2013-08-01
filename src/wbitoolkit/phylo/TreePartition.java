/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import wbitoolkit.statistic.Description;

/**
 *
 * @author wb
 */
public class TreePartition {
    private Tree tree;
    private double[][] matrix;
    private double cutoff=0.2;
    private double bootstrap=0.9;
    private List clusters;
    private List groups;
    private HashMap<String,Integer> nameMap;
    
    
    public TreePartition(Tree tree){
        this.tree=tree;
        clusters=new LinkedList();
    }
    
    public void grouping(){
        matrix=tree.getMatrix();
        double dc=distanceCutoff();
        clusters=exploreCluster(tree.getRootNode(),dc);
        flattening();
//        buildNameMap();
//        double min=getMinInterGroupsDistance();
//        double max=getMaxIntraGroupsDistance();
//        System.out.println(min-max);
    }
    
    private double getMinInterGroupsDistance(){
        double minD=100;
        for(int i=0;i<groups.size()-1;i++){
            String[] groupI=(String[])groups.get(i);
            for(int j=i+1;j<groups.size();j++){
                String[] groupJ=(String[])groups.get(j);
                for(int a=0;a<groupI.length;a++) {
                    for(int b=0;b<groupJ.length;b++){
                        int i1=nameMap.get(groupI[a]);
                        int i2=nameMap.get(groupJ[b]);
                        double dis=matrix[i1][i2];
                        if(minD>dis) {
                            minD=dis;
                        }
                    }
                }
            }
        }
        return minD;
    }
    
    private double getMaxIntraGroupsDistance(){
        double maxD=0;
        for(int i=0;i<groups.size()-1;i++){
            String[] group=(String[])groups.get(i);
            for(int j=0;j<group.length;j++){
                for(int k=j+1;k<group.length;k++){
                    int i1=nameMap.get(group[j]);
                    int i2=nameMap.get(group[k]);
                    double dis=matrix[i1][i2];
                    if(maxD<dis){
                        maxD=dis;
                    }
                }
            }
        }
        return maxD;
        
    }
    
    private void buildNameMap(){
        String[] names=tree.getLabels();
        nameMap=new HashMap();
        for(int i=0;i<names.length;i++){
            nameMap.put(names[i], i);
        }
    }
    
    private double distanceCutoff(){
        int n=matrix.length;
        Double[] dis=new Double[n*(n-1)/2];
        int k=0;
        for(int i=0;i<n-1;i++){
            for(int j=i+1;j<n;j++){
                dis[k++]=matrix[i][j];
            }
        }
        return Description.quantile(dis, cutoff);
    }
    
    private List exploreCluster(TreeNode root, double threshold){
        List cs = new LinkedList();
        for(TreeNode n:root.getChildNodes()){
            if( (n.isLeaf()) || isCluster(n,threshold) ){
                cs.add(n);
            }
            else{
                cs.addAll(exploreCluster(n,threshold));
            }
        }
        return cs;
    }

    private boolean isCluster(TreeNode node, double threshold) {
        double median = 0.0D;
        boolean res = false;
        double bootstrap=node.getProb();
        if(bootstrap>1) bootstrap/=100;
        if (!(node.isLeaf()) && (bootstrap >= 0.9D)) {
            List<TreeNode> leaves=node.getLeafNodes();
            int n=leaves.size();
            Double[] dis=new Double[n*(n-1)/2];
            int k=0;
            for(int i=0;i<n-1;i++){
                for(int j=i+1;j<n;j++){
                    dis[k++]=leaves.get(i).distanceTo(leaves.get(j));
                }
            }
            median = Description.quantile(dis, 0.5D);
            res = median < threshold;
        }
    
        return res;
    }
    
    private void flattening(){
        groups=new LinkedList();
        for(Object c:clusters){
            List<TreeNode> leaves=((TreeNode)c).getLeafNodes();
            String[] ls=new String[leaves.size()];
            int i=0;
            for(TreeNode n:leaves){
                ls[i++]=n.getName();
            }
            groups.add(ls);
        }
    }

    /**
     * @return the tree
     */
    public Tree getTree() {
        return tree;
    }

    /**
     * @param tree the tree to set
     */
    public void setTree(Tree tree) {
        this.tree = tree;
    }

    /**
     * @return the cutoff
     */
    public double getCutoff() {
        return cutoff;
    }

    /**
     * @param cutoff the cutoff to set
     */
    public void setCutoff(double cutoff) {
        this.cutoff = cutoff;
    }

    /**
     * @return the bootstrap
     */
    public double getBootstrap() {
        return bootstrap;
    }

    /**
     * @param bootstrap the bootstrap to set
     */
    public void setBootstrap(double bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * @return the clusters
     */
    public List getClusters() {
        return clusters;
    }

    /**
     * @param clusters the clusters to set
     */
    public void setClusters(List clusters) {
        this.clusters = clusters;
    }

    /**
     * @return the groups
     */
    public List getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List groups) {
        this.groups = groups;
    }

}
