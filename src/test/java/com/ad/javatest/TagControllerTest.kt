package com.ad.javatest

import com.mongodb.client.MongoCollection
import com.ad.javatest.controller.TagController
import com.ad.javatest.model.StatusInfo
import com.ad.javatest.model.Tag
import com.ad.javatest.model.enums.StatusType
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class TagControllerTest {

    private val dbMock = mockk<MongoCollection<Tag>>()
    private val mockContext = mockk<Context>()
    private val tagController = TagController(dbMock)

    private val tag = Tag(
        ObjectId("614352581b25a13e264ff4e9"),
        "someTag",
        "someUrl",
        null,
        emptyList()
    )

    @BeforeEach
    fun setUp() {
        every { mockContext.bodyAsClass(Tag::class.java) } returns tag
    }

    @Test
    fun `should successfully save`() {
        // GIVEN
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.insertOne(any()) } returns Unit
        tagController.save(mockContext)
        // THEN
        assertThat(statusHandler.isCaptured).isFalse()
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.SUCCESS)
        assertThat(responseHandler.captured.reason).isEqualTo("Tag id: ${tag.id} saved")
    }

    @Test
    fun `should handle error during save`() {
        // GIVEN
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        val errorMessage = "BOOM !!!"
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.insertOne(any()) } throws Exception(errorMessage)
        tagController.save(mockContext)
        // THEN
        assertThat(statusHandler.captured).isEqualTo(400)
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.ERROR)
        assertThat(responseHandler.captured.reason).isEqualTo(errorMessage)
    }

}