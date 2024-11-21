package edu.sabanciuniv.repository;

import edu.sabanciuniv.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
