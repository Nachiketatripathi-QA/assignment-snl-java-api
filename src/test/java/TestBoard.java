import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

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
		board.registerPlayer("VARUN");

	}

	@Test(expectedExceptions = PlayerExistsException.class)
	public void adding_new_player_should_throw_Already_PlayerExistsException()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {
		board.registerPlayer("RISHABH");

	}

	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
	public void adding_new_player_should_throw_MaxPlayersReachedExeption()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {

		board.registerPlayer("SHOBHIT");
		board.registerPlayer("PUNEET");

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

		UUID u = (UUID) ((JSONObject) board.getData().getJSONArray("players").get(0)).get("uuid");
		board.rollDice(u);

	}

	@Test(expectedExceptions = InvalidTurnException.class)
	public void roll_the_dice_to_throw_InvalidTurnException()
			throws FileNotFoundException, UnsupportedEncodingException, JSONException, InvalidTurnException {

		board.rollDice(uuid);

	}

	@Test
	public void Check_Construction_of_Board() throws IOException {
		JSONObject data1 = board.data;
		Board newtest = new Board(board.uuid);
		JSONObject data2 = newtest.data;
		assertNotEquals(data1, data2);

	}

	@Test(expectedExceptions = InvalidTurnException.class)
	public void movement_of_players_on_dice_rolling()
			throws InvalidTurnException, JSONException, NoUserWithSuchUUIDException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, IOException {
		
		
		
		for (int index = 0; index < 3; index++) {
			board.deletePlayer((UUID) ((JSONObject) board.getData().getJSONArray("players").get(index)).get("uuid"));
			
			((JSONObject) board.getData().getJSONArray("players").get(index)).put("position", 0);
			UUID uuid = (UUID) ((JSONObject) board.getData().getJSONArray("players").get(index)).get("uuid");
			Object current_position = ((JSONObject) board.getData().getJSONArray("players").get(index)).get("position");
			JSONObject obj = board.rollDice(uuid);
			Object new_position = ((JSONObject) board.getData().getJSONArray("players").get(index)).get("position");
			Object dice = obj.get("dice");
			Object message = obj.get("message");
			int number = (int) current_position + (int) dice;
			JSONObject steps = null;
			// dice roll
			steps = (JSONObject) board.getData().getJSONArray("steps").get((int) number);
			int type = (Integer) steps.get("type");
			if (type == 2) {
				assertNotEquals(message, "Player climbed a ladder, moved to " + new_position);
			} else if (type == 0) {
				assertEquals(message, "Player moved to " + new_position);
			} else if (type == 1) {
				assertNotEquals(message, "Player was bit by a snake, moved back to " + new_position);
			}
		if(index==3)
			index=0;
		
		}
	}

	@Test
	public void checking_toString_method() {

		String value = "UUID:" + board.uuid.toString() + "\n" + board.data.toString();
		assertEquals(value, board.toString());
	}

	@Test(expectedExceptions = GameInProgressException.class)
	public void N_GameInProgressException_for_already_occuring_game()
			throws MaxPlayersReachedExeption, FileNotFoundException, UnsupportedEncodingException,
			PlayerExistsException, GameInProgressException, IOException, JSONException, InvalidTurnException {
		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(1)).get("uuid"));
		board.registerPlayer("SUPERMAN");
	}

}
