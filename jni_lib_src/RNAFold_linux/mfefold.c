#include <jni.h>
#include "wbitoolkit_rna_MfeFold.h"
#include  <math.h>
#include  <string.h>
#include  "utils.h"
#include  "fold_vars.h"
#include  "fold.h"


JNIEXPORT jfloat JNICALL Java_wbitoolkit_rna_MfeFold_fold
  (JNIEnv *env, jobject obj, jstring ss, jfloat t)
{

	float e=0;
	char* seq;
	char* struc;
	
	seq=(char*)(*env)->GetStringUTFChars(env,ss,NULL);
		if(seq==NULL) return 0;
	
	temperature = t;

    	do_backtrack=0;

	/*initialize_fold(strlen(seq));*/
	 update_fold_params(); 
        
    	
       	struc=(char*)space(sizeof(char)*(strlen(seq)+1));

        e=fold(seq,struc);  
        
        free_arrays(); /* frees the memory allocated by fold() */
	
	 (*env)->ReleaseStringUTFChars(env,ss,seq); 
	return e;
}
