package f3f.dev1.domain.category.dao;

import f3f.dev1.domain.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsById (Long id);
    boolean existsByName (String name);
    Optional<Category> findById (Long id);
    Optional<Category> findCategoryByName(String name);
    List<Category> findByParentId (Long parentId);

}
