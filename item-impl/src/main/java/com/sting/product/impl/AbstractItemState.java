package com.sting.product.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;

import com.sting.product.Item;
import org.immutables.value.Value;

import java.time.LocalDateTime;

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = ItemState.class)
public interface AbstractItemState {
	@Value.Parameter
	Item getItem();

	@Value.Parameter
	LocalDateTime getTimestamp();
}