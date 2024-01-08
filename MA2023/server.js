let a,b;
var masterSocket, slaveSocket;
let aime = 1,bime = 1;
const players = []; // An array to keep track of connected players
let prvi = 0, drugi = 0;
let prviId, drugiId;
const express = require('express');
const app = express();
const http = require('http');
const socket = require('socket.io');
const server = http.createServer(app);
const io = socket(server);
let prviIgracSpojnice = false;

server.listen(2411,'192.168.1.87', () => {
  console.log('listening on 192.168.1.87:2411');
});


io.on('connection', (socket) => {

    console.log("New socket connection: " + socket.id);
    if(a == null )
    {
        a = socket.id;

	    slaveSocket = a;

        io.to(socket.id).emit("pleyer1",true);
        console.log("slaveSocket: " + socket.id);

    }
    else{  
      if(b == null){
        b = socket.id;

	    masterSocket = b;

        io.to(socket.id).emit("pleyer2",true);
       
      }
    }
socket.on('pitanjaReady', (socketId) => {
	console.log("Usao u pitanjaReady");
        if (socketId === masterSocket) {
            io.to(masterSocket).emit("spremiIgru", true);
        }
    });
 socket.on('register', async () => {
        players.push(socket.id); // Register the player's socket ID
        if (players.length === 2) {
            io.emit("startMatch", true);
		// console.log(socket.id);await new Promise(resolve => setTimeout(resolve, 1000));
        //     io.to(socket.id).emit("spremiIgru", true); 
            // Emit startMatch event to both players
        }
    });
    
  
socket.on('Imena', () => {
      const person = {a:aime, b:bime};
      io.emit("podaci",JSON.stringify(person)); 
     
  });
socket.on('turn', async () => {
        const otherPlayer = players.find(player => player !== socket.id);
        if (otherPlayer) {
            io.to(otherPlayer).emit("changeturn");
        }
    });
socket.on('turna', async () => {
        await new Promise(resolve => setTimeout(resolve, 1000));
        if (socket.id == a) {
            io.to(b).emit("changeturna");
        }
        if (socket.id == b) {
            io.to(a).emit("changeturna");
        }


    });
socket.on('points', (ab) => {

        if(socket.id == a)
        {
        ////////////  console.log("a");
            io.to(b).emit("pointsc");
        }
        if(socket.id == b)
        {
            io.to(a).emit("pointsc");
        }


    })
   // ovo cemo iskoristiti ali drugacije malo
    // socket.on('passcom', async (av, bv, cv, dv) => {
    //     const person = {a: av, b: bv, c: cv, d: dv};

    //     if (socket.id == a) {

    //         io.to(b).emit("passcomc", person);
    //     }
    //     if (socket.id == b) {
    //         io.to(a).emit("passcomc", person);
    //     }


    // })
socket.on('Ime', (ime) => {
       
      if(aime == 1)
    {
      aime = ime;
    }
    else{
      if(bime == 1){
       bime = ime;
      }
    }
  })
socket.on('reset', () => {
	console.log("reset");
        const playerIndex = players.indexOf(socket.id);
        if (playerIndex !== -1) {
            players.splice(playerIndex, 1); // Remove the player from the array
        }
        // Reset both players if they are disconnected or game is finished
        a = null;
        b = null;
	aime = 1;
    	bime = 1;
    });
socket.on('disconnect', () => {
        console.log("disconnect");
            const playerIndex = players.indexOf(socket.id);
            if (playerIndex !== -1) {
                players.splice(playerIndex, 1); // Remove the player from the array
            }
            // Reset both players if they are disconnected
            a = null;
            b = null;
        masterSocket = null;
        slaveSocket = null;
        aime = 1;
            bime = 1;
        });






socket.on('sledecaRundaKoZnaZna', () => {
        if(socket.id == masterSocket)
        io.to(masterSocket).emit("spremiIgru", true);
    });
socket.on('tacanOdgovorKoZnaZna', () => {
        console.log(socket.id + " daje tacan odgovor");
        if (prvi == 0 ) {
            prvi = 10;
            prviId = socket.id;
        } else if (prvi == -5) {
            drugi = 10;
            drugiId = socket.id;
        } else if (drugi == 0) {
            drugi = 0;
            drugiId = socket.id
        } else {
            console.log("Greska prilikom ucitavanja odgovora!")
        }
    });
socket.on('netacanOdgovorKoZnaZna', () => {
        console.log(socket.id + " daje netacan odgovor");
        if (prvi == 0 ) {
            prvi = -5;
            prviId = socket.id;
        } else {
            drugi = -5;
            drugiId = socket.id;
        }
    });
socket.on('obradaKoZnaZna', () => {
        console.log("Obrada rezultat: prvi=" + prvi + " drugi=" + drugi);
        if (socket.id == masterSocket) {
            if(prviId == a) {
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



socket.on('spremiIgruSA', () => {
    console.log("Spojnice igra");
    io.emit('spremiIgru'); // Change this line to emit to all connected sockets
});

socket.on('runduDataRedirect', (data) => {
    io.emit('runduData', data); // Change this line to emit to all connected sockets
});

socket.on('prvaRundaSpojnice', () => {
    io.to(masterSocket).emit('onemoguciDugmad');
});
})