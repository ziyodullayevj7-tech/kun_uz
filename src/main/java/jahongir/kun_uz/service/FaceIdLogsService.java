package jahongir.kun_uz.service;

import jahongir.kun_uz.repository.FaceIdLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceIdLogsService {
    @Autowired
    private FaceIdLogsRepository faceIdLogsRepository;
}
