package com.et.util.glesdriver;

import java.nio.IntBuffer;

import android.opengl.GLES20;

public class GLESDriverImpl implements GLESDriver {
  
  NativeGLESTools nativeTools = new NativeGLESTools();
  
  @Override
  public int compileShader(int shaderType, String shaderCode,
      StringBuffer errorLog) {
    int ourShaderHandle = GLES20.glCreateShader(shaderType);
    
    if (ourShaderHandle == 0) {
      errorLog.append("Could not allocate shader id.");
      return -1;
    }
    
    GLES20.glShaderSource(ourShaderHandle, shaderCode);
    GLES20.glCompileShader(ourShaderHandle);
    
    IntBuffer shaderStatus = IntBuffer.allocate(1);
    GLES20.glGetShaderiv(ourShaderHandle, GLES20.GL_COMPILE_STATUS, shaderStatus);

    if (shaderStatus.get() != GLES20.GL_TRUE) {
      errorLog
          .append("Reported compilation error: ")
          .append(nativeTools.getShaderInfoLog(ourShaderHandle))
          .append("\n");
      return -1;
    }
    return ourShaderHandle;
  }

  @Override
  public int createAndLinkProgram(int vertexShaderId, int fragmentShaderId,
      StringBuffer errorLog) {
    int programId = GLES20.glCreateProgram();
    
    if (programId == 0) {
      errorLog.append("Error creating program\n");
      return 0;
    }
    
    // clear any error codes
    GLES20.glGetError();
    
    GLES20.glAttachShader(programId, vertexShaderId);
    GLES20.glAttachShader(programId, fragmentShaderId);
    
    if (GLES20.glGetError() == GLES20.GL_INVALID_VALUE || 
        GLES20.glGetError() == GLES20.GL_INVALID_OPERATION) {
      errorLog.append("Error linking shaders.\n");
      return -1;
    }
    
    GLES20.glLinkProgram(programId);
    IntBuffer linkStatus = IntBuffer.allocate(1);
    GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus);

    if (linkStatus.get() != GLES20.GL_TRUE) {
      errorLog
          .append("Reported Linking error: ")
          .append(GLES20.glGetProgramInfoLog(programId))
          .append("\n");
      return -1;
    }
    
    return programId;
  }
}
