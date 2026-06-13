package com.techiguru.interviewai;

public class Interview {

    String category;
    String question;
    String answer;
    String score;
    String feedback;

    public Interview() {
    }

    public Interview(String category,
                     String question,
                     String answer,
                     String score,
                     String feedback) {

        this.category = category;
        this.question = question;
        this.answer = answer;
        this.score = score;
        this.feedback = feedback;
    }
}
