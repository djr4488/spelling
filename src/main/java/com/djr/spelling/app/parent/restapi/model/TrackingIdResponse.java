package com.djr.spelling.app.parent.restapi.model;

import com.djr.spelling.app.BaseResponse;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IMac on 9/6/2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TrackingIdResponse extends BaseResponse implements Serializable {
	private static final long serialVersionUID = 1;

	@XmlElement
	public String trackingId;

	public TrackingIdResponse() { }

	public TrackingIdResponse(String trackingId) {
		this.trackingId = trackingId;
	}
}
