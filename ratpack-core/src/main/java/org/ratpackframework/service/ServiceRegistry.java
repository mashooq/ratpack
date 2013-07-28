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

package org.ratpackframework.service;

import org.ratpackframework.api.Nullable;

/**
 * An object that can potentially provide objects of given types.
 *
 * A service object services as a kind of service locator. A service can be requested to provide an object of a certain type.
 * <p>
 * ServiceRegistry objects must be threadsafe.
 */
public interface ServiceRegistry {

  /**
   * Provides an object of the specified type, or throws an exception if no object of that type is available.
   *
   * @param type The type of the object to provide
   * @param <T> The type of the object to provide
   * @return An object of the specified type
   * @throws NotInServiceRegistryException If no object of this type can be returned
   */
  <T> T get(Class<T> type) throws NotInServiceRegistryException;

  /**
   * Does the same thing as {@link #get(Class)}, except returns null instead of throwing an exception.
   *
   * @param type The type of the object to provide
   * @param <T> The type of the object to provide
   * @return An object of the specified type, or null if no object of this type is available.
   */
  @Nullable
  <T> T maybeGet(Class<T> type);

}