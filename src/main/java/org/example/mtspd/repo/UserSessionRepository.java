package org.example.mtspd.repo;

import org.example.mtspd.domain.entity.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserSessionRepository extends MongoRepository<UserSession, String> {
}
