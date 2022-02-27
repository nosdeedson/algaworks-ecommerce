package com.ejs.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ejs.model.enums.SexoCliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cliente")
public class Cliente extends GenericEntity {
	
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private SexoCliente sexo;
	
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	public Cliente() {	}

	public Cliente(String nome, SexoCliente sexo) {
		this.nome = nome;
		this.sexo = sexo;
	}
	
	
}
