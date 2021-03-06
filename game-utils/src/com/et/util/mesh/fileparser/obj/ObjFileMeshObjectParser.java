package com.et.util.mesh.fileparser.obj;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.et.util.mesh.data.MeshData;
import com.et.util.mesh.data.MeshData.Builder;
import com.et.util.mesh.data.MeshObject;
import com.et.util.mesh.fileparser.MeshObjectParser;
import com.et.util.mesh.fileparser.obj.lineparsers.FaceDeclLineParser;
import com.et.util.mesh.fileparser.obj.lineparsers.NormalVectorDeclLineParser;
import com.et.util.mesh.fileparser.obj.lineparsers.TextureCoordDeclLineParser;
import com.et.util.mesh.fileparser.obj.lineparsers.VertexDeclLineParser;
import com.et.util.primitives.TextureCoords;
import com.et.util.primitives.TriangularFace;
import com.et.util.primitives.geom.NormalVector;
import com.et.util.primitives.geom.Vertex;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * A {@link MeshObjectParser} that parses Wavefront's .obj mesh files into {@link MeshObject}s. 
 */
public class ObjFileMeshObjectParser implements MeshObjectParser<ResourceObjFile> {
  
  private static final String OBJECT_LINE_HEADER = "o";
  private static final String COMMENT_LINE_HEADER = "#";

  private static final Logger logger = LoggerFactory.getLogger(ObjFileMeshObjectParser.class);
  
  private final VertexDeclLineParser vertexLineParser;
  private final NormalVectorDeclLineParser normalLineParser;
  private final TextureCoordDeclLineParser textureCoordLineParser;
  private final FaceDeclLineParser faceLineParser;
  private final ObjMeshLocalizer meshLocalizer;
  
  @Inject
  public ObjFileMeshObjectParser(VertexDeclLineParser vertexLineParser,
      NormalVectorDeclLineParser normalLineParser,
      TextureCoordDeclLineParser textureCoordLineParser,
      FaceDeclLineParser faceLineParser,
      ObjMeshLocalizer meshLocalizer) {
    this.vertexLineParser = vertexLineParser;
    this.normalLineParser = normalLineParser;
    this.textureCoordLineParser = textureCoordLineParser;
    this.faceLineParser = faceLineParser;
    this.meshLocalizer = meshLocalizer;
  }

  @Override
  public MeshObject parse(ResourceObjFile file) {
    MeshObject newObject = new MeshObject();
    
    // We want each mesh data set in the mesh object to have a copy vertices, textures, and normals
    // that it needs. The problem is the file uses global addressing. So we parse all definitions
    // into a global pool and reconstruct the faces to use local indices into a reconstructed 
    // local pool.
    List<Vertex> globalVertices = Lists.newArrayList();
    List<TextureCoords> globalTextureCoords = Lists.newArrayList();
    List<NormalVector> globalNormals = Lists.newArrayList();
    
    while (file.hasMoreElements()) {
      MeshData data = processMeshData(file, globalVertices, globalNormals,
          globalTextureCoords);
      // This is a bit of a hack we put into place to avoid parsing any preamble declarations as
      // a mesh.
      if (data.getVerticies().capacity() > 0) {
        newObject.addMesh(data);
      }
    }
    return newObject;
  }
  
  private MeshData processMeshData(ResourceObjFile file, List<Vertex> globalVertices,
      List<NormalVector> globalNormals, List<TextureCoords> globalTextureCoords) {
    MeshData.Builder meshDataBuilder = new MeshData.Builder();
    
    String nextElement = file.getNextElement();
    String op = getOperation(nextElement);
    
    setMeshDataName(op, file, nextElement, meshDataBuilder);
    
    processMeshData(meshDataBuilder, file, globalVertices, globalTextureCoords,
        globalNormals);
    
    return meshDataBuilder.build();
  }
  
  private void processMeshData(Builder meshDataBuilder, ResourceObjFile file, 
      List<Vertex> globalVertices, List<TextureCoords> globalTextureCoords, 
      List<NormalVector> globalNormals) {
    List<TriangularFace> faces = Lists.newArrayList();
    
    while (file.hasMoreElements()) {
      String nextElement = file.getNextElement();
      String op = getOperation(nextElement);
      
      if (op.equals(OBJECT_LINE_HEADER)) {
        // Return object declaration and return
        file.putLine(nextElement);
        meshLocalizer.makeLocalMeshData(meshDataBuilder, globalVertices, globalTextureCoords,
            globalNormals, faces);
        return;
      }
      
      try {
        processElement(op, nextElement, globalVertices, globalTextureCoords, globalNormals,
            faces);
      } catch (IllegalArgumentException e) {
        logger.warn("Error processing element in file.", e);
      }
    }
    meshLocalizer.makeLocalMeshData(meshDataBuilder, globalVertices, globalTextureCoords, 
        globalNormals, faces);
  }

  private void processElement(String op, String nextElement, List<Vertex> globalVertices,
      List<TextureCoords> globalTextureCoords, List<NormalVector> globalNormals,
      List<TriangularFace> parsedFaces) {
    if (op.equals(VertexDeclLineParser.VERTEX_LINE_HEADER)) {
      globalVertices.add(vertexLineParser.parseVertex(nextElement));
    } else if (op.equals(TextureCoordDeclLineParser.TEXTURE_LINE_HEADER)) {
      globalTextureCoords.add(textureCoordLineParser.parseTextureCords(nextElement));
    } else if (op.equals(NormalVectorDeclLineParser.NORMAL_LINE_HEADER)) {
      globalNormals.add(normalLineParser.parseNormalVector(nextElement));
    } else if (op.equals(FaceDeclLineParser.FACE_LINE_HEADER)) {
      parsedFaces.add(faceLineParser.parseFace(nextElement));
    } else if (op.equals(COMMENT_LINE_HEADER)) {
    } else {
      String errorMessage = String.format("Error processing operation %s in element %s.", op,
          nextElement);
      throw new IllegalArgumentException(errorMessage);
    }
  }

  private void setMeshDataName(String op, ResourceObjFile file, String nextElement,
      Builder meshDataBuilder) {
    if (!op.equals(OBJECT_LINE_HEADER)) {
      meshDataBuilder.setName("default");
      file.putLine(nextElement);
    } else {
      // Get the string after the 'o' and remove and whitespace around it.
      String name = nextElement.substring(1).trim();
      meshDataBuilder.setName(name);
    }
  }

  private String getOperation(String element) {
    String[] splitElement = (element.trim()).split(" ");
    if (splitElement.length == 0) {
      return "";
    } else {
      return splitElement[0];
    }
  }
}
