package com.ad.javatest.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.ad.javatest.model.abstraction.BsonDataModel
import com.ad.javatest.model.serializer.ObjectIdListSerializer
import org.bson.conversions.Bson
import org.bson.types.ObjectId


class Supply(
    @JsonSerialize(using = ToStringSerializer::class)
    override var id: ObjectId? = null,
    var name: String? = null,
    @JsonSerialize(using = ObjectIdListSerializer::class)
    var tags: List<ObjectId> = emptyList()
): BsonDataModel {
    override fun toBson(): Bson = combine(
        set("name", name),
        set("tags", tags)
    )
}