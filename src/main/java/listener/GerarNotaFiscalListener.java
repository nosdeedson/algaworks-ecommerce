package listener;

import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;

import com.ejs.model.Pedido;

import service.NotaFiscalService;

public class GerarNotaFiscalListener {
	
	NotaFiscalService nfService = new NotaFiscalService();
	
	@PostPersist
	@PreUpdate
	public void gerar(Pedido pedido) {
		if ( pedido.isPago() && pedido.getNotaFiscal() == null) {
			nfService.gerarNota(pedido);
		}
	}

}
