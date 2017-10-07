package tutorial.fonts;

import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.io.font.FontConstants;

import java.io.IOException;

public final class Fonts {
    public static final PdfFont Helvetica;
    static {
        try {
            Helvetica = PdfFontFactory.createFont(FontConstants.HELVETICA);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
