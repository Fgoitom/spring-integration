/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.dispatcher;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.endpoint.MessageEndpoint;
import org.springframework.integration.message.Message;

/**
 * Base class for {@link MessageDispatcher} implementations.
 * 
 * @author Mark Fisher
 */
public abstract class AbstractDispatcher implements MessageDispatcher {

	protected final Log logger = LogFactory.getLog(this.getClass());

	protected final Set<MessageEndpoint> endpoints = new CopyOnWriteArraySet<MessageEndpoint>();

	private volatile TaskExecutor taskExecutor;


	public boolean subscribe(MessageEndpoint endpoint) {
		return this.endpoints.add(endpoint);
	}

	public boolean unsubscribe(MessageEndpoint endpoint) {
		return this.endpoints.remove(endpoint);
	}

	/**
	 * Specify a {@link TaskExecutor} for invoking the endpoints.
	 * If none is provided, the invocation will occur in the thread
	 * that runs this polling dispatcher.
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	protected TaskExecutor getTaskExecutor() {
		return this.taskExecutor;
	}

	/**
	 * A convenience method for subclasses to send a Message to a single endpoint.
	 */
	protected final boolean sendMessageToEndpoint(Message<?> message, MessageEndpoint endpoint) {
		return endpoint.send(message);
	}

	public String toString() {
		return this.getClass().getSimpleName() + " with endpoints: " + this.endpoints;
	}

}
