/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.statistic;

import java.util.Random;
import wbitoolkit.tools.UsefulMethods;

/**
 *
 * @author wb
 */
public abstract class Bootstrap {
    
    private Object[] data;
    private int times;
    private double[] sp;
    private Object[] observedValue;
    private String progress;
    private boolean stop=false;
    Random r=new Random(System.currentTimeMillis()); 
    
    public enum H1{NOT_EQUAL, GREATER_THAN, LESS_THAN};
    
    public void test(H1 h1){
        if(observedValue==null){
            progress="building tree";
            observedValue=calculateStatistic(getData());
        }
        progress="start bootstrap";
        setSp(new double[observedValue.length]);
        for(int i=0;i<getSp().length;i++) getSp()[i]=0;
        for(int i=0;i<getTimes();i++){
            if(stop) {
                System.out.println("\nbootstrap stoped!");
                return;
            }
            progress="bootstrap "+(i+1)+" of "+times;
            Object[] sample=resampling();
            Object[] bsValue=calculateStatistic(sample);
//            System.out.println("#"+i);
            for(int j=0;j<getSp().length;j++){
                for(int k=0;k<getSp().length;k++){
                    if(compareWithObservation(observedValue[j],bsValue[k],h1)){
                        getSp()[j]+=1;
                        break;
                    }                       
                }
                
            }
            int p=(int)((i+1)*100.0/times);
            
            System.out.print(p+"%"+UsefulMethods.backspace(p+"%"));
        }
        
        for(int i=0;i<getSp().length;i++){
            getSp()[i]/=getTimes();
        }
    }
    
    
    
    private Object[] resampling(){
        return resampling(getData().length);
    }
    private Object[] resampling(int size){        
        Object[] ns=new Object[size];
        for(int i=0;i<size;i++){
            ns[i]=getData()[r.nextInt(size)];
        }
        return ns;
    } 
    
    public abstract Object[] calculateStatistic(Object[] sample);
    
    public abstract boolean compareWithObservation(Object os, Object bs, H1 h1); 
    
    /**
     * @return the data
     */
    public Object[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object[] data) {
        this.data = data;
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
     * @return the sp
     */
    public double[] getSp() {
        return sp;
    }

    /**
     * @param sp the sp to set
     */
    public void setSp(double[] sp) {
        this.sp = sp;
    }
    
     /**
     * @return the observedValue
     */
    public Object[] getObservedValue() {
        return observedValue;
    }

    /**
     * @param observedValue the observedValue to set
     */
    public void setObservedValue(Object[] observedValue) {
        this.observedValue = observedValue;
    }
    
    /**
     * @return the progress
     */
    public String getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(String progress) {
        this.progress = progress;
    }
    
    /**
     * @return the stop
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
