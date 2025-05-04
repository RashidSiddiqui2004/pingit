package dev.siddiqui.rashid.pingit.service;

import java.util.List;

public interface PdfDataExtractorService {

    ExtractedData extractFromPdf(String pdfUrl);

    record ExtractedData(List<String> emails, List<String> rollNumbers) {
    }
}
