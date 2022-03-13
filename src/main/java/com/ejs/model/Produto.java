package com.ejs.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import listener.GenericListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name = "Produto.listar", query = "SELECT p FROM Produto p"),
	@NamedQuery(name =  "ProdutoByCategoria", query = "SELECT p FROM Produto p WHERE EXISTS  ( SELECT 1 FROM Categoria c INNER JOIN c.produtos p2 WHERE p2=p AND c.id= :categoria)")
})
@EntityListeners({GenericListener.class})
@Table(name = "produto", uniqueConstraints = @UniqueConstraint(columnNames = {"nome"}, name = "uk_produto_nome"),
		indexes = @Index(columnList = "nome", name = "idx_produto_nome"))
public class Produto extends GenericEntity {
	
	@Column(nullable = false, length = 100)
    private String nome;
    
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    @Lob
    private byte[] foto;
    
    @ElementCollection
    @CollectionTable(name = "caracteristica_produto", joinColumns = @JoinColumn(name = "produto_id"),
    		foreignKey = @ForeignKey(name = "fk_caracteristica_X_produto"))
    private List<CaracteristicaProduto> caracteristica;
    
    @ElementCollection
    @CollectionTable(name = "tag_produto", joinColumns = @JoinColumn(name = "produto_id"),
    		foreignKey = @ForeignKey(name="fk_tag_X_produto"))
    @Column(name = "tag", length = 50)
    private List<String> tags = new ArrayList<String>();
    
    /*don't make sende to remove categoria with cascade all, because another 'produto' can be related with the 'categoria'*/
    @ManyToMany // the categoria is the owner of the relationshiop
    @JoinTable(name = "produto_categoria", 
    joinColumns = @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_id")),
    inverseJoinColumns = @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "fk_categoria_id"))    )
    private List<Categoria> categorias = new ArrayList<Categoria>();
    
    @OneToOne(mappedBy = "produto")
    private Estoque estoque;

}
