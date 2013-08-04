package com.et.util.mesh.fileparser;

import com.et.util.mesh.fileparser.annotations.ObjMeshFileParser;
import com.et.util.mesh.fileparser.obj.ObjFileMeshObjectParser;
import com.et.util.mesh.fileparser.obj.ResourceObjFile;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class MeshFileParserModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(new TypeLiteral<MeshObjectParser<ResourceObjFile>> () {})
        .annotatedWith(ObjMeshFileParser.class)
        .to(ObjFileMeshObjectParser.class);
  }

}
