package ch.swing.helper;

public class FHIRServiceException extends Exception {

	private static final long serialVersionUID = -8810189971297913263L;

	public FHIRServiceException() {
		super();
	}

	public FHIRServiceException(String message) {
		super(message);
	}

	public FHIRServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FHIRServiceException(Throwable cause) {
		super(cause);
	}
}
