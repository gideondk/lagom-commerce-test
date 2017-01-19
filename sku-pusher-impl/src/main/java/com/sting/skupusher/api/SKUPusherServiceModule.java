/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.skupusher.api;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class SKUPusherServiceModule extends AbstractModule
		implements
			ServiceGuiceSupport {
	@Override
	protected void configure() {
		// Bind the HelloStream service
		bindServices(serviceBinding(SKUPusherService.class, SKUPusherServiceImpl.class));

	}
}
