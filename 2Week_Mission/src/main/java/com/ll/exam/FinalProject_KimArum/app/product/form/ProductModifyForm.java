package com.ll.exam.FinalProject_KimArum.app.product.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductModifyForm {
    @NotBlank(message = "제목")
    private String subject;
    @NotNull(message = "가격")
    private int price;
    @NotBlank(message = "태그")
    private String productTagContents;
}
