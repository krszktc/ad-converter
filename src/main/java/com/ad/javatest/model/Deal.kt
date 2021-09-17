package com.ad.javatest.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.ad.javatest.model.abstraction.DataModel
import org.bson.types.ObjectId

class Deal(
    @JsonSerialize(using = ToStringSerializer::class)
    override var id: ObjectId? = null,
    var name: String? = null
): DataModel