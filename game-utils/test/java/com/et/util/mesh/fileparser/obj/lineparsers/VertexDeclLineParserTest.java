package com.et.util.mesh.fileparser.obj.lineparsers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.et.util.mesh.fileparser.obj.lineparsers.VertexDeclLineParser;
import com.et.util.primitives.geom.Vertex;

/**
 * Tests implementation of {@link VertexDeclLineParser}.
 */
public class VertexDeclLineParserTest {
  
  VertexDeclLineParser parser = new VertexDeclLineParser();
  
  @Test
  public void testInvalidVertexHeader() {
    testInvalidVertexParse("d 12.34 43.21 12.3"); 
  }
  
  @Test
  public void testInvalidTooMayValues() {
    testInvalidVertexParse("v 12.34 43.21 12.3 32 32");
  }
  
  @Test
  public void testErrorNotEnoughValues() {
    testInvalidVertexParse("v 12.3 21");
  }
  
  @Test
  public void testErrorOnInvalidValues() {
    testInvalidVertexParse("v f 23.2 32.1");
    testInvalidVertexParse("v 23.2 f 32.1");
    testInvalidVertexParse("v 23.2 23.2 f");
  }
  
  @Test
  public void testSimpleIntVertexValues() {
    testProperVertexParse("v 12 32 32", new Vertex(12, 32, 32));
  }
  
  @Test
  public void testSimpleIntVertexValuesWithW() {
    testProperVertexParse("v 12 32 32 3",  new Vertex(12, 32, 32, 3));
  }
  
  @Test
  public void testSimpleFloatVertexValues() {
    testProperVertexParse("v 12.32 2.12 3.4", new Vertex(12.32f, 2.12f, 3.4f));
  }
  
  @Test
  public void testSimpleFloatVertexValuesWithW() {
    Vertex expected = new Vertex(12.32f, 2.12f, 3.4f, .3f);
    testProperVertexParse("v 12.32 2.12 3.4 .3", expected);
  }
  
  private void testProperVertexParse(String vertexDeclaration, Vertex expected) {
    Vertex parsed = parser.parseVertex(vertexDeclaration);
    assertEquals(expected, parsed);
  }
  
  private void testInvalidVertexParse(String vertexDeclaration) {
    try {
      parser.parseVertex(vertexDeclaration);
      fail("Shouldn't be able to parse invalid vertex declaration.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("not a valid vertex declaration."));
    }
  }
}
