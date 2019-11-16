package com.example.weitblickapp_android.ui.faq;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FaqViewModel extends ViewModel {

    int id;
    String question;
    String answer;

    public FaqViewModel(int id, String answer, String question){
        this.id=id;
        this.answer=answer;
        this.question=question;
    }

    public FaqViewModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
