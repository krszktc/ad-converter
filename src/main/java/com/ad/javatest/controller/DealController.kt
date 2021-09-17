package com.ad.javatest.controller

import com.ad.javatest.model.Deal
import com.ad.javatest.persistence.FindById
import com.ad.javatest.persistence.QueryHandler
import com.ad.javatest.persistence.Save
import com.mongodb.client.MongoCollection
import io.javalin.http.Context

class DealController(private val client: MongoCollection<Deal>) {

    fun save(ctx: Context): Context =
        QueryHandler.save(ctx, Save(client))

    fun findById(ctx: Context): Context =
        QueryHandler.findById(ctx, FindById(client))

}