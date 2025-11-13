package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private Coach coach;
    private int count;

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(CounterOfTrainings other) {
        // Сортировка по убыванию количества тренировок
        return Integer.compare(other.count, this.count);
    }

    @Override
    public String toString() {
        return coach.getSurname() + " " + 
               coach.getName().charAt(0) + "." + 
               coach.getMiddleName().charAt(0) + ". - " + 
               count + " тренировок";
    }
}
