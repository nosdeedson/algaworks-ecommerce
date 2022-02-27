package com.ejs.model;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class EnderecoEntregaPedido {

	private String bairro;
	private String cep;
	private String cidade;
	private String complemento;
	private String estado;
	private String logradouro;
	private String numero;
}
