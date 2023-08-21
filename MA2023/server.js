const express = require('express');
const app = express();
const http = require('http');
const socket = require('socket.io');
const server = http.createServer(app);
const io = socket(server);

let playerA = null;
let playerB = null;

app.get('/', (req, res) => {
  res.send('<h1>Hello world</h1>');
});

server.listen(4001, '192.168.56.1', () => {
  console.log('listening on *:4001');
});

io.on('connection', (socket) => {
  console.log('New socket connection: ' + socket.id);

  // Assign players when they connect
  if (playerA === null) {
    playerA = socket.id;
    io.to(socket.id).emit('pleyer1', true);
  } else if (playerB === null) {
    playerB = socket.id;
    io.to(socket.id).emit('pleyer2', true);
  }

  // Handle game-related events here
  socket.on('start', () => {
    io.emit('startMatch', true);
  });

  socket.on('turn', () => {
    // Handle turn logic
    // Emit change of turn to the other player
  });

  socket.on('open', (data) => {
    // Handle opening columns and inform the other player
  });

  // Add more game-related event handlers here

  // Reset players when they disconnect
  socket.on('disconnect', () => {
    if (socket.id === playerA) {
      playerA = null;
    } else if (socket.id === playerB) {
      playerB = null;
    }
  });
});
