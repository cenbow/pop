package com.chinamobile.pcrf;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SReturnVO_BAT complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SReturnVO_BAT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="paras" type="{http://www.chinamobile.com/PCRF/}SAVP" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ResultData" type="{http://www.chinamobile.com/PCRF/}SBatData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SReturnVO_BAT", propOrder = { "resultCode", "paras",
		"resultData" })
public class SReturnVOBAT {

	protected int resultCode;
	protected List<SAVP> paras;
	@XmlElement(name = "ResultData")
	protected List<SBatData> resultData;

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
	 * Gets the value of the resultData property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the resultData property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getResultData().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link SBatData }
	 * 
	 * 
	 */
	public List<SBatData> getResultData() {
		if (resultData == null) {
			resultData = new ArrayList<SBatData>();
		}
		return this.resultData;
	}

}
