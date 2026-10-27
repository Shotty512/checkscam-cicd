package com.example.checkscam.util;

import com.example.checkscam.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DataUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String normalizePhoneNumber(String input) {
        if (input == null) {
            return null;
        }

        String phone = input.trim()
                .replaceAll("[^+\\d]", "");

        if (phone.startsWith("+84")) {
            phone = "0" + phone.substring(3);
        }

        phone = phone.replaceAll("\\D", "");

        return phone;
    }

    public static void validatePhoneNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException("Số điện thoại không được để trống.");
        }

        String normalized = normalizePhoneNumber(input);

        if (!normalized.matches("^0[0-9]{9}$")) {
            throw new ValidationException("Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có đúng 10 chữ số.");
        }
    }

    public static void validateBankAccount(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException("Số tài khoản không được để trống.");
        }

        String normalized = input.trim().replaceAll("\\D", "");

        if (!normalized.matches("^\\d{8,16}$")) {
            throw new ValidationException("Số tài khoản không hợp lệ. Phải chứa 8–16 chữ số.");
        }
    }

    public static String extractFullDomain(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }

        try {
            if (!url.matches("^(?i)(http|https|ftp)://.*")) {
                url = "http://" + url;
            }

            URL parsedUrl = new URL(url);
            String host = parsedUrl.getHost();

            if (host != null && host.startsWith("www.")) {
                host = host.substring(4);
            }

            return host;
        } catch (Exception e) {
            return null;
        }
    }

}
