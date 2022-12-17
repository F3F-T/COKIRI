package f3f.dev1.domain.category.dao;

import f3f.dev1.domain.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existById (Long id);
    Optional<Category> findById(Long id);
    List<Category> findByParentId (Long id);





}
