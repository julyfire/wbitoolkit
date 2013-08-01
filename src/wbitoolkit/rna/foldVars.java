package wbitoolkit.rna;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wb
 */
public class foldVars {
    int  noGU=0;           /* GU not allowed at all */
    int  no_closingGU=0;   /* GU allowed only inside stacks */
    int  tetra_loop=1;     /* Fold with specially stable 4-loops */
    int  energy_set=0;     /* 0 = BP; 1=any mit GC; 2=any mit AU-parameter */
    int  dangles=1;	    /* use dangling end energies (not in part_func!) */
    /*@null@*/
    
    int oldAliEn=0;        /* use old alifold energies (with gaps) */
    int ribo=0;            /* use ribosum matrices */
    String RibosumFile=null;   /* warning this variable will vanish in the future
			       ribosums will be compiled in instead */
    String nonstandards=null;  /* contains allowed non standard bases */
    double temperature=37.0;   /* rescale parameters to this temperature */
    int  james_rule=1;     /* interior loops of size 2 get energy 0.8Kcal and
			       no mismatches, default 1 */
    int  logML;          /* use logarithmic multiloop energy function */
    int[]  cut_point;      /* first position of 2nd strand for co-folding */
    int[] strand;


    bondT[]  base_pair; /* list of base pairs */

    double[] pr;          /* base pairing prob. matrix */
    int[]   iindx;            /* pr[i,j] -> pr[iindx[i]-j] */
    double pf_scale=-1;         /* scaling factor to avoid float overflows*/
    int    fold_constrained=0; /* fold with constraints */
    int    do_backtrack=1;     /* calculate pair prob matrix in part_func() */
    int    noLonelyPairs=0;    /* avoid helices of length 1 */
    char backtrack_type='F';     /* usually 'F'; 'C' require (1,N) to be bonded;
				   'M' seq is part of a multi loop */
}
