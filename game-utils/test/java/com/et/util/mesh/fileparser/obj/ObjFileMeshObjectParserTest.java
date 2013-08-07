package com.et.util.mesh.fileparser.obj;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.et.util.mesh.data.MeshData;
import com.et.util.mesh.data.MeshObject;
import com.et.util.mesh.fileparser.MeshFileParserModule;
import com.et.util.primitives.TextureCoords;
import com.et.util.primitives.geom.NormalVector;
import com.et.util.primitives.geom.Vertex;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Inject;

/**
 * Tests for {@link ObjFileMeshObjectParser}.
 */
public class ObjFileMeshObjectParserTest {
  
  private static final int NUM_OF_ELEMENTS = 4;
  private static final int NUM_FACES = 4;
  private static final int NUM_COMPONENTS_PER_FACE = 3;
  private static final int NUM_TEX_COMPONENTS = 2;
  private static final int NUM_NORMAL_COMPONENTS = 3;
  private static final int NUM_VERTEX_COMPONENTS = 4;
  
  @Inject ObjFileMeshObjectParser parser;
  TestObjFileGenerator fileGenerator;
  
  private List<Float> vertices;
  private List<Integer> vIndices;
  private List<Float> normals;
  private List<Integer> nIndices;
  private List<Float> texCoords;
  private List<Integer> tIndices;
  
  @Before
  public void setUp() {
    parser = Guice.createInjector(new MeshFileParserModule()).getInstance(
        ObjFileMeshObjectParser.class);
    
    int numIndicesToGenerate = NUM_FACES * NUM_COMPONENTS_PER_FACE;

    vertices = generateRandomFloats(/* numToGenerate */ NUM_OF_ELEMENTS * NUM_VERTEX_COMPONENTS);
    vIndices = generateRandomIndices(numIndicesToGenerate, /* maxIndex */ NUM_OF_ELEMENTS);
    
    normals = generateRandomFloats(/* numToGenerate */ NUM_OF_ELEMENTS * NUM_NORMAL_COMPONENTS);
    nIndices = generateRandomIndices(numIndicesToGenerate, /* maxIndex */ NUM_OF_ELEMENTS);
    
    texCoords = generateRandomFloats(/* numToGenerate */ NUM_OF_ELEMENTS * NUM_TEX_COMPONENTS);
    tIndices = generateRandomIndices(numIndicesToGenerate, /* maxIndex */ NUM_OF_ELEMENTS);
    
    fileGenerator = new TestObjFileGenerator(vertices, texCoords, normals, vIndices, nIndices,
        tIndices);
  }
  
  @Test
  public void testParsesVertices() {
    String file = fileGenerator.withVertices().generateFile();
    
    
    MeshData data = createMeshData("default", vertices, (List<Float>) null, (List<Float>) null,
        (List<Integer>) null, (List<Integer>) null, (List<Integer>) null);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesVerticesAndNormals() {
    String file = fileGenerator.withVertices()
        .withNormals()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, normals, (List<Float>) null,
        (List<Integer>) null, (List<Integer>) null, (List<Integer>) null);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesVerticesAndTextureCoords() {
    String file = fileGenerator.withVertices()
        .withTextureCoords()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, (List<Float>) null, texCoords,
        (List<Integer>) null, (List<Integer>) null, (List<Integer>) null);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesAllComponents() {
    String file = fileGenerator.withVertices()
        .withNormals()
        .withTextureCoords()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, normals, texCoords, (List<Integer>) null,
        (List<Integer>) null, (List<Integer>) null);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesAllComponentsOutOfOrder() {     
    String file = fileGenerator.withAllComponentsInterleaved()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, normals, texCoords, (List<Integer>) null,
        (List<Integer>) null, (List<Integer>) null);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesFaceWithJustVertex() {
    String file = fileGenerator.withVertices()
        .withFacesForSetComponents()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, (List<Float>) null, (List<Float>) null,
        vIndices, (List<Integer>) null, (List<Integer>) null);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesFaceWithVertexAndNormals() {
    String file = fileGenerator.withVertices()
        .withNormals()
        .withFacesForSetComponents()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, normals, (List<Float>) null, vIndices, 
        nIndices, (List<Integer>) null);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesFaceWithVertexAndTextureCoord() {
    String file = fileGenerator.withVertices()
        .withTextureCoords()
        .withFacesForSetComponents()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, (List<Float>) null, texCoords, vIndices, 
        (List<Integer>) null, tIndices);
    testGeneration(file, data);
  }
  
  
  @Test
  public void testParsesFaceWithAll() {
    String file = fileGenerator.withVertices()
        .withTextureCoords()
        .withNormals()
        .withFacesForSetComponents()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesFaceWithAllInterleaved() {
    String file = fileGenerator.withAllComponentsInterleaved()
        .withFacesForSetComponents()
        .generateFile();
    
    MeshData data = createMeshData("default", vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesObject() {
    String objName = "Obj1";
    String file = fileGenerator.withVertices()
        .withTextureCoords()
        .withNormals()
        .withFacesForSetComponents()
        .forObject(objName)
        .withDefaultObject(false)
        .generateFile();
    
    MeshData data = createMeshData(objName, vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    testGeneration(file, data);
  }
  
  @Test
  public void testParsesMultipleObjects() {
    String objName1 = "Obj1";
    String objName2 = "Obj2";
    String objName3 = "Obj3";
    String file = fileGenerator.withVertices()
        .withTextureCoords()
        .withNormals()
        .withFacesForSetComponents()
        .forObject(objName1)
        .forObject(objName2)
        .forObject(objName3)
        .withDefaultObject(false)
        .generateFile();
    
    MeshData data1 = createMeshData(objName1, vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    MeshData data2 = createMeshData(objName2, vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    MeshData data3 = createMeshData(objName3, vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    testGeneration(file, data1, data2, data3);
  }
  
  @Test
  public void testParsesMultipleObjectsWithDefault() {
    String objName1 = "default";
    String objName2 = "Obj2";
    String objName3 = "Obj3";
    String file = fileGenerator.withVertices()
        .withTextureCoords()
        .withNormals()
        .withFacesForSetComponents()
        .forObject(objName2)
        .forObject(objName3)
        .generateFile();
    
    MeshData data1 = createMeshData(objName1, vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    MeshData data2 = createMeshData(objName2, vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    MeshData data3 = createMeshData(objName3, vertices, normals, texCoords, vIndices, nIndices, 
        tIndices);
    testGeneration(file, data1, data2, data3);
  }
  
  // A little more complicated test to ensure objects get own copy of data. 
  @Test
  public void testObjectsPreserveOwnData() {
    int numObj1Vertices = 3;
    List<Float> obj1Vertices = generateRandomFloats(numObj1Vertices * NUM_VERTEX_COMPONENTS);
    List<Integer> obj1VIndices = generateRandomIndices(5 * NUM_COMPONENTS_PER_FACE,
        numObj1Vertices);
    
    
    TestObjFileGenerator fileGeneratorObj1 = new TestObjFileGenerator(obj1Vertices, 
        (List<Float>) null, (List<Float>) null, obj1VIndices, (List<Integer>) null,
        (List<Integer>)null);
    
    String objName1 = "Obj1";
    String file1 = fileGeneratorObj1.withVertices()
        .withFacesForSetComponents()
        .withDefaultObject(false)
        .forObject(objName1)
        .generateFile();
    
    String objName2 = "Obj2";
    List<Float> obj2Vertices = Lists.newArrayList();
    String file2 = fileGeneratorObj1.withVertices(obj2Vertices, obj1VIndices)
        .withFacesForSetComponents()
        .withDefaultObject(false)
        .forObject(objName2)
        .generateFile();
    
    
    MeshData data1 = createMeshData(objName1, obj1Vertices, (List<Float>) null, (List<Float>) null, 
        obj1VIndices, (List<Integer>) null, (List<Integer>) null);
    // We expect that the vertices from obj1 will be brought into the second mesh
    MeshData data2 = createMeshData(objName2, obj1Vertices, (List<Float>) null, (List<Float>) null, 
        obj1VIndices, (List<Integer>) null, (List<Integer>) null);
    testGeneration(file1 + file2, data1, data2);
  }
  
  private void testGeneration(String file, MeshData... data) {
    MeshObject expectedObject = createMeshObject(data);
    
    ResourceObjFile objFile = new ResourceObjFile(new ByteArrayInputStream(
        file.toString().getBytes()));
    assertEquals("For file: " + file.toString(), expectedObject, parser.parse(objFile));
  }
  
  private MeshObject createMeshObject(MeshData... meshes) {
    MeshObject meshObj = new MeshObject();
    for (MeshData data : meshes) {
      meshObj.addMesh(data);
    }
    return meshObj;
  }
  
  private MeshData createMeshData(String name, List<Float> vertices, List<Float> normals,
      List<Float> texCoords, List<Integer> vIndices, List<Integer> nIndices, 
      List<Integer> tIndices) {
    List<Vertex> vertexList = convertToVertexList(vertices);
    List<NormalVector> normalsList = convertToNormalList(normals);
    List<TextureCoords> textureList = convertToTextureCoordinates(texCoords);
    return new MeshData.Builder().setVertices(vertexList)
        .setNormals(normalsList)
        .setTextureCoords(textureList)
        .setVertexIndices(vIndices)
        .setNormalIndices(nIndices)
        .setTextureCoordIndices(tIndices)
        .setName(name)
        .build();
  }

  private List<NormalVector> convertToNormalList(List<Float> normals) {
    if (normals == null) {
      return null;
    }
    
    List<NormalVector> toReturn = Lists.newArrayList();
    for (int normalComponent = 0; normalComponent < normals.size(); normalComponent += 
        NUM_NORMAL_COMPONENTS) {
      float x = normals.get(normalComponent);
      float y = normals.get(normalComponent + 1);
      float z = normals.get(normalComponent + 2);
      NormalVector newNormal = new NormalVector(x, y, z);
      toReturn.add(newNormal);
    }
    return toReturn;
  }

  private List<TextureCoords> convertToTextureCoordinates(List<Float> texCoords) {
    if (texCoords == null) {
      return null;
    }
    
    List<TextureCoords> toReturn = Lists.newArrayList();
    for (int coordComponent = 0; coordComponent < texCoords.size(); coordComponent +=
        NUM_TEX_COMPONENTS) {
      float u = texCoords.get(coordComponent);
      float v = texCoords.get(coordComponent + 1);
      TextureCoords newCoords = new TextureCoords(u, v);
      toReturn.add(newCoords);
    }
    return toReturn;
  }

  private List<Vertex> convertToVertexList(List<Float> vertices) {
    if (vertices == null) {
      return null;
    }
    
    List<Vertex> toReturn = Lists.newArrayList();
    for (int vertexComponent = 0; vertexComponent < vertices.size(); vertexComponent +=
        NUM_VERTEX_COMPONENTS) {
      float x = vertices.get(vertexComponent);
      float y = vertices.get(vertexComponent + 1);
      float z = vertices.get(vertexComponent + 2);
      float w = vertices.get(vertexComponent + 3);
      Vertex newVertex = new Vertex(x, y, z, w);
      toReturn.add(newVertex);
    }
    return toReturn;
  }
  
  private List<Float> generateRandomFloats(int numToGenerate) {
    List<Float> floats = Lists.newArrayList();
    
    for (int i = 0; i < numToGenerate; i++) {
      floats.add((float) (Math.random()));
    }
    
    return floats;
  }
  
  private List<Integer> generateRandomIndices(int numToGenerate, int maxIndex) {
    List<Integer> toReturn = Lists.newArrayList();
    
    for (int i = 0; i < numToGenerate; i++) {
      int randInt = (int) (Math.random() * 10);
      // Face indices are 1-valued
      toReturn.add((randInt % maxIndex) + 1);
    }
    
    return toReturn;
  }

}
