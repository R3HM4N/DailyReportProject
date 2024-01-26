package az.projectdailyreport.projectdailyreport.dto.team;

import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class TeamGetByIdDto {
    private long id;
    private String name;
    private List<TeamUserDto> users;
}
