package org.example.jpa.model.p2;

import javax.persistence.*;

@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long price;

	private String name;

}
