/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.sting.product.impl;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.serialization.Jsonable;

import com.sting.product.Item;
import org.immutables.value.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = Void.class)
@JsonSubTypes({@JsonSubTypes.Type(ItemCreated.class),
		@JsonSubTypes.Type(ItemChanged.class),
		@JsonSubTypes.Type(ItemRemoved.class)})
public interface ItemEvent extends Jsonable {

	@Value.Immutable
	@ImmutableStyle
	@JsonTypeName("created")
	@JsonDeserialize(as = ItemCreated.class)
	abstract class AbstractItemCreated implements ItemEvent {
		abstract Item getItem();
	}

	@Value.Immutable
	@ImmutableStyle
	@JsonTypeName("changed")
	@JsonDeserialize(as = ItemChanged.class)
	abstract class AbstractItemChanged implements ItemEvent {
		abstract Item getItem();
	}

	@Value.Immutable
	@ImmutableStyle
	@JsonTypeName("removed")
	@JsonDeserialize(as = ItemRemoved.class)
	abstract class AbstractItemRemoved implements ItemEvent {
		abstract String getId();
	}
}