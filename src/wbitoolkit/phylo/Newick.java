/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wb
 */
public class Newick {
    private static final String sep="__";
    private int id=0;
    
    public Tree toTree(String s){
        s=formatTreeString(s); 
        TreeNode root=new TreeNode(id);
        root.setLevel(1);
        TreeNode node=buildNode(s, root, 0, s.lastIndexOf(':'));
        return new Tree(node);
        
    }
    public String toString(Tree t){
        TreeNode n=t.getRootNode();
        StringBuilder sb=new StringBuilder();
        toNewick(n,sb);
        return sb.toString();
    }
    
    private TreeNode buildNode(String s, TreeNode parent, int from, int to){
        if (s.charAt(from) != '(') {
            parent.setName(s.substring(from, to));
            return parent;
        }
 
        int b = 0;
        int colon = 0;
        int x = from;
 
        for (int i = from; i < to; i++) {
            char c = s.charAt(i);
 
            if (c == '(')
                b++;
            else if (c == ')')
                b--;
            else if (c == ':') {
                colon = i;
            }

            if ((b != 0) && ((b != 1) || (c != ','))) continue;
            
            String[] v= s.substring(colon + 1, i).split(sep);
            double prob=0,branch=0;
            if(v.length==2){
                prob=Double.parseDouble(v[0]);
                branch=Double.parseDouble(v[1]);
            }
            else{
                branch=Double.parseDouble(v[0]);
            }

            int level=parent.getLevel()+1;
            
            TreeNode child=new TreeNode(++id);
            child.setLevel(level);
            child.setParentNode(parent);
            child.setBranchLength(branch);
            child.setProb(prob);
                
            parent.addChildNode(buildNode(s, child, x + 1, colon));

            x = i;
        }
 
        return parent;
    }
    
    private void toNewick(TreeNode n, StringBuilder sb){
        if (n.isLeaf()) {
            sb.append(n.getName());
            sb.append(":");
            sb.append(n.getBranchLength());
        } 
        else {
            sb.append("(");
            toNewick(n.getChildNode(0), sb);
            for (int i = 1; i < n.numOfChildNodes(); i++) {
                sb.append(",");
                toNewick(n.getChildNode(i), sb);
            }
            sb.append(")");
            if(n.getParentNode()==null){
                sb.append(";");
                return;
            }
            sb.append(n.getProb());
            sb.append(":");
            sb.append(n.getBranchLength());
        }
    }
    
    private String formatTreeString(String s){
        s=s.replaceAll("\\):", ")0.9999:");
        String[] ss=s.split("\\:");
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<ss.length;i++){
            String[] lb=ss[i].split("\\)");
            if(lb.length==2)
                sb.append(lb[0]).append("):").append(lb[1]).append(sep);
            else
                sb.append(ss[i]).append(":");
        }
        return sb.toString().replaceAll(";:", ":0");
    }
}
