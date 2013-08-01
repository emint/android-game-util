package com.et.util.mesh.fileparser.obj;

import static org.junit.Assert.*;

import org.junit.Test;

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
      assertTrue(e.getMessage().contains("not a valid texture coord declaration."));
    }
  }
}
