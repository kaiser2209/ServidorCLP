package tipos;

public enum TipoNumero {
	BIT(01, 1),
	BYTE(02, 1),
	WORD(03, 2),
	DWORD(04, 4),
	INT(05, 2),
	REAL(06, 4),
	DINT(07, 4);
	
	int code;
	int tamanho;
	
	TipoNumero(final int code, int tamanho) {
		this.code = code;
		this.tamanho = tamanho;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public int getTamanho() {
		return this.tamanho;
	}
	
	public static TipoNumero fromId(int id) {
		for (TipoNumero tipo : values()) {
			if(tipo.getCode() == id) {
				return tipo;
			}
		}
		
		return null;
	}
}
