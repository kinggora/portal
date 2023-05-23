package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.AttachFile;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.FileRepository;
import kinggora.portal.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;

    /**
     * 첨부 파일 업로드 및 메타 데이터 저장
     * @param boardId 게시판 id
     * @param postId 게시글 id
     * @param files 파일 폼 데이터
     */
    public void saveFiles(int boardId, int postId, List<MultipartFile> files) {
        List<AttachFile> attachFiles = fileStore.uploadFiles(boardId, postId, files);
        if(!attachFiles.isEmpty()) {
            fileRepository.saveFile(attachFiles);
        }
    }

    /**
     * 첨부 파일 단건 조회
     * @param id 파일 id
     * @return
     */
    public AttachFile findFileById(Integer id) {
        return fileRepository.findFileById(id)
                .orElseThrow(() -> new BizException(ErrorCode.FILE_NOT_FOUND));
    }

    /**
     * 게시글 첨부 파일 조회
     * @param boardId 게시판 id
     * @param postId 게시글 id
     * @return
     */
    public List<AttachFile> findFiles(int boardId, int postId){
        return fileRepository.findFiles(boardId, postId);
    }

    /**
     * 게시글 첨부 파일 삭제
     * @param boardId 게시판 id
     * @param postId 게시글 id
     * @return
     */
    public void deleteFiles(int boardId, int postId) {
        List<AttachFile> files = fileRepository.findFiles(boardId, postId);
        fileStore.deleteFiles(files);
    }


}
