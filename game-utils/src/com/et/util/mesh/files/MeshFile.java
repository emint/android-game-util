package com.et.util.mesh.files;

/**
 * Defines a common interface for a file holding mesh data. Expects the file to be presented
 * as a stream of elements that are domain specific.
 */
public interface MeshFile <T> {
  T getNextElement();
  boolean hasMoreElements();
}
