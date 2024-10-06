package se331.lab.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.servlet.ServletException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import se331.lab.entity.StorageFileDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Component
public class CloudStorageHelper {
    private static Storage storage = null;

    static {
        try {
            InputStream serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String uploadFile(MultipartFile filePart, final String bucketname) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmssSSS");
        String dtString = sdf.format(new Date());
        final String fileName = dtString + "-" + filePart.getOriginalFilename();

        InputStream is = filePart.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] readBuf = new byte[4096];
        while (is.available() > 0) {
            int bytesRead = is.read(readBuf);
            os.write(readBuf, 0, bytesRead);
        }

        BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketname, fileName)
                .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .setContentType(filePart.getContentType()).build(), os.toByteArray());

        return blobInfo.getMediaLink();
    }

    public String getImageUrl(MultipartFile file, final String bucket) throws IOException, ServletException {
        final String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains(".")) {
            final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            String[] allowedExt = {"jpg", "jpeg", "png", "gif"};
            for (String s: allowedExt) {
                if (extension.equals(s)) {
                    return uploadFile(file, bucket);
                }
            }
            throw new ServletException("file must be an image");
        }
        return null;
    }

    public StorageFileDto getStorageFileDto(MultipartFile file, final String bucket) throws IOException, ServletException {
        final String fileName = file.getOriginalFilename();

        if (fileName != null && fileName.contains(".")) {
            final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            String[] allowedExt = {"jpg", "jpeg", "png", "gif"};
            for (String s: allowedExt) {
                if (extension.equals(s)) {
                    String urlName = this.uploadFile(file, bucket);
                    return StorageFileDto.builder().name(urlName).build();
                }
            }
            throw new ServletException("file must be an image");
        }
        return null;
    }

}
