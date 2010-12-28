package javax.ejb;

public @interface TransactionAttribute {
	
	 public TransactionAttributeType value() default TransactionAttributeType.REQUIRED;

}
