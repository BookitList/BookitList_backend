package cotato.bookitlist.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileToS3(Long memberId, String fileName, MultipartFile multipartFile) {
        String key = generateKey(memberId, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
        } catch (IOException ex) {
            log.error("파일 업로드 중 에러가 발생했습니다: {}", ex.getMessage());
            throw new MultipartException("파일 업로드 중 에러가 발생했습니다.", ex);
        } catch (S3Exception ex) {
            log.error("S3에 파일을 업로드하는 중 에러가 발생했습니다: {}", ex.getMessage());
            throw new MultipartException("S3에 파일을 업로드하는 중 에러가 발생했습니다.", ex);
        }

        return getS3FileUrl(key);
    }

    public String getS3FileUrl(String key) {
        try {
            return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build()).toString();
        } catch (NoSuchKeyException ex) {
            log.error("해당 키의 파일이 존재하지 않습니다: {}", key, ex);
            throw new RuntimeException("해당 키의 파일이 존재하지 않습니다.",ex);
        } catch (S3Exception ex) {
            log.error("S3에서 파일 URL을 가져오는 중 에러가 발생했습니다: {}", ex.getMessage());
            throw new RuntimeException("S3에서 파일 URL을 가져오는 중 에러가 발생했습니다.", ex);
        }
    }

    public void deleteS3File(Long memberId, String fileName) {
        String key = generateKey(memberId, fileName);
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
        } catch (S3Exception ex) {
            log.error("S3에서 파일 삭제 중 에러가 발생했습니다: {}", ex.getMessage());
            throw new RuntimeException("S3에서 파일 삭제 중 에러가 발생했습니다.", ex);
        }
    }

    private String generateKey(Long memberId, String fileName) {
        return String.format("member%s/%s", memberId, fileName);
    }


}
