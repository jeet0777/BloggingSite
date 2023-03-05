package com.training.bloggingsite.services.impl;

import com.training.bloggingsite.contolleres.CommentController;
import com.training.bloggingsite.dtos.CommentDto;
import com.training.bloggingsite.entities.Comment;
import com.training.bloggingsite.entities.Post;
import com.training.bloggingsite.entities.Role;
import com.training.bloggingsite.entities.User;
import com.training.bloggingsite.repositories.CommentRepository;
import com.training.bloggingsite.repositories.PostRepository;
import com.training.bloggingsite.repositories.UserRepository;
import com.training.bloggingsite.services.interfaces.CommentService;
import com.training.bloggingsite.utils.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public String addComment(CommentDto commentDto,long postId,String userEmail) {
        User user = this.userRepository.findByEmail(userEmail);
        Post post = this.postRepository.findById(postId).get();
        commentDto.setUserDto(UserConvertor.toUserDto(user));
        commentDto.setPostDto(PostConvertor.toPostDto(post));
        List<Role> roles = user.getRoles().stream().toList();
        if(roles.get(0).getName().equals(DefaultValue.ADMIN)){
            commentDto.setVerified(true);
            commentRepository.save(CommentConverter.toComment(commentDto));
            logger.info("Commented by "+user.getName()+" on post "+post.getTitle());
            return "redirect:/admin/post/"+post.getId();
        }
        else {
            commentDto.setVerified(false);
            commentRepository.save(CommentConverter.toComment(commentDto));
            logger.info("Commented by "+user.getName()+" on post "+post.getTitle());
            return "redirect:/user/post/"+post.getId();
        }
    }

    @Override
    public List<CommentDto> findCommentByPostVerified(long postId) {
        List<Comment> comments = this.commentRepository.findByPostIdAndIsVerifiedTrue(postId);
        List<CommentDto> commentDtos = comments.stream().map(C->CommentConverter.toCommentDto(C)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public List<CommentDto> findAllPostById(long postId) {
        List<Comment> comments = this.commentRepository.findAllByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(C->CommentConverter.toCommentDto(C)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public void updateVerification(long commentId, boolean isVerified) {
        Comment comment = this.commentRepository.findById(commentId).get();
        this.commentRepository.updateVerificationStatus(commentId,!isVerified);
        logger.info("Comment verified as : " + !isVerified + " for id "+comment.getId());
    }

    @Override
    public String redirectToPost(String email, long postId) {
        User user = this.userRepository.findByEmail(email);
        List<Role> roles = user.getRoles().stream().toList();
        if(roles.get(0).getName().equals(DefaultValue.ADMIN)){
            return "redirect:/admin/post/"+postId;
        }
        else {
            return "redirect:/user/post/"+postId;
        }
    }


}
