package com.khnure.timetable.synchronizer.mapper;

import com.khnure.timetable.synchronizer.dto.PairApiDto;
import com.khnure.timetable.synchronizer.model.Pair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring",
uses = {TeacherMapper.class, GroupMapper.class})
public interface PairMapper {
	@Mapping(source = "pairDto.startTime",target = "startTime",qualifiedByName = "toLocalDateTime")
	@Mapping(source = "pairDto.endTime",target = "endTime",qualifiedByName = "toLocalDateTime")
	Pair toModel(PairApiDto pairDto);

	@Named("toLocalDateTime")
	static LocalDateTime toLocalDateTime(String timeInSeconds){

		return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timeInSeconds)), ZoneId.systemDefault());
	}
}
