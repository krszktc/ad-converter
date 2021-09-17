package com.ad.javatest.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.ad.javatest.model.abstraction.XmlDataModel
import org.bson.types.ObjectId

class AdResponse(
    @JsonSerialize(using = ToStringSerializer::class)
    override var id: ObjectId? = null,
    var tags: List<Tag> = emptyList()
) : XmlDataModel {
    override fun toXml() = """
        <?xml version="1.0" encoding="utf-8"?>
        <VAST version="2.0">
            <Ad id="$id">
                ${tags.joinToString(separator = "") {
                """
                    <InLine>
                        <AdTitle>${it.name}</AdTitle>
                        <Creatives>
                            <Creative sequence="1">
                                <Linear>
                                    <MediaFiles>
                                        <MediaFile delivery="progressive" bitrate="256" width="480" height="352" type="video/mp4">${it.tagUrl}</MediaFile>
                                    </MediaFiles>
                                </Linear>
                            </Creative>
                        </Creatives>
                    </InLine>
                """
                }}
            </Ad>
        </VAST>
    """
}