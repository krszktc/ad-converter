package com.ad.javatest.persistence

import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId

interface DbSave<T> {
    fun save(data: T)
}

interface DbFindById<T> {
    fun findById(id: ObjectId): T?
}

interface DbUpdate<T> {
    fun update(data: T): UpdateResult
}

