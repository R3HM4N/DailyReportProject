package az.projectdailyreport.projectdailyreport.service;

import az.projectdailyreport.projectdailyreport.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String roleName);
}
