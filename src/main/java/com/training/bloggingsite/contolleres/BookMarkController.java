package com.training.bloggingsite.contolleres;

import com.training.bloggingsite.dtos.PostDto;
import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.services.interfaces.BookmarkService;
import com.training.bloggingsite.services.interfaces.PostService;
import com.training.bloggingsite.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class BookMarkController {
    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    //View All BookMarked Post for USER
    @GetMapping("admin/all-bookmarked-post")
    public ModelAndView findAllBookmarkedPostAdmin(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("admin-view-all-post");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<PostDto> postDataByBookmark = bookmarkService.getAllBookMarkedPost(userDto);
        modelAndView.addObject("postData", postDataByBookmark);
        return modelAndView;
    }

    //Change BOOKMARK Status FuserServiceOR ADMIN
    @GetMapping("admin/post/change-bookmark-status")
    public String changeBookMarkStatusAdmin(@RequestParam long postId,
                                            @RequestParam boolean isBookMarked,
                                            Principal principal) {

        bookmarkService.changeBookMarkStatus(postId, isBookMarked, principal);
        return "redirect:/admin/post/" + postId;
    }


    //View All BookMarked Post for USER
    @GetMapping("user/all-bookmarked-post")
    public ModelAndView findAllBookmarkedPostUser(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("user-view-all-post");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<PostDto> postDataByBookmark = bookmarkService.getAllBookMarkedPost(userDto);
        modelAndView.addObject("postData", postDataByBookmark);
        return modelAndView;
    }


    //Change BOOKMARK Status FOR USER
    @GetMapping("user/post/change-bookmark-status")
    public String changeBookMarkStatusUser(@RequestParam long postId,
                                           @RequestParam boolean isBookMarked,
                                           Principal principal) {
        bookmarkService.changeBookMarkStatus(postId, isBookMarked, principal);
        return "redirect:/user/post/" + postId;
    }


}
