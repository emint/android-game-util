package com.et.util.mesh.fileparser.obj;

import java.util.StringTokenizer;

import com.et.util.primitives.geom.Vertex;

/**
 * Parses a line declaring a vertex in Wavefront's .obj file.
 */
public class VertexDeclLineParser {
  
  private static final int MIN_VERTEX_DECL_COMPONENTS = 4;
  private static final int MAX_VERTEX_DECL_COMPONENTS = 5;
  private static final String VERTEX_LINE_HEADER = "v";
  
  /**
   * Parses a line declaring a vertex. These lines are of form 'v x y z [w],' where 'v' denotes a
   * vertex declaration and 'x y z [w]' denote the vertex elements. This method throws an 
   * {@link IllegalArgumentException} if the formatting is not followed.
   */
  public Vertex parseVertex(String vertexDeclaration) {
    StringTokenizer tokenizer = new StringTokenizer(vertexDeclaration);
    if (!validVertexDeclComponentCount(tokenizer) || 
        !tokenizer.nextToken().equals(VERTEX_LINE_HEADER)) {
      throwInvalidVertexDeclarationException(vertexDeclaration);
    }
    
    try {
      float x = Float.parseFloat(tokenizer.nextToken());
      float y = Float.parseFloat(tokenizer.nextToken());
      float z = Float.parseFloat(tokenizer.nextToken());
      float w = (tokenizer.hasMoreTokens() ? Float.parseFloat(tokenizer.nextToken()) : 1);
      return new Vertex(x, y, z, w);
    } catch (NumberFormatException e) {
      throwInvalidVertexDeclarationException(vertexDeclaration);
    }
    throw new IllegalStateException(String.format("Something went very wrong parsing vertex " +
    		"declaration: [%s].", vertexDeclaration));
  }

  private boolean validVertexDeclComponentCount(StringTokenizer tokenizer) {
    int count = tokenizer.countTokens();
    return count == MIN_VERTEX_DECL_COMPONENTS || count == MAX_VERTEX_DECL_COMPONENTS;
  }

  private void throwInvalidVertexDeclarationException(String vertexDeclaration) {
    String errorMessage = String.format(
        "Provided argument [%s] is not a valid vertex declaration.", vertexDeclaration);
    throw new IllegalArgumentException(errorMessage);
  }
}
