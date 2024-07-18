package com.bakaibank.booking.dto.position.converters;

import com.bakaibank.booking.dto.position.CreatePositionDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.entity.Position;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class PositionConvertersManager {
    public static Converter<Position, PositionDTO> positionToPositionDTOConverter() {
        return new Converter<>() {
            @Override
            public PositionDTO convert(MappingContext<Position, PositionDTO> mappingContext) {
                Position position = mappingContext.getSource();

                return new PositionDTO(
                        position.getId(),
                        position.getName()
                );
            }
        };
    }

    public static Converter<CreatePositionDTO, Position> createPositionDTOToPositionConverter() {
        return new Converter<>() {
            @Override
            public Position convert(MappingContext<CreatePositionDTO, Position> mappingContext) {
                CreatePositionDTO createPositionDTO = mappingContext.getSource();

                return new Position(
                        createPositionDTO.getName()
                );
            }
        };
    }
}
