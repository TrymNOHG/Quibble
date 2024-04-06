package edu.ntnu.idatt2105.backend.service.quiz;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizFilterDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadDTO;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizAuthorMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.exception.notfound.QuizNotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizAuthorRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.specification.quiz.QuizSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service provides the logic for the Quiz entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 01.04.2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizMapper quizMapper;
    private final MultipleChoiceRepository multipleChoiceRepository;
    private final QuizAuthorRepository quizAuthorRepository;
    private final AuthenticationService authenticationService;

    public Quiz getQuizById(long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz with id " + quizId + " not found"));
    }

    @Transactional
    public QuizLoadDTO createQuiz(String quizName, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + adminEmail + " not found"));
        authenticationService.verifyUserId(admin.getUserId());
        Quiz quiz = Quiz.builder()
                .quizName(quizName)
                .admin(admin)
                .difficulty(Difficulty.EASY)
                .build();
        quiz = quizRepository.save(quiz);
        return QuizLoadDTO.builder()
                .quizId(quiz.getQuizId())
                .quizName(quiz.getQuizName())
                .adminId(admin.getUserId())
                .difficulty(quiz.getDifficulty())
                .build();
    }

    /**
     * This method loads all quizzes available.
     * @return  All quizzes as QuizLoadAllDTO
     */
    public QuizLoadAllDTO loadAllQuiz(){
        List<Quiz> allQuizzes = quizRepository.findAll();
        return QuizLoadAllDTO
                .builder()
                .quizzes(allQuizzes.stream().map(quizMapper::quizToQuizLoadDTO).collect(Collectors.toSet()))
                .build();
    }

    /**
     * This method retrieves a page of quizzes.
     * @param pageable  The meta information surrounding the page, such as page index and size.
     * @return          A page of quiz load DTOs.
     */
    public Page<QuizLoadDTO> getPage(Pageable pageable) {
        Page<Quiz> allQuizzes = quizRepository.findAll(pageable);
        return quizMapper.quizPageToQuizLoadDTOPage(allQuizzes);
    }

    /**
     * This method retrieves quizzes based on the filter DTO.
     * @param quizFilterDTO     Quiz filter DTO.
     * @return                  Page of quiz load DTOs.
     */
    public Page<QuizLoadDTO> getFilteredQuizzes(QuizFilterDTO quizFilterDTO) {
        Pageable pageable = PageRequest.of(quizFilterDTO.pageNumber(),quizFilterDTO.pageSize());
        Page<Quiz> quizPage = quizRepository.findAll(QuizSpecification.filterQuizzes(quizFilterDTO), pageable);
        return quizMapper.quizPageToQuizLoadDTOPage(quizPage);
    }

    /**
     * Updates a quiz based on the quiz update DTO. Only the admin of the quiz or collaborators can update the quiz.
     *
     * @param quizUpdateDTO The quiz update DTO.
     * @return The updated quiz.
     */
    @Transactional
    public QuizLoadDTO updateQuiz(QuizUpdateDTO quizUpdateDTO) {
        LOGGER.info("Attempting to retrieve quiz with id: " + quizUpdateDTO.quizId());
        Quiz quiz = quizRepository.findById(quizUpdateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException("Id: " + quizUpdateDTO.quizId()));

        log.info("Checking if user is admin or collaborator.");
        if(getAllCollaborators(quiz.getQuizId()).stream().noneMatch(
                quizAuthor -> quizAuthor.getUser().getUserId().equals(authenticationService.getLoggedInUserId())
        )){
            authenticationService.verifyUserId(quiz.getAdmin().getUserId());
        }
        log.info("User is admin or collaborator.");

        if (quizUpdateDTO.newName() != null) {
            LOGGER.info("Updating quiz name");
            quiz.setQuizName(quizUpdateDTO.newName());
        }

        if (quizUpdateDTO.newDescription() != null) {
            LOGGER.info("Updating quiz description");
            quiz.setQuizDescription(quizUpdateDTO.newDescription());
        }

        if (quizUpdateDTO.difficulty() != null) {
            LOGGER.info("Updating quiz difficulty");
            quiz.setDifficulty(quizUpdateDTO.difficulty());
        }

        Quiz savedQuiz = quizRepository.save(quiz);

        return quizMapper.quizToQuizLoadDTO(savedQuiz);
    }

    /**
     * This method deletes a quiz based on its id. Only the admin of the quiz can delete it.
     * @param quizId    The id of the quiz.
     */
    public void deleteQuiz(Long quizId) {
        LOGGER.info("Attempting to find quiz to delete.");
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz with id: " + quizId));
        authenticationService.verifyUserId(quiz.getAdmin().getUserId());
        LOGGER.info("Attempting to delete quiz.");
        quizRepository.delete(quiz);
        LOGGER.info("Quiz successfully deleted.");
    }

    /**
     * This method adds a new collaborator to a quiz. Only admins can add collaborators.
     * @param newCollaborator   The new collaborator.
     */
    public QuizAuthorLoadDTO addCollaborator(QuizAuthorDTO newCollaborator) {
        LOGGER.info("Finding User");
        User user = userRepository.findById(newCollaborator.userId())
                .orElseThrow(() -> new UsernameNotFoundException("Id " + newCollaborator.userId()));
        LOGGER.info("User found. Looking for Quiz.");
        Quiz quiz = quizRepository.findById(newCollaborator.quizId())
                .orElseThrow(() -> new QuizNotFoundException(newCollaborator.quizId().toString()));
        authenticationService.verifyUserId(quiz.getAdmin().getUserId());
        if(quiz.getAdmin().getUserId().equals(user.getUserId())){
            throw new IllegalArgumentException("User is already the owner of the quiz.");
        }
        LOGGER.info("Quiz found. Creating quiz author object.");
        QuizAuthor quizAuthor = QuizAuthor
                .builder()
                .user(user)
                .build();

        quiz.addAuthor(quizAuthor);

        LOGGER.info("Saving quiz author object.");
        quizAuthorRepository.save(quizAuthor);
        quizRepository.save(quiz);
        LOGGER.info("Quiz author saved.");

        return QuizAuthorMapper.INSTANCE.quizAuthorToQuizAuthorLoadDTO(quizAuthor);
    }

    /**
     * This method removes a collaborator from a quiz. Only admins can remove collaborators.
     * @param newCollaborator   The collaborator to remove and the quiz.
     */
    public void removeCollaborator(QuizAuthorDTO newCollaborator) {
        LOGGER.info("Looking for collaborator.");
        QuizAuthor quizAuthor = quizAuthorRepository
                .findQuizAuthorByQuizQuizIdAndUserUserId(newCollaborator.quizId(), newCollaborator.userId())
                .orElseThrow(() -> new UsernameNotFoundException("Quiz Id: " + newCollaborator.quizId()
                        + ". User Id: " + newCollaborator.userId()));
        authenticationService.verifyUserId(quizAuthor.getQuiz().getAdmin().getUserId());
        LOGGER.info("Collaborator found.");
        quizAuthorRepository.delete(quizAuthor);
        LOGGER.info("Collaborator removed.");
    }


    /**
     * This method removes a collaborator from a quiz. Only admins can remove collaborators.
     * @param collaboratorId   The id of the collaborator
     */
    public void removeCollaborator(Long collaboratorId) {
        LOGGER.info("Looking for collaborator.");
        QuizAuthor quizAuthor = quizAuthorRepository
                .findById(collaboratorId)
                .orElseThrow(() -> new UsernameNotFoundException("Author Id: " + collaboratorId));
        LOGGER.info("Collaborator found.");
        authenticationService.verifyUserId(quizAuthor.getQuiz().getAdmin().getUserId());
        quizAuthorRepository.delete(quizAuthor);
        LOGGER.info("Collaborator removed.");
    }

    /**
     * This method retrieves all collaborators for a quiz.
     *
     * @param quizId   The id of the quiz.
     * @return        A set of quiz authors.
     */
    public Set<QuizAuthor> getAllCollaborators(Long quizId) {
        LOGGER.info("Looking for quiz.");
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz with id: " + quizId));
        LOGGER.info("Quiz found. Looking for collaborators.");
        return quiz.getCollaborators();
    }
}
