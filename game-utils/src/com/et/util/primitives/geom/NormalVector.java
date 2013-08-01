package com.et.util.primitives.geom;

import com.google.common.base.Objects;

/**
 * Data object representing a normal vector.
 */
public class NormalVector {
  
  private float x;
  private float y;
  private float z;
  
  public NormalVector(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
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
  
  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("x", x)
        .add("y", y)
        .add("z", z)
        .toString();
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(x, y, z);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof NormalVector)) { 
      return false;
    }
    
    if (this == obj) {
      return true;
    }
    
    NormalVector other = (NormalVector) obj;
    return Objects.equal(x, other.x) &&
        Objects.equal(y, other.y) &&
        Objects.equal(z, other.z);
  }
  
  
}
