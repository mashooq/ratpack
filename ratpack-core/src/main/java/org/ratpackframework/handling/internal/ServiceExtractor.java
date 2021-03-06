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

import org.ratpackframework.registry.Registry;

import java.util.List;

public abstract class ServiceExtractor {

  private ServiceExtractor() {
  }

  public static Object[] extract(List<Class<?>> serviceTypes, Registry<Object> registry) {
    Object[] services = new Object[serviceTypes.size()];
    extract(serviceTypes, registry, services, 0);
    return services;
  }

  public static void extract(List<Class<?>> serviceTypes, Registry<Object> registry, Object[] services, int startIndex) {
    for (int i = 0; i < serviceTypes.size(); ++i) {
      Class<?> type = serviceTypes.get(i);
      services[i + startIndex] = registry.get(type);
    }
  }
}
