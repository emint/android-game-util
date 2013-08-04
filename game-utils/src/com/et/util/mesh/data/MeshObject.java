package com.et.util.mesh.data;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

/**
 * Defines a mesh object as collection of smaller meshes.
 */
public class MeshObject {
  private Map<String, MeshData> meshes = Maps.newHashMap();

  public void addMesh(MeshData newMesh) {
    meshes.put(newMesh.getName(), newMesh);
  }

  public MeshData getMesh(String name) {
    return meshes.get(name);
  }

  public Collection<MeshData> getAllMeshes() {
    return meshes.values();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("meshes", meshes)
        .toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(meshes);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (obj == null || !(obj instanceof MeshObject)) {
      return false;
    }
    
    MeshObject other = (MeshObject) obj;
    return Objects.equal(meshes, other.meshes);
  }

}
