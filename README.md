# ProgrammingGameChallenge
This project contains rest api for the game five-in-a-row. 

<p>
The project allows 2 client to connect to server and play the game where each player adds a move on the board with his/her assigned symbol. 
GameBoard is matrix of 6*9 and looks like 
</br>[] [] [] [] [] [] [] [] []</br>
[] [] [] [] [] [] [] [] []</br>
[] [] [] [] [] [] [] [] []</br>
[] [] [] [] [] [] [] [] []</br>
[] [] [] [] [] [] [] [] []</br>
[] [] [] [] [] [] [] [] []</br>

Players first starts the game in order to get their symbol. Once two players connect, game is full and doesnt let anyone else to connect.
They can then take their turn one by one to add moves on board and get the win. When player chooses column to make a move, his/her respective 
symbol takes place in Board. 

</br>[] [ ] [ ] [ ] [] [] [] [] []</br>
[] [ ] [ ] [ ] [] [] [] [] []</br>
[] [ ] [ ] [ ] [] [] [] [] []</br>
[] [ ] [ ] [ ] [] [] [] [] []</br>
[] [ ] [X] [X] [] [] [] [] []</br>
[] [X] [O] [O] [] [] [] [] []</br>

Aim of players is to fill the borad with their symbols for five consecutive rows/columns/diagonals.

<h2><a id="Installation_6"></a>Installation</h2>
Download the project as zip . Unzip and then run ProgrammingApplicationTests class.  <br>
We need to have maven install in the system, these service uses maven to build project
Run command: mvn spring-boot:run (to be run for both applications)
If 8080 port is free. rest service API will be hosted on localhost:8080. <br>

</p>
<h2><a id="Usage_20"></a>Usage</h2>
<p>
<p>To access the swagger UI for client service :  <a href=http://localhost:8080/api/swagger-ui.html>http://localhost:8080/api/swagger-ui.html</a><br></p>
<br>Rest End points hosted on server : </br>
<li><b>POST /play/api/v1/start?name={}</b> - Starts the game by initializing the Player. Creates new player in the game having name passed as param </li>
<li><b>POST /play/api/v1/move?name={}&column={}</b> - Adds the move to the board. Player with param name adds a move to the param column </li>
<li><b>POST /play/api/v1/disconnect?name={}</b> - Disconnects the game for the player with param name </li>
<li><b>GET /play/api/v1/reset</b> - Resets the whole Game. Resets board, players and status </li>
<li><b>GET /play/api/v1/get/state</b> - Gets the current playing Game State. Returns status, board and players available at current stage </li>
</p>


</p>
<h3>GameService</h3>
Alternatively you can run GameMenuApplication class and play Game from console.
<br> This class uses GameRestClient to receive request and send the it to GameService srervice. Here we use RestTemplate to send the request. We use http URL 
along with domain name or IP address ( Like : http://exaple.com/api/v1/start or http://127.0.0.1:8080/api/v1/start) here 
Services address need to well known before we call the GameService service.
<p>


<h2><a id="tutorial_20"></a>Tutorial</h2>
<p>
In this example Once we start the service and it is available on localhost:8080 :
<br>

<li> Start the game for Player 1 by hitting POST url "/play/api/v1/start" and passing request parameter <b>name</b>.</li>
<li> Start the game for Player 2 by hitting POST url "/play/api/v1/start" and passing request parameter <b>name</b>. (Note that use different name to avoid error)</li>
<li> Add move for Player 1 by hitting POST url "/play/api/v1/move" and passing request parameter <b>name</b> of registered Player and request parameter <b>column</b> which will be the column you want to add move on.</li>
<li> Similarly, add the move for Player 2 by hitting POST url "/play/api/v1/move" and passing <b> name</b> and <b> column</b> </li>
<li> Keep on adding move until any Player wins </li>
<li> Use GET url "/play/api/v1/get/state" to get the state of the game. </li>
<li> If you want to disconnect , hit POST url "/play/api/v1/disconnect" with request parameter <b>name</b>. This will make other player winner. </li>
<li> Use GET url "/play/api/v1/get/reset" to reset the state of the game. </li>
</p>
