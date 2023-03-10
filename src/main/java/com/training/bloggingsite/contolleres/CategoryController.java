package com.training.bloggingsite.contolleres;

import com.training.bloggingsite.dtos.CategoryDto;
import com.training.bloggingsite.services.interfaces.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    // Adding category.
    @GetMapping("admin/add-category")
    public ModelAndView getAddCategory(){
        ModelAndView mav = new ModelAndView("add-category");
        mav.addObject("categoryData",new CategoryDto());
        return mav;
    }

    // Saving the category to database.
    @PostMapping("admin/add-category-save")
    public String postAddCategory(@Valid  @ModelAttribute("categoryData") CategoryDto categoryDto, BindingResult result){
        if(result.hasErrors()){
            logger.error(String.valueOf(result));
            return "redirect:/admin/add-category";
        }
        this.categoryService.addCategory(categoryDto);
        return "redirect:/admin/view-categories";
    }

    // Adding Sub-category.
    @GetMapping("/admin/add-subcategory")
    public ModelAndView getAddSubCategory(@RequestParam("id") long id){
        ModelAndView mav = new ModelAndView("add-sub-category");
        mav.addObject("categoryData",new CategoryDto());
        mav.addObject("parentId",id);
        return mav;
    }

    // Saving Sub-category.
    @PostMapping("/admin/add-subcategory-save")
    public String postAddSubCategory(@Valid @RequestParam("id") long parentId,
                                     @ModelAttribute("categoryData") CategoryDto categoryDto
            ,BindingResult result){
        if(result.hasErrors()){
            logger.error(result.toString());
            return "redirect:/admin/view-categories";
        }
        this.categoryService.addSubCategory(parentId,categoryDto);
        return "redirect:/admin/view-categories";
    }

    // Displaying all Categories.
    @GetMapping("admin/view-categories")
    public ModelAndView viewCategories(){
        ModelAndView mav = new ModelAndView("view-categories");
        List<CategoryDto> categoryDtos = this.categoryService.findAllCategory();
        mav.addObject("categories",categoryDtos);
        return mav;
    }

    // Displaying all Sub-Categories.
    @GetMapping("admin/view-subcategories")
    public ModelAndView viewSubCategories(@RequestParam("id") long id){
        ModelAndView mav = new ModelAndView("view-subcategories");
        CategoryDto categoryDto = this.categoryService.findCategoryById(id);
        List<CategoryDto> categoryDtos = this.categoryService.findCategoryByParent(categoryDto);
        mav.addObject("categories",categoryDtos);
        return mav;
    }

    // Delete a Category.
    @GetMapping("admin/delete-category")
    public String deleteCategory(@RequestParam("id") long id){
        this.categoryService.deleteCategory(id);
        return "redirect:/admin/view-categories";
    }


    @GetMapping("user/getAllCategories")
    public ResponseEntity getAllCategories(){
        List<CategoryDto> categoryDtos = this.categoryService.findAllCategoryIncludeChildren();
        return new ResponseEntity<List>(categoryDtos, HttpStatus.OK);
    }

}
