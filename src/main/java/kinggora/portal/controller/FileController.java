package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.UploadFile;
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

    public DataResponse<UploadFile> getFileById(@PathVariable Integer id) {
        UploadFile file = fileService.findFileById(id);
        return DataResponse.of(file);
    }

    @GetMapping("/{postId}")
    public DataResponse<List<UploadFile>> getFiles(@PathVariable Integer postId) {
        List<UploadFile> files = fileService.findFiles(postId);
        return DataResponse.of(files);
    }

    @PostMapping("/{postId}")
    public DataResponse<Void> saveFiles(@PathVariable Integer postId, FileDto dto) {
        fileService.saveFiles(postId, dto);
        return DataResponse.empty();
    }

    @GetMapping("/thumbnails")
    public DataResponse<List<UploadFile>> getThumbnailFiles(List<Integer> postIds) {
        List<UploadFile> files = fileService.findThumbnails(postIds);
        return DataResponse.of(files);
    }

    @GetMapping("/download/{id}")
    @CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        UploadFile uploadFile = fileService.findFileById(id);
        Resource resource = fileService.loadAsResource(uploadFile.getStoreName());
        String encodedFileName = UriUtils.encode(uploadFile.getOrigName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=" + encodedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
