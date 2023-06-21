package com.codestates.PreProject.comment;

import com.codestates.PreProject.answer.entity.Answer;
import com.codestates.PreProject.comment.dto.CommentPostDto;
import com.codestates.PreProject.comment.dto.CommentResponseDto;
import com.codestates.PreProject.comment.entity.AnswerComment;
import com.codestates.PreProject.comment.entity.Comment;
import com.codestates.PreProject.comment.mapper.CommentMapper;
import com.codestates.PreProject.comment.service.CommentService;
import com.codestates.PreProject.exception.ExceptionCode;
import com.codestates.PreProject.exception.LogicalException;
import com.codestates.PreProject.response.ListResponseDto;
import com.codestates.PreProject.response.SingleResponseDto;
import com.codestates.PreProject.util.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/{content}/comments")
@Validated
@Slf4j
public class CommentController <T> {
    private final static String COMMENT_DEF_URL = "/{content}/comments";

    private final CommentService commentService;
    private final CommentMapper mapper;

    public CommentController(CommentService commentService, CommentMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity createComment(@PathVariable("content") String content,
                                        @RequestBody CommentPostDto commentDto) {
        Comment<T> createdComment;

        if (content.equals("answers")) {
            createdComment = commentService.createComment(mapper.commentPostDtoToAnswerComment(commentDto));
        } else if (content.equals("questions")) {
            // Todo: questionComment implementation
            throw new LogicalException(ExceptionCode.INVALID_CONTENT);
        } else {
            throw new LogicalException(ExceptionCode.INVALID_CONTENT);
        }

        URI location = UriCreator.createUri(COMMENT_DEF_URL, createdComment.getCommentId());
        return ResponseEntity.created(location).build();
    }

    // Todo : Read, Delete implementation
    @GetMapping("/{comment-id}")
    public ResponseEntity getComment(@PathVariable("content") String content,
                                    @PathVariable("comment-id") @Positive long commentId){
        Comment comment = commentService.findVerifiedComment(commentId);

        if (content.equals("answers")) {
            if (comment instanceof AnswerComment) {
                return new ResponseEntity<>(
                        new SingleResponseDto<>(
                                mapper.answerCommentToResponseDto((AnswerComment) comment)),
                        HttpStatus.OK);
            } else {
                throw new LogicalException(ExceptionCode.COMMENT_NOT_FOUND);
            }
        } else if (content.equals("questions")) {
            // Todo: questionComment implementation
            throw new LogicalException(ExceptionCode.INVALID_CONTENT);
        } else {
            throw new LogicalException(ExceptionCode.INVALID_CONTENT);
        }
    }

    @GetMapping()
    public ResponseEntity getComments(@PathVariable("content") String content){

        if(content.equals("answers")){
            List<AnswerComment> comments = commentService.findComments();

            return new ResponseEntity<>(
                    new ListResponseDto<>(
                            mapper.answerCommentsToCommentResponses(comments))
                    ,HttpStatus.OK);
        } else if (content.equals("questions")) {
            // Todo : questionComment implementation
            throw new LogicalException(ExceptionCode.INVALID_CONTENT);
        } else {
            throw new LogicalException(ExceptionCode.INVALID_CONTENT);
        }

    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") @Positive long commentId){
        commentService.deleteComment(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
