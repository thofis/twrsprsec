package com.example.thofis.twrsprsec.repository;

import com.example.thofis.twrsprsec.security.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
