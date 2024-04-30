package self.project.websocket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import self.project.websocket.dto.MessageDto;
import self.project.websocket.model.Account;

@Mapper
public interface DtoAccountMapper {

    DtoAccountMapper INSTANCE = Mappers.getMapper(DtoAccountMapper.class);

    @Mapping(target = "phoneNumber", source = "content",
            defaultValue = "12345678910") // TODO() proper phone num mapping
    @Mapping(target = "password", source = "content",
            defaultValue = "password") // TODO() proper password mapping
    @Mapping(target = "username", source = "sender")
    @Mapping(target = "chatList", expression = "java(java.util.List.of())")
    Account toEntity(MessageDto messageDto);
}
