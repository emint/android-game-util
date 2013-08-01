package com.et.util.mesh.fileparser.obj;

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
      
    }
    return null;
  }
  
  private boolean validTextureComponentCount(StringTokenizer tokenizer) {
    int count = tokenizer.countTokens();
    return count == MIN_TEXTURE_COORD_DECL_COMPONENTS ||
        count == MAX_TEXTURE_COORD_DECL_COMPONENTS;
  }
}
