package listener;

import javax.persistence.PostLoad;

public class GenericListener {
	
	@PostLoad
	public void logLoading(Object obj) {
		
		System.out.println(obj.getClass().getSimpleName().toUpperCase() + " CARREGADO \n\n");
		
	}

}
