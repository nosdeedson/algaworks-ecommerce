package com.ejs.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class EnderecoEntregaPedido {

	@Column(length = 50)
	private String bairro;
	
	@Column(length = 9)
	private String cep;
	
	@Column(length = 50)
	private String cidade;
	
	@Column(length = 50)
	private String complemento;
	
	@Column(length = 50)
	private String estado;
	
	@Column(length = 50)
	private String logradouro;
	
	@Column(length = 5)
	private String numero;
}
