/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.statistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author wb
 */
public abstract class Permutation {
    
    private Object[] data1;
    private Object[] data2;
    private int times;
    private Double observedValue;
    private double pValue;
  
    public enum H1{NOT_EQUAL, GREATER_THAN, LESS_THAN};
    
    public void test(H1 h1){
        if(getObservedValue()==null){
            setObservedValue((Double) calculateStatistic(getData1(), getData2()));
        }
        
        //debug:        
//        System.out.println(observedValue);
//        System.out.println(h1);
        
        int num=0;
        for(int i=0;i<getTimes();i++){
            Object[][] sample=resampling();
            double bsValue=calculateStatistic(sample[0],sample[1]);
            
            //debug
//            System.out.println(bsValue);
            
            switch(h1){
                case NOT_EQUAL:
                    if(bsValue!=getObservedValue()) num++; break;
                case GREATER_THAN:
                    if(bsValue>=getObservedValue()) num++; break;
                case LESS_THAN:
                    if(bsValue<=getObservedValue()) num++; break;
            } 
        }
        this.pValue=(num+1.0) / (getTimes()+1);
    }

    private Object[][] resampling(){        
        int n=getData1().length;
        int m=getData2().length;
        ArrayList sample=new ArrayList();
        for(Object d:getData1())
            sample.add(d);
        for(Object d:getData2())
            sample.add(d);
        Random r=new Random(System.currentTimeMillis());
        Collections.shuffle(sample, r);
        
        Object[] nd1=new Object[n];
        Object[] nd2=new Object[m];
        for(int i=0;i<n;i++)
            nd1[i]=sample.get(i);
        for(int i=0;i<m;i++)
            nd2[i]=sample.get(n+i);
        
        return new Object[][]{nd1,nd2};
    }
    
    public abstract double calculateStatistic(Object[] sample1, Object[] sample2);
    
     /**
     * @return the data1
     */
    public Object[] getData1() {
        return data1;
    }

    /**
     * @param data1 the data1 to set
     */
    public void setData1(Object[] data1) {
        this.data1 = data1;
    }

    /**
     * @return the data2
     */
    public Object[] getData2() {
        return data2;
    }

    /**
     * @param data2 the data2 to set
     */
    public void setData2(Object[] data2) {
        this.data2 = data2;
    }

    /**
     * @return the times
     */
    public int getTimes() {
        return times;
    }

    /**
     * @param times the times to set
     */
    public void setTimes(int times) {
        this.times = times;
    }

    /**
     * @return the observedValue
     */
    public Double getObservedValue() {
        return observedValue;
    }

    /**
     * @param observedValue the observedValue to set
     */
    public void setObservedValue(Double observedValue) {
        this.observedValue = observedValue;
    }

    /**
     * @return the p-value
     */
    public double getPValue() {
        return pValue;
    }

}
