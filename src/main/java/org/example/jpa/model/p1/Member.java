package org.example.jpa.model.p1;

import com.sun.org.apache.xpath.internal.operations.Or;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@NamedQuery(name = "Member.findByUsername", query = "SELECT m.name FROM Member m WHERE m.name = :username")
public class Member {
	public void setAddress(Address address) {
		this.address = address;
	}


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

//	@OneToMany(mappedBy = "member")
//	private List<Order> orders;

	@ManyToOne(fetch = FetchType.LAZY)
	public Team team;

	public String name;
	public int age;

	@Embedded
	private Address address;

	public Address getAddress() {
		return address;
	}

//	public void addOrder(Order order){
//		order.setMember(this);
//		this.orders.add(order);
//	}

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
