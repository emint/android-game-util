package com.et.util.mesh.fileparser.obj.lineparsers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.et.util.mesh.fileparser.obj.lineparsers.NormalVectorDeclLineParser;
import com.et.util.primitives.geom.NormalVector;

/**
 * Tests implementation of {@link NormalVectorDeclLineParser}.
 */
public class NormalVectorDeclLineParserTest {
  
  NormalVectorDeclLineParser parser = new NormalVectorDeclLineParser();
  
  @Test
  public void testInvalidNormalHeader() {
    testInvalidNormalVectorParse("ll 12.34 .23 23.3");
  }
  
  @Test
  public void testErorNotEnoughValues() {
    testInvalidNormalVectorParse("vn 12.3 23");
  }
  
  @Test
  public void testErrorTooManyValues() {
    testInvalidNormalVectorParse("vn 12.32 34 43 32.32");
  }

  @Test
  public void testErrorOnInvalidValues() {
    testInvalidNormalVectorParse("vn f 23.34 43.2");
    testInvalidNormalVectorParse("vn 23.34 f 43.2");
    testInvalidNormalVectorParse("vn 43.2 23.34 f");
  }
  
  @Test
  public void testSimpleIntParse() {
    testProperNormalVectorParse("vn 12 34 53", new NormalVector(12f, 34f, 53f));
  }
  
  @Test
  public void testSimpleFloatPArse() {
    testProperNormalVectorParse("vn 12.32 23.21 123", new NormalVector(12.32f, 23.21f, 123f));
  }
  
  private void testProperNormalVectorParse(String normalDeclaration, NormalVector expected) {
    NormalVector parsed = parser.parseNormalVector(normalDeclaration);
    assertEquals(expected, parsed);
  }
  
  private void testInvalidNormalVectorParse(String normalDeclaration) {
    try {
      parser.parseNormalVector(normalDeclaration);
      fail("Shouldn't be able to parse invalid normal vextor.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("not a valid normal vector declaration."));
    }
  }
}
