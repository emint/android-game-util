package com.et.util.glesdriver;

/**
 * Interface over a class that wraps interactions with the underlying GLES system.
 */
public interface GLESDriver {

  
  /**
   * Compiles shader with given shader code as a shader of provided type. Will return the GLES
   * assigned ID, or -1 if there was an error compilation. Appends error message to the provided
   * error log.
   */
  int compileShader(int shaderType, String shaderCode, StringBuffer errorLog);
  
  /**
   * Creates a new program and attaches the shaders that are passed in by their GLES-generated ids.
   * Like GLES, this returns 0 if it could not create the program, and -1 if there was a problem
   * attaching or linking the program. It will log the problem raised in the provided error log.
   */
  int createAndLinkProgram(int vertexShaderId, int fragmentShaderId, StringBuffer errorLog);
}
