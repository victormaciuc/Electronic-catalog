package domain;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class StructuraSemestru {

    private int anUniversitar;
    private int semestru;

    private LocalDateTime startSemester;
    private LocalDateTime beginHoliday;
    private LocalDateTime endHoliday;
    private LocalDateTime endSemester;


    private int weekAux;

    private Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public StructuraSemestru() {
        LocalDate startSemesterAux= LocalDate.of(2020, Month.FEBRUARY,24);
        LocalDateTime d2 = LocalDateTime.of(startSemesterAux, LocalTime.MIDNIGHT);
        LocalDateTime d1 = LocalDateTime.now();
        int semestru = 2;
        if(d1.compareTo(d2) < 0) {
            semestru = 1;
        }
        int anU = d1.getYear();
        new StructuraSemestru(anU,semestru);
    }

    public StructuraSemestru(int anUniversitar, int semestru) {
        this.anUniversitar = anUniversitar;
        this.semestru = semestru;

        LocalDate startSemesterAux = LocalDate.MIN;
        LocalDate beginHolidayAux = LocalDate.MIN;
        LocalDate endHolidayAux = LocalDate.MIN;
        LocalDate endSemesterAux = LocalDate.MIN;

        if(anUniversitar == 2019 && semestru == 1) {
            startSemesterAux= LocalDate.of(2019, Month.SEPTEMBER,30);
            beginHolidayAux=LocalDate.of(2019, Month.DECEMBER,23);
            endHolidayAux=LocalDate.of(2020,Month.JANUARY,6);
            endSemesterAux=LocalDate.of(2020,Month.JANUARY,17);

            this.weekAux = 39;
        }
        if(anUniversitar == 2019 && semestru == 2) {
            ///24.02.2020 - 17.04.2020
            startSemesterAux= LocalDate.of(2020, Month.FEBRUARY,24);
            //20.04.2020 - 26.04.2020
            beginHolidayAux=LocalDate.of(2020, Month.APRIL,20);
            //27.04.2020 - 05.06.2020
            endHolidayAux=LocalDate.of(2020,Month.APRIL,27);
            endSemesterAux=LocalDate.of(2020,Month.JUNE,5);
            this.weekAux = 8;
        }

        this.startSemester = LocalDateTime.of(startSemesterAux,LocalTime.MIDNIGHT);
        this.beginHoliday = LocalDateTime.of(beginHolidayAux,LocalTime.MIDNIGHT);
        this.endHoliday = LocalDateTime.of(endHolidayAux,LocalTime.MIDNIGHT);
        this.endSemester = LocalDateTime.of(endSemesterAux,LocalTime.MIDNIGHT);
    }

    public int getWeek(LocalDateTime data) {
        Date d1 = convertToDate(data);

        if(data.compareTo(this.startSemester) < 0) {
            return -1;
        }
        else if (data.compareTo(this.beginHoliday) < 0) {
            //Inainte de vacanta

            return Integer.parseInt(new SimpleDateFormat("w").format(d1))-this.weekAux;
        }
        else if(data.compareTo(this.endHoliday) < 0) {

            return 0;
        }
        else if(data.compareTo(this.endSemester) < 0) {
            if(this.semestru == 2)
                return Integer.parseInt(new SimpleDateFormat("w").format(d1))-this.weekAux - 1;
            else
            if(Integer.parseInt(new SimpleDateFormat("w").format(d1)) == 2)
                return 13;
            return 14;
        }
        else {
            return 15;
        }

    }

}
