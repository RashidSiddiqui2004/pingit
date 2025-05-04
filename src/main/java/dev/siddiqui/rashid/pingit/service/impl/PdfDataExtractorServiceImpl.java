package dev.siddiqui.rashid.pingit.service.impl;

import dev.siddiqui.rashid.pingit.service.PdfDataExtractorService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PdfDataExtractorServiceImpl implements PdfDataExtractorService {

    @Value("${COLLEGE_NOTICE_PORTAL_URL}")
    private String collegeNoticePortalUrl;

    @SuppressWarnings("deprecation")
    @Override
    public ExtractedData extractFromPdf(String pdfUrl) {
        try {
            URL url = new URL(pdfUrl);
            URLConnection connection = url.openConnection();

            connection.setRequestProperty("Referer", collegeNoticePortalUrl);

            try (InputStream input = connection.getInputStream();
                    PDDocument document = PDDocument.load(input)) {

                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);

                List<String> emails = extractMatches(text, "\\b[a-z]+\\.[a-z]+\\.ug\\d{2}@nsut\\.ac\\.in\\b");
                List<String> rollNumbers = extractMatches(text, "\\b\\d{4}[A-Z]{3}\\d{4}\\b");

                return new ExtractedData(emails, rollNumbers);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ExtractedData(List.of(), List.of());
        }
    }

    private List<String> extractMatches(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.results()
                .map(MatchResult::group)
                .distinct()
                .collect(Collectors.toList());
    }
}
