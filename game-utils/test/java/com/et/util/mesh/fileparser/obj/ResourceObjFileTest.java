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
    String file = "line1\nline2\nline3";
    testOnFile(file, file.split("\n"));
  }
  
  @Test
  public void testHandlesEmptyFile() {
    String file = "";
    testOnFile(file, new String[0]);
  }
  
  @Test
  public void testReadsTrailingNewLines() {
    String file = "line1\nline2\n\n\n";
    String[] expectedContents = {"line1", "line2", "", ""};
    testOnFile(file, expectedContents);
  }
  
  @Test
  public void testPutLineAddsLineToEnd() {
    String file = "line1\nline2\nline3";
    String expectedFile = "line4\nline5\n" + file;
    ResourceObjFile objFile = getResourceObjFile(file);
    objFile.putLine("line4");
    objFile.putLine("line5");
    testOnFile(objFile, expectedFile.split("\n"));
  }
  
  @Test
  public void testPutLineComplicatedUseCase() {
    String file = "line1\nline2\nline3";
    ResourceObjFile objFile = getResourceObjFile(file);
    objFile.getNextElement();
    objFile.putLine("line4");
    objFile.putLine("line5");
    objFile.getNextElement();
    objFile.getNextElement();
    objFile.getNextElement();
    objFile.putLine("line6");
    String[] expectedContents = {"line6", "line3"};
    testOnFile(objFile, expectedContents);
  }
  
  private void testOnFile(String file, String... expectedContents) {
    ResourceObjFile objFile = getResourceObjFile(file);
    testOnFile(objFile, expectedContents);
  }
  
  
  private void testOnFile(ResourceObjFile objFile, String... expectedContents) {
    int lineCount = 0;
    while (objFile.hasMoreElements()) {
      String content = objFile.getNextElement();
      assertEquals(expectedContents[lineCount], content);
      lineCount++;
    }
    
    assertEquals(expectedContents.length, lineCount);
  }
  private ResourceObjFile getResourceObjFile(String file) {
    InputStream fileStream = new ByteArrayInputStream(file.getBytes());
    return new ResourceObjFile(fileStream);
  }
}
