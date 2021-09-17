package com.ad.javatest.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.ad.javatest.model.abstraction.BsonDataModel
import com.ad.javatest.model.serializer.ObjectIdListSerializer
import org.bson.conversions.Bson
import org.bson.types.ObjectId


class Tag(
    @JsonSerialize(using = ToStringSerializer::class)
    override var id: ObjectId? = null,
    var name: String? = null,
    var tagUrl: String? = null,
    @JsonSerialize(using = ToStringSerializer::class)
    var dealId: ObjectId? = null,
    @JsonSerialize(using = ObjectIdListSerializer::class)
    var supplySources: List<ObjectId> = emptyList()
): BsonDataModel {
    override fun toBson(): Bson = combine(
        set("name", name),
        set("tagUrl", tagUrl),
        set("dealId", dealId),
        set("supplySources", supplySources)
    )
}