package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class PairApiDto {
	@JsonAlias("id")
	private String id;
	@JsonAlias("start_time")
	private String startTime;
	@JsonAlias("end_time")
	private String endTime;
	@JsonAlias("subjectDto")
	private SubjectDto subject;
	private String auditory;
	private String type;
	@JsonAlias("groups")
	private List<GroupAPIDto> groupList;
	@JsonAlias("teachers")
	private List<TeacherAPIDto> teacherList;

	@Data
	public static class SubjectDto {
		private String id;
		private String brief;
		private String title;

	}
}