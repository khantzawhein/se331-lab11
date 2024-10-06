package se331.lab.controller;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se331.lab.util.CloudStorageHelper;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class BucketController {
    final CloudStorageHelper cloudStorageHelper;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "image") MultipartFile file) throws ServletException, IOException {
        return ResponseEntity.ok(cloudStorageHelper.getStorageFileDto(file, "imageupload-7a4e7.appspot.com"));
    }
}
