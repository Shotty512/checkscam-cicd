package com.example.checkscam.service.impl;

import com.example.checkscam.dto.BankScamImportRow;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.BankScam;
import com.example.checkscam.entity.BankScamStats;
import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.BankScamRepository;
import com.example.checkscam.repository.BankScamStatsRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.service.BankScamService;
import com.example.checkscam.service.ScamStatsService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankScamServiceImpl implements BankScamService {
    private final BankScamRepository repository;
    private final ScamTypesRepository scamTypesRepository;
    private final ScamStatsService scamStatsService;
    private final BankScamStatsRepository bankScamStatsRepository;
    private final EntityManager em;

    private static final String HEADER_STK = "STK";
    private static final String HEADER_NAME_ACCOUNT = "NameAccount";
    private static final String HEADER_BANK_NAME = "BankName";
    private static final int EXCEL_COL_STK = 0;
    private static final int EXCEL_COL_NAME_ACCOUNT = 1;
    private static final int EXCEL_COL_BANK_NAME = 2;


    @Transactional
    @Override
    public void handleAfterReport(Report report, Long idScamType)
            throws CheckScamException {

        BankScam bankScam = repository.findByBankAccount(report.getInfo());
        if (ObjectUtils.isEmpty(bankScam)) {
            bankScam = new BankScam();
            bankScam.setBankAccount(report.getInfo());
            bankScam.setDescription(report.getInfoDescription());
            bankScam.setNameAccount(report.getInfo2());
            bankScam.setBankName(report.getInfo3());
            bankScam = repository.save(bankScam);
        }

        ScamStatsDto statsDto = (bankScam.getStats() != null)
                ? new ScamStatsDto(bankScam.getStats())
                : new ScamStatsDto();

        statsDto = scamStatsService.handleStats(statsDto, report, idScamType);

        BankScamStats statsEntity;
        if (ObjectUtils.isEmpty(bankScam.getStats())) {
            statsEntity = new BankScamStats(statsDto); // MapStruct
            statsEntity.setBankScam(bankScam);
        } else {
            statsEntity = bankScam.getStats();
            statsEntity.apply(statsDto);
        }

        statsEntity = bankScamStatsRepository.save(statsEntity);
        bankScam.setStats(statsEntity);
        repository.save(bankScam);
    }


    @Transactional
    @Override
    public void importBankData(MultipartFile file) throws IOException {
        String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();
        log.info("Bắt đầu quá trình import dữ liệu lừa đảo ngân hàng từ file: {}", originalFilename);

        List<BankScamImportRow> rows;
        if (originalFilename.endsWith(".csv")) {
            rows = parseBankCsv(file);
        } else if (originalFilename.endsWith(".xlsx")) {
            rows = parseBankExcel(file);
        } else {
            log.error("Định dạng file không được hỗ trợ: {}. Chỉ hỗ trợ .csv và .xlsx.", originalFilename);
            throw new IllegalArgumentException("Chỉ hỗ trợ file định dạng CSV hoặc XLSX. File nhận được: " + originalFilename);
        }

        List<BankScam> batch = new ArrayList<>(500);
        int skippedRows = 0;
        int processedRows = 0;
        int duplicateRows = 0;

        for (BankScamImportRow r : rows) {
            String bankAccount = StringUtils.hasText(r.getBankAccount()) ? r.getBankAccount().trim() : null;
            String nameAccount = StringUtils.hasText(r.getNameAccount()) ? r.getNameAccount().trim() : null;
            String bankName = StringUtils.hasText(r.getBankName()) ? r.getBankName().trim() : null;

            if (!StringUtils.hasText(bankAccount)) {
                log.warn("Bỏ qua dòng do thiếu thông tin số tài khoản (STK): {}", r);
                skippedRows++;
                continue;
            }

            BankScam scam = repository.findByBankAccountAndNameAccountAndBankName(bankAccount, nameAccount, bankName);

            if (!ObjectUtils.isEmpty(scam)) {
                log.warn("Bỏ qua dòng do thông tin đã tồn tại (trùng STK, Tên chủ TK, Tên NH): STK='{}', Tên TK='{}', Tên NH='{}'", bankAccount, nameAccount, bankName);
                duplicateRows++;
                continue;
            } else {
                scam = repository.findByBankAccount(bankAccount);
                if (ObjectUtils.isEmpty(scam)) {
                    scam = new BankScam();
                    scam.setBankAccount(bankAccount);
                }
            }

            scam.setNameAccount(nameAccount);
            scam.setBankName(bankName);

            BankScamStats stats = scam.getStats();
            if (ObjectUtils.isEmpty(stats)) {
                stats = new BankScamStats();
                stats.setBankScam(scam);
            }
            stats.setVerifiedCount(1);
            stats.setReasonsJson(null);
            stats.setLastReportAt(null);

            scam.setStats(stats);
            batch.add(scam);
            processedRows++;

            if (batch.size() >= 500) {
                log.info("Đang lưu {} bản ghi lừa đảo ngân hàng...", batch.size());
                repository.saveAll(batch);
                em.flush();
                em.clear();
                batch.clear();
                log.info("Lưu và xóa batch thành công.");
            }
        }

        if (!batch.isEmpty()) {
            log.info("Đang lưu {} bản ghi lừa đảo ngân hàng cuối cùng...", batch.size());
            repository.saveAll(batch);
            em.flush();
            em.clear();
            log.info("Lưu và xóa batch cuối cùng thành công.");
        }

        log.info("Hoàn tất import dữ liệu lừa đảo ngân hàng. Số dòng đã xử lý: {}, Số dòng bị bỏ qua (thiếu STK): {}, Số dòng bị bỏ qua (trùng lặp hoàn toàn): {}", processedRows, skippedRows, duplicateRows);
        if (skippedRows > 0) {
            log.warn("Tổng cộng {} dòng đã bị bỏ qua do thiếu số tài khoản.", skippedRows);
        }
        if (duplicateRows > 0) {
            log.warn("Tổng cộng {} dòng đã bị bỏ qua do thông tin (STK, Tên chủ TK, Tên NH) đã tồn tại.", duplicateRows);
        }
    }

    private List<BankScamImportRow> parseBankCsv(MultipartFile file) throws IOException {
        log.debug("Đang thử phân tích file CSV: {}", file.getOriginalFilename());
        List<BankScamImportRow> list = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser csvParser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader(HEADER_STK, HEADER_NAME_ACCOUNT, HEADER_BANK_NAME)
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .setIgnoreEmptyLines(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord csvRecord : csvParser) {
                String stk = csvRecord.isMapped(HEADER_STK) ? csvRecord.get(HEADER_STK) : "";
                String nameAccount = csvRecord.isMapped(HEADER_NAME_ACCOUNT) ? csvRecord.get(HEADER_NAME_ACCOUNT) : "";
                String bankName = csvRecord.isMapped(HEADER_BANK_NAME) ? csvRecord.get(HEADER_BANK_NAME) : "";

                BankScamImportRow row = new BankScamImportRow();
                row.setBankAccount(stk);
                row.setNameAccount(nameAccount);
                row.setBankName(bankName);
                list.add(row);
            }
            log.info("Phân tích thành công {} dòng từ file CSV: {}", list.size(), file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Lỗi khi phân tích file CSV cho dữ liệu lừa đảo ngân hàng: {}", file.getOriginalFilename(), e);
            throw new IOException("Không thể phân tích file CSV: " + e.getMessage(), e);
        }
        return list;
    }

    private List<BankScamImportRow> parseBankExcel(MultipartFile file) throws IOException {
        log.debug("Đang thử phân tích file Excel: {}", file.getOriginalFilename());
        List<BankScamImportRow> list = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            } else {
                log.warn("File Excel trống hoặc không có dòng tiêu đề: {}", file.getOriginalFilename());
                return list;
            }

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowNum++;
                BankScamImportRow dto = new BankScamImportRow();

                String stk = getCellText(row, EXCEL_COL_STK);
                String nameAccount = getCellText(row, EXCEL_COL_NAME_ACCOUNT);
                String bankName = getCellText(row, EXCEL_COL_BANK_NAME);

                dto.setBankAccount(stk);
                dto.setNameAccount(nameAccount);
                dto.setBankName(bankName);
                list.add(dto);
            }
            log.info("Phân tích thành công {} dòng từ file Excel: {}", list.size(), file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Lỗi khi phân tích file Excel cho dữ liệu lừa đảo ngân hàng: {}", file.getOriginalFilename(), e);
            throw new IOException("Không thể phân tích file Excel: " + e.getMessage(), e);
        }
        return list;
    }

    private String getCellText(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (ObjectUtils.isEmpty(cell)) {
            return "";
        }

        String cellValue;
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    org.apache.poi.ss.usermodel.DataFormatter dateFormatter = new org.apache.poi.ss.usermodel.DataFormatter();
                    cellValue = dateFormatter.formatCellValue(cell);
                } else {
                    BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue());
                    cellValue = bd.toPlainString();
                    if (cellValue.contains(".") && cellValue.endsWith("0")) {
                        if (bd.stripTrailingZeros().scale() <= 0) {
                            cellValue = bd.stripTrailingZeros().toPlainString();
                        }
                    }
                }
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
            default:
                cellValue = "";
                break;
        }
        return cellValue.trim();
    }
}
