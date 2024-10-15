package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.ui.ConfirmDeleteWindow;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    private final Index targetIndex;
    private ConfirmationHandler confirmationHandler;

    /**
     * Default constructor for a DeleteCommand object
     * @param targetIndex
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.confirmationHandler = new DefaultConfirmationHandler();
    }

    /**
     * Constructor for a DeleteCommand object for unit tests
     * @param targetIndex
     * @param confirmationHandler
     */
    public DeleteCommand(Index targetIndex, ConfirmationHandler confirmationHandler) {
        this.targetIndex = targetIndex;
        this.confirmationHandler = confirmationHandler;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        boolean isConfirmed = confirmationHandler.confirm(personToDelete);
        if (!isConfirmed) {
            throw new CommandException(Messages.MESSAGE_USER_CANCEL);
        }
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    /**
     * Functional interface for testing purposes
     */
    public interface ConfirmationHandler {
        boolean confirm(Person personToDelete);
    }

    /**
     * Nested class for testing purposes
     */
    public static class DefaultConfirmationHandler implements ConfirmationHandler {
        /**
         * Bypasses UI popup for testing purposes
         * @param personToDelete The person to be deleted
         * @return Whether the deletion proceeds or not
         */
        public boolean confirm(Person personToDelete) {
            ConfirmDeleteWindow confirmDeleteWindow = new ConfirmDeleteWindow();
            confirmDeleteWindow.show(personToDelete);
            return confirmDeleteWindow.isConfirmed();
        }
    }
}
