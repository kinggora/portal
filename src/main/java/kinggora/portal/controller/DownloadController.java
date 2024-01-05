package kinggora.portal.controller;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.dto.request.Id;
import kinggora.portal.domain.type.FileType;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DownloadController {

    private final FileService fileService;

    @GetMapping("/download/{fileId}")
    @CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
    public ResponseEntity<Resource> downloadFile(@PathVariable Id fileId) {
        UploadFile uploadFile = fileService.findFileById(fileId.getId());
        if (!FileType.ATTACHMENT.equals(uploadFile.getType())) {
            throw new BizException(ErrorCode.INVALID_FILE_FORMAT, "다운로드 할 수 없는 파일입니다.");
        }
        Resource resource = fileService.loadAsResource(uploadFile.getStoreName());
        String encodedFileName = UriUtils.encode(uploadFile.getOrigName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=" + encodedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(uploadFile.getSize());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
