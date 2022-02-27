package com.ejs.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenericEntity {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
		
	@UpdateTimestamp
	@Column(name = "data_atualizacao", updatable = true)// updatable (false don't permit to update, true permit to update, default value true)
	private LocalDateTime dataAtualizacao;
	
	@CreationTimestamp
	@Column(name = "data_criacao", updatable = false) // updatable (false don't permit to update, true permit to update, default value true)
	private LocalDateTime dataCriacao;
	
	
}
