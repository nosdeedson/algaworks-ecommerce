package com.ejs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nota_fiscal",
uniqueConstraints = @UniqueConstraint(columnNames = {"pedido_id"}, name = "uk_pedido_id"))
public class NotaFiscal extends GenericEntity {
	
	@OneToOne(optional = false) // the notaFiscal is the owner of the relationship
	@JoinColumn(name = "pedido_id", foreignKey = @ForeignKey(name="fk_nota_fiscal_X_pedido"))
	private Pedido pedido;
	
	private String xml;
	
	@CreationTimestamp
	@Column(name = "data_emissao")
	private Date dataEmissao;
	
	
	
}
