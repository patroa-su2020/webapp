package com.patro.SpringBootProject.dao;

import com.patro.SpringBootProject.model.Role;
import com.patro.SpringBootProject.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
