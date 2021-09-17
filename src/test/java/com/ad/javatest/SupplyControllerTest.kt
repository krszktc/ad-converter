package com.ad.javatest

import com.mongodb.client.MongoCollection
import com.mongodb.client.result.UpdateResult
import com.ad.javatest.controller.SupplyController
import com.ad.javatest.model.StatusInfo
import com.ad.javatest.model.Supply
import com.ad.javatest.model.enums.StatusType
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SupplyControllerTest {

    private val dbMock = mockk<MongoCollection<Supply>>()
    private val mockContext = mockk<Context>()
    private val supplyController = SupplyController(dbMock)


    private val supply = Supply(
        ObjectId("614352581b25a13e264ff4e9"),
        "someSupply",
        emptyList()
    )

    @BeforeEach
    fun setUp() {
        every { mockContext.bodyAsClass(Supply::class.java) } returns supply
    }

    @Test
    fun `should successfully update`() {
        // GIVEN
        val updateResult = mockk<UpdateResult>()
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.updateOne(any(), any<Bson>()) } returns updateResult
        every { updateResult.matchedCount } returns 1
        supplyController.update(mockContext)
        // THEN
        assertThat(statusHandler.isCaptured).isFalse()
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.SUCCESS)
        assertThat(responseHandler.captured.reason).isEqualTo("Supply successfully updated")
    }

    @Test
    fun `shouldn't update by non existed entity`() {
        // GIVEN
        val updateResult = mockk<UpdateResult>()
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.updateOne(any(), any<Bson>()) } returns updateResult
        every { updateResult.matchedCount } returns 0
        supplyController.update(mockContext)
        // THEN
        assertThat(statusHandler.captured).isEqualTo(404)
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.BAD_REQUEST)
        assertThat(responseHandler.captured.reason).isEqualTo("Supply doesn't exist")
    }

    @Test
    fun `should catch error during update`() {
        // GIVEN
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        val errorMessage = "FIRE IN THE HOLE !!!"
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.updateOne(any(), any<Bson>()) } throws Exception(errorMessage)
        supplyController.update(mockContext)
        // THEN
        assertThat(statusHandler.captured).isEqualTo(400)
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.ERROR)
        assertThat(responseHandler.captured.reason).isEqualTo(errorMessage)
    }

}