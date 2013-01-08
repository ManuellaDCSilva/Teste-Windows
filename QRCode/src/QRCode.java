
public class QRCode {
	
	Mode mode;
	Version version;
	ErrorCorrectionLevel errorCorrectionLevel;
	String data;
	
	
	
	public QRCode(Version version,
			ErrorCorrectionLevel errorCorrectionLevel, Mode mode, String data) {
		this.mode = mode;
		this.version = version;
		this.errorCorrectionLevel = errorCorrectionLevel;
		this.data = data;
	}
	
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	public ErrorCorrectionLevel getErrorCorrectionLevel() {
		return errorCorrectionLevel;
	}
	public void setErrorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
		this.errorCorrectionLevel = errorCorrectionLevel;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	

}
