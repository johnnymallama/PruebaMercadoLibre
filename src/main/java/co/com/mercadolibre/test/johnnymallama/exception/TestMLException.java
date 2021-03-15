package co.com.mercadolibre.test.johnnymallama.exception;

public class TestMLException extends Exception {

	public TestMLException() {
	}

	public TestMLException(String message) {
		super(message);
	}

	public TestMLException(Throwable cause) {
		super(cause);
	}

	public TestMLException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestMLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
