package com.backend.spring.constants;

public class MessageConstant {

    public static final String INVALID_DATA = "Invalid data";

    public static final String INVALID_PARAMETER = "Invalid parameter.";

    public static class Auth {
        public static final String SIGNIN_SUCCESS = "Signin successfully.";
        public static final String ACCOUNT_INACTIVATED = "Account not activated.";
        public static final String ACCOUNT_ACTIVATED = "Account is activated.";
        public static final String ACCOUNT_LOCKED = "Account is locked.";
        public static final String SIGNIN_FAILED = "Wrong username or password.";
        public static final String ROLE_NOT_FOUND = "Role is not found.";
        public static final String USER_NOT_FOUND = "User is not found.";
        public static final String EMAIL_NOT_FOUND = "Email is not found.";
        public static final String EMAIL_IS_USED = "Email is used.";
        public static final String USERNAME_IS_USED = "Username is used.";
        public static final String ACTIVATE_ACCOUNT = "ACTIVATE ACCOUNT";
        public static final String RESET_PASSWORD = "RESET PASSWORD";
        public static final String SIGNUP_SUCCESS = "Register account successfully.";
        public static final String SIGNUP_FAILED = "Register account failed.";
        public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
        public static final String REFRESH_TOKEN_EXPIRED = "Refresh token expired";
        public static final String ACTIVATE_MAIL_EXPIRED = "Email activate account expired";
        public static final String GET_NEW_ACCESS_TOKEN_SUCCESS = "Get new access token successfully.";
        public static final String SEND_NEW_VERIFICATION_MAIL_SUCCESS = "Send new verification email successfully.";
        public static final String SEND_NEW_RESET_MAIL_SUCCESS = "Send new reset password email successfully.";
        public static final String SEND_RESET_MAIL_SUCCESS = "Send reset password email successfully.";
        public static final String ACTIVATE_ACCOUNT_SUCCESS = "Activate account successfully.";
        public static final String ACTIVATE_ACCOUNT_FAILURE = "Activate account failure.";
        public static final String EMAIL_NOT_EXISTED = "Email is not existed.";
        public static final String TOKEN_AUTH_NOT_VALID = "Token auth is not valid.";
        public static final String TOKEN_AUTH_EXPIRED = "Token auth is expired.";
        public static final String DATA_INVALID = "Data is invalid.";
        public static final String RESET_PASSWORD_SUCCESS = "Rest password successfully.";
        public static final String SIGNOUT_SUCCESS = "Signout successfully.";
        public static final String SIGNOUT_FAILURE = "Signout failure.";

    }

    public static class Redis {
        public static final String REFRESH_TOKEN = "RefreshToken";
    }

    public static class WebSocketComment {
        public static final String ADD_NEW_COMMENT = "/comment/add-new";
    }

    public static class Exam {
        public static final String CREATE_SUCCESS = "Create exam successfully.";
        public static final String CREATE_FAILED = "Create exam failed.";
        public static final String UPDATE_SUCCESS = "Update exam successfully.";
        public static final String UPDATE_FAILED = "Update exam failed.";
        public static final String DELETE_SUCCESS = "Delete exam successfully.";
        public static final String DELETE_FAILED = "Delete exam failed.";
        public static final String GET_DATA_SUCCESS = "Get data exam successfully.";
        public static final String DATA_NOT_FOUND = "Data exam not found.";
        public static final String EXAM_NAME_EXIST = "Exam name already exist.";
        public static final String UPDATE_STATUS_SUCCESS = "Update exam-status successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update exam-status failed.";

    }

    public static class Comment {
        public static final String GET_DATA_SUCCESS = "Get data comment successfully.";
        public static final String CREATE_SUCCESS = "Create comment successfully.";
        public static final String CREATE_FAILED = "Create comment failed.";
    }

    public static class ExamQuestion {
        public static final String GET_DATA_SUCCESS = "Get data question of exam successfully.";
        public static final String DATA_NOT_FOUND = "Data question of exam not found.";
        public static final String CREATE_SUCCESS = "Add new question to exam successfully.";
        public static final String CREATE_FAILED = "Add new question to exam failed.";
        public static final String UPDATE_SUCCESS = "Update question to exam successfully.";
        public static final String UPDATE_FAILED = "Update question to exam failed.";
        public static final String DELETE_SUCCESS = "Delete question from exam successfully.";
        public static final String DELETE_FAILED = "Delete question from exam failed.";
        public static final String FILE_REQUIRED = "File is required";
        public static final String UPLOAD_FILE_SUCCESS = "Upload file of question exam successfully.";
        public static final String UPLOAD_FILE_FAILED = "Upload file of question exam failed.";

    }

    public static class Feedback {
        public static final String GET_DATA_SUCCESS = "Get data feedback successfully.";
        public static final String DATA_NOT_FOUND = "Data feedback not found.";
        public static final String CREATE_SUCCESS = "Create feedback successfully.";
        public static final String CREATE_FAILED = "Create feedback failed.";
        public static final String UPDATE_SUCCESS = "Update feedback successfully.";
        public static final String UPDATE_FAILED = "Update feedback failed.";
        public static final String DELETE_SUCCESS = "Delete feedback successfully.";
        public static final String DELETE_FAILED = "Delete feedback failed.";

    }

    public static class FreeMaterial {
        public static final String GET_DATA_SUCCESS = "Get data free-material successfully.";
        public static final String DATA_NOT_FOUND = "Data free-material not found.";
        public static final String ADD_NEW_SUCCESS = "Add new free-material successfully.";
        public static final String ADD_NEW_FAILED = "Add new free-material failed.";
        public static final String UPDATE_SUCCESS = "Update free-material successfully.";
        public static final String UPDATE_FAILED = "Update free-material failed.";
        public static final String DELETE_SUCCESS = "Delete free-material successfully.";
        public static final String DELETE_FAILED = "Delete free-material failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status free-material successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status free-material failed.";

    }

    public static class GrammarContent {
        public static final String GET_DATA_SUCCESS = "Get data content of grammar successfully.";
        public static final String DATA_NOT_FOUND = "Data content of grammar not found.";
        public static final String ADD_NEW_SUCCESS = "Add new content of grammar successfully.";
        public static final String ADD_NEW_FAILED = "Add new content of grammar failed.";
        public static final String UPDATE_SUCCESS = "Update content of grammar successfully.";
        public static final String UPDATE_FAILED = "Update content of grammar failed.";
        public static final String DELETE_SUCCESS = "Delete content of grammar successfuly.";
        public static final String DELETE_FAILED = "Delete content of grammar failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status content of grammar successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status content of grammar failed.";
        public static final String FILE_IS_REQUIRED = "File is required.";
        public static final String UPLOAD_SUCCESS = "Upload file successfully.";
    }

    public static class Grammar {
        public static final String GET_DATA_SUCCESS = "Get data grammar successfully.";
        public static final String DATA_NOT_FOUND = "Data grammar not found.";
        public static final String CREATE_SUCCESS = "Create grammar successfully.";
        public static final String CREATE_FAILED = "Create grammar failed.";
        public static final String GRAMMAR_NAME_EXISTED = "Grammar name already existed.";
        public static final String UPDATE_SUCCESS = "Update Grammar successfully.";
        public static final String UPDATE_FAILED = "Update Grammar failed.";
        public static final String DELETE_SUCCESS = "Delete Grammar successfully.";
        public static final String DELETE_FAILED = "Delete Grammar failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status grammar successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status grammar failed.";
    }

    public static class GrammarQuestion {
        public static final String GET_DATA_SUCCESS = "Get data question of grammar successfully.";
        public static final String DATA_NOT_FOUND = "Data question of grammar not found.";
        public static final String CREATE_SUCCESS = "Create question of grammar successfully.";
        public static final String CREATE_FAILED = "Create question of grammar failed.";
        public static final String UPDATE_SUCCESS = "Update question of grammar successfully.";
        public static final String UPDATE_FAILED = "Update question of grammar failed.";
        public static final String DELETE_SUCCESS = "Delete question of grammar successfully.";
        public static final String DELETE_FAILED = "Delete question of grammar failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status question of grammar successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status question of grammar failed.";
    }

    public static class LessonContent {
        public static final String GET_DATA_SUCCESS = "Get data content of lesson successfully.";
        public static final String DATA_NOT_FOUND = "Data content of lesson not found.";
        public static final String CREATE_SUCCESS = "Create content of lesson successfully.";
        public static final String CREATE_FAILED = "Create content of lesson failed.";
        public static final String UPDATE_SUCCESS = "Update content of lesson successfully.";
        public static final String UPDATE_FAILED = "Update content of lesson failed.";
        public static final String DELETE_SUCCESS = "Delete content of lesson successfully.";
        public static final String DELETE_FAILED = "Delete content of lesson failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status content of lesson successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status content of lesson failed.";
    }

    public static class Lesson {
        public static final String GET_DATA_SUCCESS = "Get data lesson successfully.";
        public static final String DATA_NOT_FOUND = "Data lesson not found.";
        public static final String CREATE_SUCCESS = "Create lesson successfully.";
        public static final String CREATE_FAILED = "Create lesson failed.";
        public static final String UPDATE_SUCCESS = "Update lesson successfully.";
        public static final String UPDATE_FAILED = "Update lesson failed.";
        public static final String DELETE_SUCCESS = "Delete lesson successfully.";
        public static final String DELETE_FAILED = "Delete lesson failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status lesson successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status lesson failed.";
    }

    public static class Note {
        public static final String GET_DATA_SUCCESS = "Get data note successfully.";
        public static final String DATA_NOT_FOUND = "Data note not found.";
        public static final String CREATE_SUCCESS = "Create note successfully.";
        public static final String CREATE_FAILED = "Create note failed.";
        public static final String UPDATE_SUCCESS = "Update note successfully.";
        public static final String UPDATE_FAILED = "Update note failed.";
        public static final String DELETE_SUCCESS = "Delete note successfully.";
        public static final String DELETE_FAILED = "Delete note failed.";

    }

    public static class ProfileImage {
        public static final String UPDATE_SUCCESS = "Update image of profile successfully.";
        public static final String UPDATE_FAILED = "Update image of profile failed.";

    }

    public static class Question {
        public static final String GET_DATA_SUCCESS = "Get data question successfully.";
        public static final String DATA_NOT_FOUND = "Data question not found.";
        public static final String CREATE_SUCCESS = "Create question successfully.";
        public static final String CREATE_FAILED = "Create question failed.";
        public static final String UPDATE_SUCCESS = "Update question successfully.";
        public static final String UPDATE_FAILED = "Update question failed.";
        public static final String DELETE_SUCCESS = "Delete question successfully.";
        public static final String DELETE_FAILED = "Delete question failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status question successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status question failed.";
    }

    public static class QuestionGroup {
        public static final String GET_DATA_SUCCESS = "Get data question-group successfully.";
        public static final String DATA_NOT_FOUND = "Data question-group not found.";
        public static final String CREATE_SUCCESS = "Create question-group successfully.";
        public static final String CREATE_FAILED = "Create question-group failed.";
        public static final String UPDATE_SUCCESS = "Update question-group successfully.";
        public static final String UPDATE_FAILED = "Update question-group failed.";
        public static final String DELETE_SUCCESS = "Delete question-group successfully.";
        public static final String DELETE_FAILED = "Delete question-group failed.";
    }

    public static class ScoreTable {
        public static final String GET_DATA_SUCCESS = "Get data score-table successfully.";
        public static final String DATA_NOT_FOUND = "Data score-table not found.";
        public static final String CREATE_SUCCESS = "Create score-table successfully.";
        public static final String CREATE_FAILED = "Create score-table failed.";
        public static final String UPDATE_SUCCESS = "Update score-table successfully.";
        public static final String UPDATE_FAILED = "Update score-table failed.";
        public static final String DELETE_SUCCESS = "Delete score-table successfully.";
        public static final String DELETE_FAILED = "Delete score-table failed.";
    }

    public static class Section {
        public static final String GET_DATA_SUCCESS = "Get data section successfully.";
        public static final String DATA_NOT_FOUND = "Data section not found.";
        public static final String CREATE_SUCCESS = "Create section successfully.";
        public static final String CREATE_FAILED = "Create section failed.";
        public static final String UPDATE_SUCCESS = "Update section successfully.";
        public static final String UPDATE_FAILED = "Update section failed.";
        public static final String DELETE_SUCCESS = "Delete section successfully.";
        public static final String DELETE_FAILED = "Delete section failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status section successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status section failed.";
        public static final String SECTION_NAME_EXISTED = "Section name already existed.";
    }

    public static class Test {
        public static final String GET_DATA_SUCCESS = "Get data test successfully.";
        public static final String DATA_NOT_FOUND = "Data test not found.";
        public static final String CREATE_SUCCESS = "Create test successfully.";
        public static final String CREATE_FAILED = "Create test failed.";
        public static final String UPDATE_SUCCESS = "Update test successfully.";
        public static final String UPDATE_FAILED = "Update test failed.";
        public static final String DELETE_SUCCESS = "Delete test successfully.";
        public static final String DELETE_FAILED = "Delete test failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status test successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status test failed.";
        public static final String UPDATE_PARTICIPANT_SUCCESS = "Update participant-test successfully.";
        public static final String UPDATE_PARTICIPANT_FAILED = "Update participant-test failed.";
        public static final String UPDATE_QUESTION_TO_TEST_SUCCESS = "Update question to test successfully.";
        public static final String UPDATE_QUESTION_TO_TEST_FAILED = "Update question to test failed.";
    }

    public static class Topic {
        public static final String GET_DATA_SUCCESS = "Get data topic successfully.";
        public static final String DATA_NOT_FOUND = "Data topic not found.";
        public static final String CREATE_SUCCESS = "Create topic successfully.";
        public static final String CREATE_FAILED = "Create topic failed.";
        public static final String UPDATE_SUCCESS = "Update topic successfully.";
        public static final String UPDATE_FAILED = "Update topic failed.";
        public static final String DELETE_SUCCESS = "Delete topic successfully.";
        public static final String DELETE_FAILED = "Delete topic failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status topic successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status topic failed.";
        public static final String TOPIC_NAME_EXISTED = "Topic name already existed.";
        public static final String FILE_IS_REQUIRED = "File is required.";
        public static final String UPLOAD_FILE_SUCCESS = "Upload file successfully.";
        public static final String UPLOAD_FILE_FAILED = "Upload file failed.";
    }

    public static class User {
        public static final String GET_DATA_SUCCESS = "Get data user successfully.";
        public static final String DATA_NOT_FOUND = "Data user not found.";
        public static final String UPDATE_INFOR_SUCCESS = "Update user information successfully.";
        public static final String UPDATE_INFOR_FAILED = "Update user information failed.";
        public static final String UPDATE_IMAGE_SUCCESS = "Update user image profile successfully.";
        public static final String UPDATE_IMAGE_FAILED = "Update user image profile failed.";

        public static final String UPDATE_STATUS_SUCCESS = "Update status-user successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status-user failed.";
        public static final String CHANGE_PASSWORD_SUCCESS = "Change password successfully.";
        public static final String CHANGE_PASSWORD_FAILED = "Change password failed. Please check the information again.";
    }

    public static class UserExam {
        public static final String GET_DATA_SUCCESS = "Get data exam-user successfully.";
        public static final String DATA_NOT_FOUND = "Data exam-user not found.";
        public static final String CREATE_SUCCESS = "Add new user test result successfully.";
        public static final String CREATE_FAILED = "Add new user test result failed.";
        public static final String USER_JOINED = "Users have joined this exam.";
        public static final String USER_NOT_JOINED = "Users haven't joined this exam.";

    }

    public static class UserExamQuestion {
        public static final String GET_DATA_SUCCESS = "Get data of user's test questions successfully.";
        public static final String DATA_NOT_FOUND = "Data of user's test questions not found.";
        public static final String SUBMIT_SUCCESS = "Submit successfully";
    }

    public static class UserGoal {
        public static final String GET_DATA_SUCCESS = "Get data of user-goal successfully.";
        public static final String DATA_NOT_FOUND = "Data of user-goal not found.";
        public static final String CREATE_SUCCESS = "Create user-goal successfully.";
        public static final String CREATE_FAILED = "Create user-goal failed.";
        public static final String UPDATE_SUCCESS = "Update user-goal successfully.";
        public static final String UPDATE_FAILED = "Update user-goal failed.";
        public static final String USER_HAS_SET = "User has set target goal";
        public static final String USER_HAS_NOT_SET = "User hasn't set target goal";
    }

    public static class UserVocabulary {
        public static final String GET_DATA_SUCCESS = "Get data vocabulary of user successfully.";
        public static final String DATA_NOT_FOUND = "Data vocabulary of user not found.";
        public static final String CREATE_SUCCESS = "Create vocabulary of user successfully.";
        public static final String CREATE_FAILED = "Create vocabulary of user failed.";
        public static final String DELETE_SUCCESS = "Delete vocabulary of user successfully.";
        public static final String DELETE_FAILED = "Delete vocabulary of user failed.";
    }

    public static class Vocabulary {
        public static final String GET_DATA_SUCCESS = "Get data vocabulary successfully.";
        public static final String DATA_NOT_FOUND = "Data vocabulary not found.";
        public static final String CREATE_SUCCESS = "Create vocabulary successfully.";
        public static final String CREATE_FAILED = "Create vocabulary failed.";
        public static final String UPDATE_SUCCESS = "Update vocabulary successfully.";
        public static final String UPDATE_FAILED = "Update vocabulary failed.";
        public static final String DELETE_SUCCESS = "Delete vocabulary successfully.";
        public static final String DELETE_FAILED = "Delete vocabulary failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status vocabulary successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status vocabulary failed.";
        public static final String FILE_IS_REQUIRED = "File is required";
        public static final String UPLOAD_FILE_SUCCESS = "Upload file successfully.";
        public static final String UPLOAD_FILE_FAILED = "Upload file failed.";

    }

    public static class VocabularyQuestion {
        public static final String GET_DATA_SUCCESS = "Get data question of vocabulary successfully.";
        public static final String DATA_NOT_FOUND = "Data question of vocabulary not found.";
        public static final String CREATE_SUCCESS = "Create question of vocabulary successfully.";
        public static final String CREATE_FAILED = "Create question of vocabulary failed.";
        public static final String UPDATE_SUCCESS = "Update question of vocabulary successfully.";
        public static final String UPDATE_FAILED = "Update question of vocabulary failed.";
        public static final String DELETE_SUCCESS = "Delete question of vocabulary successfully.";
        public static final String DELETE_FAILED = "Delete question of vocabulary failed.";
        public static final String UPDATE_STATUS_SUCCESS = "Update status question of vocabulary successfully.";
        public static final String UPDATE_STATUS_FAILED = "Update status question of vocabulary failed.";

    }
}
