package com.et.util.primitives;

import java.util.List;

import com.google.common.base.Objects;

/**
 * A data object defininf a triangular face. This has three indices into a vertex buffer and
 * three optional indices into a texture and normal buffer.
 */
public class TriangularFace {

  List<Integer> vertexIndices;
  List<Integer> normalIndices;
  List<Integer> textureIndices;
  
  public TriangularFace(List<Integer> vertexIndices, List<Integer> normalIndices, 
      List<Integer> textureIndices) {
    this.vertexIndices = vertexIndices;
    this.normalIndices = normalIndices;
    this.textureIndices = textureIndices;
  }

  public List<Integer> getVertexIndices() {
    return vertexIndices;
  }

  public void setVertexIndices(List<Integer> vertexIndices) {
    this.vertexIndices = vertexIndices;
  }

  public List<Integer> getNormalIndices() {
    return normalIndices;
  }

  public void setNormalIndices(List<Integer> normalIndices) {
    this.normalIndices = normalIndices;
  }

  public List<Integer> getTextureIndices() {
    return textureIndices;
  }

  public void setTextureIndices(List<Integer> textureIndices) {
    this.textureIndices = textureIndices;
  }
  
  public boolean hasNormals() {
    return !normalIndices.isEmpty();
  }
  
  public boolean hasTextureCoords() {
    return !textureIndices.isEmpty();
  }
  
  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("vertexIndices", vertexIndices.toString())
        .add("normalIndices", normalIndices.toString())
        .add("textureIndices", textureIndices.toString())
        .toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(vertexIndices, normalIndices, textureIndices);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (obj == null || !(obj instanceof TriangularFace)) {
      return false;
    }
    
    TriangularFace other = (TriangularFace) obj;
    return Objects.equal(vertexIndices, other.vertexIndices) &&
        Objects.equal(normalIndices, other.normalIndices) &&
        Objects.equal(textureIndices, other.textureIndices);
  }
  
  
}
