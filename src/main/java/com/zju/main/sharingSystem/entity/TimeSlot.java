package com.zju.main.sharingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {

    private int timeSlotId;

    private int day;

    private LocalTime startTime;

    private LocalTime endTime;

    public String getString() {
        String[] days = {
                "", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"
        };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("%s %s - %s",
                days[day],
                startTime.format(formatter),
                endTime.format(formatter));
    }
}
