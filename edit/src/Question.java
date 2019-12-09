public class Question {

    private String question;
    private String correctAnswer;
    private String[] answers;

    public Question(String question, String correctAnswer, String[] answers) {
        setQuestion(question);
        setCorrectAnswer(correctAnswer);
        setAnswers(answers);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return question;
    }
}
