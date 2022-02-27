package com.ejs.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ejs.model.enums.StatusPagamento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento_cartao",
		uniqueConstraints = @UniqueConstraint(columnNames = {"pedido_id"}, name = "uk_pedido_id"))
public class PagamentoCartao extends GenericEntity {
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento status;
	
	private String numero;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "pedido_id", foreignKey = @ForeignKey(name= "fk_pagamento_cartao_X_pedido"))
	private Pedido pedido;
	
}
