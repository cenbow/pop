package com.chinamobile.pcrf;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SReturnVO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SReturnVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="paras" type="{http://www.chinamobile.com/PCRF/}SAVP" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subscriber" type="{http://www.chinamobile.com/PCRF/}SPccSubscriber" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subscribedService" type="{http://www.chinamobile.com/PCRF/}SSubscribedService" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subscribedUsrSessionPolicy" type="{http://www.chinamobile.com/PCRF/}SUsrSessionPolicy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subscribedUsrLocation" type="{http://www.chinamobile.com/PCRF/}SUsrLocation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SReturnVO", propOrder = { "resultCode", "paras", "subscriber",
		"subscribedService", "subscribedUsrSessionPolicy",
		"subscribedUsrLocation" })
public class SReturnVO {

	protected int resultCode;
	protected List<SAVP> paras;
	protected List<SPccSubscriber> subscriber;
	protected List<SSubscribedService> subscribedService;
	protected List<SUsrSessionPolicy> subscribedUsrSessionPolicy;
	protected List<SUsrLocation> subscribedUsrLocation;

	/**
	 * Gets the value of the resultCode property.
	 * 
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * Sets the value of the resultCode property.
	 * 
	 */
	public void setResultCode(int value) {
		this.resultCode = value;
	}

	/**
	 * Gets the value of the paras property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the paras property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getParas().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link SAVP }
	 * 
	 * 
	 */
	public List<SAVP> getParas() {
		if (paras == null) {
			paras = new ArrayList<SAVP>();
		}
		return this.paras;
	}

	/**
	 * Gets the value of the subscriber property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the subscriber property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSubscriber().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SPccSubscriber }
	 * 
	 * 
	 */
	public List<SPccSubscriber> getSubscriber() {
		if (subscriber == null) {
			subscriber = new ArrayList<SPccSubscriber>();
		}
		return this.subscriber;
	}

	/**
	 * Gets the value of the subscribedService property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the subscribedService property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSubscribedService().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SSubscribedService }
	 * 
	 * 
	 */
	public List<SSubscribedService> getSubscribedService() {
		if (subscribedService == null) {
			subscribedService = new ArrayList<SSubscribedService>();
		}
		return this.subscribedService;
	}

	/**
	 * Gets the value of the subscribedUsrSessionPolicy property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the subscribedUsrSessionPolicy property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSubscribedUsrSessionPolicy().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SUsrSessionPolicy }
	 * 
	 * 
	 */
	public List<SUsrSessionPolicy> getSubscribedUsrSessionPolicy() {
		if (subscribedUsrSessionPolicy == null) {
			subscribedUsrSessionPolicy = new ArrayList<SUsrSessionPolicy>();
		}
		return this.subscribedUsrSessionPolicy;
	}

	/**
	 * Gets the value of the subscribedUsrLocation property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the subscribedUsrLocation property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSubscribedUsrLocation().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SUsrLocation }
	 * 
	 * 
	 */
	public List<SUsrLocation> getSubscribedUsrLocation() {
		if (subscribedUsrLocation == null) {
			subscribedUsrLocation = new ArrayList<SUsrLocation>();
		}
		return this.subscribedUsrLocation;
	}

}
