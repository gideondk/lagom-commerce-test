package com.sting.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = Item.class)
public interface AbstractItem {
	@Value.Parameter
	String getId();

	@Value.Parameter
	String getName();

	@Value.Parameter
	String getDescription();

	@Value.Parameter
	Boolean getHasBeenRemoved();

	@Value.Parameter
	String getMainImageURL();

	@Value.Parameter
	List<ItemSize> getSizes();
}