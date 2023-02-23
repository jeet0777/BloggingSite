package com.training.bloggingsite.services.impl;

import com.training.bloggingsite.entities.Comment;
import com.training.bloggingsite.repositories.CommentRepositories;
import com.training.bloggingsite.services.interfaces.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepositories repositories;
    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        return repositories.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(long id) {
        repositories.deleteById(id);
    }

    @Override
    @Transactional
    public List<Comment> getCommentByUser(long userId) {
        List<Comment> commentByUser = new ArrayList<>();
//        repositories.findAllById(userId);
        return commentByUser;
    }

    @Override
    @Transactional
    public List<Comment> getCommentByPost() {
        List<Comment> comentByPost = new ArrayList<>();
//        repositories.findAllById()
        return comentByPost;
    }

    @Override
    @Transactional
    public List<Comment> getVerifiedComments() {
        return null;
    }

    @Override
    @Transactional
    public List<Comment> getUnverifiedComments() {
        return null;
    }
}