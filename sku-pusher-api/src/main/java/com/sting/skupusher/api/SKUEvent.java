package com.sting.skupusher.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;

import org.immutables.value.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = Void.class)
@JsonSubTypes({@JsonSubTypes.Type(SKUCreated.class),
		@JsonSubTypes.Type(SKUChanged.class),
		@JsonSubTypes.Type(SKURemoved.class)})


public interface SKUEvent extends Jsonable, AggregateEvent<SKUEvent> {

	@Override
	default public AggregateEventTag<SKUEvent> aggregateTag() {
		return SKUEventTag.INSTANCE;
	}

	@Value.Immutable
	@ImmutableStyle
	@JsonTypeName("created")
	@JsonDeserialize(as = SKUCreated.class)
	abstract class AbstractSKUCreated implements SKUEvent {
		abstract public SKU getSKU();
	}

	@Value.Immutable
	@ImmutableStyle
	@JsonTypeName("changed")
	@JsonDeserialize(as = SKUChanged.class)
	abstract class AbstractSKUChanged implements SKUEvent {
		abstract public SKU getSKU();
	}

	@Value.Immutable
	@ImmutableStyle
	@JsonTypeName("removed")
	@JsonDeserialize(as = SKURemoved.class)
	abstract class AbstractSKURemoved implements SKUEvent {
		abstract public SKU getSKU();
	}
}