package com.ad.javatest.persistence

import com.ad.javatest.model.abstraction.DataModel
import com.ad.javatest.model.StatusInfo
import com.ad.javatest.model.abstraction.XmlDataModel
import com.ad.javatest.model.enums.StatusType
import io.javalin.http.Context
import org.bson.types.ObjectId
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception


object QueryHandler {
    val LOGGER: Logger = LoggerFactory.getLogger(DataServer::class.java)

    inline fun <reified T : DataModel> save(ctx: Context, repo: DbSave<T>): Context {
        val body = ctx.body<T>()
        val typeName = T::class.simpleName
        LOGGER.info("Saving $typeName")

        return try {
            repo.save(body)
            ctx.json(StatusInfo(StatusType.SUCCESS, "$typeName id: ${body.id} saved"))
        } catch (exc: Exception) {
            ctx.json(StatusInfo(StatusType.ERROR, exc.message ?: "Undefined issue during $typeName save"))
                .status(HttpStatus.BAD_REQUEST_400)
        }
    }

    inline fun <reified T : DataModel> findById(ctx: Context, repo: DbFindById<T>): Context {
        val id = ctx.pathParam("id")
        val typeName = T::class.simpleName
        LOGGER.info("Getting $typeName")

        return try {
            val result = repo.findById(ObjectId(id))
            if (result != null)
                ctx.json(result) else
                ctx.json(StatusInfo(StatusType.BAD_REQUEST, "$typeName doesn't exist"))
                    .status(HttpStatus.NOT_FOUND_404)
        } catch (exc: Exception) {
            ctx.json(StatusInfo(StatusType.ERROR, exc.message ?: "Undefined issue during $typeName find"))
                .status(HttpStatus.BAD_REQUEST_400)
        }
    }

    inline fun <reified T : DataModel> update(ctx: Context, repo: DbUpdate<T>): Context {
        val body = ctx.body<T>()
        val typeName = T::class.simpleName
        LOGGER.info("Updating $typeName")

        return try {
            if (repo.update(body).matchedCount > 0)
                ctx.json(StatusInfo(StatusType.SUCCESS, "$typeName successfully updated")) else
                ctx.json(StatusInfo(StatusType.BAD_REQUEST, "$typeName doesn't exist"))
                    .status(HttpStatus.NOT_FOUND_404)
        } catch (exc: Exception) {
            ctx.json(StatusInfo(StatusType.ERROR, exc.message ?: "Undefined issue during $typeName update"))
                .status(HttpStatus.BAD_REQUEST_400)
        }
    }

    inline fun <reified T : XmlDataModel> getAsXml(ctx: Context, repo: DbFindById<T>, fieldName: String): Context {
        val id = ctx.queryParam(fieldName)
        val typeName = T::class.simpleName
        LOGGER.info("Getting $typeName by supplyId: $id")

        return try {
            val result = repo.findById(ObjectId(id))
            if (result != null)
                ctx.result(result.toXml()).contentType("xml") else
                ctx.json(StatusInfo(StatusType.BAD_REQUEST, "$typeName doesn't exist"))
                    .status(HttpStatus.NOT_FOUND_404)
        } catch (exc: Exception) {
            ctx.json(StatusInfo(StatusType.ERROR, exc.message ?: "Undefined issue during $typeName conversion"))
                .status(HttpStatus.BAD_REQUEST_400)
        }
    }

}