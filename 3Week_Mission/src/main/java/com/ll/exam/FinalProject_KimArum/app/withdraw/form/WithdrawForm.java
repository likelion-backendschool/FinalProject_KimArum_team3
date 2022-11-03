package com.ll.exam.FinalProject_KimArum.app.withdraw.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WithdrawForm {
    @NotEmpty
    private String bankName;
    @NotEmpty
    private String bankAccountNo;
    private long price;
}
