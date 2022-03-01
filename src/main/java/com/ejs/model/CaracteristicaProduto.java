package com.ejs.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CaracteristicaProduto {
	
	@Column(length = 100, nullable = false)
	private String tipo;
	
	private String caracteristica;

}
