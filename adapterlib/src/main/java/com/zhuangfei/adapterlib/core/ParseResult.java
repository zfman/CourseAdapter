package com.zhuangfei.adapterlib.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程实体
 * @author Administrator 刘壮飞
 *
 */
public class ParseResult implements Serializable{

	/**
	 * 课程名
	 */
	private String name;

	/**
	 * 教室
	 */
	private String room;

	/**
	 * 教师
	 */
	private String teacher;

	/**
	 * 第几周至第几周上
	 */
	private List<Integer> weekList=new ArrayList<>();

	/**
	 * 开始上课的节次
	 */
	private int start;

	/**
	 * 上课节数
	 */
	private int step;

	/**
	 * 周几上
	 */
	private int day;

	private String term;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public List<Integer> getWeekList() {
		return weekList;
	}

	public void setWeekList(List<Integer> weekList) {
		this.weekList = weekList;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
}
