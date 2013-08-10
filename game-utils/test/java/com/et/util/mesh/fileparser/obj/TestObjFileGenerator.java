package com.et.util.mesh.fileparser.obj;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Helper class to generate Wavefront .obj files with various parameters.
 */
public class TestObjFileGenerator {
  
  private static final int NUM_TEX_COMPONENTS = 2;
  private static final int NUM_NORMAL_COMPONENTS = 3;
  private static final int NUM_VERTEX_COMPONENTS = 4;
  
  private static final int NUM_COMPONENTS_PER_FACE = 3;

  private List<Float> vertices;
  private List<Float> texCoords;
  private List<Float> normals;
  
  private StringBuffer fileBuffer;
  private List<Integer> vIndices;
  private List<Integer> nIndices;
  private List<Integer> tIndices;
  
  private List<String> objectNames = Lists.newArrayList();
  private boolean withDefault = true;
  
  private boolean normalsSet = false;
  private boolean textureCoordsSet = false;
  private boolean writeFaces = false;
  
  public TestObjFileGenerator() { }
  
  /**
   * The data with which to generate the file must be provided. The indices define faces as would
   * be expected. Each triplet in the list defines the set of components for one face.
   */
  public TestObjFileGenerator(List<Float> vertices, List<Float> texCoords, List<Float> normals,
      List<Integer> vIndices, List<Integer> nIndices, List<Integer> tIndices) {
    this.vertices = vertices;
    this.texCoords = texCoords;
    this.normals = normals;
    this.vIndices = vIndices;
    this.nIndices = nIndices;
    this.tIndices = tIndices;
    
    fileBuffer = new StringBuffer();
  }
  
  /**
   * Writes any previous values for vertices.
   */
  public TestObjFileGenerator withVertices() {
    return withVertices(vertices, vIndices);
  }
  
  /**
   * Writes vertices and sets them for further use. Overwrites any previous values.
   */
  public TestObjFileGenerator withVertices(List<Float> vertices, List<Integer> vIndices) {
    writeVertices(vertices);
    this.vertices = vertices;
    this.vIndices = vIndices;
    return this;
  }
  
  /**
   * Writes any previous values for normals.
   */
  public TestObjFileGenerator withNormals() {
    return withNormals(normals, nIndices);
  }
  
  /**
   * Writes normals and sets the provided values for later use. Overwrites any previous values.
   */
  public TestObjFileGenerator withNormals(List<Float> normals, List<Integer> nIndices) {
    writeNormals(normals);
    normalsSet = true;
    this.normals = normals;
    this.nIndices = nIndices;
    return this;
  }
  
  /**
   * Writes any previous values for texture coordinates.
   */
  public TestObjFileGenerator withTextureCoords() {
    return withTextureCoords(texCoords, tIndices);
  }
  
  /**
   * Writes texture coordinates and sets provided values for later use. Overwrites any previous 
   * values.
   */
  public TestObjFileGenerator withTextureCoords(List<Float> texCoords, List<Integer> tIndices) {
    writeTexCoords(texCoords);
    textureCoordsSet = true;
    this.texCoords = texCoords;
    this.tIndices = tIndices;
    return this;
  }
  
  /**
   * Generates faces with indices for all components that were set.
   */
  public TestObjFileGenerator withFacesForSetComponents() {
    writeFaces = true;
    return this;
  }

  /**
   * Writes the set contents multiple times for each object.
   */
  public TestObjFileGenerator forObject(String objectName) {
    objectNames.add(objectName);
    return this;
  }
  
  /**
   * Specifies if we should also write a section without any object header.
   */
  public TestObjFileGenerator withDefaultObject(boolean withDefault) {
    this.withDefault = withDefault;
    return this;
  }
  
  public TestObjFileGenerator withAllComponentsInterleaved() {
    int numVertices = vertices.size();
    int numTexCoords = texCoords.size();
    int numNormals = normals.size();
    
    writeVertices(vertices.subList(0, numVertices/2));
    writeTexCoords(texCoords.subList(0, numTexCoords/2));
    writeNormals(normals.subList(0, numNormals/2));
    writeTexCoords(texCoords.subList(numTexCoords/2, numTexCoords));
    writeNormals(normals.subList(numNormals/2, numNormals));
    writeVertices(vertices.subList(numVertices/2, numVertices));
    
    normalsSet = textureCoordsSet = true;
    
    return this;
  }
  
  public String generateFile() {
    StringBuffer file = new StringBuffer();
    writeObjects(file);
    resetGeneratorState();
    return file.toString();
  }

  private void resetGeneratorState() {
    fileBuffer = new StringBuffer();
    normalsSet = textureCoordsSet = false;
    withDefault = true;
    objectNames.clear();
    writeFaces = false;
  }
  
  private void writeObjects(StringBuffer file) {
    int curObj = 0;
    if (withDefault) {
      file.append(fileBuffer);
      if (writeFaces) {
        writeFaces(file, /* objToWriteFor */ curObj);
        curObj = 1;
      }
    }
    
    for (String object : objectNames) {
      file.append("o")
          .append(" ")
          .append(object)
          .append("\n")
          .append(fileBuffer);
      writeFaces(file, /* objToWriteFor */ curObj);
      curObj++;
    }
  }
  
  private void writeVertices(List<Float> vertices) {
    writeToFile(vertices, "v", NUM_VERTEX_COMPONENTS);
  }
  
  private void writeNormals(List<Float> normals) {
    writeToFile(normals, "vn", NUM_NORMAL_COMPONENTS);
  }
  
  private void writeTexCoords(List<Float> coords) {
    writeToFile(coords, "vt", NUM_TEX_COMPONENTS);
  }
  
  private void writeToFile(List<?> data, String header, int numComponents) {
    int numElems = data.size() / numComponents;
    int pos = 0;
    for (int i = 0; i < numElems; i++) {
      fileBuffer.append(header);
      int limit = pos + numComponents;
      for (; pos < limit; pos++) {
        fileBuffer.append(" ")
            .append(data.get(pos))
            .append(" ");
      }
      fileBuffer.append("\n");
    }
  }

  private void writeFaces(StringBuffer file, int objToWriteFor) {
    int numFaces = vIndices.size() / NUM_COMPONENTS_PER_FACE;
    for (int face = 0; face < numFaces; face++) {
      file.append("f")
          .append(" ");
      for (int component = 0; component < NUM_COMPONENTS_PER_FACE; component++) {
        writeFaceComponent(file, face, component, objToWriteFor);
      }
      file.append("\n");
    }
  }

  private void writeFaceComponent(StringBuffer file, int face, int component, int objToWriteFor) {
    int index = face * NUM_COMPONENTS_PER_FACE + component;
    
    // This offset ensures each face is reading into its own set of vertices.
    int offset = objToWriteFor * (vIndices.size() / NUM_COMPONENTS_PER_FACE);
    
    file.append(vIndices.get(index) + offset);
    if (textureCoordsSet) {
      file.append("/")
          .append(tIndices.get(index) + offset);
    } else if (!textureCoordsSet && normalsSet) {
      file.append("/");
    }
    
    if (normalsSet) {
      file.append("/")
          .append(nIndices.get(index) + offset);
    }
    file.append(" ");
  }
}
