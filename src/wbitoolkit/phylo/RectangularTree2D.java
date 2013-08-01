/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import wbitoolkit.statistic.Description;

/**
 *
 * @author wb
 */
public class RectangularTree2D extends Tree2D{
    
    public RectangularTree2D(Tree tree, int width, int height, Graphics2D g2){
        super(tree,height,width,g2);
    }
    
    @Override
    public void layout(){
        init();
        fixNode(tree.getRootNode(), 0, rootLength);
    }
    
    @Override
    public void draw() {
        if(node2ds==null){
            layout();
        }      
        drawBranches();
        if(showLabel) drawLabels();
        if(showBranchLength) drawBranchLength();
        if(showProb) drawProb();
        drawRuler();
    }
    
    private void init(){
        labelSpace=maxLabelWidth()+30;
        xScalar=((double)width-2*padding-labelSpace)/tree.getHeight();
        yScalar=((double)height-2*padding-rulerSpace)/tree.getLeafNum();
        ruler=0.1*tree.getHeight();
        rootLength=0;
        node2ds=new TreeNode2D[tree.getNodeNum()];
    }
   
    private int fixNode(TreeNode node, int numOfDrawnLeaves, double depth){
        TreeNode2D n2d=new TreeNode2D(node);
        if(node.isLeaf()){
            int x=(int)(padding+depth*xScalar);
            int y=(int)(padding+(numOfDrawnLeaves+0.5)*yScalar);
            n2d.setPos(new Point(x,y));
            node2ds[n2d.getIndex()]=n2d;
            return y;
        }
        else{
            int x=(int)(padding+depth*xScalar);
            double[] ys=new double[node.numOfChildNodes()];
            int i=0;
            for(TreeNode child:node.getChildNodes()){
                double newDepth=depth+child.getBranchLength();
                ys[i++]=fixNode(child, numOfDrawnLeaves, newDepth);
                numOfDrawnLeaves+=child.numOfLeaves();
            } 
            int y=(int)Description.mean(ys);
            n2d.setPos(new Point(x,y));
            node2ds[n2d.getIndex()]=n2d;
            return y;
        }
    }
    
    private void drawBranches(){
        g2.setPaint(Color.BLACK);
        int x1,y1,x2,y2;
        x1=node2ds[0].getPos().x;
        y1=node2ds[0].getPos().y;
        x2=padding;
        y2=y1;
        g2.drawLine(x1, y1, x2, y2);
        for(int i=1;i<node2ds.length;i++){
            drawBranch(node2ds[i]);
        }
    }
    
    public void drawBranch(TreeNode2D node){
        int x1=node.getPos().x;
        int y1=node.getPos().y;
        int p=node.getParentIndex();
        int x2=node2ds[p].getPos().x;
        int y2=node2ds[p].getPos().y;
        g2.drawLine(x1, y1, x2, y1);
        g2.drawLine(x2, y1, x2, y2);
    }
    
    private void drawLabels(){
        g2.setPaint(Color.BLACK);
        List<TreeNode> leaves=tree.getLeafNodes();
        for(TreeNode leaf:leaves){
            drawLabel(new TreeNode2D(leaf));
        }
    }
    
    public void drawLabel(TreeNode2D leaf){
        if(leaf.getModel().getName()==null) return;
        int x=node2ds[leaf.getModel().getId()].getPos().x+10;
        int y=node2ds[leaf.getModel().getId()].getPos().y+heightOfString(leaf.getModel().getName())/2;
        g2.drawString(leaf.getModel().getName(), x, y);
    }
    
    private void drawBranchLength(){   
        g2.setPaint(Color.BLACK);
        for(TreeNode2D n:node2ds){
            Double bl=Description.trimDecimalTo(n.getModel().getBranchLength(),5);
            if(bl==0) continue;
            String bs=bl.toString();
            int x1=n.getPos().x;
            int y1=n.getPos().y;
            int p=n.getParentIndex();
            int x2=node2ds[p].getPos().x;      
            int x=(x1+x2)/2-widthOfString(bs)/2;
            int y=y1-2;
            g2.drawString(bs, x, y);
        }
    }
    
    private void drawProb(){
        g2.setPaint(Color.BLACK);
        for(TreeNode2D n:node2ds){
            Double bl=Description.trimDecimalTo(n.getModel().getProb(),2);
            if(bl==0) continue;
            String bs=bl.toString();
            int x=n.getPos().x+2;
            int y=n.getPos().y+heightOfString(bs)/2;
            g2.drawString(bs, x, y);
        }
    }
    
    private void drawRuler(){
        g2.setPaint(Color.BLACK);
        String rs=Description.trimDecimalTo(ruler, 3)+"";
        int x1=padding;
        int x2=(int)(padding+ruler*xScalar);
        int y=(int)(height-rulerSpace*0.4);
        g2.drawLine(x1, y, x2, y);
        int x=(x1+x2)/2-widthOfString(rs)/2;
        g2.drawString(rs,x,y-2);       
    }

    
}
    
