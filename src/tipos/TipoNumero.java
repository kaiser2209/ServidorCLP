package tipos;

public enum TipoNumero {
	BIT(01),
	BYTE(02),
	WORD(03),
	INT(04),
	DWORD(05),
	DINT(05),
	REAL(06);
	
	int code;
	
	TipoNumero(final int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
