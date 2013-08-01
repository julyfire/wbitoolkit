/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.util.ArrayList;

/**
 *
 * @author wb
 */
public class NJTree {
    private int numOfLeaf;			
    private int numOfNode;
    private int numOfBranch;
    private boolean[] freeStateOfNode; //is node still free
    private TreeNode root;
    private TreeNode[] nodes;
    private TreeNode[] internalNodes;
    private TreeBranch[] branches;
    private double[] r;			// The average distance to other leaves
    private String[] labels;
    private double[][] disMatrix;
    private double[][] newDisMatrix;  //new distance matrix after clustering
    private int mini,minj;
    private ArrayList<Double> lengthToRoot;

    public NJTree(double[][] disMatrix){
        this.disMatrix=disMatrix;
        labels=new String[disMatrix.length];
        for(int i=0;i<disMatrix.length;i++){
            labels[i]="s"+i;
        }
    }
    public NJTree(double[][] disMatrix, String[] labels) {
        this.disMatrix=disMatrix;
        this.labels=labels;   
    }
    
    public void buildTree(){
        buildNodes();
        addLastBranch();
    }
    public void buildNodes(){
        init();
        while (numOfNode < 2*numOfLeaf-2)
            findNextNode();
    }
    
    public Tree getTree(){ 
        return new Tree(root); 
    }
    
    private void init(){
        numOfLeaf = disMatrix.length;
        nodes = new TreeNode[2*numOfLeaf-2]; //unrooted tree has 2n-2 nodes
        internalNodes=new TreeNode[numOfLeaf-2];
        branches=new TreeBranch[2*numOfLeaf-3];
        freeStateOfNode=new boolean[nodes.length];
        newDisMatrix=new double[nodes.length][];
        //create leaf nodes
        for (int i=0; i<numOfLeaf; i++) {
            nodes[i] = new TreeNode(i);
            nodes[i].setName(labels[i]);
            newDisMatrix[i]=disMatrix[i];
        }
        //set all nodes being unclusterd
        for(int i=0;i<freeStateOfNode.length;i++)
            freeStateOfNode[i]=true;
        
        r = new double[2*numOfLeaf-1];
        numOfNode = numOfLeaf;
        numOfBranch=0;
        
        lengthToRoot=new ArrayList<Double>();
        
    }
    
    private void findNextNode(){
        computeR();
        getMinPair();
        addNewNode(mini, minj);
    }
      
    private void computeR() {
        for (int i=0; i<numOfNode; i++) { 
            if (isLiveAt(i)) {
                double sum = 0;
                for (int k=0; k<numOfNode; k++) {
                    if (isLiveAt(k) && k != i) {
                        sum += d(i, k);
                    }
                }
                int L = 2 * numOfLeaf - numOfNode;	// The current number of leaves
                r[i] = sum / (L - 2);	// Strange, but the book says so (p 171)
            }
        }
    }
    
    //find the free node pair(i,j) having minimal distance 
    private void getMinPair(){
        mini = -1;
        minj = -1;
        double mind = Double.POSITIVE_INFINITY;
        for (int i=0; i<numOfNode; i++) { 
            if (isLiveAt(i)) {
                for (int j=0; j<i; j++) { 
                    if (isLiveAt(j)) {
                        double d = d(i, j) - (r[i] + r[j]);
                        if (d < mind) {
                            mind = d;
                            mini = i;
                            minj = j;
                        }
                    }
                }
            }
        }
    }
    
    // Join i and j to form node K
    private void addNewNode(int i, int j) { 
        double[] dmat = new double[numOfNode];
        double dij = d(i, j);
        for (int m=0; m<numOfNode; m++)
            if (isLiveAt(m) && m != i && m != j) 
                dmat[m] = (d(i, m) + d(j, m) - dij) / 2;
        newDisMatrix[numOfNode]=dmat;
        double dik = (dij + r[i] - r[j]) / 2;
        double djk = dij - dik;
        
        TreeNode node=new TreeNode(numOfNode); //create a new node
        //add node[i] into children of the new node
        nodes[i].setBranchLength(dik);
        nodes[i].setParentNode(node);
        node.addChildNode(nodes[i]);
        //add node[j] into children of the new node
        nodes[j].setBranchLength(djk);
        nodes[j].setParentNode(node);
        node.addChildNode(nodes[j]);
        
        branches[numOfBranch++]=new TreeBranch(nodes[i],node,dik);
        branches[numOfBranch++]=new TreeBranch(nodes[j],node,djk);
        
        nodes[numOfNode++]=node;
        internalNodes[numOfNode-numOfLeaf-1]=node;
        
        killNode(i);
        killNode(j);
    }
    
    //distance between node i and j
    private double d(int i, int j){
        return newDisMatrix[Math.max(i, j)][Math.min(i, j)];
    }
    
    private void killNode(int n){
        freeStateOfNode[n]=false;
        newDisMatrix[n]=null;
    }
    private boolean isLiveAt(int n){
        return freeStateOfNode[n];
    }
    
    /**
     * Now numOfNode==2*numOfLeaf-2
     * Two nodes are live: nodes[numOfNode-1] is one of the two, go to find the other
     * then connect the last two nodes
     */
    private void addLastBranch(){
        int i = numOfNode-2;
        while (isLiveAt(i)==false) {
            i--;
        }
        double dij = d(i, numOfNode-1);
        nodes[i].setBranchLength(dij);
        nodes[i].setParentNode(nodes[numOfNode-1]);
        nodes[numOfNode-1].addChildNode(nodes[i]);
        root=nodes[numOfNode-1];
        branches[numOfBranch++]=new TreeBranch(nodes[i],nodes[numOfNode-1],dij);
    }

    /**
     * @return the root
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * @return the nodes
     */
    public TreeNode[] getNodes() {
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(TreeNode[] nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the branches
     */
    public TreeBranch[] getBranches() {
        return branches;
    }

    /**
     * @param branches the branches to set
     */
    public void setBranches(TreeBranch[] branches) {
        this.branches = branches;
    }

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
    }

    /**
     * @return the disMatrix
     */
    public double[][] getDisMatrix() {
        return disMatrix;
    }

    /**
     * @param disMatrix the disMatrix to set
     */
    public void setDisMatrix(double[][] disMatrix) {
        this.disMatrix = disMatrix;
    }

    /**
     * @return the internalNodes
     */
    public TreeNode[] getInternalNodes() {
        return internalNodes;
    }
    
    class TreeBranch{
        public TreeNode leftNode;
        public TreeNode rightNode;
        public double length;
        public TreeBranch(TreeNode leftNode, TreeNode rightNode, double length){
            this.leftNode=leftNode;
            this.rightNode=rightNode;
            this.length=length;
        }
    }
}
