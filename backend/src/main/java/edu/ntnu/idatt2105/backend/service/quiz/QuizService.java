package edu.ntnu.idatt2105.backend.service.quiz;
import edu.ntnu.idatt2105.backend.dto.category.MultipleCategoryDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizFilterDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryCreateMultDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadMultDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.keyword.QuizKeywordsCreateDTO;
import edu.ntnu.idatt2105.backend.exception.UnauthorizedException;
import edu.ntnu.idatt2105.backend.exception.notfound.CategoryNotFoundException;
import edu.ntnu.idatt2105.backend.exception.notfound.NotFoundException;
import edu.ntnu.idatt2105.backend.mapper.category.CategoryMapper;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizAuthorMapper;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizCategoryMapper;
import edu.ntnu.idatt2105.backend.model.category.Category;
import edu.ntnu.idatt2105.backend.model.category.QuizCategory;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.exception.notfound.QuizNotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.quiz.QuizKeyword;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.category.CategoryRepository;
import edu.ntnu.idatt2105.backend.repo.category.QuizCategoryRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizAuthorRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizKeywordRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.specification.quiz.QuizSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    private final QuizKeywordRepository quizKeywordRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizMapper quizMapper;
    private final QuizAuthorRepository quizAuthorRepository;
    private final AuthenticationService authenticationService;
    private final CategoryRepository categoryRepository;
    private final QuizCategoryRepository quizCategoryRepository;

    public Quiz getQuizById(long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz with id " + quizId + " not found"));
    }

    /**
     * This method retrieves all the quizzes a user has.
     * @param userId    The id of the user.
     * @return          The quiz load all data transfer object.
     */
    public QuizLoadAllDTO getAllQuizzesByUser(Long userId) {
        LOGGER.info("Looking for user.");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId.toString()));
        LOGGER.info("User found.");

        Set<QuizLoadDTO> userQuizzes = user.getQuizzesOwned()
                .stream()
                .map(quizMapper::quizToQuizLoadDTO)
                .collect(Collectors.toSet());

        LOGGER.info("All quizzes from the user has been retrieved.");
        return QuizLoadAllDTO.builder().quizzes(userQuizzes).build();
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
        LOGGER.info("Starting filtering of quizzes.");
        Pageable pageable = PageRequest.of(quizFilterDTO.pageNumber(),quizFilterDTO.pageSize());
        LOGGER.info("Finding page based on specification.");
        Page<Quiz> quizPage = quizRepository.findAll(QuizSpecification.filterQuizzes(quizFilterDTO), pageable);

        Set<Long> uniqueQuizIds = new HashSet<>();

        List<Quiz> uniqueQuizzes = quizPage.getContent().stream()
                .filter(quiz -> uniqueQuizIds.add(quiz.getQuizId()))
                .collect(Collectors.toList());

        LOGGER.info("Page found. Creating QuizLoadDTOPage and responding.");
        return quizMapper.quizPageToQuizLoadDTOPage(new PageImpl<>(uniqueQuizzes, pageable, uniqueQuizzes.size()));
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
                .quiz(quiz)
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
     * This method adds a new category to a pre-existing quiz.
     * @param quizCategoryCreateDTO     The data transfer object for quiz category creation.
     * @return                          A DTO containing the information of the saved quiz category.
     */
    public QuizCategoryLoadDTO addQuizCategory(QuizCategoryCreateDTO quizCategoryCreateDTO, String email) {
        LOGGER.info("Attempting to find quiz.");
        Quiz quiz = quizRepository.findById(quizCategoryCreateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(quizCategoryCreateDTO.quizId().toString()));

        authorizeOwnerOrCollaborator(quizCategoryCreateDTO.quizId(), email);

        LOGGER.info("Quiz found. Attempting to find category.");
        Category category = categoryRepository.findById(quizCategoryCreateDTO.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(quizCategoryCreateDTO.categoryId().toString()));

        LOGGER.info("Category found. Creating quizCategory.");
        QuizCategory quizCategory = QuizCategory
                .builder()
                .category(category)
                .quiz(quiz)
                .build();

        QuizCategory savedQuizCategory = quizCategoryRepository.save(quizCategory);
        LOGGER.info("QuizCategory is saved.");
        return QuizCategoryMapper.INSTANCE.quizCategoryToQuizCategoryLoadDTO(savedQuizCategory);
    }

    /**
     * This method saves multiple quiz categories at the same time through quiz category create multiple dto.
     * @param quizCategoryCreateMultDTO     All the categories to save.
     * @return                              All the saved quiz categories as a DTO.
     */
    public QuizCategoryLoadMultDTO addQuizCategories(QuizCategoryCreateMultDTO quizCategoryCreateMultDTO, String email){
        LOGGER.info("Adding all the quiz categories.");
        Set<QuizCategoryLoadDTO> quizCategoryLoadDTOS = new HashSet<>();
        for(Long categoryId : quizCategoryCreateMultDTO.categoryIds()) {
            quizCategoryLoadDTOS.add(this.addQuizCategory(QuizCategoryCreateDTO
                    .builder()
                    .categoryId(categoryId)
                    .quizId(quizCategoryCreateMultDTO.quizId())
                    .build(), email));
        }
        LOGGER.info("All quiz categories are saved.");
        return QuizCategoryLoadMultDTO.builder().quizCategories(quizCategoryLoadDTOS).build();
    }

    /**
     * This method retrieves all the categories in the database.
     * @return  A multiple category DTO.
     */
    public MultipleCategoryDTO getAllCategories() {
        LOGGER.info("Attempting to retrieve all categories from database.");
        List<Category> categories = categoryRepository.findAll();
        LOGGER.info("All categories retrieved. Now, converting categories to "
                + MultipleCategoryDTO.class.getSimpleName());
        return CategoryMapper.INSTANCE.categoriesToMultipleCategoryDTO(categories);
    }

    /**
     * This method removes a category from a quiz.
     * @param quizCategoryId   The id of the quiz category.
     */
    public void removeQuizCategory(Long quizCategoryId, String email) {
        LOGGER.info("Looking for quiz category.");
        QuizCategory quizCategory = quizCategoryRepository
                .findById(quizCategoryId)
                .orElseThrow(() -> new CategoryNotFoundException("QuizCategory Id: " + quizCategoryId));
        LOGGER.info("Quiz category found.");

        authorizeOwnerOrCollaborator(quizCategory.getQuiz().getQuizId(), email);

        quizCategoryRepository.delete(quizCategory);
        LOGGER.info("Quiz category removed.");
    }

    /**
     * This method adds multiple quiz keywords to a quiz.
     * @param quizKeywordsCreateDTO     The data transfer object of keywords.
     * @param email                     The user's email.
     */
    public void addQuizKeywords(QuizKeywordsCreateDTO quizKeywordsCreateDTO, String email){
        LOGGER.info("Finding quiz.");
        Quiz quiz = quizRepository.findById(quizKeywordsCreateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(quizKeywordsCreateDTO.quizId().toString()));
        authorizeOwnerOrCollaborator(quizKeywordsCreateDTO.quizId(), email);
        LOGGER.info("Adding all the quiz keywords.");
        for(String keyword : quizKeywordsCreateDTO.keywords()) {
            QuizKeyword quizKeyword = QuizKeyword
                    .builder()
                    .keyword(keyword)
                    .quiz(quiz)
                    .build();

            quizKeywordRepository.save(quizKeyword);
        }
        LOGGER.info("All quiz keywords are saved.");
    }

    /**
     * This method removes a keyword from a quiz.
     * @param quizKeywordId   The id of the quiz keyword.
     */
    public void removeQuizKeyword(Long quizKeywordId, String email) {
        LOGGER.info("Looking for quiz keyword.");
        QuizKeyword quizKeyword = quizKeywordRepository
                .findById(quizKeywordId)
                .orElseThrow(() -> new NotFoundException("QuizCategory Id: " + quizKeywordId));
        LOGGER.info("Quiz keyword found.");

        authorizeOwnerOrCollaborator(quizKeyword.getQuiz().getQuizId(), email);

        quizKeywordRepository.delete(quizKeyword);
        LOGGER.info("Quiz keyword removed.");
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


    /**
     * This method authorizes a user with owner or collaborator privileges.
     * @param quizId    The id of the quiz.
     * @param email     The email of the user.
     */
    private void authorizeOwnerOrCollaborator(Long quizId, String email) {
        LOGGER.info("Checking authorization of " + email);
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email)).getUserId();

        if (!isOwnerOrCollaborator(quizId, userId)) {
            throw new UnauthorizedException(email);
        }
        LOGGER.info("User is authorized.");
    }

    /**
     * This method checks whether a user is the owner/admin of a quiz.
     * @param quizId    The id of the quiz.
     * @param userId    The id of the user.
     * @return          Status of whether is owner.
     */
    private boolean isOwner(Long quizId, Long userId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId.toString()));
        return quiz.getAdmin().getUserId().equals(userId);
    }

    /**
     * This method checks whether a user is a collaborator of a quiz.
     * @param quizId    The id of the quiz.
     * @param userId    The id of the user.
     * @return          Status of whether is collaborator.
     */
    private boolean isCollaborator(Long quizId, Long userId) {
        return quizAuthorRepository.findQuizAuthorByQuizQuizIdAndUserUserId(quizId, userId)
                .isPresent();
    }

    /**
     * This method checks whether is a user is either an owner or collaborator of a quiz.
     * @param quizId    The id of the quiz.
     * @param userId    The id of the user.
     * @return          Status whether user is owner or collaborator.
     */
    private boolean isOwnerOrCollaborator(Long quizId, Long userId) {
        return isOwner(quizId, userId) || isCollaborator(quizId, userId);
    }
}