/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author wb
 */
public class TreeIO {
    
    public static final int NEWICK=0;
    
    public Tree readTree(String treeString){
        return readTree(treeString, NEWICK);
    }
    
    public Tree readTree(String treeString, int format){
        switch(format){
            case 0: 
                return new Newick().toTree(treeString);
            default:
                return null;
        }
    }
    
    public Tree readTree(File treeFile) throws FileNotFoundException, IOException{
        return readTree(treeFile, NEWICK);
    }
    
    public Tree readTree(File treeFile, int format) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(treeFile));
        String tree = br.readLine();
        br.close();
        return readTree(tree, format);
    }
    
    public String writeTree(Tree tree){
        return writeTree(tree, NEWICK);
    }
    public String writeTree(Tree tree, int format){
        switch(format){
            case 0:
                return new Newick().toString(tree);
            default:
                return null;
        }
    }
    public void writeTree(Tree tree, File treeFile) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter(treeFile));
        bw.write(writeTree(tree));
        bw.close();
    }
    
    public void drawTree(Tree tree){
        
    }
    

}
