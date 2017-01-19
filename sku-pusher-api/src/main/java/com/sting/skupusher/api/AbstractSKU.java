package com.sting.skupusher.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import org.immutables.value.Value;

import java.net.URL;
import java.util.List;

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = SKU.class)
public interface AbstractSKU {
	@Value.Parameter
	String getParentId();

	@Value.Parameter
	String getSKU();

	@Value.Parameter
	String getSize();

	@Value.Parameter
	String getName();

	@Value.Parameter
	String getDescription();

	@Value.Parameter
	String getMainImageURL();
}