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

import com.google.enterprise.connector.spi.RepositoryDocumentException;

/**
 */
public class JavaFileSystemType implements FileSystemType {
  /* @Override */
  public JavaReadonlyFile getFile(String path, Credentials credentials) {
    return new JavaReadonlyFile(path);
  }

  /* @Override */
  public String getName() {
    return JavaReadonlyFile.FILE_SYSTEM_TYPE;
  }

  /* @Override */
  public boolean isPath(String path) {
    // TODO: Add support for windows local files, not just Unix.
    return path.startsWith("/");
  }

  /* @Override */
  public JavaReadonlyFile getReadableFile(String path, Credentials credentials)
      throws RepositoryDocumentException {
    if (!isPath(path)) {
      throw new IllegalArgumentException("Invalid path " + path);
    }
    JavaReadonlyFile result = getFile(path, credentials);
    if (!result.exists()) {
      throw new NonExistentResourceException("Path doesn't exist: " + path);
    }
    if (!result.canRead()) {
      throw new InsufficientAccessException("User doesn't have access to the path: " + path);
    }
    return result;
  }

  /* @Override */
  public boolean isUserPasswordRequired() {
    return false;
  }

}
