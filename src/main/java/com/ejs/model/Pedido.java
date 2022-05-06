package com.ejs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;

import com.ejs.model.enums.StatusPedido;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import listener.GenericListener;
import listener.GerarNotaFiscalListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners({GerarNotaFiscalListener.class, GenericListener.class})
@Entity
@Table(name = "pedido")
public class Pedido extends GenericEntity
implements PersistentAttributeInterceptable
{
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY) // make the hibernate to use a inner join instead of a left join( the inner join is more perfomatic)
	@JoinColumn(name = "cliente_id", foreignKey = @ForeignKey(name= "fk_pedido_X_cliente"))
	private Cliente cliente;
	
	@PastOrPresent
	@CreationTimestamp
	@Column(name = "data_criacao", nullable = false)
	private LocalDateTime dataCriacao;
	
	@FutureOrPresent
	@UpdateTimestamp
	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;
	
	@FutureOrPresent
	@Column(name = "data_conclusao")
	private LocalDateTime dataConclusao;

//	no-owning of the relationship
	@LazyToOne(LazyToOneOption.NO_PROXY) // ANOTAÇÃO DO HIBERNATE USADA PARA O LAZY FUNCIONAR DEVIDO AO @ONETOONE
	@OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
	private NotaFiscal notaFiscal;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private StatusPedido status;
	
	@Positive
	@NotNull
	@Column(nullable = false)
	private BigDecimal total;
	
	@NotNull
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itensPedido = new ArrayList<ItemPedido>();
	
	
	@Embedded
	private EnderecoEntregaPedido enderecoEntrega;
	
//	no-owning of the relationship
	@LazyToOne(LazyToOneOption.NO_PROXY) // ANOTAÇÃO DO HIBERNATE USADA PARA O LAZY FUNCIONAR DEVIDO AO @ONETOONE
	@OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
	private Pagamento pagamento;
	
	public boolean isPago() {
		return StatusPedido.PAGO.equals(this.status);
	}
	
	public NotaFiscal getNotaFiscal() {
		if( this.persistentAttributeInterceptor != null) {
			return (NotaFiscal) persistentAttributeInterceptor.readObject(this, "notaFiscal", this.notaFiscal);
		}
		return this.notaFiscal;
	}
	
	public void setNotaFiscal(NotaFiscal notaFiscal) {
		if ( this.persistentAttributeInterceptor != null) {
			persistentAttributeInterceptor.writeObject(this, "notaFiscal", this.notaFiscal, notaFiscal);
		}else {
			this.notaFiscal = notaFiscal;
		}
		
	}
	
	public Pagamento getPagamento() {
		if( this.persistentAttributeInterceptor != null) {
			return (Pagamento) persistentAttributeInterceptor.readObject(this, "pagamento", this.pagamento);
		}
		return this.pagamento;
	}
	
	public void setPagamento(Pagamento pagamento) {
		if ( this.persistentAttributeInterceptor != null) {
			persistentAttributeInterceptor.writeObject(this, "pagamento", this.pagamento, pagamento);
		}else {
			this.pagamento = pagamento;
		}
	}
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@Transient
	private PersistentAttributeInterceptor persistentAttributeInterceptor;

	@Override
	public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
		return this.persistentAttributeInterceptor;
	}

	@Override
	public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor interceptor) {
		this.persistentAttributeInterceptor = interceptor;
	}
		
	
}
