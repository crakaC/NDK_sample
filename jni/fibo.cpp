#include <jni.h>
#include <android/log.h>

long fibo( int n ) {
	if ( n < 3 ) {
		return 1;
	}
	return fibo(n-1) + fibo(n-2);
}

#include "fibo.hpp"
JNIEXPORT jlong JNICALL Java_com_example_hellondk_MainActivity_fiboCpp
  (JNIEnv *env, jobject obj, jint n) {
	return fibo( n );
}
