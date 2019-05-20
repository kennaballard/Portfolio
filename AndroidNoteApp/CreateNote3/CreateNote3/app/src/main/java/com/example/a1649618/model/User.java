package com.example.a1649618.model;

import android.graphics.Bitmap;

import com.example.a1649618.sqlite.Identifiable;

import java.util.Objects;

public class User implements Identifiable<Long> {

    private long id;
    private String name;
    private Bitmap avatar;
    private String email;

    public User() {
        this(-1);
    }

    public User(long id) {
        this.id = id;
    }

    public User(long id, String name, Bitmap avatar, String email) {
        this(id);
        this.name = name;
        this.avatar = avatar;
        this.email = email;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public User setAvatar(Bitmap avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar=" + avatar +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(avatar, user.avatar) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatar, email);
    }
}
