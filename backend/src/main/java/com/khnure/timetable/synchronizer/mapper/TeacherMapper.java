package com.khnure.timetable.synchronizer.mapper;

import com.khnure.timetable.synchronizer.dto.TeacherAPIDto;
import com.khnure.timetable.synchronizer.model.Teacher;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
	Teacher toModel(TeacherAPIDto teacherDto);
	List<Teacher> toModel(List<TeacherAPIDto> teacherDtoList);
}
