package flashcards;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SetOfCards {
    private Set<FlashCard> cards;

    public SetOfCards() {
        this.cards = new HashSet<>();
    }

    public Set<FlashCard> getCards() {
        return cards;
    }

    public void setCards(Set<FlashCard> cards) {
        this.cards = cards;
    }

    public void addCard(String term, String definition) {
        cards.add(new FlashCard(term, definition, 0));
    }

    public String getTermByDef(String value) {
        return cards.stream().filter(card -> card.getDefinition().equals(value)).map(FlashCard::getTerm)
                .findFirst().get();
    }

    public boolean containsKey(String key) {
        return cards.stream().map(FlashCard::getTerm).anyMatch(term -> term.equals(key));
    }

    public void remove(String term) {
        cards.remove(new FlashCard(term));
    }

    public int loadFromFile(String file) throws IOException {
        Set<FlashCard> newCards;

        InputStream inputStream = new FileInputStream(file);
        ObjectMapper objectMapper = new ObjectMapper();
        newCards = objectMapper.readValue(inputStream, new TypeReference<HashSet<FlashCard>>(){});

        int x = newCards.size();
        newCards.addAll(cards);
        cards = newCards;
        return x;
    }

    public void saveToFile(String file) {

        try (OutputStream outputStream = new FileOutputStream(file)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, cards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FlashCard> getRandomTerms(int x) {
        List<FlashCard> list = new ArrayList<>();
        int i = 0;
        while (i < x) {
            getRandomTerm(list);
            i++;
        }
        return list;
    }

    private void getRandomTerm(List<FlashCard> list) {
        List<FlashCard> allTerms = new ArrayList<>(cards);
        int x = new Random().nextInt(allTerms.size());
        FlashCard card = allTerms.get(x);
        while (list.contains(card)) {
            x = new Random().nextInt(allTerms.size());
            card = allTerms.get(x);
        }
        list.add(card);
    }

    public boolean containsDefinition(String def) {
        return cards.stream().map(FlashCard::getDefinition).anyMatch(d -> d.equals(def));
    }

    public List<FlashCard> findHardestCards() {
        int max = cards.stream().mapToInt(FlashCard::getMistakes).max().orElse(-1);

        return cards.stream().filter(f -> f.getMistakes() == max && f.getMistakes() > 0).collect(Collectors.toList());
    }

    public void resetMistakes() {
        for (FlashCard card : cards) {
            card.setMistakes(0);
        }
    }
}
