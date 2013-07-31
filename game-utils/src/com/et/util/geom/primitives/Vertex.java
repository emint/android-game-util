package com.et.util.geom.primitives;

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
}
