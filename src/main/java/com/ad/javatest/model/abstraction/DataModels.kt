package com.ad.javatest.model.abstraction

import org.bson.conversions.Bson
import org.bson.types.ObjectId

interface DataModel {
    var id: ObjectId?
}

interface BsonDataModel: DataModel {
    fun toBson(): Bson
}

interface XmlDataModel: DataModel {
    fun toXml(): String
}