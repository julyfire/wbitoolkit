/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wb
 */
public abstract class Tree2D {
    protected Tree tree;
    protected int padding=50;
    protected int labelSpace;
    protected int rulerSpace=50;
    protected boolean showLabel=true;
    protected boolean showBranchLength=false;
    protected boolean showProb=true;
    protected int height;
    protected int width;
    protected double xScalar;
    protected double yScalar;
    protected double ruler;
    protected double rootLength=0;
    protected Graphics2D g2;
    protected TreeNode2D[] node2ds;
    
    
    public Tree2D(){
        
    }
    public Tree2D(Tree tree, int height, int width, Graphics2D g2){
        this.tree=tree;
        this.height=height;
        this.width=width;
        this.g2=g2;
    }
    
    public abstract void layout();
    
    public abstract void draw();
    
    public int widthOfString(String s){
        return g2.getFontMetrics().stringWidth(s);
    }
    
    public int heightOfString(String s){
        return g2.getFontMetrics().getHeight();
    }
    
    public int maxLabelWidth(){
        String[] labels=tree.getLabels();
        String max="";
        for(String label:labels){
            if(max.length()<label.length())
                max=label;
        }
        return widthOfString(max);
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
     * @return the padding
     */
    public int getPadding() {
        return padding;
    }

    /**
     * @param padding the padding to set
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    /**
     * @return the labelSpace
     */
    public int getLabelSpace() {
        return labelSpace;
    }

    /**
     * @param labelSpace the labelSpace to set
     */
    public void setLabelSpace(int labelSpace) {
        this.labelSpace = labelSpace;
    }

    /**
     * @return the rulerSpace
     */
    public int getRulerSpace() {
        return rulerSpace;
    }

    /**
     * @param rulerSpace the rulerSpace to set
     */
    public void setRulerSpace(int rulerSpace) {
        this.rulerSpace = rulerSpace;
    }

    /**
     * @return the showLabel
     */
    public boolean isShowLabel() {
        return showLabel;
    }

    /**
     * @param showLabel the showLabel to set
     */
    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    /**
     * @return the showBranchLength
     */
    public boolean isShowBranchLength() {
        return showBranchLength;
    }

    /**
     * @param showBranchLength the showBranchLength to set
     */
    public void setShowBranchLength(boolean showBranchLength) {
        this.showBranchLength = showBranchLength;
    }

    /**
     * @return the showProb
     */
    public boolean isShowProb() {
        return showProb;
    }

    /**
     * @param showProb the showProb to set
     */
    public void setShowProb(boolean showProb) {
        this.showProb = showProb;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the ruler
     */
    public double getRuler() {
        return ruler;
    }

    /**
     * @param ruler the ruler to set
     */
    public void setRuler(double ruler) {
        this.ruler = ruler;
    }

    /**
     * @return the rootLength
     */
    public double getRootLength() {
        return rootLength;
    }

    /**
     * @param rootLength the rootLength to set
     */
    public void setRootLength(double rootLength) {
        this.rootLength = rootLength;
    }

    /**
     * @return the g2
     */
    public Graphics2D getG2() {
        return g2;
    }

    /**
     * @param g2 the g2 to set
     */
    public void setG2(Graphics2D g2) {
        this.g2 = g2;
    }

    /**
     * @return the node2ds
     */
    public TreeNode2D[] getNode2ds() {
        return node2ds;
    }

    /**
     * @param node2ds the node2ds to set
     */
    public void setNode2ds(TreeNode2D[] node2ds) {
        this.node2ds = node2ds;
    }

}
