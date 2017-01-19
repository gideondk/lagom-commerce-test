package com.sting.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;

import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = ItemSize.class)
public interface AbstractItemSize {
	@Value.Parameter
	String getSKU();

	@Value.Parameter
	String getSize();

	@Value.Parameter
	int getStock();
}
