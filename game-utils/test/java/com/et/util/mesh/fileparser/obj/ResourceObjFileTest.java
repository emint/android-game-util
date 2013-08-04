package com.et.util.mesh.fileparser.obj;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

/**
 * Tests implementation of {@link ResourceObjFileTest}.
 */
public class ResourceObjFileTest {

  @Test
  public void testReadsMultiLineFile() {
    testOnFile("line1\nline2\nline3", 3);
  }
  
  @Test
  public void testHandlesEmptyFile() {
    testOnFile("", 0);
  }
  
  @Test
  public void testReadsTrailingNewLines() {
    testOnFile("line1\nline2\n\n\n", 4);
  }
  
  private void testOnFile(String file, int expectedLineCount) {
    InputStream fileStream = new ByteArrayInputStream(file.getBytes());
    
    ResourceObjFile objFile = new ResourceObjFile(fileStream);
    int lineCount = 0;
    
    while (objFile.hasMoreElements()) {
      objFile.getNextElement();
      lineCount++;
    }
    
    assertEquals(expectedLineCount, lineCount);
  }
}
