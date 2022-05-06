package com.ejs.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DiscriminatorValue("boleto")
@Entity
//@Table(name = "pagamento_boleto") 
public class PagamentoBoleto extends Pagamento{
	
	@NotBlank
	@Column(name = "codigo_barra")
	private String codigoBarra;
	
}
