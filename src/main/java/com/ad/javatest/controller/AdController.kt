package com.ad.javatest.controller

import com.mongodb.client.MongoCollection
import com.ad.javatest.model.AdResponse
import com.ad.javatest.persistence.FindAdById
import com.ad.javatest.persistence.QueryHandler
import io.javalin.http.Context


class AdController(private val client: MongoCollection<AdResponse>, private val fieldName: String) {

    fun findById(ctx: Context): Context =
        QueryHandler.getAsXml(ctx, FindAdById(client), fieldName)

}