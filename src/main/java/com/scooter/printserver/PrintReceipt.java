package com.scooter.printserver;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class PrintReceipt {

    private static Logger logger = Logger.getLogger(PrintReceipt.class.getName());

    static FileInputStream textStream;

    public static void printFile(File fileToPrint) {

        try {
            textStream = new FileInputStream(fileToPrint);
        } catch (FileNotFoundException ffne) {
            logger.warning(ffne.getMessage());
        }

        // Set the document type
        DocFlavor myFormat = DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;

        // Create a Doc
        Doc myDoc = new SimpleDoc(textStream, myFormat, null);

        // Build a set of attributes
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(1));
        aset.add(MediaSizeName.ISO_A4);
        aset.add(Sides.ONE_SIDED);

        // discover the printers that can print the format according to the
        // instructions in the attribute set
        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(myFormat, aset);

        // Create a print job from one of the print services
        if (services.length > 0) {
            DocPrintJob job = services[0].createPrintJob();
            try {
                job.print(myDoc, aset);
            } catch (PrintException pe) {
                logger.warning(pe.getMessage());
            }
        }
    }
}
