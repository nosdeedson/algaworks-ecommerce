package com.ejs.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item_pedido")
public class ItemPedido {
	
	@EmbeddedId
	private ItemPedidoId id;
	
	@MapsId("pedidoId")
	@ManyToOne(optional = false)
	@JoinColumn(name = "pedido_id",
			foreignKey = @ForeignKey(name = "fk_item_pedido_X_pedido"))
	private Pedido pedido;
	
	@MapsId("produtoId")
	@ManyToOne(optional = false)
	@JoinColumn(name = "produto_id",
			foreignKey = @ForeignKey(name = "fk_item_pedido_X_produto"))
	private Produto produto;
	
	@Column(name = "preco_produto", nullable = false)
	private BigDecimal precoProduto;
	
	@Column(nullable = false)
	private Integer quantidade;

}
