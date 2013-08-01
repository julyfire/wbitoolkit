/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.awt.Color;

/**
 *
 * @author wb
 */
public abstract class TreeAnnotation{

    

    public static enum AnnotationType{NODE,BRANCH,CLUSTER,GROUP,LABEL};
    private Tree2D tree;
    private int index;
    private TreeNode node;
    private Color color;
    private AnnotationType type;
    
    public TreeAnnotation(){}
    
    public TreeAnnotation(Tree2D tree){
        this.tree=tree;
    }
    
    public TreeAnnotation(Tree2D tree, TreeNode node, Color color, AnnotationType type){
        this.tree=tree;
        this.node=node;
        this.index=node.getId();
        this.color=color;
        this.type=type;
    }
    
    public void drawAnnotation(){
        switch(getType()){
            case NODE: {
                annotateNode();
                break;
            }
            case BRANCH: {
                annotateBranch();
                break;
            }
            case CLUSTER: {
                annotateCluster();
                break;
            }
            case GROUP: {
                annotateGroup();
                break;
            }
            case LABEL: {
                annotateLabel();
                break;
            }
            default:
                return;
        }
    }
    
    public abstract void annotateNode();
    
    public abstract void annotateBranch();
    
    public abstract void annotateCluster();
    
    public abstract void annotateGroup();
    
    public abstract void annotateLabel();
   
    /**
     * @return the tree
     */
    public Tree2D getTree() {
        return tree;
    }

    /**
     * @param tree the tree to set
     */
    public void setTree(Tree2D tree) {
        this.tree = tree;
    }

    /**
     * @return the node
     */
    public TreeNode getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(TreeNode node) {
        this.node = node;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the type
     */
    public AnnotationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AnnotationType type) {
        this.type = type;
    }
    
    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
