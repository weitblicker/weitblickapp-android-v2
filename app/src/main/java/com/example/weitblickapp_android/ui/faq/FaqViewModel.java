package com.example.weitblickapp_android.ui.faq;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FaqViewModel extends ViewModel {

    String titel;
    String answer;
    String question;

    public FaqViewModel(String titel, String question, String answer){
        this.titel = titel;
        this.answer = answer;
        this.question = question;
    }

    public FaqViewModel(){
    }

    public void setTitel(String titel){
        this.titel = titel;
    }

    public String getTitel(){
        return titel;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
