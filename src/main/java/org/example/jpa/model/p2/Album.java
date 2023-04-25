package org.example.jpa.model.p2;

import javax.persistence.*;

@Entity
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String singer;

}