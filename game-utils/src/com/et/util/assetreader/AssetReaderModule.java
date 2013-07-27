package com.et.util.assetreader;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Module defining bindings for AssetReader.
 */
public class AssetReaderModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AssetReader.class).to(AssetReaderImpl.class).in(Singleton.class);
  }

}
