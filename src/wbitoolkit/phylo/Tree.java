/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author wb
 */
public class Tree {
    
    private TreeNode root;
    private double height=0;
    private int leafNum=0;
    private int nodeNum=0;
    
    public Tree(TreeNode root){
        this.root=root;
        updateTree();
    }
    
    public double getHeight(){
        return height;
    }
    
    public void setHeight(double height){
        this.height=height;
    }
    
    public TreeNode getLeafByName(String name){
        TreeNode leaf=null;
        for(TreeNode l:this.getLeafNodes()){
            if(l.getName().equals(name)){
                leaf=l;
                break;
            }
        }
        return leaf;
    }
    
    public int getLeafNum(){
        return leafNum;
    }
    
    public int getNodeNum(){
        return nodeNum;
    }
    
    public List<TreeNode> getLeafNodes(){
        return root.getLeafNodes();
    }
    
    public String[] getLabels(){
        List<TreeNode> ln=getLeafNodes();
        String[] labels=new String[ln.size()];
        for(int i=0;i<ln.size();i++){
            labels[i]=ln.get(i).getName();
        }
        return labels;
    }
    
    public final void updateTree(){
        updateId();
        updateLevel(); 
        updateLeafNum();
        updateNodeNum();
        updateDistanceFromRoot();
        updateHeight();
    }
    
    private void updateHeight(){
        List<TreeNode> leaves=root.getLeafNodes();
        double mh=0;
        for(TreeNode leaf : leaves){
            double dis=leaf.getDistanceFromRoot();
            if(mh<dis)
                mh=dis;
        }
        height=mh;
    }
    private void updateLeafNum(){
        leafNum=root.numOfLeaves();
    }
    private void updateNodeNum(){
        nodeNum=root.numOfDescendants()+1;
    }
    private void updateId(){
        LinkedList list=new LinkedList();
        list.add(root);
        TreeNode tmp=null;
        int id=0;
        while(list.size()>0 || tmp!=null){
            if(tmp!=null){
                tmp.setId(id++);
                for(int i=0;i<tmp.numOfChildNodes();i++){
                    list.addFirst(tmp.getChildNode(i));
                }
            }
            tmp=(TreeNode)list.pollFirst();
        }
        
    }
    private void updateLevel(){
        updateLevel(root,1);
    }
    private void updateLevel(TreeNode node, int level){
        node.setLevel(level);
        for(int i=0;i<node.numOfChildNodes();i++){
            updateLevel(node.getChildNode(i),level+1);
        }
    }
    private void updateDistanceFromRoot(){
        root.setDistanceFromRoot();
    }
    
    public void setRootAt(TreeNode from, TreeNode to, float p){
        setRootAt(from,to,to.getBranchLength()*p);
    }
    public void setRootAt(TreeNode from, TreeNode to, double disToRoot){
        TreeNode newRoot=new TreeNode(0);
        from.setAsRoot();
        if(disToRoot==0){
            newRoot=from;
        }
        else{
            double dis=to.getBranchLength();
            from.setBranchLength(disToRoot);
            from.setParentNode(newRoot);
            from.setId(this.getNodeNum()+1);
            from.removeChildNode(to);
            newRoot.addChildNode(from);
            to.setBranchLength(dis-disToRoot);
            to.setParentNode(newRoot);
            newRoot.addChildNode(to);
        }
        root=newRoot;
        updateTree();
    }
    
    public void setRootByMidpoint(){
        double[][] matrix=getMatrix();
        List leaves=root.getLeafNodes();
        int a=0,b=0;
        double max=0;
        for(int i=0;i<matrix.length;i++){
            for(int j=i+1;j<matrix.length;j++){
                if(max<matrix[i][j]){
                    max=matrix[i][j];
                    a=i;b=j;
                }
            }
        }
        if(a==b) return;
        
        TreeNode l1=(TreeNode)leaves.get(a);
        TreeNode l2=(TreeNode)leaves.get(b);
        TreeNode cp=l1.getMRCAWith(l2);
        double d1=cp.distanceTo(l1);
        double d2=cp.distanceTo(l2);
        max/=2;
        TreeNode to=d1>d2?l1:l2;
        double dis=to.getBranchLength();
        while(dis<max){
            to=to.getParentNode();
            dis+=to.getBranchLength();      
        }
        TreeNode from=to.getParentNode();
        double disToRoot=dis-max;
        setRootAt(from, to, disToRoot);
    }
    
    public double[][] getMatrix(){
        List leaves=root.getLeafNodes();
//        sortNodesById(leaves);
        int n=leaves.size();
        double[][] matrix=new double[n][n];
        for(int i=0;i<n-1;i++){
            for(int j=i+1;j<n;j++){
                matrix[i][j]=((TreeNode)leaves.get(i)).distanceTo((TreeNode)leaves.get(j));
            }
        }
        return matrix;
    }
    
    public void sortNodesById(List nodes){
        Collections.sort(nodes, new Comparator<TreeNode>() {
            public int compare(TreeNode n1, TreeNode n2) {
                double s=n1.getId()-n2.getId();//ascend
                if(s>0) return 1;          //o1>o2
                else if(s<0) return -1;    //o1<o2
                else return 0;             //o1=o2
            }
        });
    }
    
    
    /**
     * @return the root
     */
    public TreeNode getRootNode() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRootNode(TreeNode root) {
        this.root = root;
        updateTree();
    }
    
    
}
