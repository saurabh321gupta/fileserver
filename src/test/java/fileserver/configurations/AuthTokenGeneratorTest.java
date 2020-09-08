package fileserver.configurations;


import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.InvalidParameterException;

@RunWith(MockitoJUnitRunner.class)
public class AuthTokenGeneratorTest {

    private String rdsDatabaseUser = "rdsDatabaseUser";
    private String rdsRegionName = "rdsRegionName";
    private String rdsInstanceHostName = "rdsInstanceHostName";
    private int rdsInstancePort = 3306;

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void shouldValidateDatabaseUser() {
        rdsDatabaseUser = "";
        ex.expectMessage("Database user can not be blank.");
        ex.expect(InvalidParameterException.class);
        new AuthTokenGenerator(rdsDatabaseUser, rdsRegionName, rdsInstanceHostName, rdsInstancePort);
    }

    @Test
    public void shouldValidateRegionName() {
        rdsRegionName = "";
        ex.expectMessage("Database region can not be blank");
        ex.expect(InvalidParameterException.class);
        new AuthTokenGenerator(rdsDatabaseUser, rdsRegionName, rdsInstanceHostName, rdsInstancePort);
    }

    @Test
    public void shouldValidateDatabaseHostName() {
        rdsInstanceHostName = "";
        ex.expectMessage("Database instance host name can not be blank");
        ex.expect(InvalidParameterException.class);
        new AuthTokenGenerator(rdsDatabaseUser, rdsRegionName, rdsInstanceHostName, rdsInstancePort);
    }

    @Test
    public void shouldValidateDatabaseInstancePort() {
        rdsInstancePort = -1;
        ex.expectMessage("Database instance port can not be blank.");
        ex.expect(InvalidParameterException.class);
        new AuthTokenGenerator(rdsDatabaseUser, rdsRegionName, rdsInstanceHostName, rdsInstancePort);
    }

}