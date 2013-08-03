package com.et.util.mesh.fileparser;

import com.et.util.mesh.data.MeshObject;
import com.et.util.mesh.files.MeshFile;

/**
 * Defines a parser that parses mesh data from a {@link MeshFile}.
 */
public interface MeshObjectParser <T extends MeshFile<?>> {
  MeshObject parse(T file);
}
