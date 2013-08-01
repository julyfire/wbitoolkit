/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wb
 */
public class UsefulMethods {
    
    public static void sortList(List list){
        Collections.sort(list, new Comparator<Double>() {
            public int compare(Double o1, Double o2) {
                double s=o1-o2;//ascend
                if(s>0) return 1;          //o1>o2
                else if(s<0) return -1;    //o1<o2
                else return 0;             //o1=o2
            }
        });
    }
    
    public static List<Map.Entry> sortMap(HashMap map, final boolean byKey, final boolean ascend){
        List<Map.Entry> infoIds =new ArrayList<Map.Entry>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry>() {
            public int compare(Map.Entry o1, Map.Entry o2) {
                double v1,v2;
                if(byKey){
                    v1=Double.valueOf(o1.getKey().toString());
                    v2=Double.valueOf(o2.getKey().toString());
                }
                else{
                    v1=Double.valueOf(o1.getValue().toString());
                    v2=Double.valueOf(o2.getValue().toString());
                }
                double s=ascend?v1-v2:v2-v1;
                if(s>0) return 1;          //o1>o2
                else if(s<0) return -1;    //o1<o2
                else return 0;             //o1=o2
                //return (int) ((Double)o2.getValue() - (Double)o1.getValue());
                //return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        return infoIds;
    }
    
    public static Integer[] unionOf(Integer[] set1, Integer[] set2){
        HashSet<Integer> union=new HashSet();
        for(Integer e:set1)
            union.add(e);
        for(Integer e:set2)
            union.add(e);
        Integer[] u={};
        return union.toArray(u);
    }
    public static Integer[] minusOf(Integer[] set1, Integer[] set2){
        ArrayList<Integer> minus=new ArrayList();
        Integer[] largeSet=set1;
        Integer[] smallSet=set2;
        if(set1.length<set2.length){
            largeSet=set2;
            smallSet=set1;
        }
        for(Integer e:largeSet){
            if(!minus.contains(e))
                minus.add(e);
        }
        for(Integer e:smallSet){
            if(minus.contains(e))
                minus.remove(e);
        }
        Integer[] m={};
        return minus.toArray(m);
    }
    
    public static int[] toPrimitive(Integer[] a){
        int[] pa=new int[a.length];
        for(int i=0;i<a.length;i++)
            pa[i]=a[i];
        return pa;
    }
    
    public static String getBaseFileName(String filename) { 
        //return filename.replaceAll("[.][^.]+$", "");
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    }
    
    /**
     * make the string the same length
     * @param str
     * @param size
     * @return
     */
    public static String flush(String str, int size){
        String strf=str;
        int num=size-strf.length();
        if(num>0)
            for(int i=0;i<num;i++)
                strf+=" ";
        return strf;
    }

    /**
     * repeat a string
     * @param str
     * @param num
     * @return
     */
    public static String repeat(String str, int num){
        String strs="";
        for(int i=0;i<num;i++)
            strs+=str;
        return strs;
    }

    /**
     * backspace a string
     * @param con
     * @return
     */
    public static String backspace(String con){
        return repeat("\b",con.length());
    }
    /**
     * reserve the first twofigures after the decimal point
     * @param num
     * @return
     */
    public static String decimal(double num){
        String n=String.valueOf(num);
        if(n.length()-n.indexOf(".")-1>2)
            return n.substring(0,n.indexOf(".")+3);
        else return n;
    }
    public static String decimal(double num, int d){
        String n=String.valueOf(num);
        if(n.length()-n.indexOf(".")-1>d)
            return n.substring(0,n.indexOf(".")+d+1);
        else return n;
    }

    public static String getColor(Double[] start, Double[] end, double rate){
        double[] rgb=new double[3];
        String color="#";
        for(int i=0;i<3;i++){
            rgb[i]=rate*(end[i]-start[i])+start[i];
            String c=Integer.toHexString((int)rgb[i]);
            if(c.length()==1)
                c="0"+c;
            color+=c;
        }
        return color;
    }
}
