package com.et.util.assetreader;

import java.io.IOException;

/**
 * Interface for a class which reads a certain asset.
 */
public interface AssetReader {

  /**
   * Reads the asset specified by path and returns it as a byte array.
   */
  public byte[] readFile(String filePath) throws IOException;
}
