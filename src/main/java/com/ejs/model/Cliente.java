package com.ejs.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import com.ejs.model.enums.SexoCliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cliente")
@SecondaryTable(name = "cliente_detalhe", pkJoinColumns = @PrimaryKeyJoinColumn(name = "cliente_id"),
		foreignKey = @ForeignKey(name= "cliente_X_cliente_detalhe"))
public class Cliente extends GenericEntity {
	
	private String nome;
	
	@Column(table = "cliente_detalhe")
	@Enumerated(EnumType.STRING)
	private SexoCliente sexo;
	
	@Column(table = "cliente_detalhe", name = "data_nascimento")
	private LocalDateTime dataNascimento;
	
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	@ElementCollection
	@CollectionTable(name = "cliente_contatos", joinColumns = @JoinColumn(name = "cliente_id"),
			foreignKey = @ForeignKey(name = "fk_cliente_X_contato"))
	private Map<String, String> contatos = new HashMap<String, String>();
	
	public Cliente() {	}

	public Cliente(String nome, SexoCliente sexo) {
		this.nome = nome;
		this.sexo = sexo;
	}
	
	
}
