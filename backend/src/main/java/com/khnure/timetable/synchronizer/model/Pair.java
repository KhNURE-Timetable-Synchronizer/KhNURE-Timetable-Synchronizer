package com.khnure.timetable.synchronizer.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Pair {
	private Long id;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Subject subject;
	private String auditory;
	private String type;
	private List<Group> groupList;
	private List<Teacher> teacherList;

	@Data
	public static class Subject {
		private String id;
		private String brief;
		private String title;
	}
}
