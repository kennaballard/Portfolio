package ca.qc.johnabbott.cs616.notes.server.controller;

import ca.qc.johnabbott.cs616.notes.server.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ian on 15-10-02.
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(@Param("name") String name);
    User findByEmail(@Param("email") String email);
}
