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
//ko zna zna
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

/*    socket.on('startTimerRequest', () => {
        io.emit('startTimer');
    });*/


    socket.on('spremiIgru', (igra, data, runda) => {
//        console.log(`Received spremiIgru event. Igra: ${igra}, Data: ${data}`);

        if (igra === "spojnice") {
            obradiSpojnice(data);
            novaRunda(igra, runda);
        } else if (igra === 'korakPoKorak') {
            obradiKorakPoKorak(data);
            novaRunda(igra, runda);
        } else {
            console.log(`Unknown game type: ${igra}`);
        }
    });


    socket.on('bodovi', (igra, bodovi, trenutniIgrac) => {
            switch (igra) {
                case 'spojnice':
                    if (trenutniIgrac === 'prvi') {
                        slaveUkupnoBodova += bodovi;
                        io.emit('prikazBodovaA', 2);
                    } else if (trenutniIgrac === 'drugi') {
                        masterUkupnoBodova += bodovi;
                        io.emit('prikazBodovaB', 2);
                    }
                    break;
                case 'drugaIgra':
                    // Logika za završetak runde druge igre
                    break;


                default:
                    console.log(`Nepoznat tip igre: ${igra}`);
            }
        });

    socket.on('tajmer', (igra) => {
        switch (igra) {
            case 'spojnice':
                 io.to(slaveSocket).emit('zapocniTajmer', igra);
                 io.to(masterSocket).emit('zapocniTajmer', igra);
//                            io.emit('zapocniTajmer', igra);
                break;
            case 'korakPoKorak':
                 io.to(slaveSocket).emit('zapocniTajmer', igra);
                 io.to(masterSocket).emit('zapocniTajmer', igra);
                break;
            default:
                console.log(`Nepoznat tip igre: ${igra}`);
        }
    });

     socket.on('krajIgre', (igra) => {
            switch (igra) {
                case 'spojnice':
                        io.emit('sledecaIgra');
                    break;
                case 'drugaIgra':
                    break;

                default:
                    console.log(`Nepoznat tip igre: ${igra}`);
            }
        });

})


//common
let masterUkupnoBodova = 0;
let slaveUkupnoBodova = 0;

function novaRunda(trenutnaIgra, runda) {
    switch (trenutnaIgra) {
        case 'spojnice':
            switch (runda) {
                case 1:
                    io.to(slaveSocket).emit('prvaRunda', 'prvi');
                    io.to(masterSocket).emit('prvaRunda', 'drugi');
                    // trenutnaRunda++;
                    break;
                case 2:
                    io.to(slaveSocket).emit('drugaRunda', 'prvi');
                    io.to(masterSocket).emit('drugaRunda', 'drugi');
                    break;
                default:

                    break;
            }
            break;

        case 'korakPoKorak':
            switch (runda) {
                case 1:
                    rundaKorak = runda;
                    io.to(slaveSocket).emit('prvaRunda', 'prvi');
                    io.to(masterSocket).emit('prvaRunda', 'drugi');
//                    console.log(`ruundaKorak: ${rundaKorak}`);

                    break;
                case 2:
                    // io.to(slaveSocket).emit('drugaRunda', 'prvi');
                    // io.to(masterSocket).emit('drugaRunda', 'drugi');
                    break;

                default:

                    break;
            }
            break;

        default:

            break;
    }

}

//function novaRunda(trenutnaIgra, runda) {
//    if (trenutnaIgra === 'spojnice') {
//        if (runda === 1) {
//            io.to(slaveSocket).emit('prvaRunda', 'prvi');
//            io.to(masterSocket).emit('prvaRunda', 'drugi');
////            trenutnaRunda++;
//         } else if (runda === 2) {
//            io.to(slaveSocket).emit('drugaRunda', 'prvi');
//            io.to(masterSocket).emit('drugaRunda', 'drugi');
//         } else if (runda === 3) {
//         } else if (runda === 4) {
//         } else {
//         }
//  } else if (trenutnaIgra === 'korakPoKorak') {
//        if (runda === 1) {
//            io.to(slaveSocket).emit('prvaRunda', 'prvi');
//            io.to(masterSocket).emit('prvaRunda', 'drugi');
//        } else if (runda === 2) {
////            io.to(slaveSocket).emit('drugaRunda', 'prvi');
////            io.to(masterSocket).emit('drugaRunda', 'drugi');
//        } else if (runda === 3) {
//        } else if (runda === 4) {
//        } else {
//        }
//    }
////        io.emit('novaRunda', trenutnaRunda);
////        return trenutnaRunda;
//    }
//}




//spojnice
let spojniceArray = [];
let slaveSpojnice = 0;
let masterSpojnice = 0;
function obradiSpojnice(data) {
// ??????
    spojniceArray = [];
    spojniceArray.push(data);
//  console.log(`Received spremiIgru event.  Data: ${data}`);
//  console.log('Emitting spremiSpojnice event.');
//  console.log(`spojniceArray[0]: ${JSON.stringify(spojniceArray[0])}`);
//  console.log(`spojniceArray[1]: ${JSON.stringify(spojniceArray[1])}`);
    if (spojniceArray.length != 0) {
        //emitujemo istu, prvu iz niza, spojnicu na oba socketa
        io.emit('spremiSpojnice', spojniceArray[0]);
//        novaRunda();
    }
}

//korak po korak
let korakPoKorakArray = [];
let slaveKorak = 0;
let masterKorak = 0;
let rundaKorak = 0;

function obradiKorakPoKorak(data) {
//    console.log("Received data for KorakPoKorak:", data);
//    korakPoKorakArray = [];
    korakPoKorakArray.push(data);
    if (korakPoKorakArray.length != 0) {
//        console.log("Emitting spremiKorakPoKorak event with data:", korakPoKorakArray[0]);
        io.emit('spremiKorakPoKorak', korakPoKorakArray[0]);
    } else {
        console.log("No data to emit for spremiKorakPoKorak event");
    }
}