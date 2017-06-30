import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.qainfotech.tap.training.snl.api.Board;
import com.qainfotech.tap.training.snl.api.BoardModel;
import com.qainfotech.tap.training.snl.api.GameInProgressException;
import com.qainfotech.tap.training.snl.api.InvalidTurnException;
import com.qainfotech.tap.training.snl.api.MaxPlayersReachedExeption;
import com.qainfotech.tap.training.snl.api.NoUserWithSuchUUIDException;
import com.qainfotech.tap.training.snl.api.PlayerExistsException;

/**
 * 
 * @author nachiketatripathi
 *
 */
public class TestBoard {
	Board board;
	BoardModel boardModel;
	UUID uuid;
	Object playerObject;
	JSONObject player = (JSONObject) playerObject;

	@BeforeTest
	public void load_the_Board() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		board = new Board();
		uuid = board.getUUID();
		board = new Board(uuid);
	}

	@Test
	public void adding_new_player()
			throws IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		board.registerPlayer("PRATIK");
		board.registerPlayer("RISHABH");

		board.getData();
	}

	@Test(expectedExceptions = PlayerExistsException.class)
	public void adding_new_player_should_throw_Already_PlayerExistsException()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		board.registerPlayer("RISHABH");
		board.getData();
	}

	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
	public void adding_new_player_should_throw_MaxPlayersReachedExeption()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		board.registerPlayer("VARUN");
		board.registerPlayer("SHOBHIT");
		board.registerPlayer("PUNEET");

		board.getData();
	}

	@Test
	public void deleting_the_player_removes_player()
			throws FileNotFoundException, UnsupportedEncodingException, NoUserWithSuchUUIDException {

		UUID uuid1 = (UUID) board.data.getJSONArray("players").getJSONObject(2).get("uuid");
		board.deletePlayer(uuid1);
	}

	@Test(expectedExceptions = NoUserWithSuchUUIDException.class)
	public void deleting_the_player_throws_NoUserWithSuchUUIDException()
			throws FileNotFoundException, UnsupportedEncodingException, NoUserWithSuchUUIDException {
		board.deletePlayer(uuid);

	}

	@Test
	public void roll_the_dice()
			throws FileNotFoundException, UnsupportedEncodingException, JSONException, InvalidTurnException {

		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(0)).get("uuid"));

	}

	@Test(expectedExceptions = InvalidTurnException.class)
	public void roll_the_dice_to_throw_InvalidTurnException()
			throws FileNotFoundException, UnsupportedEncodingException, JSONException, InvalidTurnException {
		board.rollDice(uuid);

	}

}
