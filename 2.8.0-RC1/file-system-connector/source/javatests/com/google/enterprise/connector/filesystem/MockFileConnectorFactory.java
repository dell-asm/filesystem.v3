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

import com.google.enterprise.connector.filesystem.FileDocumentHandle.DocumentContext;
import com.google.enterprise.connector.spi.Connector;
import com.google.enterprise.connector.spi.ConnectorFactory;
import com.google.enterprise.connector.spi.RepositoryException;
import com.google.enterprise.connector.spi.TraversalContext;
import com.google.enterprise.connector.util.BasicChecksumGenerator;
import com.google.enterprise.connector.util.diffing.ChangeQueue;
import com.google.enterprise.connector.util.diffing.ChangeQueue.DefaultCrawlActivityLogger;
import com.google.enterprise.connector.util.diffing.CheckpointAndChangeQueue;
import com.google.enterprise.connector.util.diffing.DeleteDocumentHandleFactory;
import com.google.enterprise.connector.util.diffing.DiffingConnector;
import com.google.enterprise.connector.util.diffing.DocumentSnapshot;
import com.google.enterprise.connector.util.diffing.DocumentSnapshotFactory;
import com.google.enterprise.connector.util.diffing.DocumentSnapshotRepositoryMonitorManager;
import com.google.enterprise.connector.util.diffing.DocumentSnapshotRepositoryMonitorManagerImpl;
import com.google.enterprise.connector.util.diffing.SnapshotRepository;
import com.google.enterprise.connector.util.diffing.TraversalContextManager;
import com.google.enterprise.connector.util.diffing.testing.FakeTraversalContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public class MockFileConnectorFactory implements ConnectorFactory {
  private ChangeQueue changeQueue;
  private BasicChecksumGenerator checksumGenerator;
  private final File snapshotDir;
  private final File persistDir;
  private FileAuthorizationManager authorizationManager;

  public MockFileConnectorFactory(File snapshotDir, File persistDir) {
    this.snapshotDir = snapshotDir;
    this.persistDir = persistDir;
  }

  /* @Override */
  public Connector makeConnector(Map<String, String> config)
      throws RepositoryException{
    FileSystemTypeRegistry fileSystemTypeRegistry =
        new FileSystemTypeRegistry(Arrays.asList(new JavaFileSystemType()));
    PathParser pathParser = new PathParser(fileSystemTypeRegistry);

    TraversalContext traversalContext = new FakeTraversalContext();
    TraversalContextManager tcm = new TraversalContextManager();
    tcm.setTraversalContext(traversalContext);
    DocumentContext context = new DocumentContext(fileSystemTypeRegistry,
        false, true, null, null, null,
        new MimeTypeFinder(), tcm);
    FileDocumentHandleFactory clientFactory = new FileDocumentHandleFactory(context);
    changeQueue = new ChangeQueue(100, 10000, new DefaultCrawlActivityLogger());
    checksumGenerator = new BasicChecksumGenerator("SHA1");
    List<String> startPaths = readAllStartPaths(config);

    snapshotDir.mkdirs();
    List<String> includePatterns = new ArrayList<String>();
    List<String> excludePatterns = new ArrayList<String>();

    CheckpointAndChangeQueue checkpointAndChangeQueue
        = new CheckpointAndChangeQueue(changeQueue, persistDir,
            new DeleteDocumentHandleFactory(), clientFactory);
    try {
      checkpointAndChangeQueue.start(null);
    } catch (java.io.IOException e) {
      throw new IllegalStateException(
          "Failed to create CheckpointAndChangeQueue.", e);
    }
    final boolean pushAcls = true;
    final boolean markAllDocumentsPublic = false;
    DocumentContext docContext = new DocumentContext(fileSystemTypeRegistry,
        pushAcls, markAllDocumentsPublic, null, null, null,
        new MimeTypeFinder(), tcm);
    List<? extends SnapshotRepository<? extends DocumentSnapshot>>
        repositories = new FileDocumentSnapshotRepositoryList(checksumGenerator,
          pathParser, startPaths, includePatterns, excludePatterns,
          docContext);
    DocumentSnapshotFactory documentSnapshotFactory =
        new FileDocumentSnapshotFactory();
    DocumentSnapshotRepositoryMonitorManager fileSystemMonitorManager =
        new DocumentSnapshotRepositoryMonitorManagerImpl(repositories, documentSnapshotFactory,
            snapshotDir, checksumGenerator, changeQueue,
            checkpointAndChangeQueue);

    return new DiffingConnector(authorizationManager, fileSystemMonitorManager,
        tcm);
  }

  private static ArrayList<String> readAllStartPaths(Map<String,
      String> config) {
    ArrayList<String> paths = new ArrayList<String>();
    for (int i = 0; config.containsKey("start_" + i); i++) {
      paths.add(config.get("start_" + i));
    }
    return paths;
  }
}
