/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.debugger.memory.component;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.EventDispatcher;
import com.intellij.util.containers.HashMap;
import com.intellij.util.xmlb.annotations.AbstractCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.debugger.memory.event.InstancesTrackerListener;
import com.intellij.debugger.memory.tracking.TrackingType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@State(name = "InstancesTracker", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class InstancesTracker extends AbstractProjectComponent
  implements PersistentStateComponent<InstancesTracker.MyState> {
  private final EventDispatcher<InstancesTrackerListener> myDispatcher =
    EventDispatcher.create(InstancesTrackerListener.class);
  private MyState myState = new MyState();

  public InstancesTracker(Project project) {
    super(project);
  }

  public static InstancesTracker getInstance(@NotNull Project project) {
    return project.getComponent(InstancesTracker.class);
  }

  public boolean isTracked(@NotNull String className) {
    return myState.classes.containsKey(className);
  }

  public boolean isBackgroundTrackingEnabled() {
    return myState.isBackgroundTrackingEnabled;
  }

  @Nullable
  public TrackingType getTrackingType(@NotNull String className) {
    return myState.classes.getOrDefault(className, null);
  }

  @NotNull
  public Map<String, TrackingType> getTrackedClasses() {
    return new HashMap<>(myState.classes);
  }

  public void add(@NotNull String name, @NotNull TrackingType type) {
    if (type.equals(myState.classes.getOrDefault(name, null))) {
      return;
    }

    myState.classes.put(name, type);
    myDispatcher.getMulticaster().classChanged(name, type);
  }

  public void remove(@NotNull String name) {
    TrackingType removed = myState.classes.remove(name);
    if (removed != null) {
      myDispatcher.getMulticaster().classRemoved(name);
    }
  }

  public void addTrackerListener(@NotNull InstancesTrackerListener listener) {
    myDispatcher.addListener(listener);
  }

  public void addTrackerListener(@NotNull InstancesTrackerListener listener, @NotNull Disposable parentDisposable) {
    myDispatcher.addListener(listener, parentDisposable);
  }

  public void removeTrackerListener(@NotNull InstancesTrackerListener listener) {
    myDispatcher.removeListener(listener);
  }

  public void setBackgroundTackingEnabled(boolean state) {
    boolean oldState = myState.isBackgroundTrackingEnabled;
    if (state != oldState) {
      myState.isBackgroundTrackingEnabled = state;
      myDispatcher.getMulticaster().backgroundTrackingValueChanged(state);
    }
  }

  @Nullable
  @Override
  public MyState getState() {
    return new MyState(myState);
  }

  @Override
  public void loadState(MyState state) {
    myState = new MyState(state);
  }

  static class MyState {
    boolean isBackgroundTrackingEnabled = false;

    @AbstractCollection(surroundWithTag = false, elementTypes = {Map.Entry.class}) final Map<String, TrackingType> classes =
      new ConcurrentHashMap<>();

    MyState() {
    }

    MyState(@NotNull MyState state) {
      isBackgroundTrackingEnabled = state.isBackgroundTrackingEnabled;
      for (Map.Entry<String, TrackingType> classState : state.classes.entrySet()) {
        classes.put(classState.getKey(), classState.getValue());
      }
    }
  }
}
