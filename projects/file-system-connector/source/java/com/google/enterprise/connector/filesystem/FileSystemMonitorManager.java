// Copyright 2009 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.enterprise.connector.filesystem;

import com.google.enterprise.connector.spi.RepositoryException;
import com.google.enterprise.connector.spi.TraversalContext;

/**
 * Management interface to {@link FileSystemMonitor} threads.
 */
public interface FileSystemMonitorManager {
  /**
   * Ensures all monitor threads are running.
   *
   * @param checkpoint for the last completed document or null if none have
   *        been completed.
   * @param traversalContext for traversal configuration values.
   *
   * @throws RepositoryException
   */

  void start(String checkpoint, TraversalContext traversalContext)
      throws RepositoryException;

  /** Stops all the configured {@link FileSystemMonitor} threads. */
  void stop();

  /**
   * Removes persisted state for {@link FileSystemMonitor} threads. After
   * calling this {@link FileSystemMonitor} threads will no longer be
   * able to resume from where they left off last time.
   */
  void clean();

  /**
   * Returns the number of {@link FileSystemMonitor} threads that are alive.
   * This method is for testing purposes.
   */
  int getThreadCount();

  /**
   * Returns the {@link CheckpointAndChangeQueue} for this
   * {@link FileSystemMonitorManager}
   */
  CheckpointAndChangeQueue getCheckpointAndChangeQueue();

  /** Returns whether we are after a start() call and before a stop(). */
  boolean isRunning();
}
