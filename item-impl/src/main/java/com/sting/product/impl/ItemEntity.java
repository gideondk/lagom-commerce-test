/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.product.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.sting.product.Item;
import com.sting.product.ItemSize;
import com.sting.skupusher.api.SKU;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemEntity
        extends
        PersistentEntity<ItemCommand, ItemEvent, Optional<Item>> {

    @Override
    public Behavior initialBehavior(Optional<Optional<Item>> snapshotState) {
        Optional<Item> item = snapshotState.flatMap(Function.identity());

        if (item.isPresent()) {
            return created(item.get());
        } else {
            return notCreated();
        }
    }

    private Stream<ItemSize> filterForSKU(Stream<ItemSize> in, SKU sku) {
        return in.filter(x -> x.getSKU() != sku.getSKU());
    }

    private Behavior created(Item item) {
        BehaviorBuilder b = newBehaviorBuilder(Optional.of(item));

        b.setCommandHandler(AddSKU.class, (cmd, ctx) -> {
            SKU sku = cmd.getSKU();
            ItemSize newSize = ItemSize.builder().stock(0).size(sku.getSize()).sKU(sku.getSKU()).build();
            List<ItemSize> sizes = Stream.concat(filterForSKU(item.getSizes().stream(), sku), Stream.of(newSize)).collect(Collectors.toList());

            return ctx.thenPersist(
                    ItemChanged.builder().item(item.withSizes(sizes)).build(),
                    evt -> ctx.reply(Done.getInstance()));
        });

        b.setCommandHandler(RemoveSKU.class, (cmd, ctx) -> {
            List<ItemSize> sizes = filterForSKU(item.getSizes().stream(), cmd.getSKU()).collect(Collectors.toList());
            if (sizes.size() == 0) {
                return ctx.thenPersist(
                        ItemRemoved.builder().id(item.getId()).build(),
                        evt -> ctx.reply(Done.getInstance()));
            } else {
                return ctx.thenPersist(
                        ItemChanged.builder().item(item.withSizes(sizes)).build(),
                        evt -> ctx.reply(Done.getInstance()));
            }
        });

        b.setEventHandlerChangingBehavior(ItemChanged.class,
                evt -> created(evt.getItem()));

        b.setEventHandlerChangingBehavior(ItemRemoved.class,
                evt -> created(state().get() // Yikes
                        .withHasBeenRemoved(true)));

        b.setReadOnlyCommandHandler(ItemCommand.GetItem.class,
                (get, ctx) -> ctx.reply(state()));

        return b.build();
    }

    private Behavior notCreated() {
        BehaviorBuilder b = newBehaviorBuilder(Optional.empty());

        b.setReadOnlyCommandHandler(ItemCommand.GetItem.class,
                (cmd, ctx) -> ctx.reply(state()));

        b.setCommandHandler(AddSKU.class, (cmd, ctx) -> {
            SKU sku = cmd.getSKU();

            return ctx.thenPersist(
                    ItemCreated.builder().item(Item.builder().description(sku.getDescription()).id(sku.getParentId()).name(sku.getName()).mainImageURL(sku.getMainImageURL()).build()).build(),
                    evt -> ctx.reply(Done.getInstance()));
        });

        b.setReadOnlyCommandHandler(ChangeSKU.class,
                (cmd, ctx) -> ctx.invalidCommand("Product doesn't exist yet."));

        b.setReadOnlyCommandHandler(RemoveSKU.class,
                (cmd, ctx) -> ctx.invalidCommand("Product doesn't exist yet."));

        b.setEventHandlerChangingBehavior(ItemCreated.class,
                evt -> created(evt.getItem()));

        return b.build();
    }

}
