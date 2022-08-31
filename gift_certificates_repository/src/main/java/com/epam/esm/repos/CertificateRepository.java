package com.epam.esm.repos;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repos.сustomrepos.CustomizedCertificateRepo;
import java.util.Optional;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ComponentScan("com.epam.esm")
public interface CertificateRepository
    extends JpaRepository<Certificate, Long>, CustomizedCertificateRepo<Certificate> {
  Optional<Certificate> findByName(String name);
}
