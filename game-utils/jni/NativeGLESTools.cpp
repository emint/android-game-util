/*
 * NativeGLESTools.c
 *
 *  Created on: May 14, 2013
 *      Author: emint
 */

#include "NativeGLESTools.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

// Implemented with help from
// https://code.google.com/p/gl2-android/source/browse/trunk/jni/AndroidGL20.cpp?r=9
JNIEXPORT jstring JNICALL Java_com_et_util_glesdriver_NativeGLESTools_getShaderInfoLog
  (JNIEnv *env, jobject, jint shader )
{
  char info[1024*100];
  int length = 0;
  glGetShaderInfoLog( shader, 1024*10, &length, info );
  return env->NewStringUTF( info );
}


