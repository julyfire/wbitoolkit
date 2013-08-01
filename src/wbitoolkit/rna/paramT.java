package wbitoolkit.rna;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wb
 */
public class paramT {
    int id;
    int[][] stack=new int[EP.NBPAIRS+1][EP.NBPAIRS+1];
    int[] hairpin=new int[31];
    int[] bulge=new int[EP.MAXLOOP+1];
    int[] internal_loop=new int[EP.MAXLOOP+1];
    int[][][] mismatchI=new int[EP.NBPAIRS+1][5][5];
    int[][][] mismatchH=new int[EP.NBPAIRS+1][5][5];
    int[][][] mismatchM=new int[EP.NBPAIRS+1][5][5];
    int[][] dangle5=new int[EP.NBPAIRS+1][5];
    int[][] dangle3=new int[EP.NBPAIRS+1][5];
    int[][][][] int11=new int[EP.NBPAIRS+1][EP.NBPAIRS+1][5][5];
    int[][][][][] int21=new int[EP.NBPAIRS+1][EP.NBPAIRS+1][5][5][5];
    int[][][][][][] int22=new int[EP.NBPAIRS+1][EP.NBPAIRS+1][5][5][5][5];
    int[] F_ninio=new int[5];
    double lxc;
    int MLbase;
    int[] MLintern=new int[EP.NBPAIRS+1];
    int MLclosing;
    int TerminalAU;
    int DuplexInit;
    int[] TETRA_ENERGY=new int[200];
    String Tetraloops="";
    int[] Triloop_E=new int[40];
    String Triloops="";
    double temperature=37.0;
}
