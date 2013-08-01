/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.statistic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author weibo
 */
public class RunsTest {

    private static final int DEF_DIV_SCALE=10;

    private int numOf0=0;
    private int numOf1=0;
    private int numOfRuns=1;
    private int longestRun=0;
    private ArrayList<Integer> lengthOfRuns;
    private int[] seq;



    public RunsTest(){

    }

    public RunsTest(int[] seq){
        this.seq=seq;
        descRun();
    }


    /**
     * calculate the num of 0, the num of 1 and the num of runs
     */
    private void descRun(){
        setLengthOfRuns((ArrayList<Integer>) new ArrayList());
        int pre=getSeq()[0];//previous bit
        int len=0;
        for (int i=0;i<getSeq().length;i++){
            if(getSeq()[i]==1)
                setNumOf1(getNumOf1() + 1);
            else
                setNumOf0(getNumOf0() + 1);
            //num of runs, length of runs
            if(getSeq()[i]!=pre){
                getLengthOfRuns().add(len);
                setNumOfRuns(getNumOfRuns() + 1);
                len=1;
            }
            else
                len++;

            pre=getSeq()[i];
        }
        getLengthOfRuns().add(len);
        //the length of the longest run
        for (Integer l:getLengthOfRuns())
            if(getLongestRun()<l)
                setLongestRun((int) l);
    }


    /**
     * calculate the probability of randomness
     * @param m
     * @param n
     * @param x
     * @return
     */
    public double prob(int m, int n, int x){
        int k=x/2;
        double prob=0;
        if(k<1) prob=0;
        else if(2 * k == x)
            prob=2*C(m-1,k-1).multiply(C(n-1,k-1)).divide(C(m+n,n),DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
        else
            prob=(C(m-1,k-1).multiply(C(n-1,k)).add(C(m-1,k).multiply(C(n-1,k-1)))).divide(C(m+n,n),DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();

        return prob;
    }

    /**
     * calculate cumulative probability
     * @param m
     * @param n
     * @param x
     */
    public double cumuProb(int m, int n, int x){
        double cumuProb=0;
        for(int i=2;i<=x;i++)
            cumuProb+=prob(m,n,i);
        return cumuProb;
    }

    public double cumuProb(){
        return cumuProb(numOf0,numOf1,numOfRuns);
    }

    /**
     * entropy of the seq
     * @param m
     * @param n
     * @param x
     * @param l
     * @return
     */
    public double entropy(int m, int n, int x, int l){
        if(x==1) return 0;
        double p1=(double)m/(m+n);
        double p2=(double)n/(m+n);
        return -(p1*Math.log(p1)/Math.log(2)+p2*Math.log(p2)/Math.log(2))*x/l;
    }

    public double entropy(){
        return entropy(numOf0,numOf1,numOfRuns, getLongestRun());
    }

    public int[] randomWalk(){
        int h=0;
        int[] rw=new int[seq.length];
        for(int i=0;i<seq.length;i++){
            //System.out.print((i+1)+"\t");
            h+=seq[i];
            //System.out.println(h);
            rw[i]=h;
        }
        return rw;
    }


    /**
     * calculate permutation
     * @param n
     * @param r
     * @return
     */
    public BigInteger P(int n, int r){
        BigInteger p=new BigInteger("1");
        for(int i=n-r+1;i<=n;i++)
            p=p.multiply(new BigInteger(String.valueOf(i)));

        return p;
    }

    /**
     * calculate combination
     * @param n: number of total elements
     * @param r: number of taken elements
     * @return
     */
    public BigDecimal C(int n, int r){
        BigDecimal p=new BigDecimal(P(n,r).toString());
        BigDecimal f=new BigDecimal(P(r,r).toString());
        return p.divide(f,DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP);
    }


    /**
     * @return the numOf0
     */
    public int getNumOf0() {
        return numOf0;
    }

    /**
     * @param numOf0 the numOf0 to set
     */
    public void setNumOf0(int numOf0) {
        this.numOf0 = numOf0;
    }

    /**
     * @return the numOf1
     */
    public int getNumOf1() {
        return numOf1;
    }

    /**
     * @param numOf1 the numOf1 to set
     */
    public void setNumOf1(int numOf1) {
        this.numOf1 = numOf1;
    }

    /**
     * @return the numOfRuns
     */
    public int getNumOfRuns() {
        return numOfRuns;
    }

    /**
     * @param numOfRuns the numOfRuns to set
     */
    public void setNumOfRuns(int numOfRuns) {
        this.numOfRuns = numOfRuns;
    }

    /**
     * @return the seq
     */
    public int[] getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int[] seq) {
        this.seq = seq;
        descRun();
    }

    /**
     * @return the longestRun
     */
    public int getLongestRun() {
        return longestRun;
    }

    /**
     * @param longestRun the longestRun to set
     */
    public void setLongestRun(int longestRun) {
        this.longestRun = longestRun;
    }

    /**
     * @return the lengthOfRuns
     */
    public ArrayList<Integer> getLengthOfRuns() {
        return lengthOfRuns;
    }

    /**
     * @param lengthOfRuns the lengthOfRuns to set
     */
    public void setLengthOfRuns(ArrayList<Integer> lengthOfRuns) {
        this.lengthOfRuns = lengthOfRuns;
    }


}