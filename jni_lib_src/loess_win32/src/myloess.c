#include <jni.h>
#include "wbitoolkit_smooth_Loess.h"
#include "loess.h"

JNIEXPORT jdoubleArray JNICALL Java_wbitoolkit_smooth_Loess_smooth
  (JNIEnv *env, jclass cls, jdoubleArray jx, jdoubleArray jy, jdouble span, jint degree)
{
	struct loess_struct data;
	struct pred_struct data_pred;
	long se_fit;
	jdoubleArray sy;
	int n=(*env)->GetArrayLength(env, jx);
	jdouble *x, *y;
	x=(*env)->GetDoubleArrayElements(env, jx, NULL);
	if(x==NULL) return 0;
	y=(*env)->GetDoubleArrayElements(env, jy, NULL);
	if(y==NULL) return 0;
	loess_setup(x, y, n, 1, &data);
	data.model.span=span;
	data.model.degree=degree;
	loess(&data);

	se_fit=FALSE;
	predict(x, n, &data, &data_pred, se_fit);
	sy=(*env)->NewDoubleArray(env, n);
	if(sy==NULL) return 0;
	(*env)->SetDoubleArrayRegion(env, sy, 0, n, data_pred.fit);

	loess_free_mem(&data);
	pred_free_mem(&data_pred);
	(*env)->ReleaseDoubleArrayElements(env, jx, x, 0);
	(*env)->ReleaseDoubleArrayElements(env, jy, y, 0);

	return sy;
}
