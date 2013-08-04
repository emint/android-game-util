package com.et.util.mesh.fileparser.obj;

import com.et.util.mesh.data.MeshObject;
import com.et.util.mesh.fileparser.MeshObjectParser;
import com.et.util.mesh.fileparser.obj.lineparsers.FaceDeclLineParser;
import com.et.util.mesh.fileparser.obj.lineparsers.NormalVectorDeclLineParser;
import com.et.util.mesh.fileparser.obj.lineparsers.TextureCoordDeclLineParser;
import com.et.util.mesh.fileparser.obj.lineparsers.VertexDeclLineParser;
import com.google.inject.Inject;

public class ObjFileMeshObjectParser implements MeshObjectParser<ResourceObjFile> {
  
  private final VertexDeclLineParser vertexLineParser;
  private final NormalVectorDeclLineParser normalLineParser;
  private final TextureCoordDeclLineParser textureCoordLineParser;
  private final FaceDeclLineParser faceLineParser;
  
  @Inject
  public ObjFileMeshObjectParser(VertexDeclLineParser vertexLineParser,
      NormalVectorDeclLineParser normalLineParser,
      TextureCoordDeclLineParser textureCoordLineParser,
      FaceDeclLineParser faceLineParser) {
    this.vertexLineParser = vertexLineParser;
    this.normalLineParser = normalLineParser;
    this.textureCoordLineParser = textureCoordLineParser;
    this.faceLineParser = faceLineParser;
  }

  @Override
  public MeshObject parse(ResourceObjFile file) {
    return null;
  }

}
