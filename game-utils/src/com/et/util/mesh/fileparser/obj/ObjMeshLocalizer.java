package com.et.util.mesh.fileparser.obj;

import java.util.Collection;
import java.util.List;

import com.et.util.mesh.data.MeshData.Builder;
import com.et.util.primitives.TextureCoords;
import com.et.util.primitives.TriangularFace;
import com.et.util.primitives.geom.NormalVector;
import com.et.util.primitives.geom.Vertex;
import com.google.common.collect.Lists;

/**
 * Given a global array of vertices, texture coords, and normals alongside indices into the global
 * array, creates a local copy of the referenced values and adjusts indices to point into local 
 * copy.
 */
public class ObjMeshLocalizer {
  
  public void makeLocalMeshData(Builder meshDataBuilder, List<Vertex> globalVertices,
      List<TextureCoords> globalTextureCoords, List<NormalVector> globalNormals,
      List<TriangularFace> faces) {
    // This is a strange case where an object was made but had no faces, so did not bind any
    // data to it. In this case, just return the global values.
    if (faces.size() == 0) {
      meshDataBuilder.setVertices(globalVertices)
          .setNormals(globalNormals)
          .setTextureCoords(globalTextureCoords);
      return;
    }
    
    List<Vertex> localVertices = Lists.newArrayList();
    List<TextureCoords> localTextureCoords = Lists.newArrayList();
    List<NormalVector> localNormals = Lists.newArrayList();
    List<Integer> localVIndices = Lists.newArrayList();
    List<Integer> localTCordIndices = Lists.newArrayList();
    List<Integer> localNIndices = Lists.newArrayList();
    
    for (TriangularFace face :  faces) {
      localizeElements(globalVertices, localVertices, localVIndices, face.getVertexIndices());
      
      if (face.hasNormals()) {
        localizeElements(globalNormals, localNormals, localNIndices, face.getNormalIndices());
      }
      
      if (face.hasTextureCoords()) {
        localizeElements(globalTextureCoords, localTextureCoords, localTCordIndices,
            face.getTextureIndices());
      }
    }
    
    meshDataBuilder.setVertices(localVertices)
        .setTextureCoords(localTextureCoords)
        .setNormals(localNormals)
        .setVertexIndices(localVIndices)
        .setTextureCoordIndices(localTCordIndices)
        .setNormalIndices(localNIndices);
  }
  
  private <T> void localizeElements(List<T> globalElems, List<T> localElems,
      List<Integer> localIndices, List<Integer> globalIndices) {
    for (Integer index : globalIndices) {
      T elem = globalElems.get(index);
      if (localElems.contains(elem)) {
        int pos = getElemPosition(localElems, elem);
        localIndices.add(pos);
      } else {
        localElems.add(elem);
        localIndices.add(localElems.size() - 1);
      }
    }
  }
  
  private <T> int getElemPosition(Collection<T> collection, T elem) {
    int pos = -1;
    for (T curElem : collection) {
      pos++;
      if (elem.equals(curElem)) {
        break;
      }
    }
    return pos;
  }
}
