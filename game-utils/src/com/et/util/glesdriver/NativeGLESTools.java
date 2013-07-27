package com.et.util.glesdriver;

public class NativeGLESTools {
  
  public native String getShaderInfoLog(int shader);
  
  static {
    System.loadLibrary("nativeglestools");
  }
}
