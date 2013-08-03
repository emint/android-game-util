package com.et.util.mesh.fileparser.obj;

import com.et.util.mesh.files.MeshFile;

public class ObjFile implements MeshFile<String> {

  @Override
  public String getNextElement() {
    return null;
  }

  @Override
  public boolean hasMoreElements() {
    return false;
  }

}
