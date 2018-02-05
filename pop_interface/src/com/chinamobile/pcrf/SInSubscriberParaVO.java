package com.chinamobile.pcrf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SInSubscriberParaVO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SInSubscriberParaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subscriber" type="{http://www.chinamobile.com/PCRF/}SPccSubscriber"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SInSubscriberParaVO", propOrder = { "subscriber" })
public class SInSubscriberParaVO {

	@XmlElement(required = true)
	protected SPccSubscriber subscriber;

	/**
	 * Gets the value of the subscriber property.
	 * 
	 * @return possible object is {@link SPccSubscriber }
	 * 
	 */
	public SPccSubscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * Sets the value of the subscriber property.
	 * 
	 * @param value
	 *            allowed object is {@link SPccSubscriber }
	 * 
	 */
	public void setSubscriber(SPccSubscriber value) {
		this.subscriber = value;
	}

}
