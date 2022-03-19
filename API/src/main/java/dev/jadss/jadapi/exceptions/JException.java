package dev.jadss.jadapi.exceptions;

/**
 * Way of <b>JadAPI</b> telling Errors with this custom Exception!
 */
public class JException extends RuntimeException {

    public JException(Reason reason) {
        super(reason.getErrorMessage());
    }

    public enum Reason {
        NOT_AVAILABLE("ACCESS DENIED"),
        NOT_A_PACKET("NOT A PACKET"),
        BAD_NBT("A BAD NBT VALUE WAS SUPPLIED."),
        VALUE_IS_NULL("THE VALUE SUPPLIED IS NULL!"),
        MATERIAL_IS_NULL("THE MATERIAL PROVIDED IS NULL!"),
        PLAYER_IS_NULL("THE PLAYER IS NULL!"),
        CANNOT_BE_PLAYER("YOU CANNOT USE ENTITY TYPES OF PLAYER."),
        CANNOT_PARSE("CANNOT PARSE THIS!"),
        SENDER_IS_NULL("THE SENDER IS NULL!"),
        UUID_IS_NULL("THE UUID IS NULL!"),
        PLAYER_IS_OFFLINE("THE PLAYER IS OFFLINE!"),
        ALREADY_REGISTERED("THIS IS ALREADY REGISTERED!"),
        UNREGISTERED("THIS IS NOT REGISTERED! PLEASE REGISTER BEFORE USING!"),
        ENTITY_IS_NULL("THE ENTITY IS NULL!"),
        INVALID_CLASS("AN INVALID CLASS WAS SUPPLIED."),
        SCOREBOARD_TITLE_TOO_LONG("THE SCOREBOARD TITLE IS TOO LONG!"),
        LOCATION_IS_NULL("THE LOCATION IS NULL!"),
        SIGN_LINES_LENGTH_DONT_MATCH("THE LENGTH OF THE SIGN'S LINES IS "),
        ROWS_IS_LESS_THEN_1("THE AMOUNT OF ROWS SPECIFIED IS LESS THEN 1"),
        BOTH_ARE_NULL("BOTH VALUES SPECIFIED ARE NULLLLLLL!"),
        UNKNOWN("UNKNOWN ERROR."),
        OTHER("OTHER ERROR OCCURRED."),
        JADAPI_NOT_ENABLED("JADAPI IS NOT ENABLED!"),
        SOMETHING_WENT_WRONG("SOMETHING WENT WRONG!");

        private final String errorMessage;

        Reason(String error_Message) {
            this.errorMessage = error_Message;
        }

        public String getErrorMessage() { return this.errorMessage; }
    }
}
