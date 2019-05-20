package ca.qc.johnabbott.cs616.notes.server.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ian on 15-10-02.c
 */
@Entity
@NoteDatesRange
@Table(name="note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noteid")
    private long id;

    @Column(name="created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name="reminder")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reminder;

    @Column(name="modified", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="body")
    private String body;

    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "createdby")
    private User createdBy;

    public long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

     public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
