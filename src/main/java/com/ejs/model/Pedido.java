package com.ejs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ejs.model.enums.StatusPedido;

import listener.GenericListener;
import listener.GerarNotaFiscalListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners({GerarNotaFiscalListener.class, GenericListener.class})
@Entity
@Table(name = "pedido")
public class Pedido extends GenericEntity {
	
	@ManyToOne(optional = false) // make the hibernate to use a inner join instead of a left join( the inner join is more perfomatic)
	@JoinColumn(name = "cliente_id", foreignKey = @ForeignKey(name= "fk_pedido_X_cliente"))
	private Cliente cliente;
	
	@CreationTimestamp
	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;
	
	@UpdateTimestamp
	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;
	
	@Column(name = "data_conclusao")
	private LocalDateTime dataConclusao;
	
	@OneToOne(mappedBy = "pedido")
	private NotaFiscal notaFiscal;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status;
	
	private BigDecimal total;
	
	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itensPedido = new ArrayList<ItemPedido>();
	
	
	@Embedded
	private EnderecoEntregaPedido enderecoEntrega;
	
	@OneToOne(mappedBy = "pedido")
	private Pagamento pagamento;
	
	public boolean isPago() {
		return StatusPedido.PAGO.equals(this.status);
	}
		
	
}
