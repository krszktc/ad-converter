package com.ad.javatest.model.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId


class ObjectIdListSerializer : JsonSerializer<List<ObjectId>>() {
    override fun serialize(values: List<ObjectId>, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartArray()
        values.forEach { gen.writeString(it.toString()) }
        gen.writeEndArray()
    }
}