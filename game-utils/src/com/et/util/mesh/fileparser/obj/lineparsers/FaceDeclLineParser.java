package com.et.util.mesh.fileparser.obj.lineparsers;

import java.util.List;
import java.util.StringTokenizer;

import com.et.util.primitives.TriangularFace;
import com.google.common.collect.Lists;

/**
 * Parses a line declaring the indices defining a face in Wavefront's .obj file. We assume
 * meshes are triangulated so only search for three declarations of indices.
 */
public class FaceDeclLineParser {

  public static final String FACE_LINE_HEADER = "f";
  
  private static final String COMPONENT_DELIM = "/";
  // We expect three values, one per vertex, for all set values
  private static final int NUMBER_ELEMENTS_PER_VALUE = 3;
  private static final int MIN_NUM_INDICES_AND_DELIM = 1;
  private static final int MAX_NUM_INDICES_AND_DELIM = 5;
  private static final int NUM_FACE_DECL_COMPONENTS = 4;

  
  /**
   * Parses a line declaring a face. These lines are of the form 'f x/y/z', 'f x//z', or 'f x/y' 
   * where x is an index into the set of vertices, y is an index into the set of texture 
   * coordinates, and z is an index into the set of normal coordinates.
   */
  public TriangularFace parseFace(String faceDeclaration) {
    StringTokenizer tokenizer = new StringTokenizer(faceDeclaration);
    if(!validVertexDeclComponentCount(tokenizer) ||
        !tokenizer.nextToken().equals(FACE_LINE_HEADER)) {
      throwInvalidFaceDeclarationException(faceDeclaration);
    }
    
    List<Integer> vertexIndices = Lists.newArrayList();
    List<Integer> normalIndices = Lists.newArrayList();
    List<Integer> textureIndices = Lists.newArrayList();
    
    while (tokenizer.hasMoreTokens()) {
      parseComponent(tokenizer.nextToken(), vertexIndices, normalIndices, textureIndices);
    }
    
    if (!allSetComponentsHaveSameElements(vertexIndices, normalIndices, textureIndices)) {
      throwInvalidFaceDeclarationException(faceDeclaration);
    }
    
    return new TriangularFace(vertexIndices, normalIndices, textureIndices);
  }
  
  private boolean allSetComponentsHaveSameElements(List<Integer> vertexIndices,
      List<Integer> normalIndices, List<Integer> textureIndices) {
    // Mainly a sanity check
    if (vertexIndices.size() != NUMBER_ELEMENTS_PER_VALUE) {
      return false;
    }
    
    // If we have an index value, we must have the same number of values as vertices
    return (normalIndices.size() == NUMBER_ELEMENTS_PER_VALUE || normalIndices.size() == 0) && 
        (textureIndices.size() == NUMBER_ELEMENTS_PER_VALUE || textureIndices.size() == 0);
  }

  private void parseComponent(String component, List<Integer> vertexIndices,
      List<Integer> normalIndices, List<Integer> textureCoordIndices) {
    StringTokenizer componentTokenizer = new StringTokenizer(component, COMPONENT_DELIM, true);
    if (!validComponentTokenCount(componentTokenizer)) {
      throwInvalidFaceComponentException(component);
    }
    
    try {
      vertexIndices.add(Integer.parseInt(componentTokenizer.nextToken()));
      int delimCount = 0;
      while (componentTokenizer.hasMoreTokens()) {
        String token = componentTokenizer.nextToken();
        if (token.equals(COMPONENT_DELIM)) {
          delimCount++;
        } else if (delimCount == 1) {
          textureCoordIndices.add(Integer.parseInt(token));
        } else if (delimCount == 2) {
          normalIndices.add(Integer.parseInt(token));
        } else {
          throwInvalidFaceComponentException(component);
        }
      }
    } catch (NumberFormatException e) {
      throwInvalidFaceComponentException(component);
    }
    
  }
  
  private boolean validComponentTokenCount(StringTokenizer componentTokenizer) {
    return componentTokenizer.countTokens() <= MAX_NUM_INDICES_AND_DELIM &&
        componentTokenizer.countTokens() >= MIN_NUM_INDICES_AND_DELIM;
  }

  private void throwInvalidFaceDeclarationException(String faceDeclaration) {
    String errorMessage = String.format("Provided argument [%s] is not a valid face declaration.",
        faceDeclaration);
    throw new IllegalArgumentException(errorMessage);
  }
  
  private void throwInvalidFaceComponentException(String faceComponent) {
    String errorMessage = String.format("Component [%s] is not a valid face component.",
        faceComponent);
    throw new IllegalArgumentException(errorMessage);
  }
  
  private boolean validVertexDeclComponentCount(StringTokenizer tokenizer) {
    return tokenizer.countTokens() == NUM_FACE_DECL_COMPONENTS;
  }
}
