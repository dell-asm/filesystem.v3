// Copyright 2010 Google Inc.
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

import com.google.enterprise.connector.diffing.DocumentHandle;
import com.google.enterprise.connector.diffing.DocumentHandleFactory;
import com.google.enterprise.connector.diffing.TraversalContextManager;
import com.google.enterprise.connector.filesystem.FileDocumentHandle.Field;

import org.json.JSONException;
import org.json.JSONObject;

public class FileDocumentHandleFactory implements DocumentHandleFactory {

  private final FileDocumentHandle.DocumentContext context;

  FileDocumentHandleFactory(FileSystemTypeRegistry fileSystemTypeRegistry,
      boolean pushAcls, boolean markAllDocumentsPublic,
      String domainName, String userName, String password,
      MimeTypeFinder mimeTypeFinder,
      TraversalContextManager traversalContextManager) {
    context = new FileDocumentHandle.DocumentContext(
        fileSystemTypeRegistry, pushAcls, markAllDocumentsPublic,
        FileConnectorType.newCredentials(domainName, userName, password),
        mimeTypeFinder, traversalContextManager);
  }

  public DocumentHandle fromString(String stringForm)
      throws IllegalArgumentException {
    try {
      JSONObject json = new JSONObject(stringForm);
      checkForMissingRequiredFields(json);
      return new FileDocumentHandle(json.getString(Field.FILESYS.name()),
          json.getString(Field.PATH.name()),
          json.getBoolean(Field.IS_DELETE.name()), context);
    } catch (JSONException je) {
      throw new IllegalArgumentException(
          "Unable to parse serialized JSON Object " + stringForm, je);
    }
  }

  private static void checkForMissingRequiredFields(JSONObject o)
      throws IllegalArgumentException {
    StringBuilder buf = new StringBuilder();
    for (Field f : Field.values()) {
      if (!o.has(f.name())) {
        buf.append(f);
        buf.append(", ");
      }
    }

    if (buf.length() != 0) {
      buf.insert(0, "missing fields in JSON object: ");
      buf.setLength(buf.length() - 2);
      throw new IllegalArgumentException(buf.toString());
    }
  }
}
