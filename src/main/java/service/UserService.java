// package service;

// import Domain.Contracts.UserRepository;
// import Infraestructure.Contracts.Entities.User;
// import db.Company;
// import dtos.request.UserRegisterRequest;
// import dtos.response.GenericResponse;
// import io.quarkus.elytron.security.common.BcryptUtil;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;
// import repo.CompaniesRepository;

// @ApplicationScoped
// public class UserService {

//     @Inject
//     IUserRepository userRepository;
//     @Inject
//     CompaniesRepository companiesRepository;

//     public GenericResponse<Boolean> registerUser(UserRegisterRequest request, Integer companyId) {
//         User user = new User();
//         user.setUsername(request.getUsername());
//         user.setPassword(BcryptUtil.bcryptHash(request.getPassword()));
//         user.setRole(null);
//         Company company = companiesRepository.findById(companyId);
//         if (company == null) {
//             return new GenericResponse<>(false, "Company not found",null);
//         }
//         user.setCompany(company);
//         return new GenericResponse<>(true, "User registered successfully",null);
//     }
// }
