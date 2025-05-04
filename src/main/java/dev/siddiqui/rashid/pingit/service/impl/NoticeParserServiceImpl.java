package dev.siddiqui.rashid.pingit.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.siddiqui.rashid.pingit.entity.NoticeDTO;
import dev.siddiqui.rashid.pingit.enums.NoticeStatus;
import dev.siddiqui.rashid.pingit.service.NoticeParserService; 
import dev.siddiqui.rashid.pingit.service.PdfDataExtractorService;
import dev.siddiqui.rashid.pingit.service.PdfDataExtractorService.ExtractedData;

@Service
public class NoticeParserServiceImpl implements NoticeParserService {

    @Autowired
    private PdfDataExtractorService pdfDataExtractorService;

    @Override
    public List<NoticeDTO> getParsedNotices(Document doc) throws ParseException {
        try {
            List<NoticeDTO> notices = new ArrayList<>();
            Elements rows = doc.select("tr");

            for (Element row : rows) {
                Element dateTd = row.selectFirst("td[nowrap]");
                Element titleLink = row.selectFirst("td.list-data-focus a");
                Element publishedByFont = row.select("td.list-data-focus font[size=1]").first();

                if (dateTd != null && titleLink != null && publishedByFont != null) {
                    String dateText = dateTd.text().split(" ")[0];
                    String title = titleLink.text();
                    String url = titleLink.absUrl("href");

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date publishedDate;

                    publishedDate = formatter.parse(dateText);

                    ExtractedData extractedDataFromPdf = pdfDataExtractorService.extractFromPdf(url);
                    List<String> emails = extractedDataFromPdf.emails();
                    List<String> rollNumbers = extractedDataFromPdf.rollNumbers();

                    NoticeDTO notice = new NoticeDTO(
                            null,
                            title,
                            url,
                            publishedDate,
                            NoticeStatus.ONGOING,
                            emails,
                            rollNumbers);

                    notices.add(notice);
                }
            }

            return notices;

        } catch (ParseException e) {
            throw new RuntimeException("Failed to fetch or parse notices", e);
        }
    }

}
