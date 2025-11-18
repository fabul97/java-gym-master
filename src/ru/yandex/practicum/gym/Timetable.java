package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    // List для хранения нескольких тренировок в одно время
    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }

        daySchedule.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            result.addAll(daySchedule.get(time));
        }

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        if (sessions == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(sessions);
    }

    public List<CounterOfTrainings> getCountByCoaches() {

        HashMap<Coach, Integer> coachCounters = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounters.put(coach, coachCounters.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();

        for (Map.Entry<Coach, Integer> entry : coachCounters.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(result);

        return result;
    }
}
