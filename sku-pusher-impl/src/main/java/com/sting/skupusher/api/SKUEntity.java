package com.sting.skupusher.api;

/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.sting.product.ItemSize;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class SKUEntity
        extends
        PersistentEntity<SKUCommand, SKUEvent, Optional<SKU>> {

    @Override
    public Behavior initialBehavior(Optional<Optional<SKU>> snapshotState) {
        Optional<SKU> sku = snapshotState.flatMap(Function.identity());

        if (sku.isPresent()) {
            return created(sku.get());
        } else {
            return notCreated();
        }
    }

    private Stream<ItemSize> filterForSKU(Stream<ItemSize> in, SKU sku) {
        return in.filter(x -> x.getSKU() != sku.getSKU());
    }

    private Behavior created(SKU sku) {
        BehaviorBuilder b = newBehaviorBuilder(Optional.of(sku));

        b.setCommandHandler(ChangeSKU.class, (cmd, ctx) -> {
            return ctx.thenPersist(
                    SKUChanged.builder().sKU(cmd.getSKU()).build(),
                    evt -> ctx.reply(Done.getInstance()));
        });

        b.setCommandHandler(RemoveSKU.class, (cmd, ctx) -> {
            return ctx.thenPersist(
                    SKURemoved.builder().sKU(cmd.getSKU()).build(),
                    evt -> ctx.reply(Done.getInstance()));
        });


        b.setReadOnlyCommandHandler(CreateSKU.class,
                (cmd, ctx) -> ctx.invalidCommand("SKU already exists."));

        b.setEventHandlerChangingBehavior(SKURemoved.class,
                evt -> notCreated());

        return b.build();
    }

    private Behavior notCreated() {
        BehaviorBuilder b = newBehaviorBuilder(Optional.empty());

        b.setCommandHandler(CreateSKU.class, (cmd, ctx) -> {
            SKU sku = cmd.getSKU();

            return ctx.thenPersist(
                    SKUCreated.builder().sKU(cmd.getSKU()).build(),
                    evt -> ctx.reply(Done.getInstance()));
        });

        b.setReadOnlyCommandHandler(ChangeSKU.class,
                (cmd, ctx) -> ctx.invalidCommand("SKU doesn't exist yet."));

        b.setReadOnlyCommandHandler(RemoveSKU.class,
                (cmd, ctx) -> ctx.invalidCommand("SKU doesn't exist yet."));

        b.setEventHandlerChangingBehavior(SKUCreated.class,
                evt -> created(evt.getSKU()));

        return b.build();
    }

}

