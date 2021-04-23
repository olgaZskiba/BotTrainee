package by.integrator.telegrambot.repositories;

import by.integrator.telegrambot.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
