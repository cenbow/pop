package com.chinamobile.pcrf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SInBatFileParaVO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SInBatFileParaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BATInfo" type="{http://www.chinamobile.com/PCRF/}SPccBatFile"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SInBatFileParaVO", propOrder = { "batInfo" })
public class SInBatFileParaVO {

	@XmlElement(name = "BATInfo", required = true)
	protected SPccBatFile batInfo;

	/**
	 * Gets the value of the batInfo property.
	 * 
	 * @return possible object is {@link SPccBatFile }
	 * 
	 */
	public SPccBatFile getBATInfo() {
		return batInfo;
	}

	/**
	 * Sets the value of the batInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link SPccBatFile }
	 * 
	 */
	public void setBATInfo(SPccBatFile value) {
		this.batInfo = value;
	}

}
