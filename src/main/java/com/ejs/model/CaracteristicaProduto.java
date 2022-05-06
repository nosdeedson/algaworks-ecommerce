package com.ejs.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	
	@NotBlank
	@Column(length = 100, nullable = false)
	private String tipo;
	
	@Size(max = 200)
	private String caracteristica;

}
