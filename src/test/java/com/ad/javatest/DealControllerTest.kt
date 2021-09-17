package com.ad.javatest

import com.mongodb.client.MongoCollection
import com.ad.javatest.controller.DealController
import com.ad.javatest.model.Deal
import com.ad.javatest.model.StatusInfo
import com.ad.javatest.model.enums.StatusType
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


@Disabled("By MOCKK issue with handle ctx.pathParam(). The tests are randomly passed")
class DealControllerTest {

    private val dbMock = mockk<MongoCollection<Deal>>()
    private val dealController = DealController(dbMock)
    private val mockContext = mockk<Context>()

    private val deal = Deal(
        ObjectId("614352581b25a13e264ff4e9"),
        "someDeal"
    )

    @BeforeEach
    fun setUp() {
        every { mockContext.pathParam(any()) } returns "614352581b25a13e264ff4e9"
    }

    @Test
    fun `should successfully find by Id`() {
        // GIVEN
        val responseHandler = slot<Deal>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.find(any<Bson>()).first() } returns deal
        dealController.findById(mockContext)
        // THEN
        assertThat(statusHandler.isCaptured).isFalse()
        assertThat(responseHandler.captured.id).isEqualTo(deal.id)
        assertThat(responseHandler.captured.name).isEqualTo(deal.name)
    }

    @Test
    fun `shouldn't find entity by missing id`() {
        // GIVEN
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.find(any<Bson>()).first() } returns null
        dealController.findById(mockContext)
        // THEN
        assertThat(statusHandler.captured).isEqualTo(404)
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.BAD_REQUEST)
        assertThat(responseHandler.captured.reason).isEqualTo("Deal doesn't exist")
    }

    @Test
    fun `should handle error during searching by id`() {
        // GIVEN
        val errorMessage = "Searching issue !!!"
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.find(any<Bson>()).first() } throws Exception(errorMessage)
        dealController.findById(mockContext)
        // THEN
        assertThat(statusHandler.captured).isEqualTo(400)
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.ERROR)
        assertThat(responseHandler.captured.reason).isEqualTo(errorMessage)
    }

}