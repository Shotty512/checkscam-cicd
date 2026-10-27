package com.example.checkscam.service.impl;

import com.example.checkscam.constant.StatusReportEnum;
import com.example.checkscam.dto.PhoneScamImportRow;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.*;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.PhoneScamRepository;
import com.example.checkscam.repository.PhoneScamStatsRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.service.PhoneScamService;
import com.example.checkscam.service.ScamStatsService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneScamServiceImpl implements PhoneScamService {
    private final PhoneScamRepository repository;
    private final PhoneScamStatsRepository phoneScamStatsRepository;
    private final ScamTypesRepository scamTypesRepository;
    private final ScamStatsService scamStatsService;
    private final EntityManager em;

    @Override
    @Transactional
    public void handleAfterReport(Report report, Long idScamType) throws CheckScamException {

        PhoneScam phoneScam = repository.findByPhoneNumber(report.getInfo());

        if (ObjectUtils.isEmpty(phoneScam)) {
            phoneScam = new PhoneScam();
            phoneScam.setPhoneNumber(report.getInfo());
            phoneScam.setDescription(report.getInfoDescription());
            phoneScam = repository.save(phoneScam);
        }

        ScamStatsDto statsDto = (!ObjectUtils.isEmpty(phoneScam.getStats()))
                ? new ScamStatsDto(phoneScam.getStats())
                : new ScamStatsDto();

        ScamStatsDto calculated = scamStatsService.handleStats(statsDto, report, idScamType);

        PhoneScamStats stats = phoneScam.getStats();
        if (ObjectUtils.isEmpty(stats)) {
            stats = new PhoneScamStats(calculated);
            stats.setPhoneScam(phoneScam);
        } else {
            stats.apply(calculated);
        }

        stats = phoneScamStatsRepository.save(stats);
        phoneScam.setStats(stats);
        repository.save(phoneScam);
    }

    @Transactional
    @Override
    public void importData(MultipartFile file) throws IOException {

        List<PhoneScamImportRow> rows = parse(file);
        List<PhoneScam> batch = new ArrayList<>(500);
        int skippedRows = 0;

        for (PhoneScamImportRow r : rows) {

            String phoneNumber = r.getPhoneNumber();
            if (!StringUtils.hasText(phoneNumber)) {
                log.warn("Skipping row due to empty or blank phone number: {}", r);
                skippedRows++;
                continue;
            }
            phoneNumber = phoneNumber.trim();
            PhoneScam scam = repository.findByPhoneNumber(phoneNumber);
            if (ObjectUtils.isEmpty(scam)) {
                scam = new PhoneScam();
                scam.setPhoneNumber(phoneNumber);
            }
            scam.setDescription(r.getDescription());
            PhoneScamStats stats = scam.getStats();
            if (ObjectUtils.isEmpty(stats)) {
                stats = new PhoneScamStats();
                stats.setPhoneScam(scam); // @MapsId
            }
            stats.setVerifiedCount(
                    r.getVerifiedCount() == null ? 0 : r.getVerifiedCount());
            String finalPhoneNumber = phoneNumber;
            stats.setLastReportAt(
                    Optional.ofNullable(r.getLastReportAt())
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(s -> {
                                try {
                                    return LocalDateTime.parse(s);
                                } catch (Exception e) {
                                    log.warn("Could not parse lastReportAt date '{}' for phone number '{}'. Setting to null.", s, finalPhoneNumber, e);
                                    return null;
                                }
                            })
                            .orElse(null)
            );

            stats.setReasonsJson(null);

            scam.setStats(stats);
            batch.add(scam);

            if (batch.size() >= 500) {
                log.info("Flushing batch of {} records...", batch.size());
                repository.saveAll(batch);
                em.flush();
                em.clear();
                batch.clear();
                log.info("Batch flushed and cleared.");
            }
        }
        if (!batch.isEmpty()) {
            log.info("Flushing final batch of {} records...", batch.size());
            repository.saveAll(batch);
            em.flush();
            em.clear();
            log.info("Final batch flushed and cleared.");
        }

        if (skippedRows > 0) {
            log.warn("Import completed. Skipped {} rows due to missing or blank phone numbers.", skippedRows);
        } else {
            log.info("Import completed successfully. Processed {} rows.", rows.size());
        }
    }

    private List<PhoneScamImportRow> parse(MultipartFile file) throws IOException {
        String name = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();
        if (name.endsWith(".csv")) return parseCsv(file);
        else if (name.endsWith(".xlsx")) return parseExcel(file);
        else throw new IllegalArgumentException("Chỉ hỗ trợ file định dạng CSV hoặc XLSX");
    }

    private List<PhoneScamImportRow> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CSVParser csv = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreSurroundingSpaces()
                    .withTrim()
                    .parse(reader);

            return csv.getRecords().stream()
                    .map(rec -> {
                        PhoneScamImportRow row = new PhoneScamImportRow();
                        row.setPhoneNumber(rec.get("phoneNumber"));
                        row.setDescription(rec.get("description"));
                        row.setVerifiedCount(
                                parseIntSafe(rec.get("verifiedCount")));
                        row.setLastReportAt(rec.get("lastReportAt"));
                        return row;
                    }).collect(Collectors.toList());
        }
    }

    private List<PhoneScamImportRow> parseExcel(MultipartFile file) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            if (!it.hasNext()) {
                return new ArrayList<>();
            }
            it.next();

            List<PhoneScamImportRow> list = new ArrayList<>();
            int rowNum = 1;
            while (it.hasNext()) {
                Row row = it.next();
                rowNum++;
                PhoneScamImportRow dto = new PhoneScamImportRow();
                String phoneNumber = getCell(row, 0).trim();
                String description = getCell(row, 1).trim();
                String verifiedCountStr = getCell(row, 2).trim();
                String lastReportAtStr = getCell(row, 3).trim();

                dto.setPhoneNumber(phoneNumber);
                dto.setDescription(description);
                dto.setVerifiedCount(parseIntSafe(verifiedCountStr));
                dto.setLastReportAt(lastReportAtStr);
                list.add(dto);
            }
            return list;
        }
    }


    private static String getCell(Row row, int idx) {
        Cell c = row.getCell(idx, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        String cellValue;
        if (c.getCellType() == CellType.NUMERIC) {
            cellValue = String.valueOf((long) c.getNumericCellValue());
        } else {
            cellValue = c.getStringCellValue();
        }
        return cellValue == null ? "" : cellValue;
    }

    private static Integer parseIntSafe(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            log.warn("Could not parse integer value '{}'", s, e);
            return null;
        }
    }

}
