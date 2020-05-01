package com.remiNorte.app.validations;

@SuppressWarnings("Serial")
public class UsuarioExisteException extends Throwable {

	public UsuarioExisteException(final String message) {
        super(message);
    }
	
}
