package com.et.util.mesh.fileparser;

import com.et.util.mesh.data.MeshData;
import com.et.util.mesh.files.MeshFile;

/**
 * Defines a parser that parses mesh data from a {@link MeshFile}.
 */
public interface MeshDataParser <T extends MeshFile<?>> {
  MeshData parse(T file);
}
