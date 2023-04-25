package org.example.jpa.model.p1;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;


@Entity
public class Member {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	public Team team;

	public String name;
	public int age;

	@Override
	public String toString() {
		return "Member{" +
				"id=" + id +
				", name='" + this.getName() + '\'' +
				", age=" + age +
				", team=" + this.getTeam().getName() +
				'}';
	}
}
