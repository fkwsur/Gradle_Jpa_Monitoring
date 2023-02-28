package myproject.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import myproject.app.models.User;

public interface UserRepositoryImpl extends JpaRepository<User, Long> {
}
