package com.et.util.glesdriver;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class GLESDriverModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(GLESDriver.class).to(GLESDriverImpl.class).in(Singleton.class);
  }

}
