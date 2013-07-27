package com.et.util.shaders;

/**
 * Interface for a class representing a compiled shader.
 */
public interface Shader {
  
  /**
   * Returns the program ID returned by compiling this shader.
   */
  int getProgramId();
  
  /**
   * Returns if shader compiled and linked properly.
   */
  boolean isCompiled();
  
  /**
   * Starts the compilation process. We have to control this since we need to ensure it is
   * done on the OpenGL Thread.
   */
  void compile(); 
  
  /**
   * Returns a reason for error if shader was not compiled or empty string.
   */
  String getError();
  
  /**
   * Returns the vertex and fragment shader code that was loaded.
   */
  String getVertexShaderCode();
  String getFragmentShaderCode();
}
