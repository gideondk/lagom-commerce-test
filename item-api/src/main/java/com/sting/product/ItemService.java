/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.product;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

public interface ItemService extends Service {

	ServiceCall<NotUsed, Item> getItem(String id);

	@Override
	default Descriptor descriptor() {
		// @formatter:off
		return named("product-service").withCalls(
				pathCall("/api/product/:id", this::getItem)).withAutoAcl(true);
		// @formatter:on
	}

}
