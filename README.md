
## INFO

- an API that will take in and retrieve singular **Supply** as JSON (see below)
- an API that will take in and retrieve singular **Deals** as JSON (see below)
- an API that will take in and retrieve singular **Tags** as JSON (see below)
- an ad API that will return an ad in the specified format below. Ads are
  requested using a supply ID, and each response should contain a single ad 
  (distinguishable by the Ad id in the response).
  

### Supply
```json
{
  "id": 123,
  "name": "Supply",
  "tags": [789, 123]
}
```

### Deal
```json
{
  "id": 456,
  "name": "Deal"
}
```

### Tags
```json
{
    "id": 789,
    "name": "Tag",
    "tagUrl": "https://example.com/my-video.mp4",
    "dealId": 456,
    "supplySources": [123, 456]
}
```

### Ad Response
```xml
<?xml version="1.0" encoding="utf-8"?>
<VAST version="2.0">
	<Ad id="789">
		<InLine>
			<AdTitle>Tag</AdTitle>
			<Creatives>
				<Creative sequence="1">
					<Linear>
						<MediaFiles>
							<MediaFile delivery="progressive" bitrate="256" width="480" height="352" type="video/mp4">https://example.com/my-video.mp4</MediaFile>
						</MediaFiles>
					</Linear>
				</Creative>
			</Creatives>
		</InLine>
	</Ad>
</VAST>
```
