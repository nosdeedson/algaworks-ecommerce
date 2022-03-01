package com.ejs.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categoria", uniqueConstraints = @UniqueConstraint(columnNames = {"nome"}, name = "uk_categoria_nome"),
		indexes = @Index(columnList = "nome", name = "idx_categoria_nome"))
public class Categoria extends GenericEntity {
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@ManyToOne // this atribute is the owner of the relationship
	@JoinColumn(name = "categoria_pai_id", foreignKey = @ForeignKey(name="fk_categoria_x_categoria_pai"))
	private Categoria categoriaPai;
	
	@OneToMany(mappedBy = "categoriaPai") // this is the non-owning of the relationship
	private List<Categoria> categorias = new ArrayList<Categoria>();
	
	@ManyToMany(mappedBy = "categorias") // this is the non-ownig of the relationship
	private List<Produto> produtos = new ArrayList<Produto>();
}
