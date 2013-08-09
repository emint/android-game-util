package com.et.util.mesh.files;


/**
 * Defines a common interface for a file holding mesh data. Expects the file to be presented
 * as a stream of elements that are domain specific.
 */
public interface MeshFile <T> {
  /**
   * Returns the next parsable element of this file type or null if no more.
   */
  T getNextElement();
  
  /**
   * Tells you if more elements are left to parse.
   */
  boolean hasMoreElements();
  
  /**
   * Allows you put a line which will be returned on next read.
   */
  void putLine(String line);
}
