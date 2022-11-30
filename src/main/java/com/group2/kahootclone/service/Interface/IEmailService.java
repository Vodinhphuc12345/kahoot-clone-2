package com.group2.kahootclone.service.Interface;


import com.group2.kahootclone.object.EmailDetails;

public interface IEmailService {
    String sendSimpleEmail(EmailDetails emailDetails);
}
