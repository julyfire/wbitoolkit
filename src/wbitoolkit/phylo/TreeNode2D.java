/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author wb
 */
public class TreeNode2D{
    private TreeNode model;
    private Point pos;
    
    public TreeNode2D(TreeNode model){
        this.model=model;
    }

    public int getIndex(){
        return model.getId();
    }
    
    public int getParentIndex(){
        return model.getParentNode().getId();
    }
    
    /**
     * @return the model
     */
    public TreeNode getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(TreeNode model) {
        this.model = model;
    }

    /**
     * @return the pos
     */
    public Point getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(Point pos) {
        this.pos = pos;
    }

  
}
