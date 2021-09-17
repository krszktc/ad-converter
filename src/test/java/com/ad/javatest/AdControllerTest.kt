package com.ad.javatest

import com.mongodb.client.MongoCollection
import com.ad.javatest.controller.AdController
import com.ad.javatest.model.AdResponse
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
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


@Disabled("By MOCKK issue with handle ctx.queryParam(). The tests are randomly passed")
class AdControllerTest {

    private val fieldName = "mockParamName"
    private val dbMock = mockk<MongoCollection<AdResponse>>()
    private val mockContext = mockk<Context>()
    private val adController = AdController(dbMock, fieldName)

    private val adResponse = AdResponse(
        ObjectId("114352581b25a13e264ff4e9"),
        listOf(
            Tag(
                ObjectId("214352581b25a13e264ff4e9"),
                "someTagA",
                "someUrlA",
                null,
                emptyList()
            ),
            Tag(
                ObjectId("314352581b25a13e264ff4e9"),
                "someTagB",
                "someUrlB",
                null,
                emptyList()
            )
        )
    )

    @BeforeEach
    fun setUp() {
        every { mockContext.queryParam(fieldName) } returns "614352581b25a13e264ff4e9"
    }

    @Test
    fun `should successfully generate xml`() {
        // GIVEN
        val contentTypeHandler = slot<String>()
        val responseHandler = slot<String>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.contentType(capture(contentTypeHandler)) } returns mockContext
        every { mockContext.result(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.aggregate(any()).first() } returns adResponse
        adController.findById(mockContext)
        // THEN
        assertThat(statusHandler.isCaptured).isFalse()
        assertThat(contentTypeHandler.captured).isEqualTo("xml")
        assertThat(responseHandler.captured)
            .contains("<Ad id=\"114352581b25a13e264ff4e9\">")
            .contains("<AdTitle>someTagA</AdTitle>")
            .contains("<AdTitle>someTagB</AdTitle>")
    }

    @Test
    fun `shouldn't generate xml by by missing entity`() {
        // GIVEN
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.aggregate(any()).first() } returns null
        adController.findById(mockContext)
        // THEN
        assertThat(statusHandler.captured).isEqualTo(404)
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.BAD_REQUEST)
        assertThat(responseHandler.captured.reason).isEqualTo("AdResponse doesn't exist")
    }

    @Test
    fun `should handle error during xml conversion`() {
        // GIVEN
        val errorMessage = "Conversion issue !!!"
        val responseHandler = slot<StatusInfo>()
        val statusHandler = slot<Int>()
        // WHEN
        every { mockContext.json(capture(responseHandler)) } returns mockContext
        every { mockContext.status(capture(statusHandler)) } returns mockContext
        every { dbMock.aggregate(any()).first() } throws Exception(errorMessage)
        adController.findById(mockContext)
        // THEN
        assertThat(statusHandler.captured).isEqualTo(400)
        assertThat(responseHandler.captured.state).isEqualTo(StatusType.ERROR)
        assertThat(responseHandler.captured.reason).isEqualTo(errorMessage)
    }

}