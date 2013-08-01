package com.et.util.primitives;

import com.google.common.base.Objects;

/**
 * A data object representing texture coordinates.
 */
public class TextureCoords {

  private float u;
  private float v;
  private float w;
  
  public TextureCoords(float u, float v, float w) {
    this.u = u;
    this.v = v;
    this.w = w;
  }
  
  public TextureCoords(float u, float v) {
    this(u, v, 0f);
  }

  public float getU() {
    return u;
  }

  public void setU(float u) {
    this.u = u;
  }

  public float getV() {
    return v;
  }

  public void setV(float v) {
    this.v = v;
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
        .add("u", u)
        .add("v", v)
        .add("w", w)
        .toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(u, v, w);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (obj == null || !(obj instanceof TextureCoords)){
      return false;
    }
    
    TextureCoords other = (TextureCoords) obj;
    return Objects.equal(u, other.u) &&
        Objects.equal(v, other.v) &&
        Objects.equal(w, other.w);
  }
  
  
}
