package com.sting.skupusher.api;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.sting.product.Item;
import org.immutables.value.Value;

import java.util.Optional;

public interface SKUCommand extends Jsonable {

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = CreateSKU.class)
    interface AbstractCreateSKU
            extends
            SKUCommand,
            CompressedJsonable,
            PersistentEntity.ReplyType<Done> {

        @Value.Parameter
        SKU getSKU();
    }

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = ChangeSKU.class)
    interface AbstractChangeSKU
            extends
            SKUCommand,
            CompressedJsonable,
            PersistentEntity.ReplyType<Done> {

        @Value.Parameter
        SKU getSKU();
    }

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = RemoveSKU.class)
    interface AbstractRemoveSKU
            extends
            SKUCommand,
            CompressedJsonable,
            PersistentEntity.ReplyType<Done> {

        @Value.Parameter
        SKU getSKU();
    }

}
