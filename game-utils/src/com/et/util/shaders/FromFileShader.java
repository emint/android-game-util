package com.et.util.shaders;

import java.io.IOException;

import javax.inject.Inject;

import android.opengl.GLES20;

import com.et.util.assetreader.AssetReader;
import com.et.util.glesdriver.GLESDriver;

/**
 * This class reads in a file from the android assets and then compiles it as a shader.
 * 
 * This imposes a file naming convention on the passed in file. A shader of name "shader" should
 * come in two files, one for the vertex and one for the fragment shader. The vertex shader should 
 * have "-vert.glsl" appended and the fragment shader should have "-fragment.glsl" appended. So our
 * "name" shader should have two files: "name-vert.glsl" and "name-frag.glsl." The passed in
 * value should be "path/to/name."
 * 
 * You have to call compile once on an OpenGL Thread. We can initialize by ourselves as we can not
 * ensure we'll be on the OpenGL thread (e.g. for automatic dependency injection).
 */
public class FromFileShader implements Shader {

  private StringBuffer errorLog = new StringBuffer();
  private boolean isCompiled = false;
  private String vertexShaderCode;
  private String fragmentShaderCode;
  private int programCode = -1;
  private final GLESDriver glesDriver;
  
  @Inject
  public FromFileShader(AssetReader assetReader, GLESDriver glesDriver, String shaderLocation) {
    this.glesDriver = glesDriver;
    
    try {
      vertexShaderCode = new String(assetReader.readFile(shaderLocation + "-vert.glsl"));
      fragmentShaderCode = new String(assetReader.readFile(shaderLocation + "-frag.glsl"));
    } catch (IOException e) {
      e.printStackTrace();
      errorLog.append(e.getMessage());
    }   
  }
  
  @Override
  public int getProgramId() {
    return programCode;
  }

  @Override
  public boolean isCompiled() {
    return isCompiled;
  }

  @Override
  public String getError() {
    return errorLog.toString();
  }

  @Override
  public String getVertexShaderCode() {
    return vertexShaderCode;
  }

  @Override
  public String getFragmentShaderCode() {
    return fragmentShaderCode;
  }
  
  @Override
  public void compile() {

    int fragmentShaderId = glesDriver.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode, 
        errorLog);
    int vertexShaderId = glesDriver.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode, 
        errorLog);
    
    if (fragmentShaderId == -1 || vertexShaderId == -1) {
      isCompiled = false;
      return;
    }
    
    programCode = glesDriver.createAndLinkProgram(vertexShaderId, fragmentShaderId, errorLog);
    
    if (programCode <= 0) {
      isCompiled = false;
    } else {
      isCompiled = true;
    }
  }
}

