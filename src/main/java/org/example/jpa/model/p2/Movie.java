package org.example.jpa.model.p2;

import javax.persistence.*;

@Entity
public class Movie{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String director;

}