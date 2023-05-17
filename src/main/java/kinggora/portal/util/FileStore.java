package kinggora.portal.util;

import kinggora.portal.domain.AttachFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.storage}")
    private String storagePath;

    /**
     * 첨부 파일을 스토리지에 업로드
     * @param boardId 게시판 id
     * @param postId 게시글 id
     * @param multipartFiles 파일 폼 데이터
     * @return 업로드된 파일 정보 리스트
     */
    public List<AttachFile> uploadFiles(int boardId, int postId, List<MultipartFile> multipartFiles) {
        List<AttachFile> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
                String origFileName = multipartFile.getOriginalFilename();
                long size = multipartFile.getSize();
                String extension = extracted(origFileName);
                String storeFileName = createStoreFileName(extension);
                String storeDir = storagePath + File.separator;

                File saveFile = new File(storeDir, storeFileName);
                try {
                    multipartFile.transferTo(saveFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                AttachFile attachFile = AttachFile.builder()
                        .boardId(boardId)
                        .postId(postId)
                        .origName(origFileName)
                        .storeName(storeFileName)
                        .ext(extension)
                        .size(size)
                        .storeDir(storeDir)
                        .build();
                files.add(attachFile);
            }
        }
        return files;
    }

    /**
     * 스토리지 파일 삭제
     * @param attachFiles 파일 정보 리스트
     */
    public void deleteFiles(List<AttachFile> attachFiles) {
        for (AttachFile attachFile : attachFiles) {
            File file = new File(attachFile.getStoreDir() + attachFile.getStoreName());
            if (file.exists()) {
                boolean result = file.delete();
                if(!result) {
                    throw new RuntimeException("fail FileStore.deleteFiles");
                }
            }
        }
    }

    /**
     * 스토리지에 저장될 이름 생성
     * @param ext 확장자
     * @return 파일 이름
     */
    private String createStoreFileName(String ext) {
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 파일 이름에서 확장자(ext) 추출
     * @param fileName 파일 이름
     * @return 확장자
     */
    private String extracted(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }
}
