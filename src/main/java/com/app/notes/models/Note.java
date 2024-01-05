package com.app.notes.models;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "note")
public class Note {

	@Id
	private String id;

	private String title;

	private String content;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User user;

}
