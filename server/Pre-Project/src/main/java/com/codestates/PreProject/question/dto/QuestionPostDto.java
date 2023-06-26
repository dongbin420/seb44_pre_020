package com.codestates.PreProject.question.dto;

import com.codestates.PreProject.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class QuestionPostDto {

    @NotNull(message="userid 를 입력하세요")
    private Long userId;

    @NotBlank(message="제목을 입력하세요")
    private String title;

    @NotBlank(message="질문을 입력하세요")
    private String content;

    public User getUser(){
        User user = new User();
        user.setUserId(userId);
        return user;
    }
}