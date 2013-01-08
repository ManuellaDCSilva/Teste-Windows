import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
//import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;


public class QrTest {

	public static void main(String[] args){
		QRCode qr = new QRCode();
		ErrorCorrectionLevel level = null;
		Encoder encoder = null;
		try {

			qr = encoder.encode("Hello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello World", level.Q);
			
			String aux = qr.toString();
			
			QRCodeWriter writer = new QRCodeWriter();
			//ByteMatrix matrix = writer.encode("Hello World", BarcodeFormat.QR_CODE, 400, 400);
			

			System.out.println(qr.getVersion());
			System.out.println(qr.getECLevel());
			BitArray bitVector = new BitArray();
			bitVector.appendBits(4, 32);

			bitVector.appendBits(4, 31);
			System.out.println("ToString: "+bitVector.toString());
			

			//System.out.println(qr.toString());
			
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
