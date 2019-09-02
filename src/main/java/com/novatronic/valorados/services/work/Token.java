package com.novatronic.valorados.services.work;

import java.io.Serializable;
import java.util.Date;

public class Token implements Serializable {


	private static final long serialVersionUID = -7683468078993726393L;
	private String sn;
	private String seed;
	private Date birth;
	private Date death;
	private String tokenMAC;
	private String error;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		/* this.sn = "000".concat(sn); */
		this.sn = sn;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Date getDeath() {
		return death;
	}

	public void setDeath(Date death) {
		this.death = death;
	}

	public String getTokenMAC() {
		return tokenMAC;
	}

	public void setTokenMAC(String tokenMAC) {
		this.tokenMAC = tokenMAC;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}