package com.ai.bdx.pop.adapter.common.model;

import java.io.Serializable;

public class POPDw extends BaseModel implements Serializable {

    private int dwTypeId;

    private String dwUsername;

    private String dwPassword;

    private String dwAddr;

    public POPDw(){
    	
    }
	public POPDw(int dwTypeId,
			String dwUsername, String dwPassword, String dwAddr) {
		super();
		this.dwTypeId = dwTypeId;
		this.dwUsername = dwUsername;
		this.dwPassword = dwPassword;
		this.dwAddr = dwAddr;
	}

	
	public String toString() {
		return "DamDw [dwTypeId=" + dwTypeId + ", dwUsername=" + dwUsername + ", dwPassword="
				+ dwPassword + ", dwAddr=" + dwAddr + "]";
	}

	public int getDwTypeId() {
		return dwTypeId;
	}

	public void setDwTypeId(int dwTypeId) {
		this.dwTypeId = dwTypeId;
	}

	public String getDwUsername() {
		return dwUsername;
	}

	public void setDwUsername(String dwUsername) {
		this.dwUsername = dwUsername;
	}

	public String getDwPassword() {
		return dwPassword;
	}

	public void setDwPassword(String dwPassword) {
		this.dwPassword = dwPassword;
	}

	public String getDwAddr() {
		return dwAddr;
	}

	public void setDwAddr(String dwAddr) {
		this.dwAddr = dwAddr;
	}

	

    
	
}
