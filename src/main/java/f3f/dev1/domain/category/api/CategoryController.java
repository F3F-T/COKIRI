package f3f.dev1.domain.category.api;

import f3f.dev1.domain.category.application.CategoryService;
import f3f.dev1.domain.category.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    //@PostMapping(value = "/category")
    //public ResponseEntity

    @GetMapping(value = "/category")
    public ResponseEntity<List<Category>> getAllCategory(){
        List<Category> totalCategory = categoryService.readTotalCategory();
        return new ResponseEntity<>(totalCategory, HttpStatus.OK);
    }
}
