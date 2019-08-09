package jworkspace.users;

import java.io.File;
import java.io.IOException;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import jworkspace.kernel.Workspace;
/**
 * @author Anton Troshin
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@RunWith(PowerMockRunner.class)
@PrepareForTest(Workspace.class)
public class LoginTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        testFolder.delete();
        testFolder.create();

        mockStatic(Workspace.class);
        when(Workspace.getBasePath()).thenReturn(testFolder.getRoot().getPath() + File.separator);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Test
    public void testUserProfileEngine() throws ProfileOperationException, IOException {

        Profile profile = new Profile("test", "password", "First Name", "Second Name", "test@test.com");

        UserProfileEngine userProfileEngine = new UserProfileEngine();

        // this adds incomplete profile -> to disk
        userProfileEngine.addProfile(profile.getUserName(), "password");
        // this selects incomplete profile -> from disk
        userProfileEngine.login(profile.getUserName(), "password");
        // incomplete profile is not equals to one in memory
        assert userProfileEngine.getUserName().equals(profile.getUserName());
        assert !userProfileEngine.getEmail().equals(profile.getEmail());
        assert !userProfileEngine.getUserFirstName().equals(profile.getUserFirstName());
        assert !userProfileEngine.getUserLastName().equals(profile.getUserLastName());
        // deselect incomplete profile -> to disk
        userProfileEngine.logout();
        // save complete to disk
        profile.save();
        // selects complete profile -> from disk
        userProfileEngine.login(profile.getUserName(), "password");

        assert userProfileEngine.getUserName().equals(profile.getUserName());
        assert userProfileEngine.getDescription().equals(profile.getDescription());
        assert userProfileEngine.getCurrentProfileRelativePath().equals(profile.getProfilePath());
        assert userProfileEngine.getEmail().equals(profile.getEmail());
        assert userProfileEngine.getParameters().equals(profile.getParameters());
        assert userProfileEngine.getUserFirstName().equals(profile.getUserFirstName());
        assert userProfileEngine.getUserLastName().equals(profile.getUserLastName());

        userProfileEngine.logout();
        assert !userProfileEngine.userLogged();

        userProfileEngine.removeProfile(profile.getUserName(), "password");
    }

    @After
    public void after() {
        testFolder.delete();
    }
}