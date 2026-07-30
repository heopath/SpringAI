package com.example.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DateTimeTools {

	@McpTool(description = "현재 날짜와 시간 정보를 제공합니다.")
	public String getCurrentDateTime() {

		String nowTime = LocalDateTime.now()
				.atZone(LocaleContextHolder.getTimeZone().toZoneId())
				.toString();

		log.info("현재 시간: {}", nowTime);

		return nowTime;
	}

	@McpTool(description = "지정된 시간에 알람을 설정합니다.")
	public String setAlarm(
			@ToolParam(
				description = "ISO-8601 형식의 시간",
				required = true
			)
			String time) {

		// ISO-8601에서는 24시를 지원하지 않으므로
		// 다음 날 00시로 변경한다.
		if (time.contains("T24:")) {

			int tIndex = time.indexOf("T");

			String datePart = time.substring(0, tIndex);
			String timePart = time.substring(tIndex + 1);

			LocalDate date = LocalDate.parse(datePart).plusDays(1);
			timePart = timePart.replaceFirst("24:", "00:");

			time = date + "T" + timePart;
		}

		LocalDateTime alarmTime = LocalDateTime.parse(
				time,
				DateTimeFormatter.ISO_DATE_TIME
		);

		log.info("알람 설정 시간: {}", alarmTime);

		return "알람이 " + alarmTime + "으로 설정되었습니다.";
	}
}