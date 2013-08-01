/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wb
 */
public class RectangularTreeAnnotation extends TreeAnnotation{

    public RectangularTreeAnnotation(Tree2D tree, TreeNode node, Color color, AnnotationType type){
        super(tree,node,color,type);
    }
    
    @Override
    public void annotateNode() {
        int x=getTree().node2ds[getIndex()].getPos().x;
        int y=getTree().node2ds[getIndex()].getPos().y;
        getTree().g2.setPaint(getColor());
        getTree().g2.drawOval(x-5, y-5, 10, 10);
    }

    @Override
    public void annotateBranch() {
        getTree().g2.setColor(getColor());
        getTree().g2.setStroke(new BasicStroke(2.0f));
        ((RectangularTree2D)getTree()).drawBranch(getTree().node2ds[getIndex()]);
    }

    @Override
    public void annotateCluster() {
        getTree().g2.setColor(getColor());
        getTree().g2.setStroke(new BasicStroke(2.0f));
        List<TreeNode2D> ans=new ArrayList<TreeNode2D>();
        ans.add(nextNode(ans, getNode()));
        for(TreeNode2D an:ans){
            ((RectangularTree2D)getTree()).drawBranch(an);
        }
    }
    
    private TreeNode2D nextNode(List ans, TreeNode n){
        TreeNode2D an=new TreeNode2D(n);
        an.setPos(getTree().node2ds[an.getIndex()].getPos());
        for(TreeNode cn:n.getChildNodes()){
            ans.add(nextNode(ans,cn));
        }
        return an;
    }

    @Override
    public void annotateGroup() {
        getTree().g2.setColor(getColor());
        getTree().g2.setStroke(new BasicStroke(2.0f));
        List<TreeNode> leaves=getNode().getLeafNodes();
        int x1=getTree().width-getTree().padding-10;
        int x2=x1+10;
        int y1=(int)(getTree().node2ds[leaves.get(0).getId()-1].getPos().y-getTree().yScalar*0.25);
        int y2=(int)(getTree().node2ds[leaves.get(leaves.size()-1).getId()-1].getPos().y+getTree().yScalar*0.25);
        getTree().g2.drawLine(x1, y1, x2, y1);
        getTree().g2.drawLine(x2, y1, x2, y2);
        getTree().g2.drawLine(x2, y2, x1, y2);       
    }

    @Override
    public void annotateLabel() {
        getTree().g2.setColor(getColor());
        ((RectangularTree2D)getTree()).drawLabel(new TreeNode2D(getNode()));
    }

    
    
    
}
