package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.dto.RoleDto;
import az.projectdailyreport.projectdailyreport.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(RoleDto roleDto);
    List<Role> getAllRoles();
    Role getRoleByName(String roleName);
}
