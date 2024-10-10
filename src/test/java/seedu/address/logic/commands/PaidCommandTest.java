package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class PaidCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToMarkPaid = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setHasPaid(true);
        PaidCommand paidCommand = new PaidCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(PaidCommand.MESSAGE_PAID_PERSON_SUCCESS,
                Messages.format(personToMarkPaid));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person editedPerson = new Person(personToMarkPaid.getName(), personToMarkPaid.getPhone(),
                personToMarkPaid.getEmail(), personToMarkPaid.getAddress(),
                personToMarkPaid.getTags(), true);

        expectedModel.setPerson(personToMarkPaid, editedPerson);

        assertCommandSuccess(paidCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setHasPaid(true);
        PaidCommand paidCommand = new PaidCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(paidCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToMarkPaid = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setHasPaid(true);
        PaidCommand paidCommand = new PaidCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(PaidCommand.MESSAGE_PAID_PERSON_SUCCESS,
                Messages.format(personToMarkPaid));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person editedPerson = new Person(personToMarkPaid.getName(), personToMarkPaid.getPhone(),
                personToMarkPaid.getEmail(), personToMarkPaid.getAddress(),
                personToMarkPaid.getTags(), true);
        expectedModel.setPerson(personToMarkPaid, editedPerson);

        assertCommandSuccess(paidCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setHasPaid(true);
        PaidCommand paidCommand = new PaidCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(paidCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {

        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setHasPaid(true);
        PaidCommand paidFirstCommand = new PaidCommand(INDEX_FIRST_PERSON, descriptor);
        PaidCommand paidSecondCommand = new PaidCommand(INDEX_SECOND_PERSON, descriptor);

        // same object -> returns true
        assertTrue(paidFirstCommand.equals(paidFirstCommand));

        // same values -> returns true
        PaidCommand paidFirstCommandCopy = new PaidCommand(INDEX_FIRST_PERSON, descriptor);
        assertTrue(paidFirstCommand.equals(paidFirstCommandCopy));

        // different types -> returns false
        assertFalse(paidFirstCommand.equals(1));

        // null -> returns false
        assertFalse(paidFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(paidFirstCommand.equals(paidSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setHasPaid(true);
        PaidCommand paidCommand = new PaidCommand(targetIndex, descriptor);
        String expected = PaidCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, paidCommand.toString());
    }

    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
