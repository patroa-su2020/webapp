package com.patro.SpringBootProject.dao;

import com.patro.SpringBootProject.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
