package com.bakaibank.booking.dto.team.converters;

import com.bakaibank.booking.dto.team.CreateTeamDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.entity.Team;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class TeamConvertersManager {
    public static Converter<Team, TeamDTO> teamToTeamDTOConverter() {
        return new Converter<>() {
            @Override
            public TeamDTO convert(MappingContext<Team, TeamDTO> mappingContext) {
                Team team = mappingContext.getSource();
                return new TeamDTO(
                        team.getId(),
                        team.getName()
                );
            }
        };
    }

    public static Converter<CreateTeamDTO, Team> createTeamDTOToTeamConverter() {
        return new Converter<>() {
            @Override
            public Team convert(MappingContext<CreateTeamDTO, Team> mappingContext) {
                CreateTeamDTO createTeamDTO = mappingContext.getSource();
                return new Team(
                        createTeamDTO.getName()
                );
            }
        };
    }

}
