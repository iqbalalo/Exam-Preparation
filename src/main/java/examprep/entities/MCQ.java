package examprep.entities;

import com.google.common.base.Objects;

public class MCQ implements Comparable<MCQ> {

    String id;
    String question;
    String[] answerOptions;
    String correctAnswer;
    String exam;
    String subject;
    String history;
    String note;

    public MCQ() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(String[] answerOptions) {
        this.answerOptions = answerOptions;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("question", question)
                .add("correct answer", correctAnswer)
                .add("exam", exam)
                .add("subject", subject).toString();
    }

    @Override
    public int compareTo(MCQ o) {
        Long thisId = Long.parseLong(id);
        Long thatId = Long.parseLong(o.id);

        if (thisId.equals(thatId)) {
            return 0;
        }
        else if (thisId > thatId) {
            return 1;
        }
        else {
            return -1;
        }
    }

}
