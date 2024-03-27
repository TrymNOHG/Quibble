package edu.ntnu.idatt2105.backend.service.users;

import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import org.springframework.transaction.annotation.Transactional;

public interface IUserService {

    @Transactional
    UserLoadDTO updateUser(UserUpdateDTO userUpdateDTO);

    UserLoadDTO getUserById(Long userId);

}
