package tipos;

public enum Tipo {
	ENVIA_BIT(01),
	ENVIA_BYTE(02),
	ENVIA_WORD(03),
	ENVIA_DWORD(04),
	ENVIA_INT(05),
	ENVIA_FLOAT(06),
	ENVIA_DINT(07),
	RECEBE_BIT(011),
	RECEBE_BYTE(012),
	RECEBE_WORD(013),
	RECEBE_DWORD(014),
	RECEBE_INT(015),
	RECEBE_FLOAT(016),
	RECEBE_DINT(017);
	
	int code;
	
	Tipo(final int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
