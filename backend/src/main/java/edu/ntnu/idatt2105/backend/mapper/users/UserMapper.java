package edu.ntnu.idatt2105.backend.mapper.users;

import edu.ntnu.idatt2105.backend.dto.users.MultipleUserDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class creates mapping between the User model and its respective DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * This method converts between the user model to the user load DTO.
     * @param user  The user model to convert.
     * @return      The resulting user load DTO.
     */
    default UserLoadDTO userToUserLoadDTO(User user) {
        return UserLoadDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
//                .feedbacks(QuizFeedbackMapper.INSTANCE.multipleQuizFeedbackToDTO(user.getFeedbacks()))
                .showActivity(user.isShowActivity())
                .showFeedback(user.isShowFeedback())
//                .quizHistory(QuizHistoryMapper.INSTANCE.multipleQuizHistoryToDTO(user.getQuizHistory()))
                .build();
    }

    /**
     * This method converts users to a DTO containing a set of UserLoadDTOs.
     * @param users     A set of users.
     * @return          MultipleUserDTO.
     */
    default MultipleUserDTO usersToMultipleUserLoadDTO(Set<User> users) {
        Set<UserLoadDTO> userLoadDTOS =  users.stream().map(this::userToUserLoadDTO).collect(Collectors.toSet());
        return MultipleUserDTO.builder().users(userLoadDTOS).build();
    }


}
