package com.example.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DateTimeTools {

	@Tool(description = "현재 날짜와 시간 정보를 제공합니다.")
	public String getCurrentDateTime() {

		String nowTime = LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();

		log.info("현재 시간: {}", nowTime);
		return nowTime;
	}

	@Tool(description = "지정된 시간에 알람을 설정합니다.")
	public void setAlarm(@ToolParam(description = "ISO-8601 형식의 시간", required = true) String time) {

		if (time.contains("T24:")) {
			int tIndex = time.indexOf("T");
			String datePart = time.substring(0, tIndex);
			String timePart = time.substring(tIndex + 1);
			
			// 날짜 +1
			LocalDate date = LocalDate.parse(datePart);
			date = date.plusDays(1);
			
			// "24:" → "00:"으로 교체
			timePart = timePart.replaceFirst("24:", "00:");
			
			// 재조합
			time = date + "T" + timePart;
		}
		
		LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
	    log.info("알람 설정 시간: " + alarmTime);
	}
}