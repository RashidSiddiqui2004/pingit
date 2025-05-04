package dev.siddiqui.rashid.pingit.service;

import java.text.ParseException;
import java.util.List;

import org.jsoup.nodes.Document;

import dev.siddiqui.rashid.pingit.entity.NoticeDTO;

public interface NoticeParserService {
    public List<NoticeDTO> getParsedNotices(Document doc) throws ParseException;
}
