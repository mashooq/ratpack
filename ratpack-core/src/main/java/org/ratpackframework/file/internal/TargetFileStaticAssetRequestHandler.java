/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ratpackframework.file.internal;

import com.google.common.collect.ImmutableList;
import org.ratpackframework.file.FileSystemBinding;
import org.ratpackframework.handling.Context;
import org.ratpackframework.handling.Handler;
import org.ratpackframework.http.Request;
import org.ratpackframework.path.PathBinding;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class TargetFileStaticAssetRequestHandler implements Handler {

  private final List<Handler> delegate;

  public TargetFileStaticAssetRequestHandler(Handler delegate) {
    this.delegate = ImmutableList.of(delegate);
  }

  public void handle(Context context) {
    FileSystemBinding fileSystemBinding = context.get(FileSystemBinding.class);

    Request request = context.getRequest();

    String path = request.getPath();
    PathBinding pathBinding = context.maybeGet(PathBinding.class);
    if (pathBinding != null) {
      path = pathBinding.getPastBinding();
    }

    // Decode the path.
    try {
      path = new URI(path).getPath();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    // Convert file separators.
    path = path.replace('/', File.separatorChar);

    if (
      path.contains(File.separator + '.')
          || path.contains('.' + File.separator)
          || path.startsWith(".") || path.endsWith(".")
        ) {
      return;
    }

    FileSystemBinding newBinding = fileSystemBinding.binding(path);
    context.insert(FileSystemBinding.class, newBinding, delegate);
  }
}
