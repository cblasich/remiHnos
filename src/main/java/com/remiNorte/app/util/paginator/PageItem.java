package com.remiNorte.app.util.paginator;

public class PageItem {

	private int numero;
	private boolean actual;  //es pagina actual o no
	
	public PageItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public boolean isActual() {
		return actual;
	}
	
	
	
}
