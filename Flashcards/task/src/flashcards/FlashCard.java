package flashcards;

import java.util.Objects;

public class FlashCard {
    private String term;
    private String definition;
    private int mistakes;

    public FlashCard() {
    }

    public FlashCard(String term, String definition, int mistakes) {
        this.term = term;
        this.definition = definition;
        this.mistakes = mistakes;
    }

    public FlashCard(String term) {
       this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public boolean answerIsCorrect(String answer){
        return this.definition.equals(answer);
    }

    public void increaseMistakeCount(){
        this.mistakes++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashCard card = (FlashCard) o;
        return Objects.equals(term, card.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term);
    }
}
