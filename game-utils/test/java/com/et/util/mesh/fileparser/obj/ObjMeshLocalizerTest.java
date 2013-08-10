package com.et.util.mesh.fileparser.obj;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.et.util.mesh.data.MeshData;
import com.et.util.primitives.TextureCoords;
import com.et.util.primitives.TriangularFace;
import com.et.util.primitives.geom.NormalVector;
import com.et.util.primitives.geom.Vertex;
import com.google.common.collect.Lists;

/**
 * Tests for {@link ObjMeshLocalizer}.
 */
public class ObjMeshLocalizerTest {
  
  private ObjMeshLocalizer localizer = new ObjMeshLocalizer();
  private List<Vertex> globalVertices = Lists.newArrayList(new Vertex(.23f, .32f, .12f),
      new Vertex(.53f, .12f, .32f), new Vertex(.65f, .52f, .22f), new Vertex(.47f, .65f, .83f),
      new Vertex(.47f, .2f, .43f), new Vertex(.97f, .85f, .93f));
  private List<NormalVector> globalNormals = Lists.newArrayList(new NormalVector(.23f, .32f, .53f),
      new NormalVector(.25f, 63f, 82f), new NormalVector(.35f, 66f, 92f), 
      new NormalVector(.15f, 73f, 77f), new NormalVector(.95f, 83f, 97f),
      new NormalVector(.25f, 53f, 17f));
  private List<TextureCoords> globalTextureCoords = Lists.newArrayList(
      new TextureCoords(.23f, .32f), new TextureCoords(.83f, .62f), new TextureCoords(.99f, .2f),
      new TextureCoords(.82f, .73f), new TextureCoords(.92f, .73f), new TextureCoords(.80f, .99f));
  
  @Test
  public void testCopiesDataAndChangesIndices() {
    List<Integer> vertexIndices1 = Lists.newArrayList(0, 3, 5);
    List<Integer> normalIndices1 = Lists.newArrayList(0, 3, 5);
    List<Integer> textureIndices1 = Lists.newArrayList(0, 3, 5);
    List<Integer> vertexIndices2 = Lists.newArrayList(0, 3, 2);
    List<Integer> normalIndices2 = Lists.newArrayList(0, 2, 3);
    List<Integer> textureIndices2 = Lists.newArrayList(0, 3, 3);
    List<Integer> vertexIndices3 = Lists.newArrayList(2, 3, 5);
    List<Integer> normalIndices3 = Lists.newArrayList(3, 2, 5);
    List<Integer> textureIndices3 = Lists.newArrayList(5, 3, 3);
    List<TriangularFace> faces = Lists.newArrayList(
        new TriangularFace(vertexIndices1, normalIndices1, textureIndices1), 
        new TriangularFace(vertexIndices2, normalIndices2, textureIndices2),
        new TriangularFace(vertexIndices3, normalIndices3, textureIndices3));
    MeshData.Builder meshDataBuilder = new MeshData.Builder();
    localizer.makeLocalMeshData(meshDataBuilder, globalVertices, globalTextureCoords, globalNormals,
        faces);
    MeshData data = meshDataBuilder.build();
  
  
    // We only reference elements 1,3,4, and 6 of each global array; We have to pull them in in
    // the order we see them
    List<Vertex> expectedVerticies = Lists.newArrayList(globalVertices.get(0),
        globalVertices.get(3), globalVertices.get(5), globalVertices.get(2));
    List<NormalVector> expectedNormals = Lists.newArrayList(globalNormals.get(0),
        globalNormals.get(3), globalNormals.get(5), globalNormals.get(2));
    List<TextureCoords> expectedTexCoords = Lists.newArrayList(globalTextureCoords.get(0),
        globalTextureCoords.get(3), globalTextureCoords.get(5));
    
    // 0 -> 1st elem; 3 -> 2nd eleme; 5 -> 3rd elem; 2 -> 4th elem
    // This is [0, 3, 5, 0, 3, 2, 2, 3, 5]
    List<Integer> expectedVIndices = Lists.newArrayList(0, 1, 2, 0, 1, 3, 3, 1, 2);
    // This is [0, 3, 5, 0, 2, 3, 3, 2, 5]
    List<Integer> expectedNIndices = Lists.newArrayList(0, 1, 2, 0, 3, 1, 1, 3, 2);
    // This is [0, 3, 5, 0, 3, 3, 5, 3, 3]
    List<Integer> expectedTIndices = Lists.newArrayList(0, 1, 2, 0, 1, 1, 2, 1, 1);
    
    MeshData.Builder expectedDataBuilder = new MeshData.Builder()
        .setVertices(expectedVerticies)
        .setTextureCoords(expectedTexCoords)
        .setNormals(expectedNormals)
        .setVertexIndices(expectedVIndices)
        .setNormalIndices(expectedNIndices)
        .setTextureCoordIndices(expectedTIndices);
    MeshData expectedData = expectedDataBuilder.build();
    
    assertEquals(expectedData, data);
  }

  @Test
  public void testLocalizedNoIndices() {
    List<TriangularFace> faces = Lists.newArrayList();
    MeshData.Builder meshDataBuilder = new MeshData.Builder();
    localizer.makeLocalMeshData(meshDataBuilder, globalVertices, globalTextureCoords, globalNormals,
        faces);
    MeshData data = meshDataBuilder.build();
    
    MeshData.Builder expectedDataBuilder = new MeshData.Builder()
    .setVertices(globalVertices)
    .setTextureCoords(globalTextureCoords)
    .setNormals(globalNormals);
    MeshData expectedData = expectedDataBuilder.build();
    
    assertEquals(expectedData, data);
    
  }
}
