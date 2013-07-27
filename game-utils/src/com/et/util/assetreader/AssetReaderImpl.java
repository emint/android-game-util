package com.et.util.assetreader;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import android.content.Context;

/**
 * Reads the file from the assets directory.
 */
public class AssetReaderImpl implements AssetReader {

  private final Context context;

  @Inject
  public AssetReaderImpl(Context context) {
    this.context = context;
  }

  /**
   * Reads the file using Android's asset manager.
   * 
   * Based in part on:
   * http://xjaphx.wordpress.com/2011/10/02/store-and-use-files-in-assets/
   */
  @Override
  public byte[] readFile(String filePath) throws IOException {

    InputStream inputStream = context.getAssets().open(filePath);

    int size = inputStream.available();
    byte[] data = new byte[size];

    inputStream.read(data);
    inputStream.close();

    return data;
  }

}
