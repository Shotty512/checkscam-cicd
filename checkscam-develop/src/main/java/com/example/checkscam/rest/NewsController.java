package com.example.checkscam.rest;

import com.example.checkscam.component.LocalizationUtils;
import com.example.checkscam.constant.MessageKeys;
import com.example.checkscam.dto.request.NewsRequestDto;
import com.example.checkscam.entity.Attachment;
import com.example.checkscam.entity.News;
import com.example.checkscam.exception.DataNotFoundException;
import com.example.checkscam.exception.FileUploadValidationException;
import com.example.checkscam.exception.InvalidParamException;
import com.example.checkscam.response.ResponseObject;
import com.example.checkscam.service.impl.NewsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsServiceImpl newsService;
    private final LocalizationUtils localizationUtils;

    // GET all news
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }

    // GET news by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<News>> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        if (news.isPresent()) {
            return new ResponseEntity<>(news, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST news
    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody NewsRequestDto createNewsRequestDto) {
        News createdNews = newsService.createNews(createNewsRequestDto);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    // PUT news
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody NewsRequestDto newsRequestDto) {
        News updatedNews = newsService.updateNews(id, newsRequestDto);
        if (updatedNews != null) {
            return new ResponseEntity<>(updatedNews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE news
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        boolean isDeleted = newsService.deleteNews(id);
        Map<String, String> response = new HashMap<>();
        response.put("message","Deleted News successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadAttachments(
            @PathVariable("id") Long newsId,
            @RequestParam("files")List<MultipartFile> files) {
        try {
            List<Attachment> attachments = newsService.uploadAndCreateAttachments(newsId, files);
            if (attachments.isEmpty() && (files == null || files.stream().allMatch(f -> f == null || f.isEmpty() || f.getSize() == 0))) {
                return ResponseEntity.ok().body(ResponseObject.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_ATTACHMENTS_NO_VALID_FILES))
                        .status(HttpStatus.OK)
                        .data(attachments)
                        .build());
            }

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_ATTACHMENTS_SUCCESSFULLY))
                    .status(HttpStatus.OK)
                    .data(attachments)
                    .build());

        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(e.getMessage()))
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } catch (InvalidParamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (FileUploadValidationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(e.getMessageKey(), e.getArgs()))
                    .status(e.getHttpStatus())
                    .build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_ATTACHMENTS_FILE_STORAGE_ERROR, e.getMessage()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.ERROR_OCCURRED_DEFAULT, e.getMessage()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Resource image = newsService.loadImage(imageName);
            String mimeType = newsService.getImageMimeType(imageName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"))
                    .body(image);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tên file không hợp lệ");
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể tải ảnh");
        }
    }
}