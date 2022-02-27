package com.ejs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(ItemPedidoId.class)
@Table(name = "item_pedido")
public class ItemPedido {
	
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "pedido_id")
	private Integer pedidoId;
	
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "produto_id")
	private Integer produtoId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "pedido_id", insertable = false, updatable = false,
		foreignKey = @ForeignKey(name = "fk_item_pedido_X_pedido"))
	private Pedido pedido;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "produto_id",  insertable = false, updatable = false,
		foreignKey = @ForeignKey(name = "fk_item_pedido_X_produto"))
	private Produto produto;
	
	private Integer quantidade;
}
