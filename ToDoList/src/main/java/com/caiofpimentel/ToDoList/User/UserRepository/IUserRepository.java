package com.caiofpimentel.ToDoList.User.UserRepository;

import com.caiofpimentel.ToDoList.User.UserModel.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByUsername(String username);

}
