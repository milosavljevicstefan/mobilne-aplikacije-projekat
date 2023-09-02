const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const socket = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socket(server);
const wss = new WebSocket.Server({ server });
let currentPlayer = 'playerA';
let masterSocket = null;
let slaveSocket = null;
let aime = 1;
let bime = 1;
const players = [];
let prvi = 0;
let drugi = 0;
let prviId;
let drugiId;
let a = null; 
let b = null; 

function broadcast(data) {
    wss.clients.forEach((client) => {
      if (client.readyState === WebSocket.OPEN) {
        client.send(JSON.stringify(data));
      }
    });
  }

  wss.on('connection', (ws) => {
    ws.send(JSON.stringify({ currentPlayer }));
  
    ws.on('message', (message) => {
      const data = JSON.parse(message);
  
      if (data.type === 'buttonClicked') {
        broadcast({ currentPlayer, gameState: data.gameState });
      } else if (data.type === 'changeTurn') {
        currentPlayer = data.nextPlayer;
        broadcast({ currentPlayer });
      }
    });
  });
server.listen(2411, '192.168.1.3', () => {
  console.log('Listening on 192.168.1.3:2411');
});

function changeTurn() {
    console.log("menjam krug");
  currentPlayer = currentPlayer === a ? b : a;
  io.emit('turnChange', { currentPlayer });
}

io.on('connection', (socket) => {
  console.log('New socket connection: ' + socket.id);

  if (!a) {
    a = socket.id;
    slaveSocket = a;
    io.to(socket.id).emit('pleyer1', true);
  } else if (!b) {
    b = socket.id;
    masterSocket = b;
    io.to(socket.id).emit('pleyer2', true);
  }

  socket.on('requestTurnChange', () => {
    changeTurn();
  });

  socket.on('pitanjaReady', (socketId) => {
    if (socketId === masterSocket) {
      io.to(masterSocket).emit('spremiIgru', true);
    }
  });

  socket.on('register', async () => {
    players.push(socket.id);
    if (players.length === 2) {
      io.emit('startMatch', true);
    }
  });

  socket.on('Imena', () => {
    const person = { a: aime, b: bime };
    io.emit('podaci', JSON.stringify(person));
  });

  socket.on('turn', async () => {
    const otherPlayer = players.find((player) => player !== socket.id);
    if (otherPlayer) {
      io.to(otherPlayer).emit('changeturn');
    }
  });

  socket.on('turna', async () => {
    await new Promise((resolve) => setTimeout(resolve, 1000));
    if (socket.id === a) {
      io.to(b).emit('changeturna');
    }
    if (socket.id === b) {
      io.to(a).emit('changeturna');
    }
  });

  socket.on('points', (ab) => {
    if (socket.id === a) {
      io.to(b).emit('pointsc');
    }
    if (socket.id === b) {
      io.to(a).emit('pointsc');
    }
  });

  socket.on('Ime', (ime) => {
    if (aime === 1) {
      aime = ime;
    } else if (bime === 1) {
      bime = ime;
    }
  });

  socket.on('reset', () => {
    const playerIndex = players.indexOf(socket.id);
    if (playerIndex !== -1) {
      players.splice(playerIndex, 1);
    }
    a = null;
    b = null;
    aime = 1;
    bime = 1;
  });

  socket.on('disconnect', () => {
    const playerIndex = players.indexOf(socket.id);
    if (playerIndex !== -1) {
      players.splice(playerIndex, 1);
    }
    a = null;
    b = null;
    masterSocket = null;
    slaveSocket = null;
    aime = 1;
    bime = 1;
  });

  socket.on('runduDataRedirect', (data) => {
    io.emit('runduData', data);
  });

  socket.on('sledecaRundaKoZnaZna', () => {
    if (socket.id === masterSocket) {
      io.to(masterSocket).emit('spremiIgru', true);
    }
  });

  socket.on('tacanOdgovorKoZnaZna', () => {
    if (prvi === 0) {
      prvi = 10;
      prviId = socket.id;
    } else if (prvi === -5) {
      drugi = 10;
      drugiId = socket.id;
    } else if (drugi === 0) {
      drugi = 0;
      drugiId = socket.id;
    } else {
      console.log('Error loading answers!');
    }
  });

  socket.on('netacanOdgovorKoZnaZna', () => {
    if (prvi === 0) {
      prvi = -5;
      prviId = socket.id;
    } else {
      drugi = -5;
      drugiId = socket.id;
    }
  });

  socket.on('obradaKoZnaZna', () => {
    console.log('Processing result: prvi=' + prvi + ' drugi=' + drugi);
    if (socket.id === masterSocket) {
      if (prviId === a) {
        io.emit('obradaBodovaKoZnaZna', prvi, drugi);
        prvi = 0;
        drugi = 0;
      } else {
        io.emit('obradaBodovaKoZnaZna', drugi, prvi);
        prvi = 0;
        drugi = 0;
      }
    }
  });

  socket.on('pocniSpojnice', () => {
    console.log('Start Spojnice');
    io.emit('pocetakSpojniceJava');
  });

  socket.on('buttonClicked', (data) => {
    console.log(data);
    io.emit('buttonClickedClient', data);
  });
});
