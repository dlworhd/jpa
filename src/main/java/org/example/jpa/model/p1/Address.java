package org.example.jpa.model.p1;

import javax.persistence.Embeddable;


@Embeddable
public class Address {

	public Address() {
	}

	public Address(String zip, String addr1, String addr2) {
		this.zip = zip;
		this.addr1 = addr1;
		this.addr2 = addr2;
	}

	private String zip;
	private String addr1;
	private String addr2;

	@Override
	public String toString() {
		return "Address{" +
				"zip='" + zip + '\'' +
				", addr1='" + addr1 + '\'' +
				", addr2='" + addr2 + '\'' +
				'}';
	}
}
