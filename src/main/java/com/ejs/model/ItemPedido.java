package com.ejs.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item_pedido")
public class ItemPedido {
	
	@EmbeddedId
	private ItemPedidoId id;
	
	@NotNull
	@MapsId("pedidoId")
	@ManyToOne(optional = false)
	@JoinColumn(name = "pedido_id",
			foreignKey = @ForeignKey(name = "fk_item_pedido_X_pedido"))
	private Pedido pedido;
	
	@NotNull
	@MapsId("produtoId")
	@ManyToOne(optional = false)
	@JoinColumn(name = "produto_id",
			foreignKey = @ForeignKey(name = "fk_item_pedido_X_produto"))
	private Produto produto;
	
	@NotNull
	@Positive
	@Column(name = "preco_produto", nullable = false)
	private BigDecimal precoProduto;
	
	@Positive
	@NotNull
	@Column(nullable = false)
	private Integer quantidade;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedido other = (ItemPedido) obj;
		return Objects.equals(id, other.id);
	}

	
}
