package com.et.util.mesh.fileparser.obj;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.et.util.primitives.TriangularFace;
import com.google.common.collect.Lists;

/**
 * Tests for {@link FaceDeclLineParser}. 
 */
public class FaceDeclLineParserTest {

  FaceDeclLineParser parser = new FaceDeclLineParser();
  
  @Test
  public void testInvalidFaceHeader() {
    testInvalidFaceParse("dw 12 23 32");
  }

  @Test
  public void testNotEnoughElements() {
    testInvalidFaceParse("f 12 23");
  }
  
  @Test
  public void testTooManyElements() {
    testInvalidFaceParse("f 12 23 34 32");
  }
  
  @Test 
  public void testErrorOnInvalidVertexIndexValue() {
    testInvalidFaceComponentParse("f d 21 12");
    testInvalidFaceComponentParse("f 23 f 12");
    testInvalidFaceComponentParse("f 23 12 f");
  }
  
  @Test
  public void testErrorInvalidNormalIndexValue() {
    testInvalidFaceComponentParse("f 21//d 21//43 12//34");
    testInvalidFaceComponentParse("f 21//23 21//d 12//34");
    testInvalidFaceComponentParse("f 21//23 21//43 12//d");
  }
  
  @Test
  public void testErrorInvalidTextureCoordValue() {
    testInvalidFaceComponentParse("f 21/d/23 21/23/43 12/43/34");
    testInvalidFaceComponentParse("f 21/11/23 21/d/43 12/43/34");
    testInvalidFaceComponentParse("f 21/11/23 21/23/43 12/d/34");
  }
  
  @Test
  public void testErrorMissingVertexIndex() {
    testInvalidFaceComponentParse("f /23/43 23/32/23 23/43/2");
    testInvalidFaceComponentParse("f /23/43 /32/23 /43/2");
  }
  
  @Test
  public void testErrorTooManyCompnentELements() {
    testInvalidFaceComponentParse("f 12/23/43/21 21/23/43 23/34/43");
    testInvalidFaceComponentParse("f 21/23/43 12/23/43/21 23/34/43/21/23");
  }
  
  @Test
  public void testInvalidNonuniformIndices() {
    testInvalidFaceParse("f 23/23/ 12/32/43 32/34/54");
    testInvalidFaceParse("f 12//23 12 32/23");
    testInvalidFaceParse("f 12/23/32 32/21/12 12//12");
  }
  
  @Test
  public void testGarbageValues() {
    testInvalidFaceComponentParse("f 12/xww/3342 adw2132 aqw/21/3dw");
    testInvalidFaceComponentParse("f 12/34/23 23/wad/223 12/wada/32");
  }
  
  @Test
  public void testFaceAllValues() {
    List<Integer> expectedVertex = Lists.newArrayList(1, 2, 3);
    List<Integer> expectedNormal = Lists.newArrayList(3, 4, 1);
    List<Integer> expectedTextureCoords = Lists.newArrayList(2, 3, 2);
    testProperFaceParse("f 1/2/3 2/3/4 3/2/1", new TriangularFace(expectedVertex, expectedNormal,
        expectedTextureCoords));
  }
  
  @Test
  public void testFaceOnlyVertices() {
    List<Integer> expectedVertex = Lists.newArrayList(1, 2, 3);
    List<Integer> expectedNormal = Lists.newArrayList();
    List<Integer> expectedTextureCoords = Lists.newArrayList();
    testProperFaceParse("f 1 2 3", new TriangularFace(expectedVertex, expectedNormal,
        expectedTextureCoords));
  }
  
  @Test
  public void testFaceVerticesAndNormals() {
    List<Integer> expectedVertex = Lists.newArrayList(1, 2, 3);
    List<Integer> expectedNormal = Lists.newArrayList(3, 4, 1);
    List<Integer> expectedTextureCoords = Lists.newArrayList();
    testProperFaceParse("f 1//3 2//4 3//1", new TriangularFace(expectedVertex, expectedNormal,
        expectedTextureCoords));
  }
  
  @Test
  public void testFaceVerticesAndTexture() {
    List<Integer> expectedVertex = Lists.newArrayList(1, 2, 3);
    List<Integer> expectedNormal = Lists.newArrayList();
    List<Integer> expectedTextureCoords = Lists.newArrayList(2, 3, 2);
    testProperFaceParse("f 1/2 2/3 3/2", new TriangularFace(expectedVertex, expectedNormal,
        expectedTextureCoords));
  }
  
  private void testProperFaceParse(String faceDeclaration, TriangularFace expected) {
    TriangularFace parsed = parser.parseFace(faceDeclaration);
    assertEquals(expected, parsed);
  }
  
  private void testInvalidFaceParse(String faceDeclaration) {
    try {
      parser.parseFace(faceDeclaration);
      fail("Should not be able to parse invalid face declaration.");
    } catch(IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("not a valid face declaration."));
    }
  }
  
  private void testInvalidFaceComponentParse(String faceDeclaration) {
    try {
      parser.parseFace(faceDeclaration);
      fail("Should not be able to parse face with invalid component");
    } catch(IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("not a valid face component."));
    }
  }
}
