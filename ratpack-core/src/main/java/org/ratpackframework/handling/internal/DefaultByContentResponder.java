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

package org.ratpackframework.handling.internal;

import io.netty.handler.codec.http.HttpHeaders;
import org.ratpackframework.handling.ByContentResponder;
import org.ratpackframework.handling.Context;
import org.ratpackframework.http.internal.MimeParse;

import java.util.*;

public class DefaultByContentResponder implements ByContentResponder {

  private final Map<String, Runnable> map = new LinkedHashMap<>(3);
  private String first;

  public ByContentResponder type(String mimeType, Runnable runnable) {
    if (mimeType == null) {
      throw new IllegalArgumentException("mimeType cannot be null");
    }

    String trimmed = mimeType.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("mimeType cannot be a blank string");
    }

    if (first == null) {
      first = trimmed;
    }
    map.put(trimmed, runnable);
    return this;
  }

  public ByContentResponder plainText(Runnable runnable) {
    return type("text/plain", runnable);
  }

  public ByContentResponder html(Runnable runnable) {
    return type("text/html", runnable);
  }

  public ByContentResponder json(Runnable runnable) {
    return type("application/json", runnable);
  }

  public ByContentResponder xml(Runnable runnable) {
    return type("application/xml", runnable);
  }

  public void respond(Context context) {
    if (first == null) {
      context.clientError(406);
      return;
    }

    List<String> types = new ArrayList<>(map.keySet());
    Collections.reverse(types);
    String winner = first;

    String acceptHeader = context.getRequest().getHeaders().get(HttpHeaders.Names.ACCEPT);
    if (acceptHeader != null && !acceptHeader.isEmpty()) {
      winner = MimeParse.bestMatch(types, acceptHeader);
    }

    if (winner == null || winner.isEmpty()) {
      context.clientError(406);
    } else {
      context.getResponse().contentType(winner);
      Runnable runnable = map.get(winner);
      runnable.run();
    }
  }

}
