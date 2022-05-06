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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.br.CPF;

import com.ejs.model.enums.SexoCliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
				name = "compraram_acima_media", procedureName = "compraram_acima_media",
				parameters = {
						@StoredProcedureParameter(name="ano", mode = ParameterMode.IN, type = Integer.class)
				},
				resultClasses = Cliente.class
			)
})
@SecondaryTable(name = "cliente_detalhe", pkJoinColumns = @PrimaryKeyJoinColumn(name = "cliente_id"),
		foreignKey = @ForeignKey(name= "cliente_X_cliente_detalhe"))
@Table(name = "cliente", uniqueConstraints = @UniqueConstraint(columnNames = {"cpf"}, name = "uk_cliente_cpf"),
		indexes = @Index(columnList = "nome", name = "idx_nome" ))
public class Cliente extends GenericEntity {
	
	@NotBlank
	@Column(length = 100, nullable = false)
	private String nome;
	
	@NotBlank
	@CPF
	@Column(length = 14, nullable = false)
	private String cpf;
	
	@Column(table = "cliente_detalhe", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private SexoCliente sexo;
	
	@Past
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
