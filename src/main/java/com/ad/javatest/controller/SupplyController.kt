package com.ad.javatest.controller

import com.mongodb.client.MongoCollection
import com.ad.javatest.model.Supply
import com.ad.javatest.persistence.*
import io.javalin.http.Context


class SupplyController(private val client: MongoCollection<Supply>) {

    fun save(ctx: Context): Context =
        QueryHandler.save(ctx, Save(client))

    fun update(ctx: Context): Context =
        QueryHandler.update(ctx, Update(client))

    fun findById(ctx: Context): Context =
        QueryHandler.findById(ctx, FindById(client))

}