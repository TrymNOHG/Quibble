package edu.ntnu.idatt2105.backend.repo.category;

import edu.ntnu.idatt2105.backend.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * This repository creates the CRUD operations for the category entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
}
