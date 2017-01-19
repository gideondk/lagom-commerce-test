/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.product.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.sting.product.Item;
import com.sting.product.ItemService;
import com.sting.skupusher.api.*;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class ItemServiceImpl implements ItemService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final SKUPusherService skuPusherService;

    @Inject
    public ItemServiceImpl(PersistentEntityRegistry persistentEntityRegistry,
                           SKUPusherService skuPusherService) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.skuPusherService = skuPusherService;
        persistentEntityRegistry.register(ItemEntity.class);

        skuPusherService
                .skuTopic()
                .subscribe()
                .atLeastOnce(
                        Flow.<SKUEvent>create()
                                .mapAsync(
                                        1,
                                        event -> {
                                            if (event instanceof SKUCreated) {
                                                SKUCreated created = (SKUCreated) event;
                                                return entityRef(
                                                        created.getSKU().getParentId())
                                                        .ask(AddSKU
                                                                .builder()
                                                                .sKU(created.getSKU())
                                                                .build());
                                            } else if (event instanceof SKUChanged) {
                                                SKUChanged changed = (SKUChanged) event;
                                                return entityRef(
                                                        changed.getSKU()
                                                                .getParentId())
                                                        .ask(ChangeSKU
                                                                .builder()
                                                                .sKU(changed.getSKU())
                                                                .build());
                                            } else if (event instanceof SKURemoved) {
                                                SKURemoved removed = (SKURemoved) event;
                                                return entityRef(
                                                        removed.getSKU().getParentId())
                                                        .ask(RemoveSKU
                                                                .builder()
                                                                .sKU(removed.getSKU())
                                                                .build());
                                            } else {
                                                return CompletableFuture
                                                        .completedFuture(Done
                                                                .getInstance());
                                            }
                                        }));
    }

    private PersistentEntityRef<ItemCommand> entityRef(String id) {
        return persistentEntityRegistry.refFor(ItemEntity.class, id);
    }

    @Override
    public ServiceCall<NotUsed, Item> getItem(String itemId) {
        return req -> {
            return entityRef(itemId).ask(ItemCommand.GetItem.INSTANCE)
                    .thenApply(maybeProduct -> maybeProduct.orElseGet(() -> {
                        throw new NotFound("Item " + itemId + " not found");
                    }));
        };
    }
}
