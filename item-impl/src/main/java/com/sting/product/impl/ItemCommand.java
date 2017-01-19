/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.product.impl;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.sting.product.Item;
import com.sting.skupusher.api.SKU;
import org.immutables.value.Value;

import java.util.Optional;

public interface ItemCommand extends Jsonable {

	@Value.Immutable
	@ImmutableStyle
	@JsonDeserialize(as = AddSKU.class)
	interface AbstractAddSKU
			extends
				ItemCommand,
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
				ItemCommand,
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
				ItemCommand,
				CompressedJsonable,
				PersistentEntity.ReplyType<Done> {

		@Value.Parameter
		SKU getSKU();
	}

	enum GetItem
			implements
				ItemCommand,
				PersistentEntity.ReplyType<Optional<Item>> {
		INSTANCE
	}
}
