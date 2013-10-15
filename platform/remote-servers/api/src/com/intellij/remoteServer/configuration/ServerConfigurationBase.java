package com.intellij.remoteServer.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * @author nik
 */
public abstract class ServerConfigurationBase<Self extends ServerConfigurationBase<Self>> extends ServerConfiguration implements PersistentStateComponent<Self> {
  @Override
  public PersistentStateComponent<?> getSerializer() {
    return this;
  }

  @Nullable
  @Override
  public Self getState() {
    //noinspection unchecked
    return (Self) this;
  }

  @Override
  public void loadState(Self state) {
    //noinspection unchecked
    XmlSerializerUtil.copyBean(state, (Self) this);
  }
}
