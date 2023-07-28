package com.lorenadiam;

import org.springframework.data.repository.CrudRepository;

public interface StudentIdCardRepository extends CrudRepository<StudentIdCard, Long> { // We only need CRUD (not JPA)
}
