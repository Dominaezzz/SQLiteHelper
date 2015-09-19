package com.blazetechnologies.sql;

import java.util.Locale;

/**
 * Created by Dominic on 13/09/2015.
 */
public class DateTime extends Expr{

	private DateTime(String name, String format, Expr time_string){
		super(name);
		builder.append('(');
		if(format != null){
			builder.append('\'').append(format).append('\'').append(", ");
		}
		builder.append(time_string).append(") ");
	}

	public static DateTime date(String time_string){
		return date(Expr.value(time_string));
	}

	public static DateTime time(String time_string){
		return time(Expr.value(time_string));
	}

	public static DateTime date_time(String time_string){
		return date_time(Expr.value(time_string));
	}

	public static DateTime julian_day(String time_string){
		return julian_day(Expr.value(time_string));
	}

	public static DateTime strf_time(String format, String time_string){
		return strf_time(format, Expr.value(time_string));
	}

	public static DateTime date(Expr time_string){
		return new DateTime("date", null, time_string);
	}

	public static DateTime time(Expr time_string){
		return new DateTime("time", null, time_string);
	}

	public static DateTime date_time(Expr time_string){
		return new DateTime("datetime", null, time_string);
	}

	public static DateTime julian_day(Expr time_string){
		return new DateTime("julianday", null, time_string);
	}

	public static DateTime strf_time(String format, Expr time_string){
		return new DateTime("strftime", format, time_string);
	}

	public static DateTime date(int year, int month, int day){
		return date(String.format("%04d-%02d-%02d", year, month, day));
	}

	public static DateTime time(int year, int month, int day){
		return time(String.format("%04d-%02d-%02d", year, month, day));
	}

	public static DateTime date_time(int year, int month, int day){
		return date_time(String.format("%04d-%02d-%02d", year, month, day));
	}

	public static DateTime julian_day(int year, int month, int day){
		return julian_day(String.format("%04d-%02d-%02d", year, month, day));
	}

	public static DateTime strf_time(String format, int year, int month, int day){
		return strf_time(format, String.format("%04d-%02d-%02d", year, month, day));
	}

	public static DateTime date(){
		return date("now");
	}

	public static DateTime time(){
		return time("now");
	}

	public static DateTime date_time(){
		return date_time("now");
	}

	public static DateTime julian_day(){
		return julian_day("now");
	}

	public static DateTime strf_time(String format){
		return strf_time(format, "now");
	}

	public static DateTime date(int year, int month, int day, int hour, int minute, double seconds){
		return date(String.format("%04d-%02d-%02d %02d:%02d:%05f", year, month, day, hour, minute, seconds));
	}

	public static DateTime time(int year, int month, int day, int hour, int minute, double seconds){
		return time(String.format("%04d-%02d-%02d %02d:%02d:%05f", year, month, day, hour, minute, seconds));
	}

	public static DateTime date_time(int year, int month, int day, int hour, int minute, double seconds){
		return date_time(String.format("%04d-%02d-%02d %02d:%02d:%05f", year, month, day, hour, minute, seconds));
	}

	public static DateTime julian_day(int year, int month, int day, int hour, int minute, double seconds){
		return julian_day(String.format("%04d-%02d-%02d %02d:%02d:%05f", year, month, day, hour, minute, seconds));
	}

	public static DateTime strf_time(String format, int year, int month, int day, int hour, int minute, double seconds){
		return strf_time(format, String.format("%04d-%02d-%02d %02d:%02d:%05f", year, month, day, hour, minute, seconds));
	}

	public static DateTime date(int hour, int minute, double seconds){
		return date(String.format("%02d:%02d:%05f", hour, minute, seconds));
	}

	public static DateTime time(int hour, int minute, double seconds){
		return time(String.format("%02d:%02d:%05f", hour, minute, seconds));
	}

	public static DateTime date_time(int hour, int minute, double seconds){
		return date_time(String.format("%02d:%02d:%05f", hour, minute, seconds));
	}

	public static DateTime julian_day(int hour, int minute, double seconds){
		return julian_day(String.format("%02d:%02d:%05f", hour, minute, seconds));
	}

	public static DateTime strf_time(String format, int hour, int minute, double seconds){
		return strf_time(format, String.format("%02d:%02d:%05f", hour, minute, seconds));
	}

	public static DateTime date(long seconds){
		return date(Expr.value(seconds));
	}

	public static DateTime time(long seconds){
		return time(Expr.value(seconds));
	}

	public static DateTime date_time(long seconds){
		return date_time(Expr.value(seconds));
	}

	public static DateTime julian_day(long seconds){
		return julian_day(Expr.value(seconds));
	}

	public static DateTime strf_time(String format, long seconds){
		return strf_time(format, Expr.value(seconds));
	}

	public DateTime startOfYear(){
		return addModifier(", 'start of year'");
	}

	public DateTime startOfMonth(){
		return addModifier(", 'start of month'");
	}

	public DateTime startOfDay(){
		return addModifier(", 'start of day'");
	}

	public DateTime unixepoch(){
		return addModifier(", 'unixepoch'");
	}

	public DateTime localtime(){
		return addModifier(", 'localtime'");
	}

	public DateTime utc(){
		return addModifier(", 'utc'");
	}

	public DateTime weekday(int N){
		return addModifier(", 'weekday " + N + "'");
	}

	public DateTime plus(int n, Mod unit){
		return addModifier(", '+" + Math.abs(n) + ' ' + unit + "'");
	}

	public DateTime minus(int n, Mod unit){
		return addModifier(", '-" + Math.abs(n) + ' ' + unit + "'");
	}

	private DateTime addModifier(String mod){
		builder.insert(builder.length() - 2, mod);
		return this;
	}

	public enum Mod{
		SECONDS,
		DAYS,
		MINUTES,
		HOURS,
		MONTHS,
		YEARS;

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.ENGLISH);
		}
	}

}
