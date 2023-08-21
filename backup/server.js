const express = require('express');
const socket = require('socket.io');
const fs = require('fs');
const app = express();
const port = 3000;

const server = app.listen(port, '192.168.1.3');

app.use(express.static('public'));
app.use(express.json());
console.log('Server is running');
const io = socket(server);
var count = 0;

var List = require("collections/list");

app.post('/start-game', (req, res) => {
    const player = req.body.player;
    playersWaiting.add(player);

    if (playersWaiting.length >= 2) {
        const playersToStart = playersWaiting.splice(0, 2);
        io.emit('startGame', playersToStart);
    }

    res.status(200).json({ message: 'Game started or waiting for opponent' });
});


class MojBroj{
	_id;
	_opponentId;
	number;
	constructor(id, opponentId, number){
		this._id = id;
		this._opponentId = opponentId;
		this.number = number;
	}
}

class MojBrojStop{
	_id;
	num1;
	num2;
	num3;
	num4;
	num5;
	num6;
	num7;
	constructor(id, n1, n2, n3, n4, n5, n6, n7){
		this._id = id;
		this.num1 = n1;
		this.num2 = n2;
		this.num3 = n3;
		this.num4 = n4;
		this.num5 = n5;
		this.num6 = n6;
		this.num7 = n7;
	}
}

class Skocko{
	_id;
	_opponentId;
	combo;
	colors;
	isCorrect;
	numOfTries;
	constructor(id, opponentId, combo, colors, isCorrect, numOfTries){
		this._id = id;
		this._opponentId = opponentId;
		this.combo = combo;
		this.colors = colors;
		this.isCorrect = isCorrect;
		this.numOfTries = numOfTries;
	}
}

class SkockoCombo{
	_opponentId;
	s1;
	s2;
	s3;
	s4;
	constructor(opponentId, s1, s2, s3, s4){
		this._opponentId = opponentId;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
	}
}

class Spojnica {
	_opponentId;
	matches;
	constructor(opponentId, matches){
		this._opponentId = opponentId;
		this.matches = matches;
	}
}

var playersWaiting = [];
var playersCalculated = [];

io.on('connect', (socket) => {
	console.log("New socket connection: " + socket.id);

	socket.on("connect_error", (err) => {
		console.log(`connect_error due to`);
	});


	socket.on('playerReady', (player) => {
		playersWaiting.add(player);
		console.log(player);
		if (playersWaiting.length == 2){
			io.emit('startGame', playersWaiting);
			playersWaiting = [];
		}
	});


	socket.on('cancelGame', (player) => {
		if (playersWaiting.includes(player)){
			const index = playersWaiting.indexOf(player);
			playersWaiting.splice(index, 1);
		}
	});

	socket.on('playerCalculatedNumber', (playerOneId, playerTwoId, number) => {
	if (playersCalculated.filter(e => e._opponentId === playerOneId).length > 0){
			const foundPlayers = playersCalculated.filter(e => e._opponentId === playerOneId);

			const playersToRecieve = [];
			playersToRecieve.add(foundPlayers[0]);
			playersToRecieve.add(new MojBroj(playerOneId, playerTwoId, number));
			const index = playersCalculated.indexOf(foundPlayers[0]);
			playersCalculated.splice(index, 1);
			console.log(playersCalculated);
			io.emit('endMojBroj', playersToRecieve);
			return;
		}
		playersCalculated.add(new MojBroj(playerOneId, playerTwoId, number));
	});

	socket.on('sendSkockoStartCombo', (id, s1, s2, s3, s4) => {
		io.emit('receiveSkockoStartCombo', new SkockoCombo(id, s1, s2, s3, s4));
	});

	socket.on('sendPlayerSkockoCorrect', (playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries) => {
		console.log(numOfTries);
		io.emit("showPlayerSkockoCorrect", new Skocko(playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries));
	});

	socket.on('sendPlayerSkocko', (playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries) => {
		if (numOfTries == -1){
			io.emit("giveOpponentAChanceSkocko", new Skocko(playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries));
		} else {
			io.emit("showPlayerSkocko", new Skocko(playerOneId, playerTwoId, combo, colors, isCorrect, numOfTries));
		}
	});

	socket.on('notifyOpponentSkocko', (opponentId) => {
		io.emit('opponentNotifiedSkocko', opponentId);
	});

	socket.on('stopNumberMojBroj', (id, n1, n2, n3, n4, n5, n6, n7) => {
		io.emit('stoppedNumberMojBroj', new MojBrojStop(id, n1, n2, n3, n4, n5, n6, n7));
	});

	socket.on('sendEverythingCorrectSpojnice', (opponentId) => {
		io.emit('receiveAllCorrectSpojnice', opponentId);
	});

	socket.on('sendChanceSpojnice', (opponentId, matches) => {
		io.emit('receiveChanceSpojnice', opponentId, matches);
	});

		socket.on('sendFinishSpojnice', (opponentId, matches) => {
		io.emit('receiveFinishSpojnice', opponentId, matches);
	});
})