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
        .add("vertexIndices", getBufferAsString(vertexIndices))
        .add("vertices", getBufferAsString(verticies))
        .add("normalIndices", getBufferAsString(normalIndices))
        .add("normals", getBufferAsString(normals)) 
        .add("textureIndices", getBufferAsString(textureIndices))
        .add("textureCoords", getBufferAsString(textureCoords))
        .add("name", name)
        .toString();
  }
  
  private String getBufferAsString(FloatBuffer buffer) {
    if (buffer == null) {
      return "[]";
    }
    
    StringBuilder builder = new StringBuilder("[");
    while(buffer.hasRemaining()) {
      builder.append(buffer.get())
          .append(",");
    }
    builder.append("]");
    buffer.rewind();
    return builder.toString();
  }


  private String getBufferAsString(IntBuffer buffer) {
    if (buffer == null) {
      return "[]";
    }
    
    StringBuilder builder = new StringBuilder("[");
    while(buffer.hasRemaining()) {
      builder.append(buffer.get())
          .append(",");
    }
    builder.append("]");
    buffer.rewind();
    return builder.toString();
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
      if (providedVertices == null) {
        return this;
      }
      
      verticies = FloatBuffer.allocate(providedVertices.size() * 4);
      
      for (Vertex vert : providedVertices) {
        verticies.put(vert.getX());
        verticies.put(vert.getY());
        verticies.put(vert.getZ());
        verticies.put(vert.getW());
      }
      
      verticies.rewind();
      return this;
    }
    
    // Please take note that for now we are discarding the 'w' component a there is no use for it.
    public Builder setTextureCoords(List<TextureCoords> providedCoords) {
      if (providedCoords == null) {
        return this;
      }
      
      textureCoords = FloatBuffer.allocate(providedCoords.size() * 2);
      
      for (TextureCoords coord : providedCoords) {
        textureCoords.put(coord.getU());
        textureCoords.put(coord.getV());
      }
      
      textureCoords.rewind();
      return this;
    }
    
    public Builder setNormals(List<NormalVector> providedNormals) {
      if (providedNormals == null) {
        return this;
      }
      
      normals = FloatBuffer.allocate(providedNormals.size() * 3);
      
      for (NormalVector normal : providedNormals) {
        normals.put(normal.getX());
        normals.put(normal.getY());
        normals.put(normal.getZ());
      }
      
      normals.rewind();
      return this;
    }
    
    public Builder setFaces(List<TriangularFace> providedFaces) {
      if (providedFaces == null) {
        return this;
      }
      
      for (TriangularFace face : providedFaces) {
        allocateFace(face);
      }
      return this;
    }
    
    public Builder setVertexIndices(List<Integer> indices) {
      return setIndices(indices, textureIndices);
    }
    
    public Builder setNormalIndices(List<Integer> indices) {
      return setIndices(indices, normalIndices);
    }
    
    public Builder setTextureCoordIndices(List<Integer> indices) {
      return setIndices(indices, textureIndices);
    }
    
    private Builder setIndices(List<Integer> providedIndices, IntBuffer toSet) {
      if (providedIndices == null) {
        return this;
      }
      
      toSet = IntBuffer.allocate(providedIndices.size());
      for (Integer idx : providedIndices) {
        toSet.put(idx);
      }
      
      toSet.rewind();
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
