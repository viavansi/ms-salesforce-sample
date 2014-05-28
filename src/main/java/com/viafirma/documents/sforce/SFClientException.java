package com.viafirma.documents.sforce;

public class SFClientException extends Exception {

	private static final long serialVersionUID = -1199557889512991284L;

	public SFClientException() {
		super();
	}

	public SFClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public SFClientException(String message) {
		super(message);
	}

	public SFClientException(Throwable cause) {
		super(cause);
	}
}
