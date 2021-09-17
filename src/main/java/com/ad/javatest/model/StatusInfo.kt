package com.ad.javatest.model

import com.ad.javatest.model.enums.StatusType

class StatusInfo(
    val state: StatusType,
    val reason: String
)