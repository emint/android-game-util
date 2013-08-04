package com.et.util.mesh.fileparser.obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.et.util.mesh.files.MeshFile;

/**
 * An object file derived from an android raw text resource.
 */
public class ResourceObjFile implements MeshFile<String> {
  
  private final BufferedReader fileReader;
  private String nextLine;
  
  public ResourceObjFile(InputStream fileStream) {
    fileReader = new BufferedReader(new InputStreamReader(fileStream));
    
    // To support the hasMoreELements method we do one line look ahead. If next line is null then
    // we know previous reads have reached EOF and return true.
    lookAhead();
  }

  @Override
  public String getNextElement() {
    // Save the current line
    String toReturn = nextLine;
    // Move to next line
    lookAhead();
    return toReturn;
  }

  private void lookAhead() {
    try {
      nextLine = fileReader.readLine();
    } catch (IOException e) {
      nextLine = null;
    }
  }

  @Override
  public boolean hasMoreElements() {
    return nextLine != null;
  }

}
