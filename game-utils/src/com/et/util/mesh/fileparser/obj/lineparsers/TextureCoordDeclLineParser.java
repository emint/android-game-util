package com.et.util.mesh.fileparser.obj.lineparsers;

import java.util.StringTokenizer;

import com.et.util.primitives.TextureCoords;

/**
 * Parses a line declaring a texture coordinate in Wavefront's .obj file.
 */
public class TextureCoordDeclLineParser {
  
  private static final int MIN_TEXTURE_COORD_DECL_COMPONENTS = 3;
  private static final int MAX_TEXTURE_COORD_DECL_COMPONENTS = 4;
  private static final String TEXTURE_LINE_HEADER = "vt";
  
  public TextureCoords parseTextureCords(String textureCoordsDeclaration) {
    StringTokenizer tokenizer = new StringTokenizer(textureCoordsDeclaration);
    if (!validTextureComponentCount(tokenizer) ||
        !tokenizer.nextElement().equals(TEXTURE_LINE_HEADER)) {
      throwInvalidTextureDeclarationException(textureCoordsDeclaration);
    }
    
    float u = 0;
    float v = 0;
    float w = 0;
    
    try {
      u = Float.parseFloat(tokenizer.nextToken());
      v = Float.parseFloat(tokenizer.nextToken());
      w = (tokenizer.hasMoreTokens() ? Float.parseFloat(tokenizer.nextToken()) : 0);
    } catch (NumberFormatException e) {
      throwInvalidTextureDeclarationException(textureCoordsDeclaration);
    }
    return new TextureCoords(u, v, w);
  }
  
  private void throwInvalidTextureDeclarationException(String textureCoordsDeclaration) {
    String errorMessage = String.format("Provided argument [%s] is not a valid texture " +
        "coordinate declaration.", textureCoordsDeclaration);
    throw new IllegalArgumentException(errorMessage);
  }

  private boolean validTextureComponentCount(StringTokenizer tokenizer) {
    int count = tokenizer.countTokens();
    return count == MIN_TEXTURE_COORD_DECL_COMPONENTS ||
        count == MAX_TEXTURE_COORD_DECL_COMPONENTS;
  }
}
