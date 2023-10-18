package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.AttachFile;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.FileRepository;
import kinggora.portal.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;

    /**
     * 첨부 파일 업로드 및 메타 데이터 저장
     *
     * @param postId         게시글 id
     * @param multipartFiles 파일 폼 데이터
     */
    public void saveFiles(int postId, List<MultipartFile> multipartFiles) {
        // in store
        List<AttachFile> attachFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
                attachFiles.add(fileStore.uploadFile(postId, multipartFile));
            }
        }
        if (!attachFiles.isEmpty()) {
            // in database
            fileRepository.saveFile(attachFiles);
        }
    }

    /**
     * 첨부 파일 단건 조회
     *
     * @param id 파일 id
     * @return
     */
    public AttachFile findFileById(Integer id) {
        return fileRepository.findFileById(id)
                .orElseThrow(() -> new BizException(ErrorCode.FILE_NOT_FOUND));
    }

    /**
     * 게시글 첨부 파일 조회
     *
     * @param postId 게시글 id
     * @return
     */
    public List<AttachFile> findFiles(int postId) {
        return fileRepository.findFiles(postId);
    }

    /**
     * 게시글 첨부 파일 삭제
     *
     * @param postId 게시글 id
     * @return
     */
    public void deleteFiles(int postId) {
        List<AttachFile> files = fileRepository.findFiles(postId);
        fileStore.deleteFiles(files);
    }

    /**
     * 파일 다운로드
     *
     * @param file 파일 정보
     * @return 파일 input stream resource
     */
    public Resource loadAsResource(AttachFile file) {
        return fileStore.loadAsResource(file);
    }
}
