package com.et.util.mesh.fileparser.obj;

import java.util.StringTokenizer;

import com.et.util.primitives.geom.NormalVector;

/**
 * Parses a line declaring a vormal vector in wafefront's .obj file.
 */
public class NormalVectorDeclLineParser {
  
  private static final float NUMBER_NORMAL_COMPONENTS = 4;
  private static final String NORMAL_LINE_HEADER = "vn";
  
  /**
   * Parses a line declaring a normal vector. Expected format is "vn x y z" where "x y z" specify
   * the normal components. Throws {@link IllegalArgumentException} if invalid line format is
   * provided.
   */
  public NormalVector parseNormalVector(String normalDeclaration) {
    StringTokenizer tokenizer = new StringTokenizer(normalDeclaration);
    if (tokenizer.countTokens() != NUMBER_NORMAL_COMPONENTS || 
        !tokenizer.nextToken().equals(NORMAL_LINE_HEADER)) {
      throwInvalidNormalDeclarationException(normalDeclaration);
    }
    
    try {
      float x = Float.parseFloat(tokenizer.nextToken());
      float y = Float.parseFloat(tokenizer.nextToken());
      float z = Float.parseFloat(tokenizer.nextToken());
      return new NormalVector(x, y, z);
    } catch (NumberFormatException e) {
      throwInvalidNormalDeclarationException(normalDeclaration);
    }
    throw new IllegalStateException(String.format("Something went very wrong parsing normal " +
        "declaration: [%s].", normalDeclaration));
  }

  private void throwInvalidNormalDeclarationException(String normalDeclaration) {
    String errorMessage = String.format("Provided argument [%s] is not a valid normal " +
    		"vector declaration.", normalDeclaration);
    throw new IllegalArgumentException(errorMessage);
  }

}
