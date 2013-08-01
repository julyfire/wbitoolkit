/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author wb
 */
public class TreeNode {
    private int id; //TreeNode id ranges from 0~(total number -1)
    private String name;
    private int level;
    private double branchLength;
    private double prob;
    private double distanceFromRoot;
    private TreeNode parentNode;
    private List<TreeNode> childNodes=new ArrayList<TreeNode>();
    private List<TreeNode> leafNodes;
    
    public TreeNode(){
        
    }
    public TreeNode(int id){
        this.id=id;
    }
   

    public void addChildNode(TreeNode child){
        childNodes.add(child);
    }
    
    public void removeChildNode(TreeNode child){
        childNodes.remove(child);
    }
    
    
    
    public int numOfChildNodes(){
        return childNodes.size();
    }
    
    public TreeNode getChildNode(int n){
        return childNodes.get(n);
    }
    
    /**
     * @return the leafNodes
     */
    public List<TreeNode> getLeafNodes() {

        this.leafNodes = new LinkedList();
        if (this.isLeaf())
            this.leafNodes.add(this);
        else {
            for (TreeNode child : this.childNodes) {
                this.leafNodes.addAll(child.getLeafNodes());
            }
        }

        return leafNodes;
    }
    
    public Set getLeafNodesNames(){
        Set names=new HashSet();
        List<TreeNode> n=this.getLeafNodes();
        for(TreeNode l:n){
           names.add(l.getName());
        }
        return names;
    }
    
    public int numOfLeaves(){
        return this.getLeafNodes().size();
    }
    
    public int numOfDescendants(){
        int num = numOfChildNodes();
        for (int i = 0; i < numOfChildNodes(); i++) {
           num += getChildNode(i).numOfDescendants();
        }
        return num;
    }
    
    /**
     * get most recent common ancestor with the node
     * @param node
     * @return 
     */
    public TreeNode getMRCAWith(TreeNode node){
        if(this.equals(node))
            return null;
        if(this.isAncestorOf(node))
            return this;
        else if(this.isDescendantOf(node))
            return node;
        else{
            return getMRCAWith(node.parentNode);
        }
    }
    
    public void setDistanceFromRoot(){
        double dis=this.getDistanceFromRoot();
        this.setDistanceFromRoot(this.getDistanceFromRoot() + this.branchLength);  
        for (TreeNode n : this.childNodes) {
            n.setDistanceFromRoot(this.getDistanceFromRoot());
            if (n.getDistanceFromRoot() < dis) {
                System.out.println("Warning: find negative branch length "+n.branchLength);
            }
            n.setDistanceFromRoot();
        }
    }
    
    public double distanceTo(TreeNode node){
        return this.getDistanceFromRoot()+node.getDistanceFromRoot()-this.getMRCAWith(node).getDistanceFromRoot()*2;
    }
    
//    public double distanceToNode(TreeNode node){    
//        double distance=0;
//        if(node==null || this.equals(node)){
//            distance=0;
//        }
//        else{
//            TreeNode cp=getMRCAWith(node);
//            if(cp.equals(this))
//                distance=cp.distanceToNode(node.getParentNode())+node.getBranchLength();
//            else if(cp.equals(node))
//                distance=cp.distanceToNode(this.parentNode)+this.branchLength;
//            else
//                distance=cp.distanceToNode(node.getParentNode())+node.getBranchLength()
//                        +cp.distanceToNode(this.parentNode)+this.branchLength;        
//        }
//        return distance;
//    }
    
    public boolean isRoot(){
        return this.parentNode==null;
    }
    
    public boolean isLeaf() {
        return this.childNodes.isEmpty();
    }
    
    public boolean isAncestorOf(TreeNode node){
        if(node.isYoungerThan(this)){
            if(node.getParentNode().equals(this)){
                return true;
            }
            else{
                return isAncestorOf(node.getParentNode());
            }
        }
        return false; 
    }
    public boolean isDescendantOf(TreeNode node){
        if(this.isYoungerThan(node)){
            if(this.getParentNode().equals(node)){
                return true;
            }
            else{
                return this.getParentNode().isDescendantOf(node);
            }
        }
        return false;
    }
    
    public boolean equals(TreeNode node){
        return this.getId()==node.getId();
    }
    
    public boolean isYoungerThan(TreeNode node){
        return this.getLevel()>node.getLevel();
    }
    
    public boolean isSiblinOf(TreeNode node){
        return this.getLevel()==node.getLevel() && this.getId()!=node.getId();
    }
    
    
    public void setAsRoot(){
        TreeNode oldParent=this.parentNode;
        double branch=this.branchLength;
        if(oldParent==null){
            return;
        }
        else{
            this.addChildNode(oldParent);
            oldParent.setAsRoot();
            oldParent.parentNode=this;
            oldParent.branchLength=branch;
            oldParent.removeChildNode(this);
            this.parentNode=null;
            this.branchLength=0;
        }
    }
    
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the branchLength
     */
    public double getBranchLength() {
        return branchLength;
    }

    /**
     * @param branchLength the branchLength to set
     */
    public void setBranchLength(double branchLength) {
        this.branchLength = branchLength;
    }

    /**
     * @return the prob
     */
    public double getProb() {
        return prob;
    }

    /**
     * @param prob the prob to set
     */
    public void setProb(double prob) {
        this.prob = prob;
    }

    /**
     * @return the childNodes
     */
    public List<TreeNode> getChildNodes() {
        return childNodes;
    }

    /**
     * @param childNodes the childNodes to set
     */
    public void setChildNodes(ArrayList<TreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * @return the parentNode
     */
    public TreeNode getParentNode() {
        return parentNode;
    }

    /**
     * @param parentNode the parentNode to set
     */
    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the distanceFromRoot
     */
    public double getDistanceFromRoot() {
        return distanceFromRoot;
    }

    /**
     * @param distanceFromRoot the distanceFromRoot to set
     */
    public void setDistanceFromRoot(double distanceFromRoot) {
        this.distanceFromRoot = distanceFromRoot;
    }

    
    
}
