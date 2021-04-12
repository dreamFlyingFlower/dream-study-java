package com.wy.jdks.ex;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

/**
 * LocalDateTime,LocalDate,LocalTime的API都差不多
 * 
 * @author ParadiseWY
 * @date 2019-08-22 21:12:13
 * @git {@link https://github.com/mygodness100}
 */
public class T_J8_LocalDate {

	public static void main(String[] args) {
		// 获得当前时间实例,只能这么获取
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println(localDateTime);
		// 直接指定年月日时分秒构建一个实例
		LocalDateTime localDateTime2 = LocalDateTime.of(2019, 8, 22, 13, 13, 13);
		System.out.println(localDateTime2);
		// 直接在年月日时分秒上进行运算操作,返回的都是一个新的实例
		LocalDateTime plusYears = localDateTime.plusYears(2);
		System.out.println(plusYears);
		// 直接设置日期,不可设置毫秒数
		LocalDateTime day1 = localDateTime.withDayOfMonth(10);
		System.out.println(day1);
		LocalDateTime day2 = localDateTime.withMinute(10);
		System.out.println(day2);

		Instant t1 = Instant.now();// 默认获取UTC时区的当前时间,默认是不加时区的,需要加时间偏移才正确
		System.out.println(t1);
		System.out.println(t1.toEpochMilli());// 获得毫秒数,跟其他获取的方法不一样,其他是get
		OffsetDateTime atOffset = t1.atOffset(ZoneOffset.ofHours(8));// 设置时区的偏移时间
		System.out.println(atOffset);
		Instant t2 = atOffset.toInstant();// 得到UTC时区的时间,还原了
		System.out.println(t2);

		// 计算2个时间之间的间隔,带十几秒的时间
		Duration between = Duration.between(LocalDateTime.now(), LocalDateTime.now());
		System.out.println(between.toMillis());
		// 计算2个日期之间的间隔,只有日期,不可带时间
		Period period = Period.between(LocalDate.now(), LocalDate.MAX);
		System.out.println(period.getDays());

		// TemporalAdjuster:时间矫正器,进行相对复杂操作,使用TemporalAdjusters工具类,也可自定义TemporalAdjuster
		LocalDateTime with = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
		System.out.println(with);
		localDateTime.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

		// DateTimeFormatter:格式化,可直接使用已经定义好的格式
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
		System.out.println(formatter.format(localDateTime));
		// 自定义格式化
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd 24HH:mm:ss");
		System.out.println(ofPattern.format(localDateTime));
		// 还原日期,可用DateTimeFormatter,也可以直接使用LocalDateTime
		LocalDateTime.parse("2011-11-11 22:22:22");
		formatter.parse("2011-11-11 22:22:22");

		// ZonedDate,ZonedTime,ZonedDateTime:时区类
		// 获得所有可用的时区
		Set<String> zondIds = ZoneId.getAvailableZoneIds();
		System.out.println(zondIds);
		// 设置时区的时候,获得的时间时区要和设置的时区一致,否则会出现时间不一样的结果
		LocalDateTime localDateTime3 = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
		ZonedDateTime zonedDateTime = localDateTime3.atZone(ZoneId.of("Asia/Shanghai"));
		System.out.println(zonedDateTime);
	}
}