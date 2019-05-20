package ca.qc.johnabbott.cs616.notes.server.controller;

import ca.qc.johnabbott.cs616.notes.server.model.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "note", path = "note")
public interface NoteRepository extends CrudRepository<Note, Long> {
}
