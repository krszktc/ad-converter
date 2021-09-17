package com.ad.javatest.model.enums

enum class CollectionType(val dbName: String) {
    SUPPLY("supplyCollection"),
    DEAL("dealCollection"),
    TAG("tagCollection")
}