package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        // Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        //Проверить, что за понедельник в 14:00 не вернулось занятий
    }

    // Дополнительные тесты

    @Test
    void testMultipleSessionsAtSameTime() {
        // Тест на несколько тренировок в одно время
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        Group group1 = new Group("Йога для взрослых", Age.ADULT, 60);
        Group group2 = new Group("Пилатес для взрослых", Age.ADULT, 60);

        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        assertEquals(2, sessions.size(), "В 18:00 должно быть две тренировки");
    }

    @Test
    void testEmptyTimetable() {
        // Тест пустого расписания
        Timetable timetable = new Timetable();

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);
        assertEquals(0, sessions.size(), "В пустом расписании не должно быть тренировок");

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();
        assertEquals(0, counters.size(), "В пустом расписании не должно быть тренеров");
    }

    @Test
    void testTimeOrdering() {
        // Тест правильной сортировки по времени
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Сидор", "Сидорович");
        Group group = new Group("Стретчинг", Age.ADULT, 45);

        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(15, 30));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(9, 0));
        TrainingSession session3 = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(12, 15));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertEquals(3, sessions.size(), "Должно быть три тренировки");

        assertEquals(new TimeOfDay(9, 0), sessions.get(0).getTimeOfDay(),
                "Первая тренировка должна быть в 9:00");
        assertEquals(new TimeOfDay(12, 15), sessions.get(1).getTimeOfDay(),
                "Вторая тренировка должна быть в 12:15");
        assertEquals(new TimeOfDay(15, 30), sessions.get(2).getTimeOfDay(),
                "Третья тренировка должна быть в 15:30");
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        // Тест подсчета тренировок для одного тренера
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Морозов", "Андрей", "Викторович");
        Group group = new Group("Бокс", Age.ADULT, 90);

        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session3 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();
        assertEquals(1, counters.size(), "Должен быть один тренер");
        assertEquals(coach, counters.get(0).getCoach(), "Тренер должен совпадать");
        assertEquals(3, counters.get(0).getCount(), "У тренера должно быть 3 тренировки");
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        // Тест подсчета тренировок для нескольких тренеров с сортировкой по убыванию
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Алексеев", "Алексей", "Алексеевич");
        Coach coach2 = new Coach("Борисов", "Борис", "Борисович");
        Coach coach3 = new Coach("Сергеев", "Сергей", "Сергеевич");

        Group group = new Group("Фитнес", Age.ADULT, 60);

        // 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        // 5 тренировок
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(12, 0)));

        // 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.SATURDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.SUNDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0)));

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();
        assertEquals(3, counters.size(), "Должно быть три тренера");

        assertEquals(coach2, counters.get(0).getCoach(), "Первым должен быть тренер с 5 тренировками");
        assertEquals(5, counters.get(0).getCount(), "У первого тренера должно быть 5 тренировок");

        assertEquals(coach3, counters.get(1).getCoach(), "Вторым должен быть тренер с 3 тренировками");
        assertEquals(3, counters.get(1).getCount(), "У второго тренера должно быть 3 тренировки");

        assertEquals(coach1, counters.get(2).getCoach(), "Третьим должен быть тренер с 2 тренировками");
        assertEquals(2, counters.get(2).getCount(), "У третьего тренера должно быть 2 тренировки");
    }

    @Test
    void testCoachEquality() {
        // Тест проверки правильности работы equals и hashCode для Coach
        Coach coach1 = new Coach("Кузнецов", "Иван", "Петрович");
        Coach coach2 = new Coach("Кузнецов", "Иван", "Петрович");
        Coach coach3 = new Coach("Кузнецов", "Петр", "Иванович");

        assertEquals(coach1, coach2, "Тренеры с одинаковыми ФИО должны быть равны");
        assertNotEquals(coach1, coach3, "Тренеры с разными ФИО не должны быть равны");
        assertEquals(coach1.hashCode(), coach2.hashCode(),
                "HashCode для равных тренеров должен быть одинаковым");
    }

    @Test
    void testTimeOfDayComparison() {
        // Тест сравнения времени
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        TimeOfDay time3 = new TimeOfDay(15, 0);
        TimeOfDay time4 = new TimeOfDay(10, 30);

        assertTrue(time1.compareTo(time2) < 0, "10:30 должно быть меньше 10:45");
        assertTrue(time1.compareTo(time3) < 0, "10:30 должно быть меньше 15:00");
        assertEquals(0, time1.compareTo(time4), "10:30 должно быть равно 10:30");
        assertTrue(time3.compareTo(time1) > 0, "15:00 должно быть больше 10:30");
    }

    @Test
    void testGetCountByCoachesOrdering() {
        // Дополнительный тест для проверки сортировки по убыванию
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Один", "Один", "Один");
        Coach coach2 = new Coach("Два", "Два", "Два");

        Group group = new Group("Тест", Age.ADULT, 60);

        // 10 тренировок
        for (int i = 0; i < 10; i++) {
            timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                    DayOfWeek.MONDAY, new TimeOfDay(10 + i, 0)));
        }

        // 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();

        assertTrue(counters.get(0).getCount() > counters.get(1).getCount(),
                "Тренеры должны быть отсортированы по убыванию количества тренировок");
    }

}
