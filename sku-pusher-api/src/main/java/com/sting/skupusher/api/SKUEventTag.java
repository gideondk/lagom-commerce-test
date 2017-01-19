package com.sting.skupusher.api;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class SKUEventTag {
    public static final AggregateEventTag<SKUEvent> INSTANCE =
            AggregateEventTag.of(SKUEvent.class);
}