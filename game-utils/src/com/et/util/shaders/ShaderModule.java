package com.et.util.shaders;

import com.et.util.assetreader.AssetReader;
import com.et.util.glesdriver.GLESDriver;
import com.et.util.shaders.annotation.SimpleShader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Binds the different shaders that we use.
 */
public class ShaderModule extends AbstractModule {

  @Override
  protected void configure() {}
  
  @Provides 
  @SimpleShader 
  @Singleton
  Shader provideSimpleShader(AssetReader assetProvider, GLESDriver glesDriver) {
    return new FromFileShader(assetProvider, glesDriver, "shaders/simpleshader");
  }
}
