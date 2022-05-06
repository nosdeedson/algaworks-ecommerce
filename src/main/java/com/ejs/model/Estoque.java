package com.ejs.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estoque",
uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id"}, name = "uk_produto_id"))
public class Estoque extends GenericEntity {
	
	@NotNull
	@OneToOne(optional = false)
	@JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name= "fk_estoque_X_produto"))
	private Produto produto;
	
	@Positive
	private Integer quantidade;
	
}
