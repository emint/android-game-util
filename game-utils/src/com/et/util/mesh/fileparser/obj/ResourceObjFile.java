package com.et.util.mesh.fileparser.obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;

import com.et.util.mesh.files.MeshFile;
import com.google.common.collect.Queues;

/**
 * An object file derived from an android raw text resource.
 */
public class ResourceObjFile implements MeshFile<String> {
  
  private final BufferedReader fileReader;
  private String nextLine;
  private Queue<String> returnedElements = Queues.newLinkedBlockingDeque();
  
  public ResourceObjFile(InputStream fileStream) {
    fileReader = new BufferedReader(new InputStreamReader(fileStream));
    
    // To support the hasMoreELements method we do one line look ahead. If next line is null then
    // we know previous reads have reached EOF and return true.
    lookAhead();
  }

  @Override
  public String getNextElement() {
    String toReturn;
    if (returnedElements.isEmpty()) {
      // Save the current line
      toReturn = nextLine;
      // Read ahead in file
      lookAhead();
    } else {
      toReturn = returnedElements.remove();
    }
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

  @Override
  public void putLine(String line) {
    returnedElements.add(line);
  }

}
