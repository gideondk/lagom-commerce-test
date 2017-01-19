/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.product.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.sting.product.ItemService;
import com.sting.skupusher.api.SKUPusherService;

public class ItemServiceModule extends AbstractModule
		implements
			ServiceGuiceSupport {
	@Override
	protected void configure() {
		bindServices(serviceBinding(ItemService.class, ItemServiceImpl.class));
		bindClient(SKUPusherService.class);
	}
}
