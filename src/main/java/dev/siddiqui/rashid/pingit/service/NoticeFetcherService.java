package dev.siddiqui.rashid.pingit.service;

import java.text.ParseException;
import java.util.List;

import org.jsoup.nodes.Document;

import dev.siddiqui.rashid.pingit.entity.NoticeDTO;

public interface NoticeFetcherService {

    List<NoticeDTO> fetchAll() throws ParseException;

    Document fetchAllNotices();

    NoticeDTO create(NoticeDTO noticedDto);

    List<NoticeDTO> findAll();

    NoticeDTO findById(Long id);

    NoticeDTO update(Long id, NoticeDTO user);

    void delete(Long id);
}
