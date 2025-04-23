package com.aladdin.youbank001.mail;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    VERIFICATION("Here's the 6-digit verification code you requested\n",
            """
                    Hi {userName},
                    Use the code below to finish your sign up.
                    {code}
                    This code expires in 15 minutes.
                    Ignore this email if you have not made the request."""
    ),

    PASSWORD_RESET("You have requested to reset your password\n",
            """
                    Hi {userName},
                    Use the link below to reset your password:
                    {link}
                    Ignore this email if you do remember your password, or you have not made the request."""
    ),
//TODO burda cixarislari stabillesdir
    PAYMENT_STATEMENTS("Withdraw payment \n",
            """
                    Your statements: {start} __ {end} . 
                    """),

    BALANCE_INFO("Balance information\n",
            """
                    Hi {userName},
                    Your card number {cardNumber} has a balance of -9 and is currently blocked.
                    We ask that you increase the balance within 5 days. Otherwise, your card will be canceled.
                    This code expires in 15 minutes.
                    Ignore this email if you have not made the request."""
    ),

    RESET_PIN("Email to change pin code\n",
            """ 
                    Hi {userName},
                    This email is for changing the PIN code of the card.
                    Your old PIN code {PIN}
                    """);


    @Getter
    private final String subject;
    private final String body;


    EmailTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

}