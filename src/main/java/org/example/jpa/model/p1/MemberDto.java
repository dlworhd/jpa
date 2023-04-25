package org.example.jpa.model.p1;


public class MemberDto {
	@Override
	public String toString() {
		return "MemberDto{" +
				"name='" + username + '\'' +
				", age=" + age +
				'}';
	}

	private String username;
	private Integer age;

	public MemberDto(String username, Integer age) {
		this.username = username;
		this.age = age;
	}


}
