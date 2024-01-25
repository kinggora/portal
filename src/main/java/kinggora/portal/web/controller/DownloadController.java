package kinggora.portal.web.controller;

import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.type.FileType;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.model.data.request.Id;
import kinggora.portal.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

/**
 * 파일 다운로드 API 컨트롤러
 * 파일 다운로드 API 요청의 응답을 JSON 형태로 반환
 * 응답 객체: ResponseEntity
 */
@RestController
@RequiredArgsConstructor
public class DownloadController {

    private final FileService fileService;

    /**
     * 파일 다운로드 요청 처리
     * 1. Spring Security 글로벌 설정에서 파일 다운로드 권한 인가
     * 2. DownloadController.downloadFile()
     * - 요청 파일의 타입이 ATTACHMENT 일 때 요청 수행
     * - ResponseEntity 생성 (header: 다운로드 정보, body: 파일 byte array)
     *
     * @param fileId 다운로드할 파일 id
     * @return byte[] API Response
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Id fileId) {
        UploadFile uploadFile = fileService.findFileById(fileId.getId());
        if (!FileType.ATTACHMENT.equals(uploadFile.getType())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS, "다운로드 할 수 없는 파일입니다.");
        }
        byte[] bytes = fileService.loadAsBytes(uploadFile.getStoreName());
        String encodedFileName = UriUtils.encode(uploadFile.getOrigName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=" + encodedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(uploadFile.getSize());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
