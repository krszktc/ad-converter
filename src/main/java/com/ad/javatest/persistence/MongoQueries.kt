package com.ad.javatest.persistence

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates.lookup
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.result.UpdateResult
import com.ad.javatest.model.AdResponse
import com.ad.javatest.model.abstraction.BsonDataModel
import com.ad.javatest.model.enums.CollectionType
import org.bson.types.ObjectId


class Save<T>(private val client: MongoCollection<T>): DbSave<T> {
    override fun save(data: T) =
        client.insertOne(data)
}

class FindById<T>(private val client: MongoCollection<T>): DbFindById<T> {
    override fun findById(id: ObjectId): T? =
        client.find(eq("_id", id)).first()
}

class Update<T: BsonDataModel>(private val client: MongoCollection<T>): DbUpdate<T> {
    override fun update(data: T): UpdateResult =
        client.updateOne(
            eq("_id", data.id),
            data.toBson()
        )
}

class FindAdById(private val client: MongoCollection<AdResponse>): DbFindById<AdResponse> {
    override fun findById(id: ObjectId): AdResponse? =
        client.aggregate(listOf(
            match(eq("_id", id)),
            lookup(CollectionType.TAG.dbName, "tags", "_id", "tags")
        )).first()
}