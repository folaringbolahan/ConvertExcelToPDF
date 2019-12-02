/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertexceltopdfaspose;

import com.aspose.cells.*;

/**
 *
 * @author emmanuel.idoko
 */
public class ConvertExcelToPDFAspose {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
//        Workbook workBook = new Workbook("C:\\Users\\emmanuel.idoko\\Desktop\\Effected Transactions Records.xlsx");
//        workBook.calculateFormula();
//        workBook.save("C:\\Users\\emmanuel.idoko\\Documents\\UnxUtils\\Rights Text Files\\Effected Transactions Records.pdf", FileFormatType.PDF);

          // Load the document.
            Workbook doc = new Workbook("C:\\Users\\emmanuel.idoko\\Desktop\\APR_DIV6_NIBSS_SUCCESSFUL.xlsx");
            doc.calculateFormula(true);
            PdfSaveOptions options = new PdfSaveOptions();
            options.setAllColumnsInOnePagePerSheet(true);
            options.setPdfCompression(PdfCompressionCore.LZW);
            options.setEnableHTTPCompression(true);
            options.setOptimizationType(1);
            doc.save("C:\\Users\\emmanuel.idoko\\Documents\\UnxUtils\\Rights Text Files\\APR_DIV6_NIBSS_SUCCESSFUL.pdf", options);
            System.out.println("Done converting file to pdf.");
        } catch (Exception ex) {
            System.out.println("error occured."+ex);
        }
    }

}
