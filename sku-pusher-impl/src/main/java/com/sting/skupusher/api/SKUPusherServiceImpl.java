/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.skupusher.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

public class SKUPusherServiceImpl implements SKUPusherService {

    public static final AggregateEventTag<SKUEvent> INSTANCE =
            AggregateEventTag.of(SKUEvent.class);

    PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public SKUPusherServiceImpl(
            PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(SKUEntity.class);
    }

    private PersistentEntityRef<SKUCommand> entityRef(String id) {
        return persistentEntityRegistry.refFor(SKUEntity.class, id);
    }

    @Override
    public ServiceCall<SKU, String> addSKU() {
        return req -> entityRef(req.getSKU())
                    .ask(CreateSKU.builder().sKU(req).build())
                    .thenApply(ack -> "OK");

    }

    @Override
    public Topic<SKUEvent> skuTopic() {
        return TopicProducer.singleStreamWithOffset(offset -> {
            return persistentEntityRegistry
                    .eventStream(SKUEventTag.INSTANCE, offset);
        });
    }
}
