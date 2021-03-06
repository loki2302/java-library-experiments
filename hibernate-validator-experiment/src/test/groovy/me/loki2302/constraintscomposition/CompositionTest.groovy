package me.loki2302.constraintscomposition

import org.junit.Before
import org.junit.Test

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static org.junit.Assert.*

class CompositionTest {
    Validator validator

    @Before
    void initValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.getValidator()
    }

    @Test
    public void canGetNoViolations() {
        UserDTO userDTO = new UserDTO()
        userDTO.username = 'loki'
        def violations = validator.validate(userDTO)
        assertTrue violations.empty
    }

    @Test
    public void canGetEmptyViolation() {
        UserDTO userDTO = new UserDTO()
        userDTO.username = ''
        def violations = validator.validate(userDTO)
        assertFalse violations.empty
        assertEquals('may not be empty', violations[0].interpolatedMessage)
    }

    @Test
    public void canGetEmptyPatternViolation() {
        UserDTO userDTO = new UserDTO()
        userDTO.username = 'loki2302'
        def violations = validator.validate(userDTO)
        assertFalse violations.empty
        assertEquals('must match "^[a-z]*$"', violations[0].interpolatedMessage)
    }

    @Test
    public void canHaveASingleViolationReported() {
        UserDTOWithReportAsSingleViolation userDTO = new UserDTOWithReportAsSingleViolation()
        userDTO.username = 'loki2302'
        def violations = validator.validate(userDTO)
        assertFalse violations.empty
        assertEquals("that's a bad username", violations[0].interpolatedMessage)
    }
}
