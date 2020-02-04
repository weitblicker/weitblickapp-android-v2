package com.example.weitblickapp_android.ui.faq;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FaqViewModel extends ViewModel {

    String question;
    String answer;
    String titel;

    public FaqViewModel(String question, String answer, String titel){
        this.answer=answer;
        this.question=question;
        this.titel = titel;
    }

    public FaqViewModel(){
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTitel(String titel){
        this.titel = titel;
    }

    public String getTitel(){
        return titel;
    }
}
