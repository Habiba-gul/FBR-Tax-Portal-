package Frontend;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import Backend.PaymentHistory;
import Backend.SystemManager;
import Backend.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;

public class ReportDetailController {

    @FXML private Label paymentDateLabel;
    @FXML private TableView<ReceiptRow> detailTable;
    @FXML private TableColumn<ReceiptRow, String> taxTypeCol, categoryCol;
    @FXML private TableColumn<ReceiptRow, Double> taxPercentCol, originalPriceCol, taxAmountCol;
    @FXML private Label grandTotalLabel;
    @FXML private TextArea reportTextArea;

    private PaymentHistory selectedPayment;
    private UserInfo currentUser;

    @FXML
    public void initialize() {
        taxTypeCol.setCellValueFactory(new PropertyValueFactory<>("taxType"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        taxPercentCol.setCellValueFactory(new PropertyValueFactory<>("taxPercent"));
        originalPriceCol.setCellValueFactory(new PropertyValueFactory<>("originalPrice"));
        taxAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    public void setPayment(PaymentHistory payment) {
        this.selectedPayment = payment;
        this.currentUser = SystemManager.getCurrentUser();

        paymentDateLabel.setText("Payment Date: " + payment.getPaymentDate());

        detailTable.getItems().clear();
        double total = 0.0;

        String details = payment.getDetails();
        if (details != null && !details.isEmpty()) {
            String[] rows = details.split("\\|");
            for (String row : rows) {
                if (row.trim().isEmpty()) continue;
                String[] fields = row.split(",");

                // Support both old (4 fields) and new (6 fields) format
                if (fields.length >= 4) {
                    try {
                        String taxType = fields.length >= 6 ? fields[0].trim() : "Unknown";
                        String category = fields.length >= 6 ? fields[1].trim() : fields[0].trim();
                        double taxPercent = fields.length >= 6 ? Double.parseDouble(fields[2].trim()) : 0.0;
                        double originalPrice = fields.length >= 6 ? Double.parseDouble(fields[3].trim()) : Double.parseDouble(fields[1].trim());
                        double taxAmount = fields.length >= 6 ? Double.parseDouble(fields[4].trim()) : Double.parseDouble(fields[3].trim());

                        detailTable.getItems().add(new ReceiptRow(taxType, category, taxPercent, originalPrice, taxAmount, "", 0.0));
                        total += taxAmount;
                    } catch (Exception ignored) {}
                }
            }
        }

        grandTotalLabel.setText(String.format("%.2f PKR", total));
        buildReportText(total);
    }

    private void buildReportText(double total) {
        StringBuilder sb = new StringBuilder();
        sb.append("FEDERAL BOARD OF REVENUE\n");
        sb.append("TAX PAYMENT RECORD\n");
        sb.append("=".repeat(60)).append("\n\n");
        sb.append("Taxpayer Name : ").append(currentUser.getName()).append("\n");
        sb.append("CNIC          : ").append(currentUser.getCnic()).append("\n");
        sb.append("Payment Date  : ").append(selectedPayment.getPaymentDate()).append("\n\n");

        sb.append(String.format("%-15s %-25s %-10s %-20s %-15s\n", "Tax Type", "Description", "Tax %", "Original Price", "Tax Amount"));
        sb.append("-".repeat(90)).append("\n");

        for (ReceiptRow row : detailTable.getItems()) {
            sb.append(String.format("%-15s %-25s %-10.2f %-20.2f %-15.2f\n",
                    row.getTaxType(), row.getCategory(), row.getTaxPercent(),
                    row.getOriginalPrice(), row.getAmount()));
        }

        sb.append("\nGRAND TOTAL TAX PAID: ").append(String.format("%.2f PKR", total));
        reportTextArea.setText(sb.toString());
    }

    @FXML private void handlePrint(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(detailTable.getScene().getWindow())) {
            boolean success = job.printPage(reportTextArea);
            if (success) job.endJob();
        }
    }

    @FXML private void handleSave(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("TaxRecord_" + currentUser.getCnic() + "_" + selectedPayment.getPaymentDate().toString().replace(":", "") + ".pdf");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = chooser.showSaveDialog(detailTable.getScene().getWindow());
        if (file == null) return;

        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            Font title = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font header = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normal = new Font(Font.FontFamily.HELVETICA, 11);

            doc.add(new Paragraph("FEDERAL BOARD OF REVENUE", title));
            doc.add(new Paragraph("TAX PAYMENT RECORD", title));
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("Name: " + currentUser.getName(), normal));
            doc.add(new Paragraph("CNIC: " + currentUser.getCnic(), normal));
            doc.add(new Paragraph("Payment Date: " + selectedPayment.getPaymentDate(), normal));
            doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.addCell(new Phrase("Tax Type", header));
            table.addCell(new Phrase("Description", header));
            table.addCell(new Phrase("Tax %", header));
            table.addCell(new Phrase("Original Price", header));
            table.addCell(new Phrase("Tax Amount", header));

            for (ReceiptRow r : detailTable.getItems()) {
                table.addCell(r.getTaxType());
                table.addCell(r.getCategory());
                table.addCell(String.format("%.2f", r.getTaxPercent()));
                table.addCell(String.format("%.2f", r.getOriginalPrice()));
                table.addCell(String.format("%.2f", r.getAmount()));
            }

            doc.add(table);
            doc.add(new Paragraph("\nGrand Total: " + grandTotalLabel.getText(), header));
            doc.close();

            new Alert(Alert.AlertType.INFORMATION, "PDF saved successfully!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save PDF.").show();
        }
    }

    @FXML private void handleBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}