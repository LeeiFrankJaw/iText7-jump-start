package tutorial.chapter05;

import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfStream;

import java.io.File;
import java.io.IOException;

public class Exercise {
    static final String SRC = "src/main/resources/pdf/watermark.pdf";
    static final String DEST = "results/chapter05/removed.pdf";

    public static void main(final String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        if (args.length > 0) {
            // new Exercise().removeArtifacts(args[0], DEST);
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
                          PdfName.Creator, PdfName.Producer,
                          new PdfName("www.it-ebooks.info")};

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

        // PdfDictionary pageDict = (PdfDictionary) pdfDoc.getPdfObject(2873);
        // int num = pdfDoc.getPageNumber(pageDict);
        // System.out.println(num);

        for (int i = 1; i <= n; i++) {
            // System.out.println(i);

            PdfPage page = pdfDoc.getPage(i);
            PdfDictionary pageDict = page.getPdfObject();

            int m = page.getContentStreamCount();
            pageDict.getAsArray(PdfName.Contents).remove(m - 1);

            PdfArray arr = pageDict.getAsArray(PdfName.Annots);
            int arrSize = arr.size();
            // System.out.println(arrSize);

            if (arrSize > 1 && arr.isIndirect()) {
                arr.remove(arrSize - 1);
                // System.out.println("Indirect and more than one");
            } else {
                pageDict.remove(PdfName.Annots);
                // System.out.println("Direct or exactly one");
            }
        }

        pdfDoc.close();
    }

    public void removeArtifacts(final String src,
                                final String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src),
                                             new PdfWriter(dest));

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfPage page = pdfDoc.getPage(i);
            PdfObject obj = page.getPdfObject().get(PdfName.Contents);
            if (obj instanceof PdfStream) {
                PdfStream stream = (PdfStream) obj;
                String s = new String(stream.getBytes());
                s = s.replaceAll("(?m)^/Artifact.*WOW(.|\n)*?EMC", "");
                stream.setData(s.getBytes("UTF-8"));
            }
        }

        pdfDoc.close();
    }
}
