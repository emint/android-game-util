package com.et.util.mesh.data;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import com.et.util.primitives.TextureCoords;
import com.et.util.primitives.TriangularFace;
import com.et.util.primitives.geom.NormalVector;
import com.et.util.primitives.geom.Vertex;
import com.google.common.base.Objects;

/**
 * Domain object that captures the basic data of most meshes.
 */
public class MeshData {

  private FloatBuffer verticies = null;
  private FloatBuffer normals = null;
  private FloatBuffer textureCoords = null;
  private IntBuffer vertexIndices = null;
  private IntBuffer normalIndices = null;
  private IntBuffer textureIndices = null;
  private String name;

  MeshData(Builder builder) {
    this.verticies = builder.verticies;
    this.normals = builder.normals;
    this.textureCoords = builder.textureCoords;
    this.vertexIndices = builder.vertexIndices;
    this.normalIndices = builder.normalIndices;
    this.textureIndices = builder.textureIndices;
    this.name = builder.name;
  }
  
  
  public FloatBuffer getVerticies() {
    return verticies;
  }


  public FloatBuffer getNormals() {
    return normals;
  }

  public FloatBuffer getTextureCoords() {
    return textureCoords;
  }

  public IntBuffer getVertexIndices() {
    return vertexIndices;
  }

  public IntBuffer getNormalIndices() {
    return normalIndices;
  }

  public IntBuffer getTextureIndices() {
    return textureIndices;
  }

  public String getName() {
    return name;
  }
  
  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("vertexIndices", vertexIndices)
        .add("vertices", verticies)
        .add("normalIndices", normalIndices)
        .add("normals", normals) 
        .add("textureIndices", textureIndices)
        .add("textureCoords", textureCoords)
        .add("name", name)
        .toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(vertexIndices, verticies, normalIndices, normals, textureIndices,
        textureCoords, name);
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (obj == null || !(obj instanceof MeshData)) {
      return false;
    }
    
    MeshData other = (MeshData) obj;
    return Objects.equal(name, other.name) &&
        Objects.equal(vertexIndices, other.vertexIndices) &&
        Objects.equal(verticies, other.verticies) &&
        Objects.equal(normalIndices, other.normalIndices) &&
        Objects.equal(normals, other.normals) &&
        Objects.equal(textureIndices, other.textureIndices) &&
        Objects.equal(textureCoords, other.textureCoords);
  }



  public static class Builder {
    private FloatBuffer verticies = null;
    private FloatBuffer normals = null;
    private FloatBuffer textureCoords = null;
    private IntBuffer vertexIndices = null;
    private IntBuffer normalIndices = null;
    private IntBuffer textureIndices = null;
    private String name = null;
    
    public Builder setVertices(List<Vertex> providedVertices) {
      verticies = FloatBuffer.allocate(providedVertices.size() * 3);
      
      for (Vertex vert : providedVertices) {
        verticies.put(vert.getX());
        verticies.put(vert.getY());
        verticies.put(vert.getZ());
      }
      
      return this;
    }
    
    // Please take note that for now we are discarding the 'w' component a there is no use for it.
    public Builder setTextureCoords(List<TextureCoords> providedCoords) {
      textureCoords = FloatBuffer.allocate(providedCoords.size() * 2);
      
      for (TextureCoords coord : providedCoords) {
        textureCoords.put(coord.getU());
        textureCoords.put(coord.getV());
      }
      
      return this;
    }
    
    public Builder setNormals(List<NormalVector> providedNormals) {
      normals = FloatBuffer.allocate(providedNormals.size() * 3);
      
      for (NormalVector normal : providedNormals) {
        normals.put(normal.getX());
        normals.put(normal.getY());
        normals.put(normal.getZ());
      }
      
      return this;
    }
    
    public Builder setFaces(List<TriangularFace> providedFaces) {
      for (TriangularFace face : providedFaces) {
        allocateFace(face);
      }
      return this;
    }
    
    public Builder setName(String name) {
      this.name = name;
      return this;
    }
    
    public MeshData build() {
      return new MeshData(this);
    }
    
    private void allocateFace(TriangularFace face) {
      vertexIndices = IntBuffer.allocate(face.getVertexIndices().size() * 3);
      normalIndices = (face.hasNormals() ? IntBuffer.allocate(face.getNormalIndices().size() * 3) :
        null);
      textureIndices = (face.hasTextureCoords() ? IntBuffer.allocate(
          face.getTextureIndices().size() * 3) : null);
      
      for (Integer vIdx : face.getVertexIndices()) {
        vertexIndices.put(vIdx);
      }
      
      if (normalIndices != null) {
        for (Integer nIdx : face.getNormalIndices()) {
          normalIndices.put(nIdx);
        }
      }
      
      if (textureIndices != null) {
        for (Integer tIdx : face.getTextureIndices()) {
          textureIndices.put(tIdx);
        }
      }
    }
  }
}
