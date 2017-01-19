/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.skupusher.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;

import java.util.List;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface SKUPusherService extends Service {

	ServiceCall<SKU, String> addSKU();

	@Override
	default Descriptor descriptor() {
		// @formatter:off
		return named("sku-pusher-service")
				.withCalls(pathCall("/api/sku", this::addSKU))
				.publishing(topic("skus", this::skuTopic)).withAutoAcl(true);
		// @formatter:on
	}

	Topic<SKUEvent> skuTopic();
}
