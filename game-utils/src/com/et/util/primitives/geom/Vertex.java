package com.et.util.primitives.geom;

import com.google.common.base.Objects;

/**
 * Data object representing a vertex.
 */
public class Vertex {
  private float x;
  private float y;
  private float z;
  private float w;
  
  public Vertex(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }
  
  public Vertex(float x, float y, float z) {
    this(x,y,z,1);
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getZ() {
    return z;
  }

  public void setZ(float z) {
    this.z = z;
  }

  public float getW() {
    return w;
  }

  public void setW(float w) {
    this.w = w;
  }
  
  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("x", x)
        .add("y", y)
        .add("z", z)
        .add("w", w)
        .toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(x, y, z, w);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Vertex)) { 
      return false;
    }
    
    if (this == obj) {
      return true;
    }
    
    Vertex other = (Vertex) obj;
    return Objects.equal(x, other.x) &&
        Objects.equal(y, other.y) &&
        Objects.equal(z, other.z) &&
        Objects.equal(w, other.w);
  }
}
