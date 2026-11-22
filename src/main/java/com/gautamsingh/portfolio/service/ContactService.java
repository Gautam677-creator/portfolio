package com.gautamsingh.portfolio.service;

import com.gautamsingh.portfolio.model.ContactMessage;
import com.gautamsingh.portfolio.model.ContactRequest;
import com.gautamsingh.portfolio.repository.ContactMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactMessageRepository repository;

    public ContactService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    public void save(ContactRequest request) {
        ContactMessage entity = new ContactMessage(
                request.name(), request.email(), request.subject(), request.message());
        repository.save(entity);
        log.info("New inquiry #{} from {} <{}> — {}",
                entity.getId(), entity.getName(), entity.getEmail(), entity.getSubject());
    }

    public long count() {
        return repository.count();
    }
}
