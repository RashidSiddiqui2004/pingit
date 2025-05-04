package dev.siddiqui.rashid.pingit.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.siddiqui.rashid.pingit.entity.Notice;
import dev.siddiqui.rashid.pingit.entity.NoticeDTO;
import dev.siddiqui.rashid.pingit.repository.NoticeRepository;
import dev.siddiqui.rashid.pingit.service.NoticeFetcherService;
import dev.siddiqui.rashid.pingit.service.NoticeParserService;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.jsoup.nodes.Document;

@Service
public class NoticeFetcherServiceImpl implements NoticeFetcherService {

    @Value("${COLLEGE_NOTICE_PORTAL_URL}")
    private String collegeNoticePortalUrl;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeParserService noticeParserService;

    @Override
    public List<NoticeDTO> fetchAll() throws ParseException {
        try {
            Document doc = Jsoup.connect(collegeNoticePortalUrl).get();
            return noticeParserService.getParsedNotices(doc);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch or parse notices", e);
        }
    }

    @Override
    public Document fetchAllNotices() {
        try {
            Document doc = Jsoup.connect(collegeNoticePortalUrl).get();
            return doc;
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch notices", e);
        }
    }

    @Override
    public NoticeDTO create(NoticeDTO noticeDto) {
        Notice notice = convertToEntity(noticeDto);
        Notice saved = noticeRepository.save(notice);
        return convertToDTO(saved);
    }

    @Override
    public List<NoticeDTO> findAll() {
        return noticeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NoticeDTO findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found with id " + id));
        return convertToDTO(notice);
    }

    @Override
    public NoticeDTO update(Long id, NoticeDTO noticeDto) {
        Notice existing = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found with id " + id));

        existing.setNoticeTitle(noticeDto.noticeTitle());
        existing.setNoticeUrl(noticeDto.noticeUrl());
        existing.setNoticeDate(noticeDto.noticeDate());
        existing.setEmbeddedMails(noticeDto.embeddedMails());
        existing.setEmbeddedRollNumbers(noticeDto.embeddedRollNumbers());

        Notice updated = noticeRepository.save(existing);

        return convertToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found with id " + id);
        }
        noticeRepository.deleteById(id);
    }

    private Notice convertToEntity(NoticeDTO dto) {
        Notice notice = new Notice();
        notice.setId(dto.id());
        notice.setNoticeTitle(dto.noticeTitle());
        notice.setNoticeUrl(dto.noticeUrl());
        notice.setNoticeDate(dto.noticeDate());
        notice.setNoticeStatus(dto.noticeStatus());
        notice.setEmbeddedMails(dto.embeddedMails());
        notice.setEmbeddedRollNumbers(dto.embeddedRollNumbers());

        return notice;
    }

    private NoticeDTO convertToDTO(Notice notice) {
        return new NoticeDTO(
                notice.getId(),
                notice.getNoticeTitle(),
                notice.getNoticeUrl(),
                notice.getNoticeDate(),
                notice.getNoticeStatus(),
                notice.getEmbeddedMails(),
                notice.getEmbeddedRollNumbers());
    }

}
