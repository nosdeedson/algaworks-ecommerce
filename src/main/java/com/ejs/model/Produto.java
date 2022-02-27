package com.ejs.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import listener.GenericListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners({GenericListener.class})
@Table(name = "produto")
public class Produto extends GenericEntity {
	
    private String nome;
    
    private String descricao;
    
    private BigDecimal preco;
    
    @ManyToMany(cascade = CascadeType.ALL) // the categoria is the owner of the relationshiop
    @JoinTable(name = "produto_categoria", 
    joinColumns = @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_id")),
    inverseJoinColumns = @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "fk_categoria_id"))    )
    private List<Categoria> categorias = new ArrayList<Categoria>();
    
    @OneToOne(mappedBy = "produto")
    private Estoque estoque;

}
