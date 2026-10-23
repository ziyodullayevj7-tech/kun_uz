package jahongir.kun_uz.service;

import jahongir.kun_uz.repository.LmsActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LmsActivityService {
    @Autowired
    private LmsActivityRepository lmsActivityRepository;
}
