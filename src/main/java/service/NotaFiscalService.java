package service;

import java.util.Optional;

import com.ejs.model.Pedido;

public class NotaFiscalService  {
	
	public void gerarNota(Pedido pedido) {
		if ( Optional.ofNullable(pedido).isPresent()) {
			System.out.println("\n\nGERANDO NOTA FISCAL DO PEDIDO " + pedido.getId());
		}
	}

}
