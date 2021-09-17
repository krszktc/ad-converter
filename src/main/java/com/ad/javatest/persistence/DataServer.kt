package com.ad.javatest.persistence

import com.mongodb.MongoClient
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider


object DataServer {
    const val DB_NAME = "MOCK_DB"

    val server = MongoServer(MemoryBackend())
    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClient.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    inline fun <reified T> getClient(collectionName: String): MongoCollection<T> =
        MongoClient(ServerAddress(server.bind()))
            .getDatabase(DB_NAME)
            .getCollection(collectionName, T::class.java)
            .withCodecRegistry(pojoCodecRegistry)

}