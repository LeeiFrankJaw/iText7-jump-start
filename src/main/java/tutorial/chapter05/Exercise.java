package tutorial.chapter05;

import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfDictionary;

import java.io.File;
import java.io.IOException;

public class Exercise {
    static final String SRC = "src/main/resources/pdf/watermark.pdf";
    static final String DEST = "results/chapter05/removed.pdf";

    public static void main(final String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        if (args.length > 0) {
            new Exercise().removeLastObject(args[0], DEST);
        } else {
            new Exercise().removeFirstObject(SRC, DEST);
        }
    }

    public void removeFirstObject(final String src,
                                final String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src),
                                             new PdfWriter(dest));

        PdfDocumentInfo docInfo = pdfDoc.getDocumentInfo();
        String version = pdfDoc.getPdfVersion().toPdfName().getValue();

        System.out.println("Producer:\t" + docInfo.getProducer());
        System.out.println("PDF version:\t" + version);

        int n = pdfDoc.getNumberOfPages();

        for (int i = 1; i <= n; i++) {
            PdfPage page = pdfDoc.getPage(i);
            System.out.println(page.getContentStreamCount());
            page.getPdfObject().getAsArray(PdfName.Contents).remove(0);
        }

        pdfDoc.close();
    }

    public void removeLastObject(final String src,
                                     final String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src),
                                             new PdfWriter(dest));

        PdfDocumentInfo docInfo = pdfDoc.getDocumentInfo();
        String version = pdfDoc.getPdfVersion().toPdfName().getValue();

        PdfDictionary infoDict = docInfo.getPdfObject();
        PdfName[] keys = {PdfName.Subject, PdfName.Keywords,
                          PdfName.Creator, PdfName.Producer};

        for (PdfName key : keys) {
            infoDict.remove(key);
        }

        System.out.println("Title:\t" + docInfo.getTitle());
        System.out.println("Subject:\t" + docInfo.getSubject());
        System.out.println("Keywords:\t" + docInfo.getKeywords());
        System.out.println("Author:\t" + docInfo.getAuthor());
        System.out.println("Creator:\t" + docInfo.getCreator());
        System.out.println("Producer:\t" + docInfo.getProducer());
        System.out.println("PDF version:\t" + version);

        int n = pdfDoc.getNumberOfPages();

        for (int i = 1; i <= n; i++) {
            PdfPage page = pdfDoc.getPage(i);
            int m = page.getContentStreamCount();
            page.getPdfObject().getAsArray(PdfName.Contents).remove(m-1);
        }

        pdfDoc.close();
    }
}
