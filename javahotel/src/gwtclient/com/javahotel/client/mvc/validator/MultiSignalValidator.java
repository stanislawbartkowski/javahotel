package com.javahotel.client.mvc.validator;

public class MultiSignalValidator implements ISignalValidate {

	private final ISignalValidate iSig;
	private IErrorMessage err;

	public MultiSignalValidator(ISignalValidate iSig) {
		this.iSig = iSig;
		err = null;
	}

	public void failue(IErrorMessage errmess) {
		if (err == null) {
			err = errmess;
		}
	}

	public void success() {
	}

	public void conclude() {
		if (err != null) {
			iSig.failue(err);
		} else {
			iSig.success();
		}
	}

}
