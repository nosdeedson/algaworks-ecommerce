package com.ejs.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ejs.model.enums.StatusPagamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_pagamento", discriminatorType = DiscriminatorType.STRING)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "pagamento" , uniqueConstraints = @UniqueConstraint(columnNames = "pedido_id", name = "uk_pagamento_X_pedido"))
public abstract class Pagamento extends GenericEntity{

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE) // don't use identity
//	private Integer id;
	
//	@MapsId // this anotation makes the pedido_id the primary key in the table, but this class can't extend 'GenericEntity'
	@OneToOne(optional = false)
	@JoinColumn(name = "pedido_id", foreignKey = @ForeignKey(name= "fk_pagamento_X_pedido"))
	private Pedido pedido;
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento status;
}
