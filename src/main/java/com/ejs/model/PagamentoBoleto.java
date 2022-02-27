package com.ejs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.ejs.model.enums.StatusPagamento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento_boleto")
public class PagamentoBoleto extends GenericEntity {
	
	@Column(name = "pedido_id")
	private Integer pedidoId;
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento status;
	
	@Column(name = "codigo_barra")
	private String codigoBarra;
	
}
