package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.AttachFile;
import kinggora.portal.domain.dto.FileDto;
import kinggora.portal.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public DataResponse<AttachFile> getFileById(@PathVariable Integer id) {
        AttachFile file = fileService.findFileById(id);
        return DataResponse.of(file);
    }

    @GetMapping("/{postId}")
    public DataResponse<List<AttachFile>> getFiles(@PathVariable Integer postId) {
        List<AttachFile> files = fileService.findFiles(postId);
        return DataResponse.of(files);
    }

    @PostMapping
    public DataResponse<Void> saveFiles(FileDto dto) {
        fileService.saveFiles(dto.getPostId(), dto.getNewFiles());
        return DataResponse.empty();
    }

    @GetMapping("/download/{id}")
    @CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        AttachFile attachFile = fileService.findFileById(id);
        Resource resource = fileService.loadAsResource(attachFile);
        String encodedFileName = UriUtils.encode(attachFile.getOrigName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=" + encodedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
