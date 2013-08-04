package com.et.util.mesh.fileparser.obj.lineparsers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.et.util.mesh.fileparser.obj.lineparsers.TextureCoordDeclLineParser;
import com.et.util.primitives.TextureCoords;

/**
 * Tests implementation of {@link TextureCoordDeclLineParser}.
 */
public class TextureCoordDeclLineParserTest {
  
  TextureCoordDeclLineParser parser = new TextureCoordDeclLineParser();
  
  @Test
  public void testInvalidTextureCoordHeader() {
    testInvalidTextureCoordParse("fd 12 23 34");
  }
  
  @Test
  public void testInvalidNotEnoughValues() {
    testInvalidTextureCoordParse("vt 12");
  }
  
  @Test
  public void testInvalidTooManyValues() {
    testInvalidTextureCoordParse("vt 12 23 34 45");
  }
  
  @Test
  public void testErrorOnInvalidValues() {
    testInvalidTextureCoordParse("vt f 12 23");
    testInvalidTextureCoordParse("vt 12 f 23");
    testInvalidTextureCoordParse("vt f 12 f");
  }
  
  @Test
  public void testSimpleIntParse() {
    testProperTextureCoordParse("vt 12 23", new TextureCoords(12, 23));
  }
  
  @Test
  public void testSimpleFloatParse() {
    testProperTextureCoordParse("vt 12.23 32.2", new TextureCoords(12.23f, 32.2f));
  }
  
  @Test
  public void testSimpleIntParseWithW() {
    testProperTextureCoordParse("vt 12 23 22", new TextureCoords(12, 23, 22));
  }
  
  @Test
  public void testSimpleFloatParseWithW() {
    testProperTextureCoordParse("vt 12.32 23.2 23.1", new TextureCoords(12.32f, 23.2f, 23.1f));
  }
  
  private void testProperTextureCoordParse(String textureCoordsDeclaration, 
      TextureCoords expected) {
    TextureCoords parsed = parser.parseTextureCords(textureCoordsDeclaration);
    assertEquals(expected, parsed);
  }
  
  private void testInvalidTextureCoordParse(String textureCoordDeclaration) {
    try {
      parser.parseTextureCords(textureCoordDeclaration);
      fail("Shouldn't be able to parse invalid texture coord.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("not a valid texture coordinate declaration."));
    }
  }
}
