package edu.ntnu.idatt2105.backend.mapper.users;

import edu.ntnu.idatt2105.backend.dto.users.MultipleUserDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizHistoryMapper;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizFeedbackMapper;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * This class creates mapping between the User model and its respective DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    ImageService imageService = new ImageService();

    default UserLoadDTO userToUserLoadDTO(User user) {
        return UserLoadDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
//                .feedbacks(QuizFeedbackMapper.INSTANCE.multipleQuizFeedbackToDTO(user.getFeedbacks()))
                .profilePicture(user.getProfilePicLink())
//                .quizHistory(QuizHistoryMapper.INSTANCE.multipleQuizHistoryToDTO(user.getQuizHistory()))
                .build();
    }

    default MultipleUserDTO userToMultipleUserLoadDTO(List<User> users) {
        List<UserLoadDTO> userLoadDTOS =  users.stream().map(this::userToUserLoadDTO).toList();
        return MultipleUserDTO.builder().users(userLoadDTOS).build();
    }


}
