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

package org.ratpackframework.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.ratpackframework.handling.Handler;
import org.ratpackframework.launch.HandlerFactory;
import org.ratpackframework.launch.LaunchConfig;
import org.ratpackframework.launch.LaunchException;
import org.ratpackframework.server.internal.NettyRatpackService;
import org.ratpackframework.server.internal.RatpackChannelInitializer;
import org.ratpackframework.server.internal.ServiceBackedServer;

/**
 * Builds a {@link RatpackServer}.
 */
public abstract class RatpackServerBuilder {

  private RatpackServerBuilder() {
  }

  /**
   * Constructs a new server based on the builder's state.
   * <p>
   * The returned server has not been started.
   *
   * @return A new, not yet started, Ratpack server.
   */
  public static RatpackServer build(LaunchConfig launchConfig) {
    ChannelInitializer<SocketChannel> channelInitializer = buildChannelInitializer(launchConfig);
    NettyRatpackService service = new NettyRatpackService(launchConfig, channelInitializer);
    return new ServiceBackedServer(service, launchConfig);
  }


  private static ChannelInitializer<SocketChannel> buildChannelInitializer(LaunchConfig launchConfig) {
    return new RatpackChannelInitializer(launchConfig, createHandler(launchConfig));
  }

  private static Handler createHandler(LaunchConfig launchConfig) {
    HandlerFactory handlerFactory = launchConfig.getHandlerFactory();
    try {
      return handlerFactory.create(launchConfig);
    } catch (Exception e) {
      throw new LaunchException("Could not create handler via handler factory: " + handlerFactory.getClass().getName(), e);
    }
  }

}
