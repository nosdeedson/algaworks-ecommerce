package com.ejs.model.chaveComposta1;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedidoIdChaveComposta1 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	private Integer pedidoId;
	
	@EqualsAndHashCode.Include
	private Integer produtoId;

}
