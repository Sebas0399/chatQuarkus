package service;

import db.Company;
import db.User;
import dtos.request.UserRegisterRequest;
import dtos.response.GenericResponse;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import repo.CompaniesRepository;
import repo.UserRepository;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;
    @Inject
    CompaniesRepository companiesRepository;

    public GenericResponse<Boolean> registerUser(UserRegisterRequest request, Integer companyId) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BcryptUtil.bcryptHash(request.getPassword()));
        user.setRole(null);
        Company company = companiesRepository.findById(companyId);
        if (company == null) {
            return new GenericResponse<>(false, "Company not found",null);
        }
        user.setCompany(company);
        return new GenericResponse<>(true, "User registered successfully",null);
    }
}
