package wbitoolkit.rna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import wbitoolkit.common.LibPathSet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wb
 */
public class Fold {

    private static String path;
    static{
        path="/home/wb/NetBeansProjects/WBIToolkit/lib";
//        path=LibPathSet.getPackageDir();
        
    }
    
    private int init_length=-1;
    private int uniq_ML=0;
    private static int[] indx;
    private int[] c;
    private int[] cc;
    private int[] cc1;
    private int[] f5;
    private int[] fML;
    private int[] Fmi;
    private int[] fM1;
    private int[] DMLi;
    private int[] DMLi1;
    private int[] DMLi2;
    private char[] ptype;
    private int[] BP;
    private short[] S, S1;
    private static foldVars fv;
    private static paramT P;
    
    private static final double LOCALITY=0;
    private static final int NBASES=8;
    private static final int MAXALPHA=20;
    private static final String Law_and_Order="_ACGUTXKI";
    private static short[] alias=new short[MAXALPHA+1];
    private static int[][] pair=new int[MAXALPHA+1][MAXALPHA+1];
    private static int[] rtype=new int[]{0,2,1,4,3,6,5,7};
    private static int[][] BP_pair=new int[][]{
           /* _  A  C  G  U  X  K  I */
            { 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 5, 0, 0, 5},
            { 0, 0, 0, 1, 0, 0, 0, 0},
            { 0, 0, 2, 0, 3, 0, 0, 0},
            { 0, 6, 0, 4, 0, 0, 0, 6},
            { 0, 0, 0, 0, 0, 0, 2, 0},
            { 0, 0, 0, 0, 0, 1, 0, 0},
            { 0, 6, 0, 0, 5, 0, 0, 0}
        };
    private static String nonstandards="";

    public static void loadEnergyParameters(){
        EP.int11_H=loadE11h();
        EP.int11_37=loadE11_37();
        EP.int21_H=loadE21h();
        EP.int21_37=loadE21_37();
        EP.int22_H=loadE22h();
        EP.int22_37=loadE22_37();
    }
    
    public static void update_fold_params(int n){
        P=scale_parameters(n);
        make_pair_matrix();
    }
    
    
    public void initialize_fold(int length){
        int n=length;
        setInitLength(n);
//        indx=new int[n+1];
        c=new int[n*(n+1)/2+2];
        fML=new int[n*(n+1)/2+2];
        f5=new int[n+2];
        cc=new int[n+2];
        cc1=new int[n+2];
        Fmi=new int[n+1];
        DMLi=new int[n+1];
        DMLi1=new int[n+1];
        DMLi2=new int[n+1];
        ptype=new char[n*(n+1)/2+2];
//        fv=new foldVars();
//        fv.base_pair=new bondT[1+n/2];
//        for(int i=1;i<=n;i++)
//            indx[i]=i*(i-1)/2;
//        if(P==null){
//            update_fold_params();
//        }
    }
    
    public void reset_fold(){
        for(int i=0;i<c.length;i++) {
            c[i]=0;
            fML[i]=0;
            ptype[i]=0;
        }
        for(int i=0;i<f5.length;i++) {
            f5[i]=0;
            cc[i]=0;
            cc1[i]=0;
        }
        for(int i=0;i<Fmi.length;i++) {
            Fmi[i]=0;
            DMLi[i]=0;
            DMLi1[i]=0;
            DMLi2[i]=0;
        }
    }
    
    
    
    public float fold(String sequence){
        return fold(sequence,null);
    }
    public float fold(String string, String structure){
        int length, energy;
        length=string.length();
//        if(length>getInitLength()) initialize_fold(length);
//        if(Math.abs(P.temperature-fv.temperature)>1e-6) update_fold_params();
        encode_seq(string);
        BP=new int[length+2];
        make_ptypes(S, structure);
        energy=fill_arrays(string);
        return (float)(energy/100.0);
    }
    
   
    
    private static paramT scale_parameters(int length){
        indx=new int[length+1];
        fv=new foldVars();
        fv.base_pair=new bondT[1+length/2];
        for(int i=1;i<=length;i++)
            indx[i]=i*(i-1)/2;
        
        paramT p=new paramT();
        int i,j,k,l;
        double tempf=(fv.temperature+EP.K0)/EP.Tmeasure;
        for(i=0;i<31;i++)
            p.hairpin[i]=(int)(EP.hairpin37[i]*(tempf));
        for(i=0;i<=Math.min(30, EP.MAXLOOP);i++){
            p.bulge[i]=(int)(EP.bulge37[i]*tempf);
            p.internal_loop[i]=(int)(EP.internal_loop37[i]*tempf);
        }
        p.lxc = EP.lxc37*tempf;
        for (; i<=EP.MAXLOOP; i++) {
            p.bulge[i] = p.bulge[30]+(int)(p.lxc*Math.log((double)(i)/30.));
            p.internal_loop[i] = p.internal_loop[30]+(int)(p.lxc*Math.log((double)(i)/30.));
        }
        for (i=0; i<5; i++)
            p.F_ninio[i] = (int) (EP.F_ninio37[i]*tempf);
   
        for (i=0; (i*7)<EP.Tetraloops.length(); i++) 
            p.TETRA_ENERGY[i] = (int)(EP.TETRA_ENTH37 - (EP.TETRA_ENTH37-EP.TETRA_ENERGY37[i])*tempf);
        for (i=0; (i*5)<EP.Triloops.length(); i++) 
            p.Triloop_E[i] =  EP.Triloop_E37[i];
   
        p.MLbase = (int)(EP.ML_BASE37*tempf);
        for (i=0; i<=EP.NBPAIRS; i++) { /* includes AU penalty */
            p.MLintern[i] = (int)(EP.ML_intern37*tempf);
            p.MLintern[i] +=  (i>2)?EP.TerminalAU:0;
        }
        p.MLclosing = (int)(EP.ML_closing37*tempf);

        p.TerminalAU = EP.TerminalAU;
  
        p.DuplexInit = (int)(EP.DuplexInit*tempf);

        /* stacks    G(T) = H - [H - G(T0)]*T/T0 */
        for (i=0; i<=EP.NBPAIRS; i++)
            for (j=0; j<=EP.NBPAIRS; j++)
                p.stack[i][j] =(int) (EP.enthalpies[i][j] -(EP.enthalpies[i][j] - EP.stack37[i][j])*tempf);

        /* mismatches */
        for (i=0; i<=EP.NBPAIRS; i++)
            for (j=0; j<5; j++)
                for (k=0; k<5; k++) {
                    p.mismatchI[i][j][k] =(int)( EP.mism_H[i][j][k] -
                        (EP.mism_H[i][j][k] - EP.mismatchI37[i][j][k])*tempf);
                    p.mismatchH[i][j][k] = (int)(EP.mism_H[i][j][k] -
                        (EP.mism_H[i][j][k] - EP.mismatchH37[i][j][k])*tempf);
                    p.mismatchM[i][j][k] =(int)(EP.mism_H[i][j][k] -
                        (EP.mism_H[i][j][k] - EP.mismatchM37[i][j][k])*tempf);
        }
   
        /* dangles */
        for (i=0; i<=EP.NBPAIRS; i++)
            for (j=0; j<5; j++) {
                int dd;
                dd = (int)(EP.dangle5_H[i][j] - (EP.dangle5_H[i][j] - EP.dangle5_37[i][j])*tempf); 
                p.dangle5[i][j] = (dd>0) ? 0 : dd;  /* must be <= 0 */
                dd = (int)(EP.dangle3_H[i][j] - (EP.dangle3_H[i][j] - EP.dangle3_37[i][j])*tempf);
                p.dangle3[i][j] = (dd>0) ? 0 : dd;  /* must be <= 0 */
        }
        /* interior 1x1 loops */
        for (i=0; i<=EP.NBPAIRS; i++)
            for (j=0; j<=EP.NBPAIRS; j++)
                for (k=0; k<5; k++)
                    for (l=0; l<5; l++) 
                        p.int11[i][j][k][l] =(int) (EP.int11_H[i][j][k][l] -
                            (EP.int11_H[i][j][k][l] - EP.int11_37[i][j][k][l])*tempf);

        /* interior 2x1 loops */
        for (i=0; i<=EP.NBPAIRS; i++)
            for (j=0; j<=EP.NBPAIRS; j++)
                for (k=0; k<5; k++)
                    for (l=0; l<5; l++) {
                        int m;
                        for (m=0; m<5; m++)
                            p.int21[i][j][k][l][m] = (int)(EP.int21_H[i][j][k][l][m] -
                                (EP.int21_H[i][j][k][l][m] - EP.int21_37[i][j][k][l][m])*tempf);
                    }
        /* interior 2x2 loops */
        for (i=0; i<=EP.NBPAIRS; i++)
            for (j=0; j<=EP.NBPAIRS; j++)
                for (k=0; k<5; k++)
                    for (l=0; l<5; l++) {
                        int m,n;
                        for (m=0; m<5; m++)
                            for (n=0; n<5; n++)	     
                                p.int22[i][j][k][l][m][n] = (int)(EP.int22_H[i][j][k][l][m][n] -
                                    (EP.int22_H[i][j][k][l][m][n]-EP.int22_37[i][j][k][l][m][n])*tempf);
                    }

        p.Tetraloops=EP.Tetraloops;
        p.Triloops= EP.Triloops;

        p.temperature = fv.temperature;
        p.id = 0;
        return p;
    }
    
    private static int encode_char(char c){
        /* return numerical representation of base used e.g. in pair[][] */
        int code;
        
        if (fv.energy_set>0) code = (int) (c-'A')+1;
        else {
            int pos;
            pos = Law_and_Order.indexOf(c);
            if (pos==-1) code=0;
            else code = pos;
            if (code>4) code--; /* make T and U equivalent */
        }
        return code;
    }
    
    private static void make_pair_matrix(){
        int i,j;
        
        
        if (fv.energy_set==0) {
            for (i=0; i<5; i++) alias[i] = (short) i;
            alias[5] = 3; /* X <. G */
            alias[6] = 2; /* K <. C */
            alias[7] = 0; /* I <. default base '@' */
            for (i=0; i<NBASES; i++) {
                for (j=0; j<NBASES; j++) 
                    pair[i][j] = BP_pair[i][j];
            }      
            if (fv.noGU>0) pair[3][4] = pair[4][3] =0;
            if (nonstandards!=null) {  /* allow nonstandard bp's */ 
                for (i=0; i<nonstandards.length(); i+=2) 
                    pair[encode_char(nonstandards.charAt(i))]
                [encode_char(nonstandards.charAt(i+1))]=7;
            }
            for (i=0; i<NBASES; i++) {
                for (j=0; j<NBASES; j++) 
                    rtype[pair[i][j]] = pair[j][i];
            }      
        } else {
            for (i=0; i<=MAXALPHA; i++) {
                for (j=0; j<=MAXALPHA; j++) 
                    pair[i][j] = 0;
            }
            if (fv.energy_set==1) {
                for (i=1; i<MAXALPHA;) {
                    alias[i++] = 3;  /* A <. G */
                    alias[i++] = 2;  /* B <. C */
                }
                for (i=1; i<MAXALPHA; i++) {
                    pair[i][i+1] = 2;    /* AB <. GC */
                    i++;
                    pair[i][i-1] = 1;    /* BA <. CG */
                }
            }
            else if (fv.energy_set==2) {
                for (i=1; i<MAXALPHA;) {
                    alias[i++] = 1;  /* A <. A*/
                    alias[i++] = 4;  /* B <. U */
                }
                for (i=1; i<MAXALPHA; i++) {
                    pair[i][i+1] = 5;    /* AB <. AU */
                    i++;
                    pair[i][i-1] = 6;    /* BA <. UA */
                }
            }
            else if (fv.energy_set==3) {
                for (i=1; i<MAXALPHA-2; ) {
                    alias[i++] = 3;  /* A <. G */
                    alias[i++] = 2;  /* B <. C */
                    alias[i++] = 1;  /* C <. A */
                    alias[i++] = 4;  /* D <. U */
                }
                for (i=1; i<MAXALPHA-2; i++) {
                    pair[i][i+1] = 2;    /* AB <. GC */
                    i++;
                    pair[i][i-1] = 1;    /* BA <. CG */
                    i++;
                    pair[i][i+1] = 5;    /* CD <. AU */
                    i++;
                    pair[i][i-1] = 6;    /* DC <. UA */
                }
            }
            else System.out.println("What energy_set are YOU using??");
            for (i=0; i<=MAXALPHA; i++) {
                for (j=0; j<=MAXALPHA; j++) 
                    rtype[pair[i][j]] = pair[j][i];
            }
        }
    }
    
    private void encode_seq(String sequence){
        int i,l;
        sequence=sequence.toUpperCase();
        l=sequence.length();
        S=new short[l+2];
        S1=new short[l+2];
        S[0]=(short)l;
        for (i=1; i<=l; i++) { /* make numerical encoding of sequence */
            S[i]= (short) encode_char((sequence.charAt(i-1)));
            S1[i] = alias[S[i]];   /* for mismatches of nostandard bases */
        }
        /* for circular folding add first base at position n+1 and last base at
            position 0 in S1	*/
        S[l+1] = S[1]; S1[l+1]=S1[1]; S1[0] = S1[l];
    }
    
    private void make_ptypes(short[] S, String structure){
        int n,i,j,k,l;

        n=S[0];
        for (k=1; k<n-EP.TURN; k++)
            for (l=1; l<=2; l++) {
                int type,ntype=0,otype=0;
                i=k; j = i+EP.TURN+l; if (j>n) continue;
                type = pair[S[i]][S[j]];
                while ((i>=1)&&(j<=n)) {
                    if ((i>1)&&(j<n)) ntype = pair[S[i-1]][S[j+1]];
                    if (fv.noLonelyPairs>0 && (otype>0) && (ntype>0))
                        type = 0; /* i.j can only form isolated pairs */
                    ptype[indx[j]+i] = (char) type;
                    otype =  type;
                    type  = ntype;
                    i--; j++;
                }
            }

        if (fv.fold_constrained>0 &&(structure!=null)) {
            int hx;
            char type;
            int[] stack =new int[n+1];

            for(hx=0, j=1; j<=n; j++) {
                switch (structure.charAt(j-1)) {
                    case '|': BP[j] = -1; break;
                    case 'x': /* can't pair */
                        for (l=1; l<j-EP.TURN; l++) ptype[indx[j]+l] = 0;
                        for (l=j+EP.TURN+1; l<=n; l++) ptype[indx[l]+j] = 0;
                        break;
                    case '(':
                        stack[hx++]=j;
                        /* fallthrough */
                    case '<': /* pairs upstream */
                        for (l=1; l<j-EP.TURN; l++) ptype[indx[j]+l] = 0;
                        break;
                    case ')':
                        if (hx<=0) {
                            System.out.println("unbalanced brackets in constraints");
                        }
                        i = stack[--hx];
                        type = ptype[indx[j]+i];
                        for (k=i+1; k<=n; k++) ptype[indx[k]+i] = 0;
                        /* don't allow pairs i<k<j<l */
                        for (l=j; l<=n; l++)
                            for (k=i+1; k<=j; k++) ptype[indx[l]+k] = 0;
                        /* don't allow pairs k<i<l<j */
                        for (l=i; l<=j; l++)
                            for (k=1; k<=i; k++) ptype[indx[l]+k] = 0;
                        for (k=1; k<j; k++) ptype[indx[j]+k] = 0;
                        ptype[indx[j]+i] = (type==0)?7:type;
                        /* fallthrough */
                    case '>': /* pairs downstream */
                        for (l=j+EP.TURN+1; l<=n; l++) ptype[indx[l]+j] = 0;
                        break;
                }
            }
            if (hx!=0) {
                System.out.println("unbalanced brackets in constraint string");
            }
        }
    }

    private int HairpinE(int size, int type, int si1, int sj1, String string){
        int energy;
        energy=(size<=30)?P.hairpin[size]:P.hairpin[30]+(int)(P.lxc*Math.log(size/30.0));
        if(fv.tetra_loop>0)
            if(size==4){
                String tl=string.substring(0,6);
                int pos=P.Tetraloops.indexOf(tl);
                if(pos>=0)
                    energy+=P.TETRA_ENERGY[pos/7];
            }
        if(size==3){
            String tl=string.substring(0,5);
            int pos=P.Tetraloops.indexOf(tl);
            if(pos>=0)
                energy+=P.Triloop_E[pos/6]; 
            
            if(type>2)
                energy+=P.TerminalAU;
        }
        else
            energy+=P.mismatchH[type][si1][sj1];
        
        return energy;
    }
    
    private int LoopEnergy(int n1, int n2, int type, int type_2, 
                            int si1, int sj1, int sp1, int sq1){
        int nl, ns, energy;
        if(n1>n2) {nl=n1; ns=n2;}
        else {nl=n2; ns=n1;}
        
        if(nl==0)
            return P.stack[type][type_2];
        
        if(ns==0){
            energy=nl<=EP.MAXLOOP?P.bulge[nl]:P.bulge[30]+(int)(P.lxc*Math.log(nl/30.0));
            if(nl==1) energy+=P.stack[type][type_2];
            else{
                if(type>2) energy+=P.TerminalAU;
                if(type_2>2) energy+=P.TerminalAU;
            }
            return energy;
        }
        else{
            if(ns==1){
                if(nl==1)
                    return P.int11[type][type_2][si1][sj1];
                if (nl==2) {                   /* 2x1 loop */
                    if (n1==1)
                        energy = P.int21[type][type_2][si1][sq1][sj1];
                    else
                        energy = P.int21[type_2][type][sq1][si1][sp1];
                    return energy;
                }
            }
            else if (n1==2 && n2==2)         /* 2x2 loop */
                return P.int22[type][type_2][si1][sp1][sq1][sj1];
            { /* generic interior loop (no else here!)*/
                energy = (n1+n2<=EP.MAXLOOP)?(P.internal_loop[n1+n2]):
                    (P.internal_loop[30]+(int)(P.lxc*Math.log((n1+n2)/30.)));

                energy += Math.min(EP.MAX_NINIO, (nl-ns)*P.F_ninio[2]);

                energy += P.mismatchI[type][si1][sj1]+P.mismatchI[type_2][sq1][sp1];
            }
  
            return energy;
        }
            
    }
    
     private int fill_arrays(String string){
        /* fill "c", "fML" and "f5" arrays and return  optimal energy */
        int   i, j, k, length, energy;
        int   decomp, new_fML, max_separation;
        int   type, type_2, tt;
        boolean no_close;
        int   bonus=0;
        
        length=string.length();
        max_separation = (int) ((1.-LOCALITY)*(double)(length-2)); /* not in use */
        
        for (j=1; j<=length; j++) {
            Fmi[j]=DMLi[j]=DMLi1[j]=DMLi2[j]=EP.INF;
        }
        
        for (j = 1; j<=length; j++)
            for (i=(j>EP.TURN?(j-EP.TURN):1); i<j; i++) {
                c[indx[j]+i] = fML[indx[j]+i] = EP.INF;
                if (uniq_ML>0) fM1[indx[j]+i] = EP.INF;
            }
        
        for (i = length-EP.TURN-1; i >= 1; i--) { /* i,j in [1..length] */

            for (j = i+EP.TURN+1; j <= length; j++) {
                int p, q, ij;
                ij = indx[j]+i;
                bonus = 0;
                type = ptype[ij];

                /* enforcing structure constraints */
                if ((BP[i]==j)||(BP[i]==-1)||(BP[i]==-2)) bonus -= EP.BONUS;
                if ((BP[j]==-1)||(BP[j]==-3)) bonus -= EP.BONUS;
                if ((BP[i]==-4)||(BP[j]==-4)) type=0;

                no_close = (((type==3)||(type==4))&& fv.no_closingGU>0 &&(bonus==0));

                if (j-i-1 > max_separation) type = 0;  /* forces locality degree */

                if (type>0) {   /* we have a pair */
                    int new_c=0, stackEnergy=EP.INF;
                    /* hairpin ----------------------------------------------*/

                    if (no_close) new_c = EP.FORBIDDEN;
                    else
                        new_c = HairpinE(j-i-1, type, S1[i+1], S1[j-1], string.substring(i-1));

                    /*--------------------------------------------------------
                    check for elementary structures involving more than one
                    closing pair.
                    --------------------------------------------------------*/

                    for (p = i+1; p <= Math.min(j-2-EP.TURN,i+EP.MAXLOOP+1) ; p++) {
                        int minq = j-i+p-EP.MAXLOOP-2;
                        if (minq<p+1+EP.TURN) minq = p+1+EP.TURN;
                        for (q = minq; q < j; q++) {
                            type_2 = ptype[indx[q]+p];

                            if (type_2==0) continue;
                            type_2 = rtype[type_2];

                	    if (fv.no_closingGU>0)
                                if (no_close||(type_2==3)||(type_2==4))
                                    if ((p>i+1)||(q<j-1)) continue;  /* continue unless stack */
                            energy = LoopEnergy(p-i-1, j-q-1, type, type_2,
				S1[i+1], S1[j-1], S1[p-1], S1[q+1]);

                            /* duplicated code is faster than function call */

                            new_c = Math.min(energy+c[indx[q]+p], new_c);
                            if ((p==i+1)&&(j==q+1)) stackEnergy = energy; /* remember stack energy */

                        } /* end q-loop */
                    } /* end p-loop */

                    /* multi-loop decomposition ------------------------*/


                    if (!no_close) {
                        int MLenergy;
                        decomp = DMLi1[j-1];
                        if (fv.dangles>0) {
                            int d3=0, d5=0;
                            tt = rtype[type];
                            d3 = P.dangle3[tt][S1[i+1]];
                            d5 = P.dangle5[tt][S1[j-1]];
                            if (fv.dangles==2) /* double dangles */
                                decomp += d5 + d3;
                            else {          /* normal dangles */
                                decomp = Math.min(DMLi2[j-1]+d3+P.MLbase, decomp);
                                decomp = Math.min(DMLi1[j-2]+d5+P.MLbase, decomp);
                                decomp = Math.min(DMLi2[j-2]+d5+d3+2*P.MLbase, decomp);
                            }
                        }

                        MLenergy = P.MLclosing+P.MLintern[type]+decomp;

                        new_c = MLenergy < new_c ? MLenergy : new_c;
                    }

                    /* coaxial stacking of (i.j) with (i+1.k) or (k+1.j-1) */

                    if (fv.dangles==3) {
                        decomp = EP.INF;
                        for (k = i+2+EP.TURN; k < j-2-EP.TURN; k++) {
                            type_2 = ptype[indx[k]+i+1]; type_2 = rtype[type_2];
                            if (type_2>0)
                                decomp = Math.min(decomp, c[indx[k]+i+1]+P.stack[type][type_2]+
                                    fML[indx[j-1]+k+1]);
                                type_2 = ptype[indx[j-1]+k+1]; type_2 = rtype[type_2];
                            if (type_2>0)
                                decomp = Math.min(decomp, c[indx[j-1]+k+1]+P.stack[type][type_2]+
                                    fML[indx[k]+i+1]);
                        }
                        /* no TermAU penalty if coax stack */
                        decomp += 2*P.MLintern[1] + P.MLclosing;
                        new_c = Math.min(new_c, decomp);
                    }

                    new_c = Math.min(new_c, cc1[j-1]+stackEnergy);
                    cc[j] = new_c + bonus;
                    if (fv.noLonelyPairs>0)
                        c[ij] = cc1[j-1]+stackEnergy+bonus;
                    else
                        c[ij] = cc[j];

                } /* end >> if (pair) << */

                else c[ij] = EP.INF;


                /* done with c[i,j], now compute fML[i,j] */
                /* free ends ? -----------------------------------------*/

                new_fML = fML[ij+1]+P.MLbase;
                new_fML = Math.min(fML[indx[j-1]+i]+P.MLbase, new_fML);
                energy = c[ij]+P.MLintern[type];
                if (fv.dangles==2) {  /* double dangles */
                    energy += (i==1) ? /* works also for circfold */
                        P.dangle5[type][S1[length]] : P.dangle5[type][S1[i-1]];
                /* if (j<length) */ energy += P.dangle3[type][S1[j+1]];
                }
                new_fML = Math.min(energy, new_fML);
                if (uniq_ML>0)
                    fM1[ij] = Math.min(fM1[indx[j-1]+i] + P.MLbase, energy);

                if (fv.dangles%2==1) {  /* normal dangles */
                    tt = ptype[ij+1]; /* i+1,j */
                    new_fML = Math.min(c[ij+1]+P.dangle5[tt][S1[i]]
                                 +P.MLintern[tt]+P.MLbase,new_fML);
                    tt = ptype[indx[j-1]+i];
                    new_fML = Math.min(c[indx[j-1]+i]+P.dangle3[tt][S1[j]]
                                 +P.MLintern[tt]+P.MLbase, new_fML);
                    tt = ptype[indx[j-1]+i+1];
                    new_fML = Math.min(c[indx[j-1]+i+1]+P.dangle5[tt][S1[i]]+
                                 P.dangle3[tt][S1[j]]+P.MLintern[tt]+2*P.MLbase, new_fML);
                }

                /* modular decomposition -------------------------------*/

                for (decomp = EP.INF, k = i+1+EP.TURN; k <= j-2-EP.TURN; k++)
                    decomp = Math.min(decomp, Fmi[k]+fML[indx[j]+k+1]);

                DMLi[j] = decomp;               /* store for use in ML decompositon */
                new_fML = Math.min(new_fML,decomp);

                /* coaxial stacking */
                if (fv.dangles==3) {
                    /* additional ML decomposition as two coaxially stacked helices */
                    for (decomp = EP.INF, k = i+1+EP.TURN; k <= j-2-EP.TURN; k++) {
                        type = ptype[indx[k]+i]; type = rtype[type];
                        type_2 = ptype[indx[j]+k+1]; type_2 = rtype[type_2];
                        if (type>0 && type_2>0)
                            decomp = Math.min(decomp,
                                    c[indx[k]+i]+c[indx[j]+k+1]+P.stack[type][type_2]);
                    }

                    decomp += 2*P.MLintern[1];	/* no TermAU penalty if coax stack */
	
//                        /* This is needed for Y shaped ML loops with coax stacking of
//                            interior pairts, but backtracking will fail if activated */
//                        DMLi[j] = Math.min(DMLi[j], decomp);
//                        DMLi[j] = Math.min(DMLi[j], DMLi[j-1]+P.MLbase);
//                        DMLi[j] = Math.min(DMLi[j], DMLi1[j]+P.MLbase);
//                        new_fML = Math.min(new_fML, DMLi[j]);

                    new_fML = Math.min(new_fML, decomp);
                }

                fML[ij] = Fmi[j] = new_fML;     /* substring energy */

            }

            {
                int[] FF; /* rotate the auxilliary arrays */
                FF = DMLi2; DMLi2 = DMLi1; DMLi1 = DMLi; DMLi = FF;
                FF = cc1; cc1=cc; cc=FF;
                for (j=1; j<=length; j++) {cc[j]=Fmi[j]=DMLi[j]=EP.INF; }
            }
        }

        /* calculate energies of 5' and 3' fragments */

        f5[EP.TURN+1]=0;
        for (j=EP.TURN+2; j<=length; j++) {
            f5[j] = f5[j-1];
            type=ptype[indx[j]+1];
            if (type>0) {
                energy = c[indx[j]+1];
                if (type>2) energy += P.TerminalAU;
                if ((fv.dangles==2)&&(j<length))  /* double dangles */
                    energy += P.dangle3[type][S1[j+1]];
                f5[j] = Math.min(f5[j], energy);
            }
            type=ptype[indx[j-1]+1];
            if ((type>0)&&(fv.dangles%2==1)) {
                energy = c[indx[j-1]+1]+P.dangle3[type][S1[j]];
                if (type>2) energy += P.TerminalAU;
                f5[j] = Math.min(f5[j], energy);
            }
            for (i=j-EP.TURN-1; i>1; i--) {
                type = ptype[indx[j]+i];
                if (type>0) {
                    energy = f5[i-1]+c[indx[j]+i];
                    if (type>2) energy += P.TerminalAU;
                    if (fv.dangles==2) {
                        energy += P.dangle5[type][S1[i-1]];
                        if (j<length) energy += P.dangle3[type][S1[j+1]];
                    }
                    f5[j] = Math.min(f5[j], energy);
                    if (fv.dangles%2==1) {
                        energy = f5[i-2]+c[indx[j]+i]+P.dangle5[type][S1[i-1]];
                        if (type>2) energy += P.TerminalAU;
                        f5[j] = Math.min(f5[j], energy);
                    }
                }
                type = ptype[indx[j-1]+i];
                if ((type>0)&&(fv.dangles%2==1)) {
                    energy = c[indx[j-1]+i]+P.dangle3[type][S1[j]];
                    if (type>2) energy += P.TerminalAU;
                    f5[j] = Math.min(f5[j], f5[i-1]+energy);
                    f5[j] = Math.min(f5[j], f5[i-2]+energy+P.dangle5[type][S1[i-1]]);
                }
            }
        }

        return f5[length];
    }
     
     private static int[][][][] loadE11h() {
        String json = null;
        try {
            json=readToString(path+"/data/11h.json");
        } catch (IOException ex) {
            System.err.println("File 11h.json is not found!");
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(json);
        } catch (JSONException ex) {
            System.err.println("JSON parser error!");
        }
        int[][][][] i11h=new int[8][8][5][5];
        for(int i=0;i<8;i++){
            JSONArray ji;
            try{
               ji=(JSONArray)ja.get(i); 
            }catch(Exception e){
               ji=null; 
            }
            for(int j=0;j<8;j++){
                JSONArray jj;
                try{
                   jj=(JSONArray)ji.get(j); 
                }catch(Exception e){
                   jj=null; 
                }
                for(int k=0;k<5;k++){
                    JSONArray jk;
                    try{
                       jk=(JSONArray)jj.get(k); 
                    }catch(Exception e){
                       jk=null; 
                    }
                    for(int l=0;l<5;l++){
                        try{
                            i11h[i][j][k][l]=jk.getInt(l);
                        }catch(Exception e){
                            i11h[i][j][k][l]=0;
                        }                                                
                    }                       
                }                    
            }      
        }
        return i11h;
    }

    private static int[][][][] loadE11_37() {
        String json = null;
        try {
            json=readToString(path+"/data/11_37.json");
        } catch (IOException ex) {
            System.err.println("File 11_37.json is not found!");
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(json);
        } catch (JSONException ex) {
            System.err.println("JSON parser error!");
        }
        int[][][][] i11_37=new int[8][8][5][5];
        for(int i=0;i<8;i++){
            JSONArray ji;
            try{
               ji=(JSONArray)ja.get(i); 
            }catch(Exception e){
               ji=null; 
            }
            for(int j=0;j<8;j++){
                JSONArray jj;
                try{
                   jj=(JSONArray)ji.get(j); 
                }catch(Exception e){
                   jj=null; 
                }
                for(int k=0;k<5;k++){
                    JSONArray jk;
                    try{
                       jk=(JSONArray)jj.get(k); 
                    }catch(Exception e){
                       jk=null; 
                    }
                    for(int l=0;l<5;l++){
                        try{
                            i11_37[i][j][k][l]=jk.getInt(l);
                        }catch(Exception e){
                            i11_37[i][j][k][l]=0;
                        }                                                
                    }                       
                }                    
            }      
        }
        return i11_37;
    }

    private static int[][][][][] loadE21h() {
        String json = null;
        try {
            json=readToString(path+"/data/21h.json");
        } catch (IOException ex) {
            System.err.println("File 21h.json is not found!");
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(json);
        } catch (JSONException ex) {
            System.err.println("JSON parser error!");
        }
        int[][][][][] i21h=new int[8][8][5][5][5];
        for(int i=0;i<8;i++){
            JSONArray ji;
            try{
               ji=(JSONArray)ja.get(i); 
            }catch(Exception e){
               ji=null; 
            }
            for(int j=0;j<8;j++){
                JSONArray jj;
                try{
                   jj=(JSONArray)ji.get(j); 
                }catch(Exception e){
                   jj=null; 
                }
                for(int k=0;k<5;k++){
                    JSONArray jk;
                    try{
                       jk=(JSONArray)jj.get(k); 
                    }catch(Exception e){
                       jk=null; 
                    }
                    for(int l=0;l<5;l++){
                        JSONArray jl;
                        try{
                           jl=(JSONArray)jk.get(l); 
                        }catch(Exception e){
                           jl=null; 
                        }
                        for(int m=0;m<5;m++){
                            try{
                                i21h[i][j][k][l][m]=jl.getInt(m);
                            }catch(Exception e){
                                i21h[i][j][k][l][m]=0;
                            }                                                       
                        }                         
                    }                       
                }                    
            }      
        }
        return i21h;
    }

    private static int[][][][][] loadE21_37() {
        String json = null;
        try {
            json=readToString(path+"/data/21_37.json");
        } catch (IOException ex) {
            System.err.println("File 21_37.json is not found!");
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(json);
        } catch (JSONException ex) {
            System.err.println("JSON parser error!");
        }
        int[][][][][] i21_37=new int[8][8][5][5][5];
        for(int i=0;i<8;i++){
            JSONArray ji;
            try{
               ji=(JSONArray)ja.get(i); 
            }catch(Exception e){
               ji=null; 
            }
            for(int j=0;j<8;j++){
                JSONArray jj;
                try{
                   jj=(JSONArray)ji.get(j); 
                }catch(Exception e){
                   jj=null; 
                }
                for(int k=0;k<5;k++){
                    JSONArray jk;
                    try{
                       jk=(JSONArray)jj.get(k); 
                    }catch(Exception e){
                       jk=null; 
                    }
                    for(int l=0;l<5;l++){
                        JSONArray jl;
                        try{
                           jl=(JSONArray)jk.get(l); 
                        }catch(Exception e){
                           jl=null; 
                        }
                        for(int m=0;m<5;m++){
                            try{
                                i21_37[i][j][k][l][m]=jl.getInt(m);
                            }catch(Exception e){
                                i21_37[i][j][k][l][m]=0;
                            }                                                       
                        }                         
                    }                       
                }                    
            }      
        }
        return i21_37;
    }

    private static int[][][][][][] loadE22h() {
        String json = null;
        try {
            json=readToString(path+"/data/22h.json");
        } catch (IOException ex) {
            System.err.println("File 22h.json is not found!");
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(json);
        } catch (JSONException ex) {
            System.err.println("JSON parser error!");
        }
        int[][][][][][] i22h=new int[8][8][5][5][5][5];
        for(int i=0;i<8;i++){
            JSONArray ji;
            try{
               ji=(JSONArray)ja.get(i); 
            }catch(Exception e){
               ji=null; 
            }
            for(int j=0;j<8;j++){
                JSONArray jj;
                try{
                   jj=(JSONArray)ji.get(j); 
                }catch(Exception e){
                   jj=null; 
                }
                for(int k=0;k<5;k++){
                    JSONArray jk;
                    try{
                       jk=(JSONArray)jj.get(k); 
                    }catch(Exception e){
                       jk=null; 
                    }
                    for(int l=0;l<5;l++){
                        JSONArray jl;
                        try{
                           jl=(JSONArray)jk.get(l); 
                        }catch(Exception e){
                           jl=null; 
                        }
                        for(int m=0;m<5;m++){
                            JSONArray jm;
                            try{
                               jm=(JSONArray)jl.get(m); 
                            }catch(Exception e){
                               jm=null; 
                            }
                            for(int n=0;n<5;n++){
                                try{
                                    i22h[i][j][k][l][m][n]=jm.getInt(n);
                                }catch(Exception e){
                                    i22h[i][j][k][l][m][n]=0;
                                }
                            }                            
                        }                         
                    }                       
                }                    
            }      
        }
        return i22h;
    }

    private static int[][][][][][] loadE22_37() {
        String json = null;
        try {
            json=readToString(path+"/data/22_37.json");
        } catch (IOException ex) {
            System.err.println("File 22_37.json is not found!");
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(json);
        } catch (JSONException ex) {
            System.err.println("JSON parser error!");
        }
        int[][][][][][] i22_37=new int[8][8][5][5][5][5];
        for(int i=0;i<8;i++){
            JSONArray ji;
            try{
               ji=(JSONArray)ja.get(i); 
            }catch(Exception e){
               ji=null; 
            }
            for(int j=0;j<8;j++){
                JSONArray jj;
                try{
                   jj=(JSONArray)ji.get(j); 
                }catch(Exception e){
                   jj=null; 
                }
                for(int k=0;k<5;k++){
                    JSONArray jk;
                    try{
                       jk=(JSONArray)jj.get(k); 
                    }catch(Exception e){
                       jk=null; 
                    }
                    for(int l=0;l<5;l++){
                        JSONArray jl;
                        try{
                           jl=(JSONArray)jk.get(l); 
                        }catch(Exception e){
                           jl=null; 
                        }
                        for(int m=0;m<5;m++){
                            JSONArray jm;
                            try{
                               jm=(JSONArray)jl.get(m); 
                            }catch(Exception e){
                               jm=null; 
                            }
                            for(int n=0;n<5;n++){
                                try{
                                    i22_37[i][j][k][l][m][n]=jm.getInt(n);
                                }catch(Exception e){
                                    i22_37[i][j][k][l][m][n]=0;
                                }
                            }                            
                        }                         
                    }                       
                }                    
            }      
        }
        return i22_37;
    }
    
    private static String readToString(String fileName) throws IOException {
		String encoding = "ISO-8859-1";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

    /**
     * @return the init_length
     */
    public int getInitLength() {
        return init_length;
    }

    /**
     * @param init_length the init_length to set
     */
    public void setInitLength(int init_length) {
        this.init_length = init_length;
    }
}
